package com.example.carrent.menu.grid.bditems

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.carrent.ClassProject.DbHelper
import com.example.carrent.ClassProject.items
import com.example.carrent.menu.grid.ItemInfoActivity
import com.example.carrent.R
import org.w3c.dom.Text
import java.io.IOException

class Items_adapter(var items: List<items>, var context:Context) : RecyclerView.Adapter<Items_adapter.MyViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val db = DbHelper(context,null)

        val Days = arrayListOf<String>("Пн","Вт","Ср","Чт","Пт","Сб","Вс")
        val time_id = items[position].time_free
        val time_free = db.getTime(time_id.toString())
        val time_free_days = arrayListOf<String>(
            time_free.Monday,
            time_free.Tuesday,
            time_free.Wednesday,
            time_free.Thursday,
            time_free.Friday,
            time_free.Saturday,
            time_free.Sunday
        )
        var i = 0
        for(i in 0..6){
            holder.timeViews[i].text = Days[i]
            if (time_free_days[i] == "Empty"){
                val drawable = DrawableCompat.wrap(holder.timeViews[i].background)
                DrawableCompat.setTint(drawable, Color.parseColor("#A9A3A3"))
            }
            else{
                val drawable = DrawableCompat.wrap(holder.timeViews[i].background)
                DrawableCompat.setTint(drawable, Color.parseColor("#564F4F"))
            }
        }
        
        holder.price.text = items[position].price.toString()
        holder.name.text = items[position].name + " " + items[position].model_type
        holder.rate.text = items[position].rating.toString()

        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadius = 16f // Радиус скругления в пикселях
        drawable.setColor(ContextCompat.getColor(context, android.R.color.white)) //Цвет фона
        holder.image.setImageDrawable(drawable)
        holder.image.clipToOutline = true


        items[position].image?.let { path ->
            val uri = Uri.parse(path)
            displayImageFromInternalStorage(holder.image, uri)
        } ?: run {
            Toast.makeText(context, "Изображение не найдено", Toast.LENGTH_SHORT).show()
        }

        //val bitmap = (items[position] as Drawable).toBitmap()
        //val bitmap = BitmapFactory.decodeFile((items[position].image))
        //holder.image.setImageBitmap(bitmap)

        holder.image.setOnClickListener{
            val intent = Intent(context, ItemInfoActivity::class.java)

            intent.putExtra("UserName", items[position].id_user.toString())
            intent.putExtra("itemName", items[position].id.toString())

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = itemView.findViewById(R.id.item_image)
        val price: TextView = view.findViewById(R.id.item_Price)
        val name:  TextView = view.findViewById(R.id.item_name)
        val rate:  TextView = view.findViewById(R.id.item_rate)
        val timeViews = arrayListOf<TextView>(
            view.findViewById(R.id.item_time1),
            view.findViewById(R.id.item_time2),
            view.findViewById(R.id.item_time3),
            view.findViewById(R.id.item_time4),
            view.findViewById(R.id.item_time5),
            view.findViewById(R.id.item_time6),
            view.findViewById(R.id.item_time7)
        )
    }
    fun displayImageFromInternalStorage(imageView: ImageView, uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            imageView.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
            // Обработка ошибки
        }
    }
}

