package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.adapters.CategoryAdapter
import com.example.musicapp.adapters.SectionSongLIstAdapter
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.models.CategoryModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var categoryAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategories()
        setupSection()
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
    private fun setupSection(){
        FirebaseFirestore.getInstance().collection("sections")
            .document("section_1")
            .get().addOnSuccessListener {
                val section = it.toObject(CategoryModel::class.java)
                section?.apply {
                    binding.section1MainLayout.visibility = View.VISIBLE
                    binding.section1Title.text = name
                    binding.section1RecyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                    binding.section1RecyclerView.adapter = SectionSongLIstAdapter(songs)
                    binding.section1MainLayout.setOnClickListener {
                        SongsListActivity.category = section
                        startActivity(Intent(this@MainActivity,SongsListActivity::class.java))
                    }
                }
            }
    }
}