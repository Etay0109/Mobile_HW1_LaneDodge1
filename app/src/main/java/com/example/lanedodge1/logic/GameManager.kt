package com.example.lanedodge1.logic

import com.example.lanedodge1.utilities.CellType

class GameManager(private val lifeCount: Int = 3) {
    companion object {
        const val ROWS = 8
        const val COLS = 5
    }
    private var ticks = 0
    private val obstaclesMatrix = Array(ROWS) { Array(COLS) { CellType.EMPTY } }
    private var lastRowState = Array(COLS) { CellType.EMPTY }

    var carPosition: Int = COLS / 2
        private set

    var collisions: Int = 0
        private set

    var coins: Int = 0
        private set

    var distance: Int = 0
        private set

    fun getScore(): Int {
        return distance + coins * 10
    }

    val isGameOver: Boolean
        get() = collisions == lifeCount

    fun onCollision() {
        if (!isGameOver)    collisions++
    }

    fun moveCarLeft() {
        if (carPosition > 0) carPosition--
    }

    fun moveCarRight() {
        if (carPosition < COLS - 1) carPosition++
    }

    fun getObstaclesMatrix(): Array<Array<CellType>> = obstaclesMatrix

    fun initObstacles() {   //Random 2 stones in the matrix
        for (row in 0 until ROWS) {
            for (col in 0 until COLS) {
                obstaclesMatrix[row][col] = CellType.EMPTY
            }
        }
        val row1 = (0 until ROWS - 2).random()
        val col1 = (0 until COLS).random()
        obstaclesMatrix[row1][col1] = CellType.OBSTACLE
        var row2: Int
        var col2: Int
        do {
            row2 = (0 until ROWS - 2).random()
            col2 = (0 until COLS).random()
        } while (row2 == row1 && col2 == col1)

        obstaclesMatrix[row2][col2] = CellType.OBSTACLE
    }

    fun checkCollision() {
        val lastRow = ROWS - 1

        for (col in 0 until COLS) {

            val current = obstaclesMatrix[lastRow][col]
            val previous = lastRowState[col]

            if (current == CellType.OBSTACLE && previous == CellType.OBSTACLE) {
                if (col == carPosition) {
                    onCollision()
                }
                obstaclesMatrix[lastRow][col] = CellType.EMPTY
            }

            if (current == CellType.COIN) {

                if (col == carPosition) {
                    coins++
                    obstaclesMatrix[lastRow][col] = CellType.EMPTY
                }
                else if (previous == CellType.COIN) {
                    obstaclesMatrix[lastRow][col] = CellType.EMPTY
                }
            }
        }

        for (col in 0 until COLS) {
            lastRowState[col] = obstaclesMatrix[lastRow][col]
        }
    }




    fun addNewObstacle() {  //Add new obstacle at the first row
        ticks++
        if (ticks % 3 == 0) {
            val col = (0 until COLS).random()
            if (obstaclesMatrix[0][col] == CellType.EMPTY) {
                obstaclesMatrix[0][col] = if (ticks % 4 == 0)
                    CellType.COIN else CellType.OBSTACLE

            }
        }
    }

    fun moveObstaclesDown() {   //Move obstacle down on the matrix
        for (row in ROWS - 2 downTo 0) {
            for (col in 0 until COLS) {
                if (obstaclesMatrix[row][col] != CellType.EMPTY &&
                    obstaclesMatrix[row + 1][col] == CellType.EMPTY) {

                    obstaclesMatrix[row + 1][col] = obstaclesMatrix[row][col]
                    obstaclesMatrix[row][col] = CellType.EMPTY
                }
            }
        }
    }

    fun tick() {
        distance ++
        moveObstaclesDown()
        checkCollision()
        addNewObstacle()
    }
}