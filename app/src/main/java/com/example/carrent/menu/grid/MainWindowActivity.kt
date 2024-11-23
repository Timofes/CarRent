package com.example.carrent.menu.grid

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrent.menu.grid.bditems.Items_adapter
import com.example.carrent.R
import com.example.carrent.ClassProject.TimeClass
import com.example.carrent.ClassProject.items
import com.example.carrent.menu.profile.ProfileActivity
import com.example.carrent.menu.sms.SmsActivity
import com.example.carrent.menu.time.TimeActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.FileProvider
import com.example.carrent.ClassProject.DbHelper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainWindowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_window)

        val itemsList: RecyclerView = findViewById(R.id.object_list)
        var items = arrayListOf<items>()

        val button1: ImageButton = findViewById(R.id.imageButton1)
        val button2: ImageButton = findViewById(R.id.imageButton2)
        val button3: ImageButton = findViewById(R.id.imageButton3)
        val button4: ImageButton = findViewById(R.id.imageButton4)
        val Search: SearchView = findViewById(R.id.Search)

        val sharedPreferences = getSharedPreferences("id_user", Context.MODE_PRIVATE)
        val login_user = sharedPreferences.getString("login", "")

        val db_Cars = DbHelper(this, null)

        var time = TimeClass(
            Monday = "7:00-8:00",
            Tuesday = "Empty",
            Wednesday = "Empty",
            Thursday = "11:30-15:30",
            Friday = "Empty",
            Saturday = "Empty",
            Sunday = "Empty",
        )

        val id_user = db_Cars.Userinfoid(login_user.toString())
        var id_Time = db_Cars.addTime(time)

       // val uri = getUriFromDrawable(this, R.drawable.nocar)

       /* var car = items(1,id_user, "Lada", uri.toString(), 7000, "Седан", "Vesta", id_Time.toString(), 0.7,"Московская 23",2015,"Только от 10 лет стажа")
        var count_car = db_Cars.infoUser(login_user.toString()).count.toInt() + 1

        db_Cars.redUser("count", count_car.toString(), login_user.toString())

        db_Cars.addCars(car)



        time = TimeClass(
            Monday = "Empty",
            Tuesday = "All",
            Wednesday = "Empty",
            Thursday = "Empty",
            Friday = "Empty",
            Saturday = "5:00-18:00",
            Sunday = "Empty",
        )
        id_Time = db_Cars.addTime(time)
        car = items(1, id_user, "XCITE", "car", 15000, "Кроссовер", "X-CROSS 7", id_Time.toString(), 4.7, "Комсомольская 15",2024,"Абсолютно новая fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")
        count_car = db_Cars.infoUser(login_user.toString()).count.toInt() + 1
        db_Cars.redUser("count", count_car.toString(), login_user.toString())
        db_Cars.addCars(car)

        car = items(1,id_user, "Volkswagen", "polo", 9000, "Седан", "Polo", id_Time.toString(), 3.5,"Пролетарская 17",2017,"Машина")
        count_car = db_Cars.infoUser(login_user.toString()).count.toInt() + 1
        db_Cars.redUser("count", count_car.toString(), login_user.toString())
        db_Cars.addCars(car)*/

        items = db_Cars.getCars(null,true)

        if( items.isNotEmpty()) {
            itemsList.layoutManager = LinearLayoutManager(this)
            itemsList.adapter = Items_adapter(items, this)
        }

        Search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                items.clear()
                if(p0.toString().isNotEmpty()){
                    processSearchQuery(p0 ?: "")
                }else
                {
                    SearchQueryEmpty(p0 ?: "")
                }
                return true
            }

        })

        button2.setOnClickListener{
            val intent = Intent(this,SmsActivity::class.java)
            var options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, button2, "sms")
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,button1, "grid")
            startActivity(intent,options.toBundle())
        }
        button3.setOnClickListener{
            val intent = Intent(this, TimeActivity::class.java)
            var options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, button3, "time")
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,button1, "grid")
            startActivity(intent,options.toBundle())
        }
        button4.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            var options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, button4, "profile")
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,button1, "grid")
            startActivity(intent,options.toBundle())

            //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_left);
        }
    }
    fun getUriFromDrawable(context: Context, drawableResId: Int): Uri? {
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)
        return bitmap?.let {
            val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir) // временный файл
            val outputStream = FileOutputStream(tempFile)
            it.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) //сохраняем с качеством 100
            outputStream.flush()
            outputStream.close()
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempFile) //возвращаем Uri
        }
    }
    fun processSearchQuery(query: String) {
        val db = DbHelper(this,null)
        val itemsList: RecyclerView = findViewById(R.id.object_list)
        val items = arrayListOf<items>()
        val cars = db.getCars(null,true)
        for (i in cars.indices){
            val cars_name = cars[i].name+" "+cars[i].model_type
            if (query.lowercase() in cars_name.lowercase()){
                items.add(cars[i])
            }
        }
        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.adapter = Items_adapter(items, this)
    }
    fun SearchQueryEmpty(query: String) {
        val db = DbHelper(this,null)
        val itemsList: RecyclerView = findViewById(R.id.object_list)
        var items = arrayListOf<items>()
        val cars = db.getCars(null,true)
        items = cars
        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.adapter = Items_adapter(items, this)
    }
    fun displayImageFromInternalStorage(imageView: ImageView, uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            imageView.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
            // Обработка ошибки
        }
    }
}