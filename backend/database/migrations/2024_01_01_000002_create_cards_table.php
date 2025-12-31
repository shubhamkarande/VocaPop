<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('cards', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->uuid('deck_id');
            $table->string('front');
            $table->string('back');
            $table->string('example_sentence')->nullable();
            $table->string('word_type')->nullable();
            $table->string('image_url')->nullable();
            $table->string('audio_url')->nullable();
            $table->timestamps();

            $table->foreign('deck_id')->references('id')->on('decks')->onDelete('cascade');
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('cards');
    }
};
