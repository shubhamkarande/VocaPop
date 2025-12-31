<?php

namespace App\Http\Controllers;

use App\Models\Card;
use App\Models\Deck;
use App\Models\UserCard;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;

class LearningController extends Controller
{
    public function getDecks(Request $request)
    {
        // Return all decks with card counts
        $decks = Deck::withCount('cards')->get();
        
        return $decks->map(function ($deck) {
            return [
                'id' => $deck->id,
                'name' => $deck->name,
                'language' => $deck->language,
                'description' => $deck->description,
                'is_starter' => $deck->is_starter,
                'card_count' => $deck->cards_count,
                'learned_count' => 0, // TODO: Calculate actual learned count
            ];
        });
    }

    public function getDueCards(Request $request)
    {
        $user = $request->user();
        
        // First, check if user has any UserCard entries
        $userCardCount = UserCard::where('user_id', $user->id)->count();
        
        if ($userCardCount == 0) {
            // New user - initialize cards from starter decks
            $this->initializeUserCards($user);
        }
        
        // Get cards that are due for review (next_review_date <= now)
        $due = UserCard::with('card')
            ->where('user_id', $user->id)
            ->where('next_review_date', '<=', now())
            ->limit(20) // Limit session size
            ->get();
            
        // Map to structure expected by client
        return $due->map(function ($uc) {
            return [
                'card' => [
                    'id' => $uc->card->id,
                    'deck_id' => $uc->card->deck_id,
                    'front' => $uc->card->front,
                    'back' => $uc->card->back,
                    'example_sentence' => $uc->card->example_sentence,
                    'word_type' => $uc->card->word_type,
                    'audio_url' => $uc->card->audio_url,
                    'image_url' => $uc->card->image_url,
                ],
                'progress' => [
                    'id' => $uc->id,
                    'user_id' => $uc->user_id,
                    'card_id' => $uc->card_id,
                    'ease_factor' => $uc->ease_factor,
                    'interval_days' => $uc->interval_days,
                    'repetitions' => $uc->repetitions,
                    'next_review_date' => $uc->next_review_date?->toIso8601String(),
                    'last_review_date' => $uc->last_review_date?->toIso8601String(),
                ]
            ];
        });
    }
    
    /**
     * Initialize UserCard entries for a new user from starter decks
     */
    private function initializeUserCards($user)
    {
        // Get all cards from starter decks (or all decks for MVP)
        $starterDecks = Deck::where('is_starter', true)->pluck('id');
        
        $cards = Card::whereIn('deck_id', $starterDecks)->get();
        
        // If no starter decks, get first 20 cards from any deck
        if ($cards->isEmpty()) {
            $cards = Card::limit(20)->get();
        }
        
        foreach ($cards as $card) {
            UserCard::create([
                'id' => Str::uuid(),
                'user_id' => $user->id,
                'card_id' => $card->id,
                'ease_factor' => 2.5,
                'interval_days' => 0,
                'repetitions' => 0,
                'next_review_date' => now(), // Due immediately for new cards
                'last_review_date' => null,
            ]);
        }
    }

    public function submitReview(Request $request)
    {
        $validated = $request->validate([
            'card_id' => 'required|exists:cards,id',
            'grade' => 'required|integer|min:0|max:5',
        ]);

        $user = $request->user();
        
        // Find or create UserCard
        $userCard = UserCard::firstOrCreate(
            ['user_id' => $user->id, 'card_id' => $validated['card_id']],
            [
                'id' => Str::uuid(),
                'ease_factor' => 2.5,
                'interval_days' => 0,
                'repetitions' => 0,
                'next_review_date' => now(),
            ]
        );

        // SM-2 Algorithm implementation
        $currentEf = $userCard->ease_factor ?? 2.5;
        $currentInterval = $userCard->interval_days ?? 0;
        $reps = $userCard->repetitions ?? 0;
        
        $q = $validated['grade'];
        
        // Calculate new ease factor
        $newEf = max(1.3, $currentEf + (0.1 - (5 - $q) * (0.08 + (5 - $q) * 0.02)));
        
        // Calculate new interval and repetitions
        if ($q < 3) {
            // Failed - reset
            $newReps = 0;
            $newInterval = 1;
        } else {
            // Passed
            $newReps = $reps + 1;
            if ($newReps == 1) {
                $newInterval = 1;
            } elseif ($newReps == 2) {
                $newInterval = 6;
            } else {
                $newInterval = round($currentInterval * $newEf);
            }
        }
        
        // Update UserCard
        $userCard->ease_factor = $newEf;
        $userCard->repetitions = $newReps;
        $userCard->interval_days = $newInterval;
        $userCard->next_review_date = now()->addDays($newInterval);
        $userCard->last_review_date = now();
        $userCard->save();
        
        return response()->json([
            'id' => $userCard->id,
            'user_id' => $userCard->user_id,
            'card_id' => $userCard->card_id,
            'ease_factor' => $userCard->ease_factor,
            'interval_days' => $userCard->interval_days,
            'repetitions' => $userCard->repetitions,
            'next_review_date' => $userCard->next_review_date?->toIso8601String(),
            'last_review_date' => $userCard->last_review_date?->toIso8601String(),
        ]);
    }

    public function sync(Request $request)
    {
        // Full sync implementation
        return response()->json(['success' => true]);
    }
}
