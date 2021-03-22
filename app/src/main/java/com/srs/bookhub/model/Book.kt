package com.srs.bookhub.model

import android.media.Image

data class Book(
    var bookId:String,
    var bookName:String,
    var bookAuthor:String,
    var bookRating:String,
    var bookPrice:String,

    var bookImage: String)

{

}