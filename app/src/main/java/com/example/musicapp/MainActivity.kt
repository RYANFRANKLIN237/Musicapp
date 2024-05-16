package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicapp.adapters.CategoryAdapter
import com.example.musicapp.adapters.SectionSongLIstAdapter
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.models.CategoryModel
import com.example.musicapp.models.SongModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var categoryAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategories()
        setupSection("section_1",binding.section1MainLayout,binding.section1Title,binding.section1RecyclerView)
        setupSection("section_2",binding.section2MainLayout,binding.section2Title,binding.section2RecyclerView)
        setupMostlyPlayed("mostly_played",binding.mostlyPlayedMainLayout,binding.mostlyPlayedTitle,binding.mostlyPlayedRecyclerView)
    }

    override fun onResume() {
        super.onResume()
        showPlayerView()
    }

    private fun showPlayerView(){
        binding.playerView.setOnClickListener {
            startActivity(Intent(this,PlayerActivity::class.java))
        }
        MyExoplayer.getCurrentSong()?.let {
            binding.playerView.visibility = View.VISIBLE
            binding.songTitleTextView.text= "Now Playing : " + it.title
            Glide.with(binding.songCoverImageView).load(it.coverUrl)
                .apply(
                    RequestOptions().transform(RoundedCorners(32))
                )
                .into(binding.songCoverImageView)
        } ?: run {
            binding.playerView.visibility = View.GONE
        }
    }

    //categories
    private fun getCategories(){
        //to return all documents in category collection in firestore
      FirebaseFirestore.getInstance().collection("category")
          .get().addOnSuccessListener {
              val categoryList= it.toObjects(CategoryModel::class.java)
              setupCategoryRecyclerView(categoryList)
          }
    }

    private fun setupCategoryRecyclerView(categoryList: List<CategoryModel>){
        categoryAdapter = CategoryAdapter(categoryList)

        //make recycler view horizontal
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        binding.categoriesRecyclerView.adapter = categoryAdapter
    }

    //sections
    private fun setupSection(id: String, mainLayout: RelativeLayout, titleView: TextView,recyclerView: RecyclerView){
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get().addOnSuccessListener {
                val section = it.toObject(CategoryModel::class.java)
                section?.apply {
                    mainLayout.visibility = View.VISIBLE
                    titleView.text = name
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                    recyclerView.adapter = SectionSongLIstAdapter(songs)
                    mainLayout.setOnClickListener {
                        SongsListActivity.category = section
                        startActivity(Intent(this@MainActivity,SongsListActivity::class.java))
                    }
                }
            }
    }

    private fun setupMostlyPlayed(id: String, mainLayout: RelativeLayout, titleView: TextView,recyclerView: RecyclerView){
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get().addOnSuccessListener {
                //get most played songs
                FirebaseFirestore.getInstance().collection("songs")
                    .orderBy("count", Query.Direction.DESCENDING)
                    .limit(5)
                    .get().addOnSuccessListener {songListSnapshot->
                      val songModelList = songListSnapshot.toObjects<SongModel>()
                        val songsIdList =songModelList.map {
                            it.id
                        }.toList()
                        val section = it.toObject(CategoryModel::class.java)
                        section?.apply {
                            section.songs = songsIdList
                            mainLayout.visibility = View.VISIBLE
                            titleView.text = name
                            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                            recyclerView.adapter = SectionSongLIstAdapter(songs)
                            mainLayout.setOnClickListener {
                                SongsListActivity.category = section
                                startActivity(Intent(this@MainActivity,SongsListActivity::class.java))
                            }
                        }
                    }

            }
    }
}