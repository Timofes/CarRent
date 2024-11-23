package com.example.carrent.menu.time

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import com.example.carrent.ClassProject.DbHelper
import com.example.carrent.ClassProject.TimeClass
import com.example.carrent.ClassProject.items
import com.example.carrent.R
import com.example.carrent.menu.grid.MainWindowActivity
import com.example.carrent.menu.profile.ProfileActivity
import com.example.carrent.menu.sms.SmsActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class TimeActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    var time_add = arrayListOf<String>("","","","","","","")
    var number_time_add = 0
    var Now_Car = 0
    lateinit var Car: ArrayList<items>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        val button1: ImageButton = findViewById(R.id.imageButton1_time)
        val button2: ImageButton = findViewById(R.id.imageButton2_time)
        val button3: ImageButton = findViewById(R.id.imageButton3_time)
        val button4: ImageButton = findViewById(R.id.imageButton4_time)

        val plus: ImageView = findViewById(R.id.plus)
        val minus: ImageView = findViewById(R.id.minus)

        val ListTime: TextView = findViewById(R.id.List_time)
        val ButtonLeft: ImageButton = findViewById(R.id.button_Left)
        val ButtonRight: ImageButton = findViewById(R.id.button_Right)
        var text_Empty: TextView = findViewById(R.id.textView5)
        val image: ImageView = findViewById(R.id.item_image_info_time)

        val sharedPreferences = getSharedPreferences("id_user", MODE_PRIVATE)
        val login_user = sharedPreferences.getString("login", "")

        val db = DbHelper(this,null)
        val User = db.infoUser(login_user.toString())
        val id_user = db.Userinfoid(User.login)
        var Count_Car = User.count
        Now_Car = 0
        ListTime.text = (Now_Car+1).toString() + "/" + Count_Car
        ButtonLeft.isVisible = false
        if (Count_Car.toInt() <= 1){
            ButtonRight.isVisible = false
        }else{
            ButtonRight.isVisible = true
        }

        if (Count_Car.toInt()>0){
            Visible(true)
        }

        Car = db.getCars(id_user.toLong(),false)

        val new_Car = Car
        if (Count_Car.toInt() > 0) {
            text_Empty.isVisible = false
            CreateItemTime(new_Car[Now_Car])
        }else{
            text_Empty.isVisible = true
            Visible(false)
        }

        image.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        plus.setOnClickListener{
            val user_add_ad = db.infoUser(login_user.toString()).ad

            if (user_add_ad == "true") {
                var time_add1 = TimeClass(
                    Monday = "Empty",
                    Tuesday = "Empty",
                    Wednesday = "Empty",
                    Thursday = "Empty",
                    Friday = "Empty",
                    Saturday = "Empty",
                    Sunday = "Empty"
                )
                val id_time_add = db.addTime(time_add1)
                var Cars_add = items(
                    id = 0,
                    id_user = id_user,
                    name = "",
                    image = "car",
                    price = 0,
                    body_type = "",
                    model_type = "",
                    time_free = id_time_add.toString(),
                    rating = 0.0,
                    addres = "",
                    year = 0,
                    description = "",
                )
                Now_Car = Count_Car.toInt()
                Count_Car = (Count_Car.toInt() + 1).toString()
                db.addCars(Cars_add)
                db.redUser("count", Count_Car, login_user.toString())
                ListTime.text = (Now_Car + 1).toString() + "/" + Count_Car
                Visible(true)
                text_Empty.isVisible = false
                if (Count_Car.toInt() > 1) {
                    ButtonLeft.isVisible = true
                }
                Car = db.getCars(id_user.toLong(), false)
                CreateItemTime(Car[Now_Car])
            }
            else{
                Toast.makeText(this,"Вам запрещено создавать объявления",Toast.LENGTH_SHORT).show()
            }
        }

        minus.setOnClickListener{
            if (Count_Car.toInt() > 0) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Подтверждение")
                    .setMessage("Вы хотите удалить объявление?")
                    .setPositiveButton("Да", DialogInterface.OnClickListener { dialog, id ->
                        db.deleteCars(Car[Now_Car].id.toString())
                        if ((Now_Car + 2) == Count_Car.toInt()) {
                            ButtonRight.isVisible = false
                        } else if ((Now_Car + 1) == Count_Car.toInt()) {
                            Now_Car = Now_Car - 1
                        } else if (Now_Car > 0 && (Now_Car + 1) != Count_Car.toInt()) {
                        }
                        Count_Car = (Count_Car.toInt() - 1).toString()
                        db.redUser("count", Count_Car, login_user.toString())
                        Car = db.getCars(id_user.toLong(), false)
                        ListTime.text = (Now_Car + 1).toString() + "/" + Count_Car
                        if (Count_Car.toInt() == 0) {
                            ButtonRight.isVisible = false
                            ButtonLeft.isVisible = false
                            Visible(false)
                            text_Empty.isVisible = true
                        } else {
                            CreateItemTime(Car[Now_Car])
                        }
                        if (Count_Car.toInt() == 1) {
                            ButtonRight.isVisible = false
                            ButtonLeft.isVisible = false
                        }
                    })
                    .setNegativeButton("Нет", DialogInterface.OnClickListener { dialog, id ->

                    })
                val dialog = builder.create()
                dialog.show()
            }
            else{
                Toast.makeText(this,"Нет объявлений для удаления", Toast.LENGTH_SHORT).show()
            }
        }

        ButtonRight.setOnClickListener{
            if (EmptyItem()) {
                Now_Car = ReductItem(Now_Car, true)
                Car = db.getCars(id_user.toLong(), false)
                ListTime.text = (Now_Car + 1).toString() + "/" + Count_Car
                if (ButtonLeft.isVisible == false) {
                    ButtonLeft.isVisible = true
                }
                if (Now_Car + 1 == Count_Car.toInt()) {
                    ButtonRight.isVisible = false
                }
            }else{
                Toast.makeText(this,"Поля не могут быть пустыми!",Toast.LENGTH_LONG).show()
            }
        }

        ButtonLeft.setOnClickListener{
            if (EmptyItem()) {
                Now_Car = ReductItem(Now_Car, false)
                ListTime.text = (Now_Car + 1).toString() + "/" + Count_Car
                if (ButtonRight.isVisible == false) {
                    ButtonRight.isVisible = true
                }
                if (Now_Car == 0) {
                    ButtonLeft.isVisible = false
                }
            }else{
                Toast.makeText(this,"Поля не могут быть пустыми!",Toast.LENGTH_LONG).show()
            }
        }

        button1.setOnClickListener{
            val intent = Intent(this, MainWindowActivity::class.java)
            var options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, button1, "grid")
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,button3, "time")
            if (Count_Car.toInt() > 0){
                SaveSetup(Now_Car, options.toBundle(), intent)
            }else{
                startActivity(intent,options.toBundle())
            }
        }
        button2.setOnClickListener{
            val intent = Intent(this, SmsActivity::class.java)
            var options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, button2, "sms")
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,button3, "time")
            if (Count_Car.toInt() > 0){
                SaveSetup(Now_Car, options.toBundle(), intent)
            }else{
                startActivity(intent,options.toBundle())
            }
        }
        button4.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            var options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, button4, "profile")
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,button3, "time")
            if (Count_Car.toInt() > 0){
                SaveSetup(Now_Car, options.toBundle(), intent)
            }else{
                startActivity(intent,options.toBundle())
            }
        }
    }

    fun saveImageToInternalStorage(bitmap: Bitmap, fileName: String): Uri? {
        val context = applicationContext ?: return null // Обработка null-значения контекста
        val imageDir = context.filesDir
        val imageFile = File(imageDir, "$fileName.jpg") //Или другой формат, например, PNG

        try {
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // 100 - качество сжатия
            outputStream.flush()
            outputStream.close()
            return FileProvider.getUriForFile(context, "${applicationContext.packageName}.fileprovider", imageFile)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
    fun saveImageToExternalStorage(bitmap: Bitmap, fileName: String): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // или "image/png"
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) // или другое место
        }

        return try {
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.also { uri ->
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // 100 - качество
                }
            }
        } catch (e: Exception) {
            Log.e("ImageSaving", "Error saving image: ${e.message}")
            null
        }
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val image: ImageView = findViewById(R.id.item_image_info_time)
        val name: TextView = findViewById(R.id.item_name_info_time)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val uri = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val put = saveImageToInternalStorage(bitmap,Car[Now_Car].id.toString()+"_"+Car[Now_Car].id.toString())
                //val put = saveImageToExternalStorage(bitmap,Car[Now_Car].id.toString()+"_"+Car[Now_Car].id.toString())
                Car[Now_Car].image = put.toString()
                //val bitmap1 = MediaStore.Images.Media.getBitmap(contentResolver, put)
                //image.setImageBitmap(bitmap1)
                Car[Now_Car].image?.let { path ->
                    val uri = Uri.parse(path)
                    displayImageFromInternalStorage(image, uri)
                } ?: run {
                    Toast.makeText(this, "Изображение не найдено", Toast.LENGTH_SHORT).show()
                }

            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show() //Обработка ошибки
            }
        }
    }

    fun SaveSetup(number: Int, options: Bundle?, intent: Intent) {
        val name: EditText = findViewById(R.id.item_name_info_time)
        val marka: EditText = findViewById(R.id.editMarka_time)
        val price: EditText = findViewById(R.id.item_Price_info_time)
        val image: ImageView = findViewById(R.id.item_image_info_time)
        val addres: TextView = findViewById(R.id.addres_info_time)
        val body_type: TextView = findViewById(R.id.body_time_info_time)
        val year: TextView = findViewById(R.id.year_info_time)
        val description: TextView = findViewById(R.id.description_info_time)
        val TimeNumber: TextView = findViewById(R.id.Time_number_time)

        var number1 = 0
        if (TimeNumber.text.toString() == "Нет") {
            time_add[number_time_add] = "Empty"
        } else if (TimeNumber.text.toString() == "Любое") {
            time_add[number_time_add] = "All"
        } else {
            time_add[number_time_add] = TimeNumber.text.toString()
        }

        if (EmptyItem() == false){
            Toast.makeText(this,"Поля не могут быть пустыми",Toast.LENGTH_SHORT).show()
            return
        }

        val db = DbHelper(this, null)
        val time_id = Car[number].time_free
        val time_free = db.getTime(time_id.toString())
        val Cars = db.getCars_id(Car[Now_Car].id.toString())[0].image
        if (name.text.toString() != Car[number].name ||
            marka.text.toString() != Car[number].model_type ||
            price.text.toString() != Car[number].price.toString() ||
            addres.text.toString() != Car[number].addres ||
            body_type.text.toString() != Car[number].body_type ||
            year.text.toString() != Car[number].year.toString() ||
            description.text.toString() != Car[number].description ||
            time_add[0].toString() != time_free.Monday ||
            time_add[1].toString() != time_free.Tuesday ||
            time_add[2].toString() != time_free.Wednesday ||
            time_add[3].toString() != time_free.Thursday ||
            time_add[4].toString() != time_free.Friday ||
            time_add[5].toString() != time_free.Saturday ||
            time_add[6].toString() != time_free.Sunday ||
            Cars != Car[number].image.toString()
        ) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Подтверждение")
                .setMessage("Вы хотите сохранить настройки объявления?")
                .setPositiveButton("Да", DialogInterface.OnClickListener { dialog, id ->
                    val db = DbHelper(this, null)
                    var text = name.text.toString()
                    db.redCar("name", text, Car[number].id.toString())
                    Car[number].name = text
                    text = marka.text.toString()
                    db.redCar("model_type", text, Car[number].id.toString())
                    Car[number].model_type = text
                    text = price.text.toString()
                    db.redCar("price", text, Car[number].id.toString())
                    Car[number].price = text.toInt()
                    text = addres.text.toString()
                    db.redCar("addres", text, Car[number].id.toString())
                    Car[number].addres = text
                    text = body_type.text.toString()
                    db.redCar("body_type", text, Car[number].id.toString())
                    Car[number].body_type = text
                    text = year.text.toString()
                    db.redCar("year", text, Car[number].id.toString())
                    Car[number].year = text.toInt()
                    text = description.text.toString()
                    db.redCar("description", text, Car[number].id.toString())
                    Car[number].description = text

                    db.redCar("image", Car[number].image, Car[number].id.toString())

                    text = time_add[0]
                    db.redTime("Monday", text, time_id)
                    time_free.Monday = text;

                    text = time_add[1]
                    db.redTime("Tuesday", text, time_id)
                    time_free.Tuesday = text;

                    text = time_add[2]
                    db.redTime("Wednesday", text, time_id)
                    time_free.Wednesday = text;

                    text = time_add[3]
                    db.redTime("Thursday", text, time_id)
                    time_free.Thursday = text;

                    text = time_add[4]
                    db.redTime("Friday", text, time_id)
                    time_free.Friday = text;

                    text = time_add[5]
                    db.redTime("Saturday", text, time_id)
                    time_free.Saturday = text;

                    text = time_add[6]
                    db.redTime("Sunday", text, time_id)
                    time_free.Sunday = text;

                    startActivity(intent, options)
                })
                .setNegativeButton("Нет", DialogInterface.OnClickListener { dialog, id ->
                    startActivity(intent, options)
                })
            val dialog = builder.create()
            dialog.show()
        } else {
            startActivity(intent,options)
        }
    }

    fun Visible(Vis: Boolean){
        val name: EditText = findViewById(R.id.item_name_info_time)
        val marka: EditText = findViewById(R.id.editMarka_time)
        val price: EditText = findViewById(R.id.item_Price_info_time)
        val image: ImageView = findViewById(R.id.item_image_info_time)
        val addres: TextView = findViewById(R.id.addres_info_time)
        val body_type: TextView = findViewById(R.id.body_time_info_time)
        val year: TextView = findViewById(R.id.year_info_time)
        val description: TextView = findViewById(R.id.description_info_time)
        val TimeNumber: TextView = findViewById(R.id.Time_number_time)
        val ListTime: TextView = findViewById(R.id.List_time)
        val timeViews = arrayListOf<TextView>(
            findViewById(R.id.item_time1_info_time),
            findViewById(R.id.item_time2_info_time),
            findViewById(R.id.item_time3_info_time),
            findViewById(R.id.item_time4_info_time),
            findViewById(R.id.item_time5_info_time),
            findViewById(R.id.item_time6_info_time),
            findViewById(R.id.item_time7_info_time)
        )
        if (Vis){
            name.isVisible = true
            marka.isVisible = true
            price.isVisible = true
            image.isVisible = true
            addres.isVisible = true
            body_type.isVisible = true
            year.isVisible = true
            description.isVisible = true
            TimeNumber.isVisible = true
            ListTime.isVisible = true
            for (i in 0..6){
                timeViews[i].isVisible = true
            }
        }else{
            name.isVisible = false
            marka.isVisible = false
            price.isVisible = false
            image.isVisible = false
            addres.isVisible = false
            body_type.isVisible = false
            year.isVisible = false
            description.isVisible = false
            TimeNumber.isVisible = false
            ListTime.isVisible = false
            for (i in 0..6){
                timeViews[i].isVisible = false
            }
        }
    }

    fun CreateItemTime(Car: items){
        val Days = arrayListOf<String>("Пн","Вт","Ср","Чт","Пт","Сб","Вс")
        val DaysFull = arrayListOf<String>("Понедельник","Вторник","Среду","Четверг","Пятницу","Субботу","Воскресенье")

        val name: EditText = findViewById(R.id.item_name_info_time)
        val marka: EditText = findViewById(R.id.editMarka_time)
        val price: EditText = findViewById(R.id.item_Price_info_time)
        val image: ImageView = findViewById(R.id.item_image_info_time)
        val addres: TextView = findViewById(R.id.addres_info_time)
        val body_type: TextView = findViewById(R.id.body_time_info_time)
        val year: TextView = findViewById(R.id.year_info_time)
        val description: TextView = findViewById(R.id.description_info_time)
        val TimeNumber: TextView = findViewById(R.id.Time_number_time)
        val timeViews = arrayListOf<TextView>(
            findViewById(R.id.item_time1_info_time),
            findViewById(R.id.item_time2_info_time),
            findViewById(R.id.item_time3_info_time),
            findViewById(R.id.item_time4_info_time),
            findViewById(R.id.item_time5_info_time),
            findViewById(R.id.item_time6_info_time),
            findViewById(R.id.item_time7_info_time)
        )
        val db = DbHelper(this,null)
        val time_id = Car.time_free
        val time_free = db.getTime(time_id.toString())
        time_add = arrayListOf<String>(
            time_free.Monday,
            time_free.Tuesday,
            time_free.Wednesday,
            time_free.Thursday,
            time_free.Friday,
            time_free.Saturday,
            time_free.Sunday
        )
        val drawable1 = GradientDrawable()
        drawable1.shape = GradientDrawable.RECTANGLE
        drawable1.cornerRadius = 16f // Радиус скругления в пикселях
        drawable1.setColor(ContextCompat.getColor(this, android.R.color.white)) //Цвет фона
        image.setImageDrawable(drawable1)
        image.clipToOutline = true

        number_time_add = 0
        name.setText(Car.name)
        marka.setText(Car.model_type)
        price.setText(Car.price.toString())

        Car.image?.let { path ->
            val uri = Uri.parse(path)
            displayImageFromInternalStorage(image, uri)
        } ?: run {
            Toast.makeText(this, "Изображение не найдено", Toast.LENGTH_SHORT).show()
        }

        addres.setText(Car.addres)
        body_type.setText(Car.body_type)
        year.setText(Car.year.toString())
        description.setText(Car.description)

        var DaysToNumber = ""
        DaysToNumber = time_free.Monday
        if( DaysToNumber == "Empty") {
            DaysToNumber = "Нет"
        }
        else if(DaysToNumber == "All"){
            DaysToNumber = "Любое"
        }

        for (i in 0..6){
            val drawable = DrawableCompat.wrap(timeViews[i].background)
            DrawableCompat.setTint(drawable, Color.parseColor("#A9A3A3"))
        }

        TimeNumber.setText(DaysToNumber)
        val drawable = DrawableCompat.wrap(timeViews[0].background)
        DrawableCompat.setTint(drawable, Color.parseColor("#564F4F"))

        for(i in 0..6) {
            timeViews[i].text = Days[i]
            timeViews[i].setOnClickListener {
                val drawable = DrawableCompat.wrap(timeViews[i].background)
                DrawableCompat.setTint(drawable, Color.parseColor("#564F4F"))
                when (i) {
                    0 -> {
                        DaysToNumber = time_free.Monday
                    }

                    1 -> {
                        DaysToNumber = time_free.Tuesday
                    }

                    2 -> {
                        DaysToNumber = time_free.Wednesday
                    }

                    3 -> {
                        DaysToNumber = time_free.Thursday
                    }

                    4 -> {
                        DaysToNumber = time_free.Friday
                    }

                    5 -> {
                        DaysToNumber = time_free.Saturday
                    }

                    6 -> {
                        DaysToNumber = time_free.Sunday
                    }
                }
                if (TimeNumber.text.toString() == "Нет"){
                    time_add[number_time_add] = "Empty"
                }else if(TimeNumber.text.toString() == "Любое"){
                    time_add[number_time_add] = "All"
                }else {
                    time_add[number_time_add] = TimeNumber.text.toString()
                }
                if (DaysToNumber == "Empty") {
                    DaysToNumber = "Нет"
                } else if (DaysToNumber == "All") {
                    DaysToNumber = "Любое"
                }
                number_time_add = i
                TimeNumber.hint = "Время в " + DaysFull[i] + ": "
                TimeNumber.text = DaysToNumber
                for (j in 0..6) {
                    if (i != j) {
                        val drawable = DrawableCompat.wrap(timeViews[j].background)
                        DrawableCompat.setTint(drawable, Color.parseColor("#A9A3A3"))
                    }
                }
            }
        }

    }

    fun EmptyItem(): Boolean{
        val name: EditText = findViewById(R.id.item_name_info_time)
        val marka: EditText = findViewById(R.id.editMarka_time)
        val price: EditText = findViewById(R.id.item_Price_info_time)
        val image: ImageView = findViewById(R.id.item_image_info_time)
        val addres: TextView = findViewById(R.id.addres_info_time)
        val body_type: TextView = findViewById(R.id.body_time_info_time)
        val year: TextView = findViewById(R.id.year_info_time)
        val description: TextView = findViewById(R.id.description_info_time)
        val TimeNumber: TextView = findViewById(R.id.Time_number_time)
        if (name.text.isNotEmpty()&&
            marka.text.isNotEmpty()&&
            price.text.toString().isNotEmpty()&&
            addres.text.isNotEmpty()&&
            year.text.toString().isNotEmpty()&&
            description.text.isNotEmpty()&&
            body_type.text.isNotEmpty()&&
            time_add[0].isNotEmpty()&&
            time_add[1].isNotEmpty()&&
            time_add[2].isNotEmpty()&&
            time_add[3].isNotEmpty()&&
            time_add[4].isNotEmpty()&&
            time_add[5].isNotEmpty()&&
            time_add[6].isNotEmpty()
            ){
            return true
        }
        return false
    }

    fun ReductItem(number: Int, Right: Boolean):Int{
        val name: EditText = findViewById(R.id.item_name_info_time)
        val marka: EditText = findViewById(R.id.editMarka_time)
        val price: EditText = findViewById(R.id.item_Price_info_time)
        val image: ImageView = findViewById(R.id.item_image_info_time)
        val addres: TextView = findViewById(R.id.addres_info_time)
        val body_type: TextView = findViewById(R.id.body_time_info_time)
        val year: TextView = findViewById(R.id.year_info_time)
        val description: TextView = findViewById(R.id.description_info_time)
        val TimeNumber: TextView = findViewById(R.id.Time_number_time)

        var number1 = 0
        if (TimeNumber.text.toString() == "Нет"){
            time_add[number_time_add] = "Empty"
        }else if(TimeNumber.text.toString() == "Любое"){
            time_add[number_time_add] = "All"
        }else {
            time_add[number_time_add] = TimeNumber.text.toString()
        }
        val db = DbHelper(this,null)
        val time_id = Car[number].time_free
        val time_free = db.getTime(time_id.toString())
        val Cars = db.getCars_id(Car[Now_Car].id.toString())[0].image
        if (name.text.toString() != Car[number].name||
            marka.text.toString() != Car[number].model_type||
            price.text.toString() != Car[number].price.toString()||
            addres.text.toString() != Car[number].addres||
            body_type.text.toString() != Car[number].body_type||
            year.text.toString() != Car[number].year.toString()||
            description.text.toString() != Car[number].description||
            time_add[0].toString() != time_free.Monday||
            time_add[1].toString() != time_free.Tuesday||
            time_add[2].toString() != time_free.Wednesday||
            time_add[3].toString() != time_free.Thursday||
            time_add[4].toString() != time_free.Friday||
            time_add[5].toString() != time_free.Saturday||
            time_add[6].toString() != time_free.Sunday||
            Cars != Car[number].image
            ){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Подтверждение")
                .setMessage("Вы хотите сохранить настройки объявления?")
                .setPositiveButton("Да", DialogInterface.OnClickListener { dialog, id ->
                    val db = DbHelper(this, null)
                    var text = name.text.toString()
                    db.redCar("name", text, Car[number].id.toString())
                    Car[number].name = text
                    text = marka.text.toString()
                    db.redCar("model_type", text, Car[number].id.toString())
                    Car[number].model_type = text
                    text = price.text.toString()
                    db.redCar("price", text, Car[number].id.toString())
                    Car[number].price = text.toInt()
                    text = addres.text.toString()
                    db.redCar("addres", text, Car[number].id.toString())
                    Car[number].addres = text
                    text = body_type.text.toString()
                    db.redCar("body_type", text, Car[number].id.toString())
                    Car[number].body_type = text
                    text = year.text.toString()
                    db.redCar("year", text, Car[number].id.toString())
                    Car[number].year = text.toInt()
                    text = description.text.toString()
                    db.redCar("description", text, Car[number].id.toString())
                    Car[number].description = text

                    db.redCar("image", Car[number].image, Car[number].id.toString())

                    Toast.makeText(this,"$text",Toast.LENGTH_LONG).show()

                    text = time_add[0]
                    db.redTime("Monday", text, time_id)
                    time_free.Monday = text;

                    text = time_add[1]
                    db.redTime("Tuesday", text, time_id)
                    time_free.Tuesday = text;

                    text = time_add[2]
                    db.redTime("Wednesday", text, time_id)
                    time_free.Wednesday = text;

                    text = time_add[3]
                    db.redTime("Thursday", text, time_id)
                    time_free.Thursday = text;

                    text = time_add[4]
                    db.redTime("Friday", text, time_id)
                    time_free.Friday = text;

                    text = time_add[5]
                    db.redTime("Saturday", text, time_id)
                    time_free.Saturday = text;

                    text = time_add[6]
                    db.redTime("Sunday", text, time_id)
                    time_free.Sunday = text;

                    number1 = number

                    if (Right){
                        number1 = number1 + 1
                    }else{
                        number1 = number1 - 1
                    }
                    CreateItemTime(Car[number1])
                })
                .setNegativeButton("Нет", DialogInterface.OnClickListener { dialog, id ->
                     number1 = number
                    if (Right){
                        number1 = number1 + 1
                    }else{
                        number1 = number1 - 1
                    }
                    CreateItemTime(Car[number1])

                })
            val dialog = builder.create()
            dialog.show()
            number1 = number
            if (Right){
                number1 = number1 + 1
            }else{
                number1 = number1 - 1
            }
        }else {
            number1 = number
            if (Right) {
                number1 = number1 + 1
            } else {
                number1 = number1 - 1
            }
            CreateItemTime(Car[number1])
        }
        return number1
    }
}