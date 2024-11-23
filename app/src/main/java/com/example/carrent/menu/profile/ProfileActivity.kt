package com.example.carrent.menu.profile

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.carrent.ClassProject.DbHelper
import com.example.carrent.R
import com.example.carrent.login.MainActivity
import com.example.carrent.menu.grid.MainWindowActivity
import com.example.carrent.menu.sms.SmsActivity
import com.example.carrent.menu.time.TimeActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val button1: ImageButton = findViewById(R.id.imageButton1_profile)
        val button2: ImageButton = findViewById(R.id.imageButton2_profile)
        val button3: ImageButton = findViewById(R.id.imageButton3_profile)
        val button4: ImageButton = findViewById(R.id.imageButton4_profile)

        val buttonexit : Button = findViewById(R.id.buttonexit)
        val SettingLaw : ImageButton = findViewById(R.id.imageButtonlaw)
        val SettingAd : ImageButton = findViewById(R.id.imageButtonad)

        val icon:  ImageView = findViewById(R.id.profile_icon)
        val login: TextView = findViewById(R.id.profile_login)
        val email: TextView = findViewById(R.id.profile_Email)
        val firstName: TextView = findViewById(R.id.profile_firstName)
        val lastName: TextView = findViewById(R.id.profile_lastName)
        val surName: TextView = findViewById(R.id.profile_surName)
        val law: TextView = findViewById(R.id.profile_law)
        val rate: TextView = findViewById(R.id.profile_rate)
        val ad: TextView = findViewById(R.id.profile_ad)
        val count: TextView = findViewById(R.id.profile_count)

        val sharedPreferences = getSharedPreferences("id_user", Context.MODE_PRIVATE)
        val login_user = sharedPreferences.getString("login", "")

        val db = DbHelper(this,null)
        val info_user = db.infoUser(login_user.toString())

        login.text = info_user.login
        email.text = info_user.email
        firstName.text = info_user.firstname
        lastName.text = info_user.lastname
        surName.text = info_user.surname
        email.text = info_user.email
        val textLaw = if (info_user.law == "true")
            "Подтверждены"
        else
            "Отсутсвуют"

        law.text = "Водительские права: "+textLaw
        rate.text = "Рейтинг: "+info_user.rate
        var textad = ""
        if (info_user.ad == "true"){
            textad = "Можно"
        }
        else{
            textad = "Нельзя"
        }
        ad.text = "Выкладывать объявления: "+textad
        count.text = "Количество автомобилей: "+info_user.count

        buttonexit.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Подтверждение")
                .setMessage("Вы уверены, что хотите выйти из аккаунта?")
                .setPositiveButton("Да", DialogInterface.OnClickListener { dialog, id ->
                    val shared = getSharedPreferences("user_id", Context.MODE_PRIVATE)
                    if ((firstName.text.toString() != info_user.firstname)or
                        (surName.text.toString() != info_user.surname)or
                        (lastName.text.toString() != info_user.lastname)or
                        (email.text.toString() != info_user.email)) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Подтверждение")
                            .setMessage("Вы хотите сохранить настройки профиля?")
                            .setPositiveButton("Да", DialogInterface.OnClickListener { dialog, id ->
                                var text = firstName.text.toString()
                                db.redUser("firstname", text, info_user.login)
                                text = surName.text.toString()
                                db.redUser("surname", text, info_user.login)
                                text = lastName.text.toString()
                                db.redUser("lastname", text, info_user.login)
                                text = email.text.toString()
                                db.redUser("email", text, info_user.login)
                                val dialog = builder.create()
                                dialog.show()
                                with(shared.edit()) {
                                    remove("user_id")
                                    apply()
                                }

                                val sharedPreferences =
                                    getSharedPreferences("Save_open", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("log", "false")
                                editor.apply()

                                val intent = Intent(this, MainActivity::class.java)
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)
                                finish()

                            })
                            .setNegativeButton(
                                "Нет",
                                DialogInterface.OnClickListener { dialog, id ->
                                    val dialog = builder.create()
                                    dialog.show()
                                    with(shared.edit()) {
                                        remove("user_id")
                                        apply()
                                    }

                                    val sharedPreferences =
                                        getSharedPreferences("Save_open", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putString("log", "false")
                                    editor.apply()

                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(intent)
                                    finish()
                                })
                    }
                    else{
                        val sharedPreferences =
                            getSharedPreferences("Save_open", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("log", "false")
                        editor.apply()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                })
                .setNegativeButton("Нет", DialogInterface.OnClickListener { dialog, id ->

                })

            val dialog = builder.create()
            dialog.show()
        }


        SettingLaw.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Подтверждение")
                .setMessage("Вы даёте слово, что у вас есть права?")
                .setPositiveButton("Да", DialogInterface.OnClickListener { dialog, id ->
                    db.redUser("law","true",info_user.login)
                    law.text = "Водительские права: Подтверждены"
                })
                .setNegativeButton("Нет", DialogInterface.OnClickListener { dialog, id ->
                    db.redUser("law","false",info_user.login)
                    law.text = "Водительские права: Отсутсвуют"
                })

            val dialog = builder.create()
            dialog.show()
        }

        SettingAd.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Подтверждение")
                .setMessage("Вы хотите выкладывать объявления?")
                .setPositiveButton("Да", DialogInterface.OnClickListener { dialog, id ->
                    db.redUser("ad","true",info_user.login)
                    ad.text = "Выкладывать объявления: Можно"
                })
                .setNegativeButton("Нет", DialogInterface.OnClickListener { dialog, id ->
                    db.redUser("ad","false",info_user.login)
                    ad.text = "Выкладывать объявления: Нельзя"
                })

            val dialog = builder.create()
            dialog.show()
        }

        button1.setOnClickListener{
            if ((firstName.text.toString() != info_user.firstname) or
                (surName.text.toString() != info_user.surname)or
                (lastName.text.toString() != info_user.lastname)or
                (email.text.toString() != info_user.email)) {
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Подтверждение")
                    .setMessage("Вы хотите сохранить настройки профиля?")
                    .setPositiveButton("Да", DialogInterface.OnClickListener { dialog, id ->
                        var text = firstName.text.toString()
                        db.redUser("firstname", text, info_user.login)
                        text = surName.text.toString()
                        db.redUser("surname", text, info_user.login)
                        text = lastName.text.toString()
                        db.redUser("lastname", text, info_user.login)
                        text = email.text.toString()
                        db.redUser("email", text, info_user.login)
                        val intent = Intent(this, MainWindowActivity::class.java)
                        var options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this,
                            button1,
                            "grid"
                        )
                        options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this,
                            button4,
                            "profile"
                        )
                        startActivity(intent, options.toBundle())
                    })
                    .setNegativeButton("Нет", DialogInterface.OnClickListener { dialog, id ->
                        val intent = Intent(this, MainWindowActivity::class.java)
                        var options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this,
                            button1,
                            "grid"
                        )
                        options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this,
                            button4,
                            "profile"
                        )
                        startActivity(intent, options.toBundle())
                    })
                val dialog = builder.create()
                dialog.show()
            }
            else{
                val intent = Intent(this, MainWindowActivity::class.java)
                var options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    button1,
                    "grid"
                )
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    button4,
                    "profile"
                )
                startActivity(intent, options.toBundle())
            }
        }
        button2.setOnClickListener{
            if ((firstName.text.toString() != info_user.firstname)or
                (surName.text.toString() != info_user.surname)or
                (lastName.text.toString() != info_user.lastname)or
                (email.text.toString() != info_user.email)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Подтверждение")
                    .setMessage("Вы хотите сохранить настройки профиля?")
                    .setPositiveButton("Да", DialogInterface.OnClickListener { dialog, id ->
                        var text = firstName.text.toString()
                        db.redUser("firstname", text, info_user.login)
                        text = surName.text.toString()
                        db.redUser("surname", text, info_user.login)
                        text = lastName.text.toString()
                        db.redUser("lastname", text, info_user.login)
                        text = email.text.toString()
                        db.redUser("email", text, info_user.login)
                        val intent = Intent(this, SmsActivity::class.java)
                        var options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this, button2, "sms")
                        options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this, button4, "profile")
                        startActivity(intent, options.toBundle())
                    })
                    .setNegativeButton("Нет", DialogInterface.OnClickListener { dialog, id ->
                        val intent = Intent(this, SmsActivity::class.java)
                        var options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this, button2, "sms")
                        options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this, button4, "profile")
                        startActivity(intent, options.toBundle())
                    })
                val dialog = builder.create()
                dialog.show()
            }
            else{
                val intent = Intent(this, SmsActivity::class.java)
                var options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, button2, "sms")
                options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, button4, "profile")
                startActivity(intent, options.toBundle())
            }
        }
        button3.setOnClickListener {
            if ((firstName.text.toString() != info_user.firstname)or
                (surName.text.toString() != info_user.surname)or
                (lastName.text.toString() != info_user.lastname)or
                (email.text.toString() != info_user.email)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Подтверждение")
                    .setMessage("Вы хотите сохранить настройки профиля?")
                    .setPositiveButton("Да", DialogInterface.OnClickListener { dialog, id ->
                        var text = firstName.text.toString()
                        db.redUser("firstname", text, info_user.login)
                        text = surName.text.toString()
                        db.redUser("surname", text, info_user.login)
                        text = lastName.text.toString()
                        db.redUser("lastname", text, info_user.login)
                        text = email.text.toString()
                        db.redUser("email", text, info_user.login)
                        val intent = Intent(this, TimeActivity::class.java)
                        var options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this, button3, "time")
                        options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this, button4, "profile")
                        startActivity(intent, options.toBundle())
                    })
                    .setNegativeButton("Нет", DialogInterface.OnClickListener { dialog, id ->
                        val intent = Intent(this, TimeActivity::class.java)
                        var options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this, button3, "time")
                        options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this, button4, "profile")
                        startActivity(intent, options.toBundle())
                    })
                val dialog = builder.create()
                dialog.show()
            }
        else {
                val intent = Intent(this, TimeActivity::class.java)
                var options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, button3, "time")
                options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, button4, "profile")
                startActivity(intent, options.toBundle())
            }
        }
    }
}