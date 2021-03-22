package com.srs.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {
    @Insert
    fun insert(bookEntity: BookEntity)
    @Delete
    fun delete(bookEntity: BookEntity)

    @Query("SELECT * FROM Books")
    fun getAllBooks():List<BookEntity>

    @Query("SELECT * FROM Books WHERE book_id= :bookId" )
    fun getBookById(bookId:String):BookEntity
}