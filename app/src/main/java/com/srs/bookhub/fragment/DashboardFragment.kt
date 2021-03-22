package com.srs.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.android.volley.Request.Method.GET
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.srs.bookhub.R
import com.srs.bookhub.adapter.DashboardRecyclerAdapter
import com.srs.bookhub.model.Book
import com.srs.bookhub.util.ConnectionManager
import com.android.volley.Response


class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var btnCheckInternet: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout:RelativeLayout

    val bookInfoList = arrayListOf<Book>()

    /*val bookInfoList = arrayListOf<Book>(
        Book("1","P.S. I love You", "Cecelia Ahern",  "4.5","Rs. 299", R.drawable.ps_ily),
        Book("2","The Great Gatsby", "F. Scott Fitzgerald", "4.1", "Rs. 399", R.drawable.great_gatsby),
        Book("3","Anna Karenina", "Leo Tolstoy",  "4.3", "Rs. 199",R.drawable.anna_kare),
        Book("4","Madame Bovary", "Gustave Flaubert", "4.0", "Rs. 500", R.drawable.madame),
        Book("5","War and Peace", "Leo Tolstoy", "4.8", "Rs. 249", R.drawable.war_and_peace),
        Book("6","Lolita", "Vladimir Nabokov",  "3.9","Rs. 349", R.drawable.lolita),
        Book("7","Middlemarch", "George Eliot",  "4.2","Rs. 599", R.drawable.middlemarch),
        Book("8","The Adventures of Huckleberry Finn", "Mark Twain", "4.5", "Rs. 699", R.drawable.adventures_finn),
        Book("9","Moby-Dick", "Herman Melville", "4.5","Rs. 499",  R.drawable.moby_dick),
        Book("10","The Lord of the Rings", "J.R.R Tolkien",  "5.0", "Rs. 749",R.drawable.lord_of_rings)
    )*/
    lateinit var recyclerAdapter: DashboardRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressLayout.visibility=View.VISIBLE
        layoutManager = LinearLayoutManager(activity)
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v1/book/fetch_books"
            val jsonObjectRequest = object : JsonObjectRequest(GET, url, null,
                Response.Listener {
                    // println("Response is $it")
                    try {
                        progressLayout.visibility=View.GONE
                        val success = it.getBoolean("success")

                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")
                                )
                                bookInfoList.add(bookObject)
                                recyclerAdapter =
                                    DashboardRecyclerAdapter(activity as Context, bookInfoList)
                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager
                               /* recyclerDashboard.addItemDecoration(
                                    DividerItemDecoration(
                                        recyclerDashboard.context,
                                        (layoutManager as LinearLayoutManager).orientation
                                    )
                                )*/

                            }

                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: Occurred", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener {
                    // println("Error is $it")
                    Toast.makeText(context, "Volly Error", Toast.LENGTH_SHORT).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["content-type"] = "application/json"
                    headers["token"] = "beb07c5a3a3920"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        } else {

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Failure")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listner ->
                ActivityCompat.finishAffinity(activity as Activity)

            }
            dialog.create()
            dialog.show()


        }
        return view
    }


}
