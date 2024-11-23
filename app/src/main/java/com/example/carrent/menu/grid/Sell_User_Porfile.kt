package com.example.carrent.menu.grid

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.carrent.ClassProject.DbHelper
import com.example.carrent.R
import com.example.carrent.menu.profile.ProfileActivity
import com.example.carrent.menu.sms.SmsActivity
import com.example.carrent.menu.time.TimeActivity

class Sell_User_Porfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_user_porfile)

        val button1: ImageButton = findViewById(R.id.imageButton1_profile_cell)
        val button2: ImageButton = findViewById(R.id.imageButton2_profile_cell)
        val button3: ImageButton = findViewById(R.id.imageButton3_profile_cell)
        val button4: ImageButton = findViewById(R.id.imageButton4_profile_cell)

        var UserWho: TextView = findViewById(R.id.UserWho)
        val icon:  ImageView = findViewById(R.id.profile_icon_cell)
        val login: TextView = findViewById(R.id.profile_login_cell)
        val email: TextView = findViewById(R.id.profile_Email_cell)
        val firstName: TextView = findViewById(R.id.profile_firstName_cell)
        val lastName: TextView = findViewById(R.id.profile_lastName_cell)
        val surName: TextView = findViewById(R.id.profile_surName_cell)
        val law: TextView = findViewById(R.id.profile_law_cell)
        val rate: TextView = findViewById(R.id.profile_rate_cell)
        val ad: TextView = findViewById(R.id.profile_ad_cell)
        val count: TextView = findViewById(R.id.profile_count_cell)

        val db = DbHelper(this,null)
        val id_user = intent.getStringExtra("UserName")
        val User = db.infoUser_id(id_user.toString())

        val sharedPreferences = getSharedPreferences("id_user", MODE_PRIVATE)
        val login_user = sharedPreferences.getString("login", "")

        if (login_user.toString() == User.login.toString()){
            UserWho.text = "Ваш аккаунт"
        }else{
            UserWho.text = ""
        }

        login.text = User.login
        email.text = User.email
        firstName.text = User.firstname
        lastName.text = User.lastname
        surName.text = User.surname
        law.text = User.law
        rate.text = User.rate
        ad.text = User.ad
        count.text = User.count
        val textLaw = if (User.law == "true")
            "Подтверждены"
        else
            "Отсутсвуют"

        law.text = "Водительские права: "+textLaw
        rate.text = "Рейтинг: "+User.rate
        var textad = ""
        if (User.ad == "true"){
            textad = "Можно"
        }
        else{
            textad = "Нельзя"
        }
        ad.text = "Выкладывать объявления: "+textad
        count.text = "Количетсво автомобилей: "+User.count

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
}