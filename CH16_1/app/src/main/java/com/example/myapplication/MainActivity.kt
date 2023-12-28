package com.example.myapplication

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var edBook: EditText
    private lateinit var edPrice: EditText
    private lateinit var btnQuery: Button
    private lateinit var btnInsert: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val items = ArrayList<String>()

    private lateinit var dbrw: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edBook = findViewById(R.id.ed_book)
        edPrice = findViewById(R.id.ed_price)
        btnQuery = findViewById(R.id.btn_query)
        btnInsert = findViewById(R.id.btn_insert)
        btnUpdate = findViewById(R.id.btn_update)
        btnDelete = findViewById(R.id.btn_delete)
        listView = findViewById(R.id.listView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        dbrw = MyDBHelper(this).writableDatabase

        btnInsert.setOnClickListener {
            if (edBook.length() < 1 || edPrice.length() < 1)
                Toast.makeText(this, "欄位請勿留空", Toast.LENGTH_SHORT).show()
            else {
                try {
                    dbrw.execSQL("INSERT INTO myTable(book,price) values(?,?)",
                        arrayOf(edBook.text.toString(), edPrice.text.toString()))
                    Toast.makeText(this, "新增書名" + edBook.text.toString() +
                            "    價格" + edPrice.text.toString(), Toast.LENGTH_SHORT).show()
                    edBook.setText("")
                    edPrice.setText("")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "新增失敗:" + e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnUpdate.setOnClickListener {
            if (edBook.length() < 1 || edPrice.length() < 1)
                Toast.makeText(this, "欄位請勿留空", Toast.LENGTH_SHORT).show()
            else {
                try {
                    dbrw.execSQL("UPDATE myTable SET price = "
                            + edPrice.text.toString()
                            + " WHERE book LIKE '"
                            + edBook.text.toString() + "'")
                    Toast.makeText(this, "新增書名" + edBook.text.toString() +
                            "    價格" + edPrice.text.toString(), Toast.LENGTH_SHORT).show()
                    edBook.setText("")
                    edPrice.setText("")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "新增失敗:" + e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnDelete.setOnClickListener {
            if (edBook.length() < 1 || edPrice.length() < 1)
                Toast.makeText(this, "欄位請勿留空", Toast.LENGTH_SHORT).show()
            else {
                try {
                    dbrw.execSQL("DELETE FROM mytable WHERE book LIKE '" + edBook.text.toString() + "'")
                    Toast.makeText(this, "刪除書名" +
                            edBook.text.toString(), Toast.LENGTH_SHORT).show()
                    edBook.setText("")
                    edPrice.setText("")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "刪除失敗:" + e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnQuery.setOnClickListener {
            val c: Cursor = if (edBook.length() < 1)
                dbrw.rawQuery("SELECT * FROM myTable", null)
            else
                dbrw.rawQuery("SELECT * FROM myTable WHERE book LIKE '" +
                        edBook.text.toString() + "'", null)

            c.moveToFirst()
            items.clear()
            Toast.makeText(this, "共有" + c.count + "筆", Toast.LENGTH_SHORT).show()
            for (i in 0 until c.count) {
                items.add("書籍:" + c.getString(0) + "\t\t\t\t價格:" + c.getString(1))
                c.moveToNext()
            }
            adapter.notifyDataSetChanged()
            c.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbrw.close()
    }
}
