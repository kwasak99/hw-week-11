package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ResultActivity : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var posterImageView: ImageView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        titleTextView = findViewById(R.id.txtMovieTitle)
        posterImageView = findViewById(R.id.imgPoster)
        backButton = findViewById(R.id.btnBack)

        titleTextView.text = intent.getStringExtra(Extras.TITLE).orEmpty()
        loadPoster(intent.getStringExtra(Extras.POSTER_URL))

        backButton.setOnClickListener { finish() }
    }

    private fun loadPoster(posterUrl: String?) {
        if (posterUrl.isNullOrBlank() || posterUrl == "N/A") {
            posterImageView.setImageResource(R.drawable.ic_no_poster)
            return
        }
        Picasso.get()
            .load(posterUrl)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_no_poster)
            .fit()
            .centerInside()
            .into(posterImageView, object : Callback {
                override fun onSuccess() = Unit

                override fun onError(e: Exception) {
                    Toast.makeText(
                        this@ResultActivity,
                        R.string.poster_load_error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
