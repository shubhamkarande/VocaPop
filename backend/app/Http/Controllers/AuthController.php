<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{
    public function register(Request $request)
    {
        try {
            $validated = $request->validate([
                'name' => 'nullable|string|max:255',
                'email' => 'required|email|unique:users',
                'password_hash' => 'required|string',
                'language' => 'nullable|string',
                'daily_goal' => 'nullable|integer',
            ]);

            $user = User::create([
                'name' => $validated['name'] ?? null,
                'email' => $validated['email'],
                'password' => Hash::make($validated['password_hash']),
                'selected_language' => $validated['language'] ?? 'fr',
                'daily_goal' => $validated['daily_goal'] ?? 10,
            ]);

            $token = $user->createToken('auth_token')->plainTextToken;

            return response()->json([
                'token' => $token,
                'refreshToken' => 'refresh_not_implemented',
                'user' => [
                    'id' => (string) $user->id,
                    'name' => $user->name,
                    'email' => $user->email,
                    'selectedLanguage' => $user->selected_language,
                    'dailyGoal' => $user->daily_goal ?? 10,
                    'streak' => $user->streak ?? 0,
                    'wordsMastered' => $user->words_mastered ?? 0,
                ]
            ]);
        } catch (\Exception $e) {
            return response()->json([
                'error' => $e->getMessage()
            ], 422);
        }
    }

    public function login(Request $request)
    {
        try {
            $request->validate([
                'email' => 'required|email',
                'password_hash' => 'required',
            ]);

            $user = User::where('email', $request->email)->first();

            if (! $user || ! Hash::check($request->password_hash, $user->password)) {
                return response()->json([
                    'error' => 'Invalid email or password'
                ], 401);
            }

            $token = $user->createToken('auth_token')->plainTextToken;

            return response()->json([
                'token' => $token,
                'refreshToken' => 'refresh_not_implemented',
                'user' => [
                    'id' => (string) $user->id,
                    'name' => $user->name,
                    'email' => $user->email,
                    'selectedLanguage' => $user->selected_language,
                    'dailyGoal' => $user->daily_goal ?? 10,
                    'streak' => $user->streak ?? 0,
                    'wordsMastered' => $user->words_mastered ?? 0,
                ]
            ]);
        } catch (\Exception $e) {
            return response()->json([
                'error' => $e->getMessage()
            ], 500);
        }
    }
}
