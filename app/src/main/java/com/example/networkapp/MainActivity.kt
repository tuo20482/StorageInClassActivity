package com.example.networkapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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

        titleTextView = findViewById<TextView>(R.id.comicTitleTextView)
        descriptionTextView = findViewById<TextView>(R.id.comicDescriptionTextView)
        numberEditText = findViewById<EditText>(R.id.comicNumberEditText)
        showButton = findViewById<Button>(R.id.showComicButton)
        comicImageView = findViewById<ImageView>(R.id.comicImageView)

        showButton.setOnClickListener {
            downloadComic(numberEditText.text.toString())
        }
        loadComic()
    }

    private fun downloadComic (comicId: String) {
        val url = "https://xkcd.com/$comicId/info.0.json"
        requestQueue.add (
            JsonObjectRequest(url, {showComic(it)}, {
            })
        )
    }

    private fun showComic (comicObject: JSONObject) {
        titleTextView.text = comicObject.getString("title")
        descriptionTextView.text = comicObject.getString("alt")
        Picasso.get().load(comicObject.getString("img")).into(comicImageView)
        saveComic(
            comicObject.getString("title"),
            comicObject.getString("alt"),
            comicObject.getString("img")
        )
    }

    private fun saveComic(title: String, description: String, imageUrl: String) {
        with(preferences.edit()) {
            putString(COMIC_TITLE_KEY, title)
            putString(COMIC_DESCRIPTION_KEY, description)
            putString(COMIC_IMAGE_URL_KEY, imageUrl)
            apply()
        }
    }

    private fun loadComic() {
        with (preferences) {
            titleTextView.text = getString(COMIC_TITLE_KEY, "Title")
            descriptionTextView.text = getString(COMIC_DESCRIPTION_KEY, "Description")
            getString(COMIC_IMAGE_URL_KEY, null)?.let {
                Picasso.get().load(it).into(comicImageView)
            }
        }
    }


}