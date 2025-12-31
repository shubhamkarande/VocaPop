<?php

namespace Database\Seeders;

use App\Models\Card;
use App\Models\Deck;
use App\Models\User;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        // 1. Create a Test User
        $user = User::create([
            'email' => 'test@vocapop.com',
            'password' => Hash::make('password123'),
            'selected_language' => 'fr',
            'daily_goal' => 20,
            'streak' => 5,
            'words_mastered' => 104
        ]);

        // ========================================
        // FRENCH STARTER DECK (30 words)
        // ========================================
        $frenchDeck = Deck::create([
            'name' => 'French Essentials',
            'language' => 'fr',
            'description' => 'Most common words to start your French journey.',
            'is_starter' => true
        ]);

        $frenchCards = [
            // Greetings
            ['Bonjour', 'Hello / Good morning', 'Bonjour, comment allez-vous?', 'Greeting'],
            ['Au revoir', 'Goodbye', 'Au revoir et à demain!', 'Greeting'],
            ['Bonsoir', 'Good evening', 'Bonsoir, madame.', 'Greeting'],
            ['Merci', 'Thank you', 'Merci beaucoup pour votre aide.', 'Phrase'],
            ["S'il vous plaît", 'Please', "Un café, s'il vous plaît.", 'Phrase'],
            
            // Common Nouns
            ['La maison', 'The house', 'Ma maison est grande.', 'Noun'],
            ['Le chat', 'The cat', 'Le chat dort sur le canapé.', 'Noun'],
            ['Le chien', 'The dog', 'Le chien joue dans le jardin.', 'Noun'],
            ["L'eau", 'Water', "J'ai besoin d'eau.", 'Noun'],
            ['Le pain', 'The bread', 'Je mange du pain au petit-déjeuner.', 'Noun'],
            ['La voiture', 'The car', 'Ma voiture est rouge.', 'Noun'],
            ['Le livre', 'The book', 'Ce livre est intéressant.', 'Noun'],
            ['La fleur', 'The flower', 'Cette fleur sent bon.', 'Noun'],
            ['Le soleil', 'The sun', 'Le soleil brille.', 'Noun'],
            ['La lune', 'The moon', 'La lune est belle ce soir.', 'Noun'],
            
            // Verbs
            ['Manger', 'To eat', 'Je veux manger une pizza.', 'Verb'],
            ['Boire', 'To drink', 'Je vais boire un café.', 'Verb'],
            ['Dormir', 'To sleep', 'Je dois dormir maintenant.', 'Verb'],
            ['Parler', 'To speak', 'Je parle français.', 'Verb'],
            ['Aimer', 'To love / To like', "J'aime la musique.", 'Verb'],
            ['Aller', 'To go', 'Je vais au marché.', 'Verb'],
            ['Venir', 'To come', 'Tu viens avec moi?', 'Verb'],
            ['Faire', 'To do / To make', 'Que fais-tu?', 'Verb'],
            
            // Adjectives
            ['Grand', 'Big / Tall', 'Il est très grand.', 'Adjective'],
            ['Petit', 'Small / Short', 'Le lapin est petit.', 'Adjective'],
            ['Beau', 'Beautiful (masc)', 'C\'est un beau jour.', 'Adjective'],
            ['Belle', 'Beautiful (fem)', 'Elle est très belle.', 'Adjective'],
            ['Heureux', 'Happy', 'Je suis heureux.', 'Adjective'],
            ['Triste', 'Sad', 'Pourquoi es-tu triste?', 'Adjective'],
            ['Nouveau', 'New', 'J\'ai un nouveau téléphone.', 'Adjective'],
        ];

        foreach ($frenchCards as $c) {
            Card::create([
                'deck_id' => $frenchDeck->id,
                'front' => $c[0],
                'back' => $c[1],
                'example_sentence' => $c[2],
                'word_type' => $c[3],
            ]);
        }

        // ========================================
        // SPANISH STARTER DECK (25 words)
        // ========================================
        $spanishDeck = Deck::create([
            'name' => 'Spanish Essentials',
            'language' => 'es',
            'description' => 'Essential Spanish vocabulary for beginners.',
            'is_starter' => true
        ]);

        $spanishCards = [
            // Greetings
            ['Hola', 'Hello', '¡Hola, amigos!', 'Greeting'],
            ['Adiós', 'Goodbye', '¡Adiós, hasta mañana!', 'Greeting'],
            ['Buenos días', 'Good morning', 'Buenos días, señor.', 'Greeting'],
            ['Gracias', 'Thank you', 'Muchas gracias por todo.', 'Phrase'],
            ['Por favor', 'Please', 'Un café, por favor.', 'Phrase'],
            
            // Common Nouns
            ['La casa', 'The house', 'Mi casa es grande.', 'Noun'],
            ['El gato', 'The cat', 'El gato duerme.', 'Noun'],
            ['El perro', 'The dog', 'El perro corre en el parque.', 'Noun'],
            ['El agua', 'Water', 'Necesito agua.', 'Noun'],
            ['El libro', 'The book', 'Este libro es interesante.', 'Noun'],
            ['El sol', 'The sun', 'El sol brilla hoy.', 'Noun'],
            ['La luna', 'The moon', 'La luna es hermosa.', 'Noun'],
            
            // Verbs
            ['Comer', 'To eat', 'Quiero comer pizza.', 'Verb'],
            ['Beber', 'To drink', 'Voy a beber agua.', 'Verb'],
            ['Hablar', 'To speak', 'Hablo español.', 'Verb'],
            ['Amar', 'To love', 'Te amo mucho.', 'Verb'],
            ['Ir', 'To go', 'Voy a la tienda.', 'Verb'],
            ['Venir', 'To come', '¿Vienes conmigo?', 'Verb'],
            
            // Adjectives
            ['Grande', 'Big', 'Es muy grande.', 'Adjective'],
            ['Pequeño', 'Small', 'El ratón es pequeño.', 'Adjective'],
            ['Hermoso', 'Beautiful', '¡Qué día tan hermoso!', 'Adjective'],
            ['Feliz', 'Happy', 'Estoy muy feliz.', 'Adjective'],
            ['Triste', 'Sad', '¿Por qué estás triste?', 'Adjective'],
            ['Nuevo', 'New', 'Tengo un carro nuevo.', 'Adjective'],
            ['Bueno', 'Good', 'Es muy bueno.', 'Adjective'],
        ];

        foreach ($spanishCards as $c) {
            Card::create([
                'deck_id' => $spanishDeck->id,
                'front' => $c[0],
                'back' => $c[1],
                'example_sentence' => $c[2],
                'word_type' => $c[3],
            ]);
        }

        // ========================================
        // JAPANESE STARTER DECK (20 words)
        // ========================================
        $japaneseDeck = Deck::create([
            'name' => 'Japanese Basics',
            'language' => 'ja',
            'description' => 'Essential Japanese phrases and vocabulary.',
            'is_starter' => true
        ]);

        $japaneseCards = [
            // Greetings
            ['こんにちは', 'Hello', 'こんにちは、元気ですか？', 'Greeting'],
            ['さようなら', 'Goodbye', 'さようなら、また明日！', 'Greeting'],
            ['おはよう', 'Good morning', 'おはようございます。', 'Greeting'],
            ['ありがとう', 'Thank you', 'ありがとうございます。', 'Phrase'],
            ['すみません', 'Excuse me / Sorry', 'すみません、お水をください。', 'Phrase'],
            
            // Common Words
            ['猫 (ねこ)', 'Cat', '猫がかわいいです。', 'Noun'],
            ['犬 (いぬ)', 'Dog', '犬が走っています。', 'Noun'],
            ['水 (みず)', 'Water', '水を飲みたいです。', 'Noun'],
            ['本 (ほん)', 'Book', 'この本は面白いです。', 'Noun'],
            ['食べる (たべる)', 'To eat', '寿司を食べたいです。', 'Verb'],
            ['飲む (のむ)', 'To drink', 'お茶を飲みます。', 'Verb'],
            ['行く (いく)', 'To go', '学校に行きます。', 'Verb'],
            ['大きい (おおきい)', 'Big', 'この家は大きいです。', 'Adjective'],
            ['小さい (ちいさい)', 'Small', '猫は小さいです。', 'Adjective'],
            ['美味しい (おいしい)', 'Delicious', 'このラーメンは美味しい！', 'Adjective'],
            ['綺麗 (きれい)', 'Beautiful / Clean', '花が綺麗です。', 'Adjective'],
            ['嬉しい (うれしい)', 'Happy', '今日は嬉しいです。', 'Adjective'],
            ['はい', 'Yes', 'はい、そうです。', 'Phrase'],
            ['いいえ', 'No', 'いいえ、違います。', 'Phrase'],
            ['お願いします', 'Please', 'コーヒーをお願いします。', 'Phrase'],
        ];

        foreach ($japaneseCards as $c) {
            Card::create([
                'deck_id' => $japaneseDeck->id,
                'front' => $c[0],
                'back' => $c[1],
                'example_sentence' => $c[2],
                'word_type' => $c[3],
            ]);
        }

        // Update deck card counts
        $frenchDeck->update(['card_count' => count($frenchCards)]);
        $spanishDeck->update(['card_count' => count($spanishCards)]);
        $japaneseDeck->update(['card_count' => count($japaneseCards)]);

        $this->command->info('✅ Database seeded with:');
        $this->command->info('   - 1 test user (test@vocapop.com / password123)');
        $this->command->info('   - French deck: ' . count($frenchCards) . ' cards');
        $this->command->info('   - Spanish deck: ' . count($spanishCards) . ' cards');
        $this->command->info('   - Japanese deck: ' . count($japaneseCards) . ' cards');
    }
}
