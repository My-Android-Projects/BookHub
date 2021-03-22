package com.srs.bookhub.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response

import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.srs.bookhub.R
import com.srs.bookhub.database.BookDatabase
import com.srs.bookhub.database.BookEntity
import com.srs.bookhub.util.ConnectionManager
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {
    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView

    lateinit var btnAddToFav: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolBar: Toolbar

    var bookId: String? = "100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDesc = findViewById(R.id.txtBookDesc)

        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.title = "Book Details"

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")

            /*progressLayout.visibility=View.GONE
            progressBar.visibility= View.GONE
            println("book_id: $bookId")
            txtBookDesc.text=bookId*/

            Toast.makeText(this, "bookId: $bookId", Toast.LENGTH_LONG).show()


        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some Unexpected error occurred",
                Toast.LENGTH_LONG
            ).show()

        }
        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some Unexpected error occurred",
                Toast.LENGTH_LONG
            ).show()

        }
        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {

            val queue = Volley.newRequestQueue(this@DescriptionActivity)
            val url = "http://13.235.250.119/v1/book/get_book/"
            val jsonParams = JSONObject()
            jsonParams.put("book_id", bookId)
            val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                Response.Listener {
                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE
                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(imgBookImage)

                            txtBookName.text = bookJsonObject.getString("name")
                            txtBookAuthor.text = bookJsonObject.getString("author")
                            txtBookPrice.text = bookJsonObject.getString("price")
                            txtBookRating.text = bookJsonObject.getString("rating")
                            txtBookDesc.text = bookJsonObject.getString("description")
                            val bookImgUrl = bookJsonObject.getString("image")
                            val bookEntity = BookEntity(
                                bookId?.toInt() as Int,
                                txtBookName.text.toString(),
                                txtBookAuthor.text.toString(),
                                txtBookPrice.text.toString(),
                                txtBookRating.text.toString(),
                                txtBookDesc.text.toString(),
                                bookImgUrl
                            )
                            val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                            val isFav = checkFav.get()
                            if (isFav) {
                                btnAddToFav.text = "Remove from Favourites"
                                val favColor = ContextCompat.getColor(
                                    applicationContext,
                                    R.color.colorFavourite
                                )
                                btnAddToFav.setBackgroundColor(favColor)
                            } else {
                                btnAddToFav.text = "Add to Favourites"
                                val noFavColor =
                                    ContextCompat.getColor(applicationContext, R.color.primary)
                                btnAddToFav.setBackgroundColor(noFavColor)
                            }
                            btnAddToFav.setOnClickListener {


                                if (!DBAsyncTask(applicationContext, bookEntity, 1).execute().get()) //if not in fav
                                {
                                    println("add to favourite: $bookId")
                                    val async=DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                    val result=async.get()
                                    println("result: $result")
                                    if(result)
                                    {
                                        //Toast.makeText(this@DescriptionActivity,"Book added to favourite",Toast.LENGTH_LONG).show()
                                        btnAddToFav.text = "Remove from Favourites"
                                        val favColor = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.colorFavourite
                                        )
                                        println("favColor: $favColor")
                                        btnAddToFav.setBackgroundColor(favColor)
                                    }
                                    else
                                    {
                                        Toast.makeText(this@DescriptionActivity,"Error Occurred",Toast.LENGTH_LONG).show()

                                    }
                                }
                                else
                                {
                                    val async=DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                    val result=async.get()
                                    if(result)
                                    {
                                        //Toast.makeText(this@DescriptionActivity,"Book removed from favourite",Toast.LENGTH_LONG).show()
                                        btnAddToFav.text = "Add to Favourites"
                                        val noFavColor =
                                            ContextCompat.getColor(applicationContext, R.color.primary)
                                        btnAddToFav.setBackgroundColor(noFavColor)
                                    }
                                    else
                                    {
                                        Toast.makeText(this@DescriptionActivity,"Error Occurred",Toast.LENGTH_LONG).show()

                                    }
                                }
                            }


                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Error: Occurred",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Error: Occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Volley Error: Occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["content-type"] = "application/json"
                    headers["token"] = "beb07c5a3a3920"
                    return headers
                }
            }
            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Failure")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, listner ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)

            }
            dialog.create()
            dialog.show()

        }


    } //onCreate


    //sub class
    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        /*
        Mode 1->Check DB if book is favorite or not
        Mode 2->Save the book into DB as favourite
        Mode 3->Remove the favourite book
         */

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null
                    //Check DB if book is favorite or not
                }
                2 -> {
                    //Save the book into DB as favourite
                    db.bookDao().insert(bookEntity)
                    db.close()
                    return true
                }
                3 -> {
                    //Remove the favourite book
                    db.bookDao().delete(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }
}