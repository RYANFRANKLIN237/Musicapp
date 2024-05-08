package com.example.musicapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.adapters.CategoryAdapter
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.models.CategoryModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var categoryAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategories()
    }

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
}