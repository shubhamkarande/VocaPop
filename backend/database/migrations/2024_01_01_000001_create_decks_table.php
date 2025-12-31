<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('decks', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->string('name');
            $table->string('language');
            $table->text('description')->nullable();
            $table->boolean('is_starter')->default(false);
            $table->integer('card_count')->default(0);
            $table->integer('learned_count')->default(0);
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('decks');
    }
};
