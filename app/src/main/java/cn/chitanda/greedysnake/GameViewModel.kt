package cn.chitanda.greedysnake

import android.graphics.Point
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.chitanda.greedysnake.ui.Direction
import cn.chitanda.greedysnake.ui.Snack
import cn.chitanda.greedysnake.ui.SnackLife
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *@author: Chen
 *@createTime: 2021/8/2 10:13
 *@description:
 **/
private const val TAG = "GameViewModel"

class GameViewModel : ViewModel() {
    private val _point = MutableStateFlow(Point(0, 0))
    val point: StateFlow<Point>
        get() = _point

    private val _snake = Snack.create(Point(30, 30), this::createFood)
    val snack: StateFlow<List<Point>>
        get() = _snake.data
    private val _gameStatus = MutableStateFlow(Game.Status.Close)
    val gameStatus: StateFlow<Game.Status>
        get() = _gameStatus

    private var createFoodJob: Job? = null
    private var moveJob: Job? = null

    init {
        viewModelScope.launch {
            _snake.reset()
        }
    }

    fun startGame() {
        createFoodJob?.cancel()
        moveJob?.cancel()
        createFoodJob = viewModelScope.launch(Dispatchers.Default) {
            _snake.reset()
            while (_snake.life.value != SnackLife.Dead) {
                createFood()
                delay(1000L * (5..15).random())
            }
        }
        moveJob = viewModelScope.launch(Dispatchers.Default) {
            while (_snake.life.value != SnackLife.Dead) {
                delay(_snake.speed)
                _snake.move()
            }
        }
        viewModelScope.launch {
            _gameStatus.emit(Game.Status.Running)
            _snake.life.collectLatest {
                if (it == SnackLife.Dead) {
                    createFoodJob?.cancel()
                    moveJob?.cancel()
                    _gameStatus.emit(Game.Status.Close)
                }
            }
        }
    }

    private suspend fun createFood() {
        if (_snake.life.value != SnackLife.Dead) {
            val range = 0..29
            val new = Point(range.random(), range.random())
            _point.emit(new)
            _snake.food = new
        }
    }

    fun left() {
        _snake.direction = Direction.Left
    }

    fun right() {
        _snake.direction = Direction.Right
    }

    fun up() {
        _snake.direction = Direction.Top
    }

    fun down() {
        _snake.direction = Direction.Bottom
    }

}