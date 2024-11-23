package com.example.carrent.menu.sms

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
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.carrent.ClassProject.DbHelper
import com.example.carrent.ClassProject.Sms
import com.example.carrent.ClassProject.Sms_mini
import com.example.carrent.menu.grid.ItemInfoActivity
import com.example.carrent.R
import com.example.carrent.menu.grid.bditems.Items_adapter.MyViewHolder
import org.w3c.dom.Text
import java.io.IOException


class sms_adapter(private val items: List<Any>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_A = 0
        private const val TYPE_B = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Sms -> TYPE_A
            is Sms_mini -> TYPE_B
            else -> throw IllegalArgumentException("Неизвестный тип элемента")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_A -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sms_in_list, parent, false)
                MyViewHolderA(view)
            }
            TYPE_B -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sms_in_list_choice, parent, false)
                MyViewHolderB(view)
            }
            else -> throw IllegalArgumentException("Неизвестный тип представления")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyViewHolderA -> {
                val item = items[position] as Sms
                holder.login.text = item.login_input
                holder.text.text = item.text
            }
            is MyViewHolderB -> {
                val item = items[position] as Sms_mini
                holder.login.text = item.login_input
                holder.text.text = item.text
                holder.time.text = item.time

                if (item.Type == "3"){
                    holder.check.isVisible = false
                    holder.close.isVisible = false
                }

                holder.close.setOnClickListener{
                    val db = DbHelper(context,null)

                    db.redSms("Type","3",item.id.toString())
                    db.addSms(Sms(
                        id = "0",
                        login_user = item.login_input,
                        Type = "1",
                        login_input = item.login_user,
                        text = item.text+" отклонён",
                        time = "0"
                    ))
                    holder.check.isVisible = false
                    holder.close.isVisible = false

                }
                holder.check.setOnClickListener{
                    val db = DbHelper(context,null)

                    db.redSms("Type","3",item.id.toString())
                    db.addSms(Sms(
                        id = "0",
                        login_user = item.login_input,
                        Type = "1",
                        login_input = item.login_user,
                        text = item.text+" забронирован",
                        time = "0"
                    ))

                    holder.check.isVisible = false
                    holder.close.isVisible = false

                }

            }
        }
    }

    override fun getItemCount(): Int = items.size

    class MyViewHolderA(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val login: TextView = itemView.findViewById(R.id.item_name_user_sms)
        val text: TextView = itemView.findViewById(R.id.item_text_sms)
    }

    class MyViewHolderB(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val login: TextView = itemView.findViewById(R.id.item_name_user_sms)
        val text: TextView = itemView.findViewById(R.id.item_text_sms)
        val time: TextView = itemView.findViewById(R.id.list_time_sms_item)
        val check: ImageView = itemView.findViewById(R.id.image_Circle_check)
        val close: ImageView = itemView.findViewById(R.id.image_Circle_close)
    }
}