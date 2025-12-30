package com.example.lanedodge1.logic

import android.content.Context
import com.example.lanedodge1.model.Record
import com.example.lanedodge1.utilities.Constants
import com.example.lanedodge1.utilities.SharedPreferencesManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecordManager(context: Context) {

    private val gson = Gson()
    private val sp = SharedPreferencesManager.getInstance()

    init {

        SharedPreferencesManager.init(context.applicationContext)
    }

    fun getRecords(): MutableList<Record> {
        val json = sp.getString(Constants.SP_KEYS.TOP_TEN_KEY, "")
        if (json.isEmpty()) return mutableListOf()

        val type = object : TypeToken<MutableList<Record>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addRecord(record: Record) {
        val records = getRecords()
        records.add(record)
        records.sortByDescending { it.score }

        val topTen = records.take(10)
        sp.putString(Constants.SP_KEYS.TOP_TEN_KEY, gson.toJson(topTen))
    }

    fun isTopTen(score: Int): Boolean {
        val records = getRecords()
        return records.size < 10 || score > records.last().score
    }

}
