<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Deck extends Model
{
    use HasFactory, HasUuids;

    protected $fillable = [
        'name',
        'language',
        'description',
        'is_starter',
    ];
    
    public function cards()
    {
        return $this->hasMany(Card::class);
    }
}
