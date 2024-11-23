package com.example.carrent.ClassProject

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(val context:Context,factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, "Data", factory, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE Data (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, password TEXT, email TEXT,firstname TEXT, lastname TEXT, surname TEXT, law TEXT, rate TEXT, ad TEXT, count TEXT)"
        db!!.execSQL(query)

        val queryCar = "CREATE TABLE Cars (id INTEGER PRIMARY KEY AUTOINCREMENT,id_user LONG,name TEXT, image TEXT, price INT, body_type TEXT, model_type TEXT, time_free LONG, rating TEXT, addres TEXT, year TEXT, description TEXT)"
        db!!.execSQL(queryCar)

        val queryTime = "CREATE TABLE Time (id INTEGER PRIMARY KEY AUTOINCREMENT, Monday TEXT,Tuesday TEXT,Wednesday TEXT,Thursday TEXT,Friday TEXT,Saturday TEXT,Sunday TEXT)"
        db!!.execSQL(queryTime)

        val querySms = "CREATE TABLE Sms (id INTEGER PRIMARY KEY AUTOINCREMENT, login_user TEXT, Type TEXT, login_input TEXT, text TEXT, time TEXT)"
        db!!.execSQL(querySms)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        p1: Int,
        p2: Int
    ) {
        db!!.execSQL("DROP TABLE IF EXISTS Data")
        onCreate(db)
        db!!.execSQL("DROP TABLE IF EXISTS Time")
        onCreate(db)
        db!!.execSQL("DROP TABLE IF EXISTS Cars")
        onCreate(db)
        db!!.execSQL("DROP TABLE IF EXISTS Sms")
        onCreate(db)
    }



    fun addUser(user:User){

        val values = ContentValues()
        values.put("login", user.login)
        values.put("password",user.password)
        values.put("email",user.email)
        values.put("firstname", user.firstname)
        values.put("lastname",user.lastname)
        values.put("surname",user.surname)
        values.put("law", user.law)
        values.put("rate",user.rate)
        values.put("ad",user.ad)
        values.put("count",user.count)

        val db = this.writableDatabase
        db.insert("Data", null, values)

        db.close()
    }

    fun addCars(car: items){
        val values = ContentValues()
        values.put("id_user",car.id_user)
        values.put("name", car.name)
        values.put("image",car.image)
        values.put("price",car.price)
        values.put("body_type", car.body_type)
        values.put("model_type",car.model_type)
       // addTime(car.time_free)
        values.put("time_free",car.time_free)
        //val id_Time = addTime(car.time_free)
        //values.put("time_free",id_Time)
        values.put("rating", car.rating)
        values.put("addres", car.addres)
        values.put("year", car.year)
        values.put("description",car.description)

        val db = this.writableDatabase
        db.insert("Cars", null, values)

        db.close()
    }

    fun addTime(Time: TimeClass): Long{

        val values = ContentValues()
        values.put("Monday", Time.Monday)
        values.put("Tuesday",Time.Tuesday)
        values.put("Wednesday",Time.Wednesday)
        values.put("Thursday", Time.Thursday)
        values.put("Friday",Time.Friday)
        values.put("Saturday",Time.Saturday)
        values.put("Sunday", Time.Sunday)

        val db = this.writableDatabase
        val id = db.insert("Time", null, values)

        db.close()

        return id
    }

    fun addSms(sms:Sms){

        val values = ContentValues()
        values.put("login_user", sms.login_user)
        values.put("Type",sms.Type)
        values.put("login_input",sms.login_input)
        values.put("text", sms.text)
        values.put("time", sms.time)

        val db = this.writableDatabase
        db.insert("Sms", null, values)

        db.close()
    }

    fun getUserlogin(login: String) : Boolean{
        val db = this.readableDatabase

        val selectionArgs = arrayOf(login)
        val result = db.rawQuery("SELECT * FROM Data WHERE login = ?",selectionArgs)
        return result.moveToFirst()
    }

    fun deleteCars(id_car:String){
        val db = this.writableDatabase
        val selectionArgs = arrayOf(id_car)
        db.delete("Cars","id = ?",selectionArgs)
        db.close()
    }

    @SuppressLint("Range")
    fun Userinfoid(login: String) : Long{
        val db = this.readableDatabase

        val selectionArgs = arrayOf(login)
        val cursor = db.rawQuery("SELECT id FROM Data WHERE login = ?",selectionArgs)
        var id = -1L
        cursor.use {
            if (cursor.moveToFirst()) {
                id = cursor.getLong(cursor.getColumnIndex("id"))
            }
        }
        return id
    }

    @SuppressLint("Range")
    fun getCars(id_user_input: Long?, AllUser: Boolean) : ArrayList<items>{
        val db = this.readableDatabase

        val selectionArgs = arrayOf(id_user_input.toString())
        val cursor = if (AllUser){
            db.rawQuery("SELECT * FROM Cars ", null)
        }
        else{
            db.rawQuery("SELECT * FROM Cars WHERE id_user = ?", selectionArgs)
        }

        val result = arrayListOf<items>()

        cursor.use {
            if (cursor.moveToFirst()) {
                  do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val id_user = cursor.getLong(cursor.getColumnIndex("id_user"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val image = cursor.getString(cursor.getColumnIndex("image"))
                val price = cursor.getInt(cursor.getColumnIndex("price"))
                val body_type = cursor.getString(cursor.getColumnIndex("body_type"))
                val model_type = cursor.getString(cursor.getColumnIndex("model_type"))
                val time_free_id = cursor.getLong(cursor.getColumnIndex("time_free"))
                val addres = cursor.getString(cursor.getColumnIndex("addres"))
                val year = cursor.getInt(cursor.getColumnIndex("year"))
                var description = cursor.getString(cursor.getColumnIndex("description"))
                      val rating = cursor.getDouble(cursor.getColumnIndex("rating"))

                val car = items(id, id_user, name, image, price, body_type, model_type, time_free_id.toString(), rating,addres,year,description)
                 result.add(car)
                  } while (cursor.moveToNext())
            }
        }

        return result
    }

    @SuppressLint("Range")
    fun getSms(id_user_input: String) : ArrayList<Sms>{
        val db = this.readableDatabase

        val selectionArgs = arrayOf(id_user_input.toString())
        val cursor = db.rawQuery("SELECT * FROM Sms WHERE login_user = ?", selectionArgs)

        val result = arrayListOf<Sms>()

        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex("id"))
                    val id_user = cursor.getString(cursor.getColumnIndex("login_user"))
                    val Type = cursor.getString(cursor.getColumnIndex("Type"))
                    val text = cursor.getString(cursor.getColumnIndex("text"))
                    val time = cursor.getString(cursor.getColumnIndex("time"))
                    val login_input = cursor.getString(cursor.getColumnIndex("login_input"))

                   // val car = items(id, id_user, name, image, price, body_type, model_type, time_free_id.toString(), rating,addres,year,description)
                    val sms = Sms(id,id_user,Type,login_input,text,time)
                    result.add(sms)
                } while (cursor.moveToNext())
            }
        }

        return result
    }

    @SuppressLint("Range")
    fun getCars_id(id_car: String) : ArrayList<items>{
        val db = this.readableDatabase

        val selectionArgs = arrayOf(id_car.toString())
        val cursor = db.rawQuery("SELECT * FROM Cars WHERE id = ? ", selectionArgs)

        val result = arrayListOf<items>()

        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex("id"))
                    val id_user = cursor.getLong(cursor.getColumnIndex("id_user"))
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val image = cursor.getString(cursor.getColumnIndex("image"))
                    val price = cursor.getInt(cursor.getColumnIndex("price"))
                    val body_type = cursor.getString(cursor.getColumnIndex("body_type"))
                    val model_type = cursor.getString(cursor.getColumnIndex("model_type"))
                    val time_free_id = cursor.getLong(cursor.getColumnIndex("time_free"))
                    val addres = cursor.getString(cursor.getColumnIndex("addres"))
                    val year = cursor.getInt(cursor.getColumnIndex("year"))
                    var description = cursor.getString(cursor.getColumnIndex("description"))
                    val rating = cursor.getDouble(cursor.getColumnIndex("rating"))

                    val car = items(id, id_user, name, image, price, body_type, model_type, time_free_id.toString(), rating,addres,year,description)
                    result.add(car)
                } while (cursor.moveToNext())
            }
        }

        return result
    }

    @SuppressLint("Range")
    fun getTime(id: String): TimeClass{
        val db = this.readableDatabase

        val selectionArgs = arrayOf(id.toString())
        val cursor = db.rawQuery("SELECT * FROM Time WHERE id = ?",selectionArgs)
        var Time = TimeClass(
            Monday = "Empty",
            Tuesday = "Empty",
            Wednesday = "Empty",
            Thursday = "Empty",
            Friday = "Empty",
            Saturday = "Empty",
            Sunday = "Empty"
        )

        cursor.use {
            do {
                if (cursor.moveToFirst()) {
                    val Monday = cursor.getString(cursor.getColumnIndex("Monday"))
                    val Tuesday = cursor.getString(cursor.getColumnIndex("Tuesday"))
                    val Wednesday = cursor.getString(cursor.getColumnIndex("Wednesday"))
                    val Thursday = cursor.getString(cursor.getColumnIndex("Thursday"))
                    val Friday = cursor.getString(cursor.getColumnIndex("Friday"))
                    val Saturday = cursor.getString(cursor.getColumnIndex("Saturday"))
                    val Sunday = cursor.getString(cursor.getColumnIndex("Sunday"))
                    Time = TimeClass(Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday)
                }
            }while(cursor.moveToNext())
        }

            return Time
    }

    fun getUser(login: String, password:String) : Boolean{
        val db = this.readableDatabase

        val selectionArgs = arrayOf(login, password)
        val result = db.rawQuery("SELECT * FROM Data WHERE login = ? AND password = ?",selectionArgs)
        return result.moveToFirst()
    }

    fun redUser(brim: String, str: String, login: String) {
        val db = this.writableDatabase
        val selection = "login = ?"
        val selectionArgs = arrayOf(login)

        val updateValues = ContentValues()
        updateValues.put(brim, str)

        db.update("Data", updateValues, selection, selectionArgs)
    }

    fun redTime(brim: String, str: String, id: String) {
        val db = this.writableDatabase
        val selection = "id = ?"
        val selectionArgs = arrayOf(id)

        val updateValues = ContentValues()
        updateValues.put(brim, str)

        db.update("Time", updateValues, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun getidtime(Time: TimeClass):String{
        val db = this.readableDatabase
        val selectionArgs = arrayOf(Time.Monday,Time.Tuesday,Time.Wednesday,Time.Thursday,Time.Friday,Time.Saturday,Time.Sunday)

        val cursor = db.rawQuery("SELECT id FROM Time WHERE Monday = ? AND Tuesday = ? AND Wednesday = ? AND " +
                "Thursday = ? AND Friday = ? AND Saturday = ? AND Sunday = ?", selectionArgs)
        var id_time = ""
        cursor.use {
            if (cursor.moveToFirst()) {
                id_time = cursor.getString(cursor.getColumnIndex("id"))
            }
        }
        return id_time.toString()
    }

    fun redCar(brim:String,str: String, id_car: String) {
        val db = this.writableDatabase
        val selection = "id = ?"
        val selectionArgs = arrayOf(id_car)

        val updateValues = ContentValues()
        updateValues.put(brim, str)

        db.update("Cars", updateValues, selection, selectionArgs)
    }

    fun redSms(brim:String,str: String, id_sms: String) {
        val db = this.writableDatabase
        val selection = "id = ?"
        val selectionArgs = arrayOf(id_sms)

        val updateValues = ContentValues()
        updateValues.put(brim, str)

        db.update("Sms", updateValues, selection, selectionArgs)
    }


    @SuppressLint("Range")
    fun infoUser(login: String) : User{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Data WHERE login = ?", arrayOf(login))
        var login = ""
        var password = ""
        var email = ""
        var firstname = ""
        var lastname = ""
        var surname = ""
        var law = ""
        var rate = ""
        var ad = ""
        var count = ""
        cursor.use {
            if (cursor.moveToFirst()) {
                login = cursor.getString(cursor.getColumnIndex("login"))
                //password = cursor.getString(cursor.getColumnIndex("password"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                firstname = cursor.getString(cursor.getColumnIndex("firstname"))
                lastname = cursor.getString(cursor.getColumnIndex("lastname"))
                surname = cursor.getString(cursor.getColumnIndex("surname"))
                law = cursor.getString(cursor.getColumnIndex("law"))
                rate = cursor.getString(cursor.getColumnIndex("rate"))
                ad = cursor.getString(cursor.getColumnIndex("ad"))
                count = cursor.getString(cursor.getColumnIndex("count"))
            }
        }
        cursor.close()
        db.close()
        return User(login,password,email,firstname,lastname,surname,law,rate,ad,count)
    }

    @SuppressLint("Range")
    fun infoUser_id(id: String) : User{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Data WHERE id = ?", arrayOf(id))
        var login = ""
        var password = ""
        var email = ""
        var firstname = ""
        var lastname = ""
        var surname = ""
        var law = ""
        var rate = ""
        var ad = ""
        var count = ""
        cursor.use {
            if (cursor.moveToFirst()) {
                login = cursor.getString(cursor.getColumnIndex("login"))
                //password = cursor.getString(cursor.getColumnIndex("password"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                firstname = cursor.getString(cursor.getColumnIndex("firstname"))
                lastname = cursor.getString(cursor.getColumnIndex("lastname"))
                surname = cursor.getString(cursor.getColumnIndex("surname"))
                law = cursor.getString(cursor.getColumnIndex("law"))
                rate = cursor.getString(cursor.getColumnIndex("rate"))
                ad = cursor.getString(cursor.getColumnIndex("ad"))
                count = cursor.getString(cursor.getColumnIndex("count"))
            }
        }
        cursor.close()
        db.close()
        return User(login,password,email,firstname,lastname,surname,law,rate,ad,count)
    }

}
