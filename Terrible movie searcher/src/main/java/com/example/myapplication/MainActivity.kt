package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button

    private val queue: RequestQueue by lazy { Volley.newRequestQueue(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        searchEditText = findViewById(R.id.txtSearch)
        searchButton = findViewById(R.id.btnSearch)

        searchButton.setOnClickListener { search() }
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
                true
            } else {
                false
            }
        }
    }

    override fun onStop() {
        super.onStop()
        queue.cancelAll(REQUEST_TAG)
    }
    override fun onRestart() {
        super.onRestart()
        searchEditText.text.clear()
    }

    private fun search() {
        val query = searchEditText.text.toString().trim()
        if (query.isEmpty()) {
            showToast(R.string.empty_search)
            return
        }
        if (BuildConfig.OMDB_API_KEY.isBlank()) {
            showToast(R.string.missing_api_key)
            return
        }

        val url = Uri.Builder()
            .scheme("https")
            .authority("www.omdbapi.com")
            .appendQueryParameter("apikey", BuildConfig.OMDB_API_KEY)
            .appendQueryParameter("s", query)
            .build()
            .toString()

        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response -> handleResponse(response) },
            { showToast(R.string.network_error) }
        )
        request.tag = REQUEST_TAG
        request.setShouldCache(false)
        queue.add(request)
    }

    private fun handleResponse(response: JSONObject) {
        val found = response.optString("Response") == "True"
        val first = response.optJSONArray("Search")?.optJSONObject(0)
        if (!found || first == null) {
            showToast(R.string.no_movies_found)
            return
        }

        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(Extras.TITLE, first.optString("Title"))
            putExtra(Extras.POSTER_URL, first.optString("Poster"))
        }
        startActivity(intent)
    }

    private fun showToast(messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }

    private companion object {
        const val REQUEST_TAG = "search"
    }
}
