package com.example.contactmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val btn = findViewById<Button>(R.id.buttonSave)
        btn.setOnClickListener {
            addContact()
//
//        val intent = Intent(this@MainActivity2, MainActivity::class.java)
//
//        startActivity(intent)
        }
    }
    fun addContact() {
        val etContactName: EditText = findViewById(R.id.etName)
        val etContactNumber: EditText = findViewById(R.id.etNumber)
        val name: String = etContactName.text.toString()
        val phone = etContactNumber.text.toString()
        val intent = Intent(ContactsContract.Intents.Insert.ACTION)
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE)
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name)
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone)
        startActivityForResult(intent, 1)

    }

}
