package com.example.carrent.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carrent.ClassProject.DbHelper
import com.example.carrent.ClassProject.User
import com.example.carrent.R

class RegistrActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registr)

        val buttonInput: Button = findViewById(R.id.Input_button_Registr)

        val userLogin: EditText = findViewById(R.id.login_Data_Registr)
        val userPassword: EditText = findViewById(R.id.password_Data_Registr)
        val userEmail: EditText = findViewById(R.id.Email_Data_Registr)
        val userName: EditText = findViewById(R.id.first_name_Data_Registr)
        val userlastName: EditText = findViewById(R.id.last_name_Data_Registr)
        val usersurName: EditText = findViewById(R.id.sur_name_Data_Registr)
        val point: TextView = findViewById(R.id.point)

        val linkToReg: TextView = findViewById(R.id.BackWindow)

        linkToReg.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        userLogin.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val y = userLogin.y
                val ypadding = userLogin.height
                point.y = y + (ypadding/2) - 15
            }
        }

        userPassword.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val y = userPassword.y
                val ypadding = userPassword.height
                point.y = y + (ypadding/2) - 15
            }
        }

        userEmail.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val y = userEmail.y
                val ypadding = userEmail.height
                point.y = y + (ypadding/2) - 15
            }
        }

        userlastName.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val y = userlastName.y
                val ypadding = userlastName.height
                point.y = y + (ypadding/2) - 15
            }
        }

        userName.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val y = userName.y
                val ypadding = userName.height
                point.y = y + (ypadding/2) - 15
            }
        }

        usersurName.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val y = usersurName.y
                val ypadding = usersurName.height
                point.y = y + (ypadding/2) - 15
            }
        }

        buttonInput.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val firstName = userName.text.toString().trim()
            val LastName = userlastName.text.toString().trim()
            val SurName = usersurName.text.toString().trim()

            if (login == "" || password == "" || email == "" || firstName == "" || LastName == "" || SurName == "") {
                Toast.makeText(this,"Поля пустые", Toast.LENGTH_LONG).show()
            }
            else{
                val user = User(login, password, email,firstName,LastName,SurName,"0","0","0","0")

                val db = DbHelper(this, null)
                if (db.getUserlogin(login)){
                    Toast.makeText(this,"Логин занят", Toast.LENGTH_LONG).show()
                }
                else {
                    db.addUser(user)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }
            }
        }
    }
}