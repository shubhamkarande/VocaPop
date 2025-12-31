<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Card extends Model
{
    use HasFactory, HasUuids;

    protected $fillable = [
        'deck_id',
        'front',
        'back',
        'example_sentence',
        'word_type',
        'image_url',
        'audio_url',
    ];
    
    public function deck()
    {
        return $this->belongsTo(Deck::class);
    }
}
