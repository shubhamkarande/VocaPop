package com.vocapop.data.local

import com.vocapop.shared.cache.VocaPopDatabase

class DatabaseHelper(
    databaseDriverFactory: DatabaseDriverFactory
) {
    val database = VocaPopDatabase(databaseDriverFactory.createDriver())
    
    val queries = database.vocaPopDatabaseQueries
    
    fun clearDatabase() {
        queries.transaction {
            queries.deleteUser()
            queries.deleteAllDecks()
            queries.deleteAllCards()
            queries.deleteAllUserCards()
        }
    }
}
