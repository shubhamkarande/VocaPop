<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\LearningController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

Route::post('/auth/register', [AuthController::class, 'register']);
Route::post('/auth/login', [AuthController::class, 'login']);

Route::middleware('auth:sanctum')->group(function () {
    Route::get('/user', function (Request $request) {
        return $request->user();
    });
    
    Route::get('/decks', [LearningController::class, 'getDecks']);
    Route::get('/cards/due', [LearningController::class, 'getDueCards']);
    Route::post('/review', [LearningController::class, 'submitReview']);
    Route::post('/sync', [LearningController::class, 'sync']);
});
