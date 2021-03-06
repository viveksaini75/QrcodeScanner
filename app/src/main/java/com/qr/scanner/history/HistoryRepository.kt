package com.qr.scanner.history

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.lifecycle.LiveData
import com.qr.scanner.utils.subscribeOnBackground
import com.qr.scanner.model.Result

class HistoryRepository(application: Application) {

    private var allHistory: LiveData<List<Result>>? = null
    private var favorites: LiveData<List<Result>>? = null
    private var generate: LiveData<List<Result>>? = null

    private val historyDao = HistoryDao?.getInstance(application)

    init {
        allHistory = historyDao.getAllHistory()
        favorites = historyDao.getFavorites()
        generate = historyDao.getGenerate()
    }

    fun insert(result: Result?) {
        subscribeOnBackground {
            historyDao.insert(result!!)
        }
    }

    fun insert(result: Result?,doNotSaveDuplicates:Boolean) {
        subscribeOnBackground {
            historyDao.insert(result!!,doNotSaveDuplicates)
        }
    }

    fun delete(result: Result?) {
        subscribeOnBackground {
            historyDao.delete(result!!)
        }
    }

    fun deleteAll() {
        subscribeOnBackground {
            historyDao.deleteAllHistory()
        }
    }

    fun getAllHistory(): LiveData<List<Result>>? {
        return allHistory!!
    }

    fun getFavorites(): LiveData<List<Result>> {
        return favorites!!
    }

    fun getGenerate(): LiveData<List<Result>> {
        return generate!!
    }

    fun update(result: Result?) {
        subscribeOnBackground {
            historyDao.update(result!!)
        }
    }


}