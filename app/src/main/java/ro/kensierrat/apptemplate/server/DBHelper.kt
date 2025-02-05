package ro.kensierrat.apptemplate.server

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import androidx.core.database.getBlobOrNull
import ro.kensierrat.apptemplate.domain.GenericModel
import java.sql.SQLException
import java.util.UUID

class DBHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                DATE_COL + " TEXT," +
                INT_COL + " INTEGER," +
                STRING_COL + " TEXT" +
                ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)

        val secQuery = ("CREATE TABLE " + TABLE_NAME2 + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                DATE_COL + " TEXT," +
                INT_COL + " INTEGER," +
                STRING_COL + " TEXT" +
                ")")
        db.execSQL(secQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2)
        onCreate(db)
    }

    // This method is for adding data in our database
    fun addGeneric(item: GenericModel){
        val db = this.writableDatabase

        try {
            val values = ContentValues()
            values.put(ID_COL, item.id.toString())
            values.put(DATE_COL, item.genericDate)
            values.put(INT_COL, item.genericInt)
            values.put(STRING_COL, item.genericString)
            val result = db.insert(TABLE_NAME, null, values)
            if (result == -1L) {
                Log.e("ItemsDbHelper", "Error inserting item into database")
                Toast.makeText(context, "Error inserting item into database", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SQLException) {
            Log.e("ItemsDbHelper", "Database insertion error: ${e.message}")
            Toast.makeText(context, "Database error during insertion", Toast.LENGTH_SHORT).show()
        } finally {
            db.close()
        }
    }

    // below method is to get
    // all data from our database
    fun getAllGenerics(): List<GenericModel> {
        val db = this.readableDatabase
        val itemList: MutableList<GenericModel> = mutableListOf()
        val results = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
        try {
            with(results) {
                while (moveToNext()) {
                    val id = getInt(getColumnIndexOrThrow(ID_COL))
                    val date = getString(getColumnIndexOrThrow(DATE_COL))
                    val intval = getInt(getColumnIndexOrThrow(INT_COL))
                    val str = getString(getColumnIndexOrThrow(STRING_COL))
                    itemList.add(GenericModel(id, date, intval, str))
                }
            }
        } catch (e: Exception) {
            Log.e("ItemsDbHelper", "Database read error: ${e.message}")
            Toast.makeText(context, "Error reading items from database", Toast.LENGTH_SHORT).show()
        } finally {
            results?.close()
            db.close()
        }
        return itemList
    }

    fun getGeneric(id: Int): GenericModel {
        val db = this.readableDatabase
        val results = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = " + id, null)
        var item = GenericModel(-1, "", -1, "")
        try {
            with(results) {
                while (moveToNext()) {
                    val id = getInt(getColumnIndexOrThrow(ID_COL))
                    val date = getString(getColumnIndexOrThrow(DATE_COL))
                    val intval = getInt(getColumnIndexOrThrow(INT_COL))
                    val str = getString(getColumnIndexOrThrow(STRING_COL))
                    item = GenericModel(id, date, intval, str)
                }
            }
        } catch (e: Exception) {
            Log.e("ItemsDbHelper", "Database read error: ${e.message}")
            Toast.makeText(context, "Error reading items from database", Toast.LENGTH_SHORT).show()
        } finally {
            results?.close()
            db.close()
        }
        return item
    }

    fun nukeDB() {
        val db = this.readableDatabase
        val results = db.delete(TABLE_NAME, null, null)
        val results2 = db.delete(TABLE_NAME2, null, null)
        Log.e("LOCALDB", "NUKED DB")
    }

    companion object{
        // here we have defined variables for our database
        private val DATABASE_NAME = "LOCALDB_GENERICONLY"
        private val DATABASE_VERSION = 2

        val TABLE_NAME = "generics"
        val TABLE_NAME2 = "complex_generics"

        val ID_COL = "id"
        val DATE_COL = "genericDate"
        val INT_COL = "genericInt"
        val STRING_COL = "genericString"
    }

    fun addComplexGeneric(item: GenericModel){
        val db = this.writableDatabase

        try {
            val values = ContentValues()
            values.put(ID_COL, item.id.toString())
            values.put(DATE_COL, item.genericDate)
            values.put(INT_COL, item.genericInt)
            values.put(STRING_COL, item.genericString)
            val result = db.insert(TABLE_NAME2, null, values)
            if (result == -1L) {
                Log.e("ItemsDbHelper", "Error inserting item into database")
                Toast.makeText(context, "Error inserting item into database", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SQLException) {
            Log.e("ItemsDbHelper", "Database insertion error: ${e.message}")
            Toast.makeText(context, "Database error during insertion", Toast.LENGTH_SHORT).show()
        } finally {
            db.close()
        }
    }

    // below method is to get
    // all data from our database
    fun getComplexGeneric(id: Int): GenericModel {
        val db = this.readableDatabase
        val results = db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " WHERE id = " + id, null)
        var item = GenericModel(-1, "", -1, "")
        try {
            with(results) {
                while (moveToNext()) {
                    val id = getInt(getColumnIndexOrThrow(ID_COL))
                    val date = getString(getColumnIndexOrThrow(DATE_COL))
                    val intval = getInt(getColumnIndexOrThrow(INT_COL))
                    val str = getString(getColumnIndexOrThrow(STRING_COL))
                    item = GenericModel(id, date, intval, str)
                }
            }
        } catch (e: Exception) {
            Log.e("ItemsDbHelper", "Database read error: ${e.message}")
            Toast.makeText(context, "Error reading items from database", Toast.LENGTH_SHORT).show()
        } finally {
            results?.close()
            db.close()
        }
        return item
    }


    fun deleteGeneric(id: Int) {
        val db = writableDatabase
        try {
            val rowsDeleted = db.delete(TABLE_NAME, "$ID_COL = ?", arrayOf(id.toString()))
            if (rowsDeleted == 0) {
                Log.e("ItemsDbHelper", "No rows deleted for ID: $id")
                Toast.makeText(context, "No item found to delete", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SQLException) {
            Log.e("ItemsDbHelper", "Database deletion error: ${e.message}")
            Toast.makeText(context, "Database error during deletion", Toast.LENGTH_SHORT).show()
        } finally {
            db.close()
        }
    }

    /*
    *
    fun deleteAlbumByUUID(id: String) {
        val db = writableDatabase
        try {
            val rowsDeleted = db.delete(TABLE_NAME, "$UUID_COL = ?", arrayOf(id))
            if (rowsDeleted == 0) {
                Log.e("ItemsDbHelper", "No rows deleted for ID: $id")
                Toast.makeText(context, "No item found to delete", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SQLException) {
            Log.e("ItemsDbHelper", "Database deletion error: ${e.message}")
            Toast.makeText(context, "Database error during deletion", Toast.LENGTH_SHORT).show()
        } finally {
            db.close()
        }
    }

    fun updateItem(item: AlbumModel) {
        val db = writableDatabase
        try {
            val values = ContentValues().apply {
                put(TITLE_COL, item.title)
                put(ARTIST_COL, item.artist)
                put(GENRE_COL, item.genreAsString())
                put(STYLE_COL, item.styleAsString())
                put(RELEASE_COL, item.release)
                put(COVER_COL, item.localCover)
            }
            val rowsAffected = db.update(
                TABLE_NAME,
                values,
                "$UUID_COL = ?",
                arrayOf(item.id.toString())
            )
            if (rowsAffected == 0) {
                Log.e("ItemsDbHelper", "No rows updated for ID: ${item.id}")
                Toast.makeText(context, "No item found to update", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SQLException) {
            Log.e("ItemsDbHelper", "Database update error: ${e.message}")
            Toast.makeText(context, "Database error during update", Toast.LENGTH_SHORT).show()
        } finally {
            db.close()
        }
    }
    *
    * */

    fun updateGeneric(id: Int, genericModel: GenericModel) {
        TODO()
    }

}