package com.example.lanedodge1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.lanedodge1.logic.GameManager
import com.example.lanedodge1.utilities.CellType
import com.example.lanedodge1.utilities.SignalManager
import com.example.lanedodge1.utilities.TiltDetector
import com.example.lanedodge1.interfaces.TiltCallback
import com.example.lanedodge1.utilities.SingleSoundPlayer
import com.google.android.material.textview.MaterialTextView
import com.example.lanedodge1.logic.RecordManager
import com.example.lanedodge1.model.Record
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var main_IMG_cells: Array<Array<AppCompatImageView>>

    private val cellsId = arrayOf(
        intArrayOf(R.id.cell_0_0, R.id.cell_0_1, R.id.cell_0_2, R.id.cell_0_3, R.id.cell_0_4),
        intArrayOf(R.id.cell_1_0, R.id.cell_1_1, R.id.cell_1_2, R.id.cell_1_3, R.id.cell_1_4),
        intArrayOf(R.id.cell_2_0, R.id.cell_2_1, R.id.cell_2_2, R.id.cell_2_3, R.id.cell_2_4),
        intArrayOf(R.id.cell_3_0, R.id.cell_3_1, R.id.cell_3_2, R.id.cell_3_3, R.id.cell_3_4),
        intArrayOf(R.id.cell_4_0, R.id.cell_4_1, R.id.cell_4_2, R.id.cell_4_3, R.id.cell_4_4),
        intArrayOf(R.id.cell_5_0, R.id.cell_5_1, R.id.cell_5_2, R.id.cell_5_3, R.id.cell_5_4),
        intArrayOf(R.id.cell_6_0, R.id.cell_6_1, R.id.cell_6_2, R.id.cell_6_3, R.id.cell_6_4)
    )

    private lateinit var bottom_car_row: Array<AppCompatImageView>
    private lateinit var bottom_obstacle_row: Array<AppCompatImageView>
    private lateinit var main_BTN_left: ImageButton
    private lateinit var main_BTN_right: ImageButton
    private lateinit var timerJob: Job
    private lateinit var gameManager: GameManager
    private var lastCollisions = 0
    private var lastCoinsCollected = 0
    private var isFastMode = false
    private lateinit var main_LBL_score: MaterialTextView
    private lateinit var main_BTN_top_ten: MaterialButton
    private lateinit var main_BTN_back_to_menu: MaterialButton

    private lateinit var main_LAYOUT_game_over: View
    private lateinit var tiltDetector: TiltDetector
    private lateinit var recordManager: RecordManager
    private var useSensor = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        isFastMode = intent.getBooleanExtra(
            MenuActivity.Companion.FAST_MODE,
            false
        )
        useSensor = intent.getBooleanExtra(
            MenuActivity.Companion.USE_SENSOR,
            false
        )
        recordManager = RecordManager(this)

        gameManager = GameManager(main_IMG_hearts.size)
        initViews()
        if (useSensor) {
            initTiltDetector()
        }
    }

    private fun findViews() {
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
        main_IMG_cells = Array(GameManager.Companion.ROWS - 1) { row ->
            Array(GameManager.Companion.COLS) { col ->
                findViewById(cellsId[row][col])
            }
        }
        bottom_car_row = arrayOf(
            findViewById(R.id.cell_7_0_car),
            findViewById(R.id.cell_7_1_car),
            findViewById(R.id.cell_7_2_car),
            findViewById(R.id.cell_7_3_car),
            findViewById(R.id.cell_7_4_car)
        )

        bottom_obstacle_row = arrayOf(
            findViewById(R.id.cell_7_0_obstacle),
            findViewById(R.id.cell_7_1_obstacle),
            findViewById(R.id.cell_7_2_obstacle),
            findViewById(R.id.cell_7_3_obstacle),
            findViewById(R.id.cell_7_4_obstacle)
        )

        main_BTN_left = findViewById(R.id.main_BTN_left)
        main_BTN_right = findViewById(R.id.main_BTN_right)

        main_BTN_top_ten = findViewById(R.id.main_BTN_top_ten)
        main_LBL_score = findViewById(R.id.main_LBL_score)
        main_LAYOUT_game_over = findViewById(R.id.main_LAYOUT_game_over)
        main_BTN_back_to_menu = findViewById(R.id.main_BTN_back_to_menu)

    }
    private fun initViews() {
        main_LAYOUT_game_over.visibility = View.GONE

        if (useSensor) {
            main_BTN_left.visibility = View.INVISIBLE
            main_BTN_right.visibility = View.INVISIBLE
        }

        gameManager.initObstacles()

        main_BTN_left.setOnClickListener {
            gameManager.moveCarLeft()
            refreshUI()
        }

        main_BTN_right.setOnClickListener {
            gameManager.moveCarRight()
            refreshUI()
        }

        main_BTN_top_ten.setOnClickListener {
            val intent = Intent(this, TopTenActivity::class.java)
            startActivity(intent)
        }

        main_BTN_back_to_menu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        refreshUI()
        startTimer()
    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            this,
            object : TiltCallback {

                override fun tiltLeft() {
                    gameManager.moveCarLeft()
                    refreshUI()
                }

                override fun tiltRight() {
                    gameManager.moveCarRight()
                    refreshUI()
                }
            }
        )
    }


    private fun refreshUI() {
        // vibrate,toast and sound if collision
        if (gameManager.collisions > lastCollisions) {
            SignalManager.Companion.getInstance().vibrate()
            SignalManager.Companion.getInstance().toast("Crash!")
            val ssp = SingleSoundPlayer(this)
            ssp.playSound(R.raw.car_crash)
        }
        lastCollisions = gameManager.collisions

        if (gameManager.coins > lastCoinsCollected) {
            val ssp = SingleSoundPlayer(this)
            ssp.playSound(R.raw.coin_sound)
        }
        lastCoinsCollected = gameManager.coins

        if (gameManager.collisions != 0) { //Update the hearts in the UI
            val heart_to_remove = gameManager.collisions - 1
            if (heart_to_remove in main_IMG_hearts.indices) {
                main_IMG_hearts[heart_to_remove].visibility = View.INVISIBLE
            }
        }
        //Lost
        if (gameManager.isGameOver) {
            stopTimer()
            main_LAYOUT_game_over.visibility = View.VISIBLE
            val score = gameManager.getScore()

            if (recordManager.isTopTen(score)) {
                lifecycleScope.launch {
                    delay(1000)
                    showNameDialog(score)
                }
            }
            return
        }
        //Ongoing
        val matrix = gameManager.getObstaclesMatrix()

        for (row in matrix.indices) {
            for (col in matrix[row].indices) {

                if (row < matrix.lastIndex) {  //Update the obstacles and coins in the UI except the last row
                    if (matrix[row][col] == CellType.OBSTACLE) {
                        main_IMG_cells[row][col].setImageResource(R.drawable.obstacle)
                        main_IMG_cells[row][col].visibility = View.VISIBLE

                    } else if (matrix[row][col] == CellType.COIN) {
                        main_IMG_cells[row][col].setImageResource(R.drawable.coin)
                        main_IMG_cells[row][col].visibility = View.VISIBLE

                    } else {
                        main_IMG_cells[row][col].visibility = View.INVISIBLE
                    }
                }

                else {  //Update the car, obstacles and coin in the UI in the last row
                    if (matrix[row][col] == CellType.OBSTACLE) {
                        bottom_obstacle_row[col].setImageResource(R.drawable.obstacle)
                        bottom_obstacle_row[col].visibility = View.VISIBLE

                    } else if (matrix[row][col] == CellType.COIN) {
                        bottom_obstacle_row[col].setImageResource(R.drawable.coin)
                        bottom_obstacle_row[col].visibility = View.VISIBLE

                    } else {
                        bottom_obstacle_row[col].visibility = View.INVISIBLE
                    }
                    bottom_car_row[col].apply {
                        visibility = if (col == gameManager.carPosition) View.VISIBLE else View.INVISIBLE
                        rotation = -90f
                    }
                    main_LBL_score.text = "Score: ${gameManager.getScore()}"
                }
            }
        }
    }

    private fun startTimer() {
        if (::timerJob.isInitialized && timerJob.isActive) {
            return
        }
        timerJob = lifecycleScope.launch {
            while (true) {
                gameManager.tick()
                refreshUI()

                if (gameManager.isGameOver) {
                    stopTimer()
                    break
                }

                delay(
                    if (isFastMode) 400L else 600L
                )

            }
        }
    }

    private fun stopTimer() {
        if (::timerJob.isInitialized && timerJob.isActive) {
            timerJob.cancel()
        }
    }


    override fun onResume() {   //resume the app when we are back
        super.onResume()
        if (useSensor) {
            tiltDetector.start()
        }
        if (!gameManager.isGameOver) {
            startTimer()
        }
    }

    override fun onPause() {    // Pause the app when we are not using it
        super.onPause()
        if (useSensor) {
            tiltDetector.stop()
        }
        stopTimer()
    }
    private fun getCurrentLocationSimple(onResult: (Double, Double) -> Unit) { // Gets the device's last known location


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onResult(0.0, 0.0)
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    onResult(location.latitude, location.longitude)
                } else {
                    onResult(0.0, 0.0)
                }
            }
    }

    private fun showNameDialog(score: Int) { // Shows a dialog to enter the player's name when a Top 10 score is achieved
        val editText = android.widget.EditText(this)
        editText.hint = "Enter your name"

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("New High Score 🎉")
            .setMessage("You made it to the Top 10!")
            .setView(editText)
            .setCancelable(false)
            .setPositiveButton("Save") { _, _ ->
                val name = editText.text.toString().ifBlank { "Player" }

                getCurrentLocationSimple { lat, lon ->
                    val record = Record(
                        name = name,
                        score = score,
                        lat = lat,
                        lon = lon
                    )
                    recordManager.addRecord(record)
                }
            }
            .show()
    }

}