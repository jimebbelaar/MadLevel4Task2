package com.example.madlevel4task2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.madlevel4task2.model.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM game_table")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)

    @Query("DELETE FROM game_table")
    suspend fun deleteAllGames()

    @Query("SELECT COUNT(*) FROM game_table WHERE result = 1")
    suspend fun getNumberOfWins(): Int

    @Query("SELECT COUNT(*) FROM game_table WHERE result = 0")
    suspend fun getNumberOfDraws(): Int

    @Query("SELECT COUNT(*) FROM game_table WHERE result = -1")
    suspend fun getNumberOfLosses(): Int
}