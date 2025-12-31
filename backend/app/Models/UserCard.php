<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class UserCard extends Model
{
    use HasFactory, HasUuids;

    protected $fillable = [
        'user_id',
        'card_id',
        'ease_factor',
        'interval_days',
        'repetitions',
        'next_review_date',
        'last_review_date',
    ];
    
    protected $casts = [
        'next_review_date' => 'datetime',
        'last_review_date' => 'datetime',
    ];
    
    public function user()
    {
        return $this->belongsTo(User::class);
    }
    
    public function card()
    {
        return $this->belongsTo(Card::class);
    }
}
