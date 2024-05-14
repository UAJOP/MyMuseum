package com.kaanbalci.mymuseum.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.kaanbalci.mymuseum.R
import com.kaanbalci.mymuseum.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        window.statusBarColor = ContextCompat.getColor(this, R.color.asıl)

        val guncelkullanici = auth.currentUser
        if(guncelkullanici != null){
            val intent = Intent(this, AkisActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun giristikla(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.sifreTextPassword.text.toString()
        if (email.equals("") || password.equals("")){
            Toast.makeText(this,"Lütfen bilgileri doldurun !",Toast.LENGTH_LONG).show()
        }
        else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent = Intent(this@MainActivity, AkisActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }



    }

    fun kayitOlTikla(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.sifreTextPassword.text.toString()

        if(email.equals("") || password.equals("")) {
            Toast.makeText(this,"Lütfen bilgileri doldurun !",Toast.LENGTH_LONG).show()
        }
        else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                // BURDA DEĞERLERİN BOŞ OLUP OLMADIĞINI KONTROL ETTİKTEN SONRA BU DEĞERLERİ DATABASEYE GÖNDERİYO
                // VE KONTROL İŞLEMERİNİ YAPIYOR BAŞARILIYSA AKIŞ SAYFASINA DEĞİLSE HATA VERİYOR
                val intent = Intent(this@MainActivity, AkisActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }


    }



}