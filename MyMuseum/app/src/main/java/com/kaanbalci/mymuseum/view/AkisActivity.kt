package com.kaanbalci.mymuseum.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.kaanbalci.mymuseum.R
import com.kaanbalci.mymuseum.adapter.FeedRecyclerAdapter
import com.kaanbalci.mymuseum.databinding.ActivityAkisBinding
import com.kaanbalci.mymuseum.model.Post

class AkisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAkisBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var feedAdapter: FeedRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkisBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        db = Firebase.firestore
        postArrayList = ArrayList<Post>()

        getData()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = feedAdapter

        val myToolbar: Toolbar = findViewById(R.id.myToolbar)
        setSupportActionBar(myToolbar)
        window.statusBarColor = ContextCompat.getColor(this, R.color.asıl)

    }
    private fun getData(){
        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if (value != null){
                    if (!value.isEmpty){
                       val documnts = value.documents

                        postArrayList.clear()

                        for (document in documnts){

                            val comment = document.get("comment") as String
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String

                            val post = Post(userEmail,comment,downloadUrl)
                            postArrayList.add(post)
                        }

                        feedAdapter.notifyDataSetChanged()

                    }
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menum,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addpost){
            val intent = Intent(this, YuklemeActivity::class.java)
            startActivity(intent)
        }
        else if (item.itemId == R.id.singout){
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if (item.itemId == R.id.profile){
            val intent = Intent(this, ProfilActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}