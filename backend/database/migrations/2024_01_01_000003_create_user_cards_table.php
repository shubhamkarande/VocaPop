<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('user_cards', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->uuid('user_id');
            $table->uuid('card_id');
            $table->float('ease_factor')->default(2.5);
            $table->integer('interval_days')->default(0);
            $table->integer('repetitions')->default(0);
            $table->dateTime('next_review_date');
            $table->dateTime('last_review_date')->nullable();
            $table->timestamps();

            $table->foreign('user_id')->references('id')->on('users')->onDelete('cascade');
            $table->foreign('card_id')->references('id')->on('cards')->onDelete('cascade');
            
            $table->unique(['user_id', 'card_id']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('user_cards');
    }
};
