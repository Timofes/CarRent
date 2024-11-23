package com.example.carrent.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.carrent.ClassProject.DbHelper
import com.example.carrent.ClassProject.TimeClass
import com.example.carrent.ClassProject.User
import com.example.carrent.ClassProject.items
import com.example.carrent.R
import com.example.carrent.menu.grid.MainWindowActivity
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sharedPreferences = getSharedPreferences("Save_open", MODE_PRIVATE)
        if (sharedPreferences.getString("log", "") == "true"){
            val intent = Intent(this, MainWindowActivity::class.java)
            startActivity(intent)
        }
        val db = DbHelper(this,null)
        if (db.getUser("admin","1") == false){
            db.addUser(User(
                login = "admin",
                password = "1",
                email = "bondtimofes@gmail.com",
                firstname = "Тимофей",
                lastname = "Бондаренко",
                surname = "Романович",
                law = "true",
                rate = "0.0",
                ad = "true",
                count = "3"
            ))
            var time = TimeClass(
                Monday = "7:00-8:00",
                Tuesday = "Empty",
                Wednesday = "Empty",
                Thursday = "11:30-15:30",
                Friday = "Empty",
                Saturday = "Empty",
                Sunday = "Empty",
            )
            var id_Time = db.addTime(time)
            var uri = getUriFromDrawable(this, R.drawable.nocar)
            var car = items(1,1, "Lada", uri.toString(), 7000, "Седан", "Vesta",
                id_Time.toString(), 0.0,"Московская 23",2015,"Только от 10 лет стажа")
            db.addCars(car)

            time = TimeClass(
                Monday = "Empty",
                Tuesday = "7:00-22:00",
                Wednesday = "Empty",
                Thursday = "11:30-15:30",
                Friday = "Empty",
                Saturday = "Empty",
                Sunday = "All",
            )
            id_Time = db.addTime(time)
            uri = getUriFromDrawable(this, R.drawable.polo)
            car = items(1,1, "Volkswagen", uri.toString(), 9000, "Седан", "Polo",
                id_Time.toString(), 0.0,"Пролетарская 17",2017,"Машина неплохая")
            db.addCars(car)

            time = TimeClass(
                Monday = "Empty",
                Tuesday = "All",
                Wednesday = "Empty",
                Thursday = "Empty",
                Friday = "Empty",
                Saturday = "5:00-18:00",
                Sunday = "Empty",
            )
            id_Time = db.addTime(time)
            uri = getUriFromDrawable(this, R.drawable.car)
            car = items(1, 1, "XCITE", uri.toString(), 15000, "Кроссовер", "X-CROSS 7",
                id_Time.toString(), 0.0, "Комсомольская 15",2024,"Абсолютно новая")

            db.addCars(car)
        }

        val buttonInput: Button = findViewById(R.id.Input_button)
        val userLogin: EditText = findViewById(R.id.Login_Data)
        val userPassword: EditText = findViewById(R.id.Password_Data)
        val linkToReg: TextView = findViewById(R.id.NextWindows)

        linkToReg.setOnClickListener{
            val intent = Intent(this, RegistrActivity::class.java)
            startActivity(intent)
        }
        buttonInput.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if (login == "" || password == "" ){
                Toast.makeText(this,"Поля пустые", Toast.LENGTH_LONG).show()
            }
            else{
                val db = DbHelper(this, null)
                val isAuth = db.getUser(login,password)
                if (isAuth) {
                    Toast.makeText(this, "Пользователь $login существует", Toast.LENGTH_LONG).show()
                    userLogin.text.clear()
                    userPassword.text.clear()

                    var sharedPreferences = getSharedPreferences("id_user", MODE_PRIVATE)
                    var editor = sharedPreferences.edit()
                    editor.putString("login", login)
                    editor.apply()


                    sharedPreferences = getSharedPreferences("Save_open", MODE_PRIVATE)
                    editor = sharedPreferences.edit()
                    editor.putString("log", "true")
                    editor.apply()


                    val intent = Intent(this, MainWindowActivity::class.java)
                    startActivity(intent)

                }else
                {
                    Toast.makeText(this, "Пользователь $login не авторизован", Toast.LENGTH_LONG).show()
                }
            }
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
}