package com.example.lanedodge1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class MenuActivity : AppCompatActivity() {

    companion object {
        const val USE_SENSOR = "USE_SENSOR"
        const val FAST_MODE = "FAST_MODE"
    }


    private lateinit var menu_BTN_buttons: MaterialButton
    private lateinit var menu_BTN_sensor: MaterialButton
    private lateinit var menu_SW_fast: SwitchMaterial
    private lateinit var menu_BTN_top_ten: MaterialButton




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        findViews()
        initViews()

    }

    private fun initViews() {
        menu_BTN_buttons.setOnClickListener {
            startGame(false)
        }

        menu_BTN_sensor.setOnClickListener {
            startGame(true)
        }
        menu_BTN_top_ten.setOnClickListener {
            openTopTen()
        }
    }


    private fun findViews() {
        menu_BTN_buttons = findViewById(R.id.menu_BTN_buttons)
        menu_BTN_sensor = findViewById(R.id.menu_BTN_sensor)
        menu_SW_fast = findViewById(R.id.menu_SW_fast)
        menu_BTN_top_ten = findViewById(R.id.menu_BTN_top_ten)
    }

    private fun startGame(useSensor: Boolean) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(USE_SENSOR, useSensor)
        intent.putExtra(FAST_MODE, menu_SW_fast.isChecked)
        startActivity(intent)
    }

    private fun openTopTen() { // Opens the Top 10 scores screen
        val intent = Intent(this, TopTenActivity::class.java)
        startActivity(intent)
    }

}