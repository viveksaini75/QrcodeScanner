package com.qr.scanner.history

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.qr.scanner.model.Result
import io.reactivex.Single

@Dao
interface HistoryDao {

    companion object {
        private var INSTANCE: HistoryDao? = null
        fun getInstance(context: Context): HistoryDao {
            return INSTANCE ?: Room
                .databaseBuilder(context.applicationContext, HistoryDataBase::class.java, "result_db")
                .addMigrations(object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE Result ADD COLUMN name TEXT")
                    }
                }).allowMainThreadQueries()
                .build()
                .historyDao().apply {
                    INSTANCE = this
                }
        }
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(result: Result)

    @Update
    fun update(result: Result)

    @Delete
    fun delete(result: Result)

    @Query("delete from results")
    fun deleteAllHistory()

    @Query("select * from results ORDER BY date DESC")
    fun getAllHistory(): LiveData<List<Result>>

    @Query("SELECT * FROM results WHERE isFavorite = 1 ORDER BY date DESC")
    fun getFavorites(): LiveData<List<Result>>

    @Query("SELECT * FROM results WHERE isGenerated = 1 ORDER BY date DESC")
    fun getGenerate(): LiveData<List<Result>>

    @Query("SELECT * FROM results WHERE format = :format AND text = :text LIMIT 1")
    fun find(format: String, text: String): List<Result>


    fun insert(result: Result, doNotSaveDuplicates: Boolean) {
        return if (doNotSaveDuplicates) {
            saveIfNotPresent(result)
        } else {
            insert(result)
        }
    }

    fun saveIfNotPresent(result: Result){
        if(find(result.format.name, result.text).isEmpty()){
            insert(result)
        }else {
            find(result.format.name, result.text)[0].id
        }
    }

}