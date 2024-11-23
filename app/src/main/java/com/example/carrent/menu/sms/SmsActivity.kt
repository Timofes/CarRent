package com.example.carrent.menu.sms

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrent.ClassProject.DbHelper
import com.example.carrent.ClassProject.Sms
import com.example.carrent.ClassProject.Sms_mini
import com.example.carrent.R
import com.example.carrent.menu.grid.MainWindowActivity
import com.example.carrent.menu.grid.bditems.Items_adapter
import com.example.carrent.menu.profile.ProfileActivity
import com.example.carrent.menu.time.TimeActivity

class SmsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms)

        val itemsList: RecyclerView = findViewById(R.id.object_list_time)

        val button1: ImageButton = findViewById(R.id.imageButton1_sms)
        val button2: ImageButton = findViewById(R.id.imageButton2_sms)
        val button3: ImageButton = findViewById(R.id.imageButton3_sms)
        val button4: ImageButton = findViewById(R.id.imageButton4_sms)

        val sharedPreferences = getSharedPreferences("id_user", Context.MODE_PRIVATE)
        val login_user = sharedPreferences.getString("login", "")

        val db = DbHelper(this,null)

        val item = arrayListOf<Any>()
        val items = db.getSms(login_user.toString())
        var i = items.count()-1
        while(i >= 0){
            if (items[i].Type == "1"){
                item.add(Sms(items[i].id,items[i].login_user,items[i].Type,items[i].login_input,items[i].text,items[i].time))
            }
            else{
                item.add(Sms_mini(items[i].id,items[i].login_user,items[i].login_input,items[i].Type,items[i].text,items[i].time))
            }
            i--
        }

        if( items.isNotEmpty()) {
            itemsList.layoutManager = LinearLayoutManager(this)
            itemsList.adapter = sms_adapter(item, this)
        }


        button1.setOnClickListener{
            val intent = Intent(this, MainWindowActivity::class.java)
            var options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, button1, "grid")
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,button2, "sms")
            startActivity(intent,options.toBundle())
        }
        button3.setOnClickListener{
            val intent = Intent(this, TimeActivity::class.java)
            var options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, button3, "time")
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,button2, "sms")
            startActivity(intent,options.toBundle())
        }
        button4.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            var options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, button4, "profile")
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,button2, "sms")
            startActivity(intent,options.toBundle())
        }
    }
}