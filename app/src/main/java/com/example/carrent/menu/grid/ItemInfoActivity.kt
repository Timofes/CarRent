package com.example.carrent.menu.grid

import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import com.example.carrent.ClassProject.DbHelper
import com.example.carrent.ClassProject.Sms
import com.example.carrent.ClassProject.TimeClass
import com.example.carrent.R
import com.example.carrent.menu.profile.ProfileActivity
import com.example.carrent.menu.sms.SmsActivity
import com.example.carrent.menu.time.TimeActivity
import java.io.IOException

class ItemInfoActivity : AppCompatActivity() {

    var time_now = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_info)

        val button1: ImageButton = findViewById(R.id.imageButton1)
        val button2: ImageButton = findViewById(R.id.imageButton2)
        val button3: ImageButton = findViewById(R.id.imageButton3)
        val button4: ImageButton = findViewById(R.id.imageButton4)

        val UserWho: TextView = findViewById(R.id.UserWhoAd)
        val name: TextView = findViewById(R.id.item_name_info)
        val login: TextView = findViewById(R.id.login_user_info)
        val price: TextView = findViewById(R.id.item_Price_info)
        val image: ImageView = findViewById(R.id.item_image_info)
        val rate: TextView = findViewById(R.id.item_rate_info)
        val addres: TextView = findViewById(R.id.addres_info)
        val body_type: TextView = findViewById(R.id.body_time_info)
        val year: TextView = findViewById(R.id.year_info)
        val description: TextView = findViewById(R.id.description_info)
        val TimeNumber: TextView = findViewById(R.id.Time_number)
        val UserButton: ImageButton = findViewById(R.id.imageButton)
        val bron: Button = findViewById(R.id.bron)

        val timeViews = arrayListOf<TextView>(
            findViewById(R.id.item_time1_info),
            findViewById(R.id.item_time2_info),
            findViewById(R.id.item_time3_info),
            findViewById(R.id.item_time4_info),
            findViewById(R.id.item_time5_info),
            findViewById(R.id.item_time6_info),
            findViewById(R.id.item_time7_info)
        )
        val Days = arrayListOf<String>("Пн","Вт","Ср","Чт","Пт","Сб","Вс")
        val DaysFull = arrayListOf<String>("Понедельник","Вторник","Среду","Четверг","Пятницу","Субботу","Воскресенье")

        val db = DbHelper(this,null)
        val id_car = intent.getStringExtra("itemName")
        val id_user = intent.getStringExtra("UserName")
        val car_user  = db.getCars(id_user?.toLong(),false)
        val User = db.infoUser_id(car_user[0].id_user.toString())
        val car = db.getCars_id(id_car.toString())

        val sharedPreferences = getSharedPreferences("id_user", Context.MODE_PRIVATE)
        val login_user = sharedPreferences.getString("login", "")

        if (login_user.toString() == User.login.toString()){
            UserWho.text = "Ваше объявление"
            bron.isVisible = false
        }else{
            UserWho.text = ""
            bron.isVisible = true
        }

        var Time: TimeClass
        Time = db.getTime(car[0].time_free.toString())
        val TextToTime = "Свободное время в "
        var DaysToNumber = ""
        DaysToNumber = Time.Monday
        if( DaysToNumber == "Empty") {
            DaysToNumber = "Нет"
            bron.isVisible = false
        }
        else if(DaysToNumber == "All"){
            DaysToNumber = "Любое"
            if (login_user.toString() != User.login.toString()){
            bron.isVisible = true}
        }
        else{
            if (login_user.toString() != User.login.toString()){
                bron.isVisible = true}
        }

        for (i in 0..6){
                val drawable = DrawableCompat.wrap(timeViews[i].background)
                DrawableCompat.setTint(drawable, Color.parseColor("#A9A3A3"))
        }

        TimeNumber.text = TextToTime + DaysFull[0] + ":\n " + DaysToNumber
        val drawable = DrawableCompat.wrap(timeViews[0].background)
        DrawableCompat.setTint(drawable, Color.parseColor("#564F4F"))

        login.text =  db.infoUser_id(car[0].id_user.toString()).login
        name.text = car[0].name + "\n" + car[0].model_type

        val drawable1 = GradientDrawable()
        drawable1.shape = GradientDrawable.RECTANGLE
        drawable1.cornerRadius = 16f // Радиус скругления в пикселях
        drawable1.setColor(ContextCompat.getColor(this, android.R.color.white)) //Цвет фона
        image.setImageDrawable(drawable1)
        image.clipToOutline = true

        car[0].image?.let { path ->
            val uri = Uri.parse(path)
            displayImageFromInternalStorage(image, uri)
        } ?: run {
            Toast.makeText(this, "Изображение не найдено", Toast.LENGTH_SHORT).show()
        }

        price.text = car[0].price.toString()
        rate.text = car[0].rating.toString()
        body_type.text = "Кузов: " + car[0].body_type
        addres.text = "Адрес: "+car[0].addres
        year.text = "Год выпуска: "+car[0].year.toString()
        description.text = "Описание: "+car[0].description

        var i = 0
        for(i in 0..6){
            timeViews[i].text = Days[i]
            timeViews[i].setOnClickListener{
                time_now = i
                val drawable = DrawableCompat.wrap(timeViews[i].background)
                DrawableCompat.setTint(drawable, Color.parseColor("#564F4F"))
                when(i){
                    0 ->{DaysToNumber = Time.Monday}
                    1 ->{DaysToNumber = Time.Tuesday}
                    2 ->{DaysToNumber = Time.Wednesday}
                    3 ->{DaysToNumber = Time.Thursday}
                    4 ->{DaysToNumber = Time.Friday}
                    5 ->{DaysToNumber = Time.Saturday}
                    6 ->{DaysToNumber = Time.Sunday}
                }
                if( DaysToNumber == "Empty") {
                    DaysToNumber = "Нет"
                    bron.isVisible = false
                }
                else if(DaysToNumber == "All"){
                    DaysToNumber = "Любое"
                    if (login_user.toString() != User.login.toString()){
                        bron.isVisible = true}
                }
                else{
                    if (login_user.toString() != User.login.toString()){
                        bron.isVisible = true}
                }
                TimeNumber.text = TextToTime + DaysFull[i] + ":\n " + DaysToNumber
                for (j in 0..6){
                    if (i != j){
                        val drawable = DrawableCompat.wrap(timeViews[j].background)
                        DrawableCompat.setTint(drawable, Color.parseColor("#A9A3A3"))
                    }
                }
            }
        }
        bron.setOnClickListener{
            var login_user_add = db.infoUser_id(car[0].id_user.toString()).login.toString()
            db.addSms(Sms(
                id = "0",
                login_user =  login_user.toString()  ,
                Type = "1",
                login_input = login_user_add,
                text = "Отправлен запрос на "+car[0].name+" "+car[0].model_type,
                time = "0"
            ))
            db.addSms(Sms(
                id = "0",
                login_user = login_user_add ,
                Type = "2",
                login_input = login_user.toString() ,
                text = "Запрос на "+car[0].name+" "+car[0].model_type,
                time = "На "+DaysFull[time_now]+" "+DaysToNumber
            ))
        }

        UserButton.setOnClickListener{

            val intent = Intent(this, Sell_User_Porfile::class.java)
            intent.putExtra("UserName", car[0].id_user.toString())
            startActivity(intent)
        }
        button1.setOnClickListener{
            val intent = Intent(this, MainWindowActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,button1, "grid")
            startActivity(intent,options.toBundle())
        }
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
    fun displayImageFromInternalStorage(imageView: ImageView, uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            imageView.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}