package com.example.insert_food
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
//自訂建構子並繼承 SQLiteOpenHelper 類別
class insert_food_DB (
    context: Context,
    name: String = database,
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = v
) : SQLiteOpenHelper(context, name, factory, version) {
    companion object {
        private const val database = "editFoodDB" //資料庫名稱
        private const val v = 1 //資料庫版本

    }
    override fun onCreate(db: SQLiteDatabase) {
        //建立 myFoodTable 資料表
        db.execSQL("CREATE TABLE myFoodTable(food_name text PRIMARY KEY, calorie float NOT NULL, protein float, fat float, carbohydrate float)")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        //升級資料庫版本時，刪除舊資料表，並重新執行 onCreate()，建立新資料表
        db.execSQL("DROP TABLE IF EXISTS myFoodTable")
        onCreate(db)
    }
}