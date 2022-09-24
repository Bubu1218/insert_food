package com.example.insert_food

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //取得資料庫實體
        dbrw = insert_food_DB(this).writableDatabase
        //宣告 Adapter 並連結 ListView
        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, items)
        findViewById<ListView>(R.id.listView).adapter = adapter
        //設定監聽器
        setListener()
    }
    override fun onDestroy() {
        dbrw.close() //關閉資料庫
        super.onDestroy()
    }
    //設定監聽器
    private fun setListener() {
        val ed_food_name = findViewById<EditText>(R.id.ed_food_name)
        val ed_calorie = findViewById<EditText>(R.id.ed_calorie)
        val ed_protein = findViewById<EditText>(R.id.ed_protein)
        val ed_fat = findViewById<EditText>(R.id.ed_fat)
        val ed_carbohydrate = findViewById<EditText>(R.id.ed_carbohydrate)
        findViewById<Button>(R.id.btn_insert).setOnClickListener {
            //判斷是否有填入品名或熱量
            if (ed_food_name.length() < 1 || ed_calorie.length() < 1)
                showToast("品名、熱量欄位請勿留空")
            else
                try {
                    //新增一筆紀錄於 myFoodTable 資料表
                    dbrw.execSQL(
                        "INSERT INTO myFoodTable(food_name, calorie, protein, fat, carbohydrate) VALUES(?,?,?,?,?)",
                        arrayOf(ed_food_name.text.toString(),
                            ed_calorie.text.toString(),
                            ed_protein.text.toString(),
                            ed_fat.text.toString(),
                            ed_carbohydrate.text.toString())
                    )////////////////////////////////////////////////////////////////////////////////
                    showToast("新增:${ed_food_name.text},熱量:${ed_calorie.text},蛋白質:${ed_protein.text},脂肪:${ed_fat.text},醣類:${ed_carbohydrate.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("新增失敗:$e")
                }
        }
        findViewById<Button>(R.id.btn_update).setOnClickListener {
            //判斷是否有填入品名或熱量
            if (ed_food_name.length() < 1 || ed_calorie.length() < 1)
                showToast("品名、熱量欄位請勿留空")
            else
                try {
                    //尋找相同品名的紀錄並更新 各欄位的值
                    if(ed_protein.length()>0)
                        dbrw.execSQL("UPDATE myFoodTable SET calorie=${ed_calorie.text}, protein = ${ed_protein.text} WHERE food_name LIKE '${ed_food_name.text}'")
                    if(ed_fat.length()>0)
                        dbrw.execSQL("UPDATE myFoodTable SET calorie=${ed_calorie.text}, fat = ${ed_fat.text} WHERE food_name LIKE '${ed_food_name.text}'")
                    if(ed_carbohydrate.length()>0)
                        dbrw.execSQL("UPDATE myFoodTable SET calorie=${ed_calorie.text}, carbohydrate = ${ed_carbohydrate.text} WHERE food_name LIKE '${ed_food_name.text}'")
                    else
                        dbrw.execSQL("UPDATE myFoodTable SET calorie=${ed_calorie.text} WHERE food_name LIKE '${ed_food_name.text}'")

                    showToast("更新:${ed_food_name.text},熱量:${ed_calorie.text},蛋白質:${ed_protein.text},脂肪:${ed_fat.text},醣類:${ed_carbohydrate.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("更新失敗:$e")
                }
        }
        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            //判斷是否有填入品名
            if (ed_food_name.length() < 1)
                showToast("品名請勿留空")
            else
                try {
                    //從 myFoodTable 資料表刪除相同品名的紀錄
                    dbrw.execSQL("DELETE FROM myFoodTable WHERE food_name LIKE '${ed_food_name.text}'")
                    showToast("刪除:${ed_food_name.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("刪除失敗:$e")
                }
        }
        findViewById<Button>(R.id.btn_query).setOnClickListener {
            //若無輸入品名則 SQL 語法為查詢全部菜色，反之查詢該品名資料
            val queryString = if (ed_food_name.length() < 1)
                "SELECT * FROM myFoodTable"
            else
                "SELECT * FROM myFoodTable WHERE food_name LIKE '%${ed_food_name.text}%'"
            val c = dbrw.rawQuery(queryString, null)
            c.moveToFirst() //從第一筆開始輸出
            items.clear() //清空舊資料
            showToast("共有${c.count}筆資料")
            for (i in 0 until c.count) {
                //加入新資料
                items.add("品名:${c.getString(0)}\t\t\t\t\t\t\t\t\t\t 熱量:${c.getInt(1)}")
                c.moveToNext() //移動到下一筆
            }
            adapter.notifyDataSetChanged() //更新列表資料
            c.close() //關閉 Cursor
        }
    }
    //建立 showToast 方法顯示 Toast 訊息
    private fun showToast(text: String) =
        Toast.makeText(this,text, Toast.LENGTH_LONG).show()
    //清空輸入的品名與各欄位值
    private fun cleanEditText() {
        findViewById<EditText>(R.id.ed_food_name).setText("")
        findViewById<EditText>(R.id.ed_calorie).setText("")
        findViewById<EditText>(R.id.ed_protein).setText("")
        findViewById<EditText>(R.id.ed_fat).setText("")
        findViewById<EditText>(R.id.ed_carbohydrate).setText("")
    }
}