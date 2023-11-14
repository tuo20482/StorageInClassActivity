package com.example.networkapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject

private const val COMIC_TITLE_KEY = "comic_title"
private const val COMIC_DESCRIPTION_KEY = "comic_description"
private const val COMIC_IMAGE_URL_KEY = "comic_image_url"

class MainActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var requestQueue: RequestQueue
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var numberEditText: EditText
    private lateinit var showButton: Button
    private lateinit var comicImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = getPreferences(MODE_PRIVATE)

        requestQueue = Volley.newRequestQueue(this)

        titleTextView = findViewById(R.id.comicTitleTextView)
        descriptionTextView = findViewById(R.id.comicDescriptionTextView)
        numberEditText = findViewById(R.id.comicNumberEditText)
        showButton = findViewById(R.id.showComicButton)
        comicImageView = findViewById(R.id.comicImageView)

        showButton.setOnClickListener {
            downloadComic(numberEditText.text.toString())
        }
        loadComic()
    }

    private fun downloadComic(comicId: String) {
        requestQueue.add(
            JsonObjectRequest(
                "https://xkcd.com/$comicId/info.0.json",
                { showComic(it)
                saveComic(it)},
                {})
        )
    }

    private fun showComic(comicObject: JSONObject) {
        titleTextView.text = comicObject.getString("title")
        descriptionTextView.text = comicObject.getString("alt")
        Picasso.get().load(comicObject.getString("img")).into(comicImageView)
    }

    private fun saveComic(comicObject: JSONObject) {
        with(preferences.edit()) {
            putString(COMIC_TITLE_KEY, comicObject.getString("title"))
            putString(COMIC_DESCRIPTION_KEY, comicObject.getString("alt"))
            putString(COMIC_IMAGE_URL_KEY, comicObject.getString("img"))
            apply()
        }
    }

    private fun loadComic() {
        with(preferences) {
            titleTextView.text = getString(COMIC_TITLE_KEY, "Title")
            descriptionTextView.text = getString(COMIC_DESCRIPTION_KEY, "Description")
            getString(COMIC_IMAGE_URL_KEY, null)?.let {
                Picasso.get().load(it).into(comicImageView)
            }
        }
    }


}