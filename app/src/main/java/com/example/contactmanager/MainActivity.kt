package com.example.contactmanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val Contact_Permission_Request = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CheckForPermission(Manifest.permission.READ_CONTACTS, "contact", Contact_Permission_Request)
        setContentView(R.layout.activity_main)
        ContactLoad()
//        val ltn = findViewById<LinearLayout>(R.id.linear)
//        ltn.setOnClickListener(){
//            Toast.makeText(this,"click happened ",Toast.LENGTH_SHORT).show()
//
//        }
        val fabBtn = findViewById<FloatingActionButton>(R.id.fab)
        fabBtn.setOnClickListener(){
            val intent = Intent(this@MainActivity, MainActivity2::class.java)

            startActivity(intent)
            Toast.makeText(this, "add contact pressed", Toast.LENGTH_SHORT).show()
        }




    }

    //permission related code


    fun CheckForPermission(permission: String, name: String, requestcode: Int){
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
          when{
              ContextCompat.checkSelfPermission(applicationContext, permission)== PackageManager.PERMISSION_GRANTED ->{
                  Toast.makeText(this, "$name  Permission Granted", Toast.LENGTH_SHORT).show()

              }
              shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestcode)

              else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestcode)
          }
      }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fun innercheck(name: String){
            if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "$name permission refused ", Toast.LENGTH_SHORT).show()
            }else{ Toast.makeText(applicationContext, "$name permission granted ", Toast.LENGTH_SHORT).show()

            }
        }
        when (requestCode){
            Contact_Permission_Request -> innercheck("contact")

        }
    }

    private fun  showDialog(permission: String, name: String, requestcode: Int){
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission Required")
            setPositiveButton("ok"){ dialog, which ->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestcode)
            }

        }
        val dialog = builder.create()
        dialog.show()
    }

    fun ContactLoad(){
        val ContactList :MutableList<ContactDTO> = ArrayList()
        val Contact_list = findViewById<RecyclerView>(R.id.Contact_list)
        Contact_list.layoutManager = LinearLayoutManager(this)

        val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        if (contacts != null) {
            while (contacts.moveToNext()){
                val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val obj = ContactDTO()
                obj.name = name
                obj.number = number
                ContactList.add(obj)
//                val photo_Uri = contacts.getString((contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)))
//                if (photo_Uri != null){
//                     obj.image = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(photo_Uri))
//                }



            }
            Contact_list.adapter = ContactAdapter(ContactList)

            contacts.close()
        }

    }

    // menu operations

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.refresh_Btn -> {
                Toast.makeText(this, "Refresh is pressed", Toast.LENGTH_SHORT).show()
                ContactLoad()
            }
            R.id.add -> {
                val intent = Intent(this@MainActivity, MainActivity2::class.java)

                startActivity(intent)
                Toast.makeText(this, "add contact pressed", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    fun onItemClick(item :ContactDTO , position: Int){

    }

    //Contact adapter operations



    class ContactAdapter(private val list: MutableList<ContactDTO>) :
            RecyclerView.Adapter<ContactAdapter.ViewHolder>() {


        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.name)
            val number: TextView = view.findViewById(R.id.number)

        }
        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.text_row_item, viewGroup, false)


            return ViewHolder(view)
        }


        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.name.text = list[position].name
            viewHolder.number.text = list[position].number



        }
        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = list.size

    }




}
