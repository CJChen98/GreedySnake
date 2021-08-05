package cn.chitanda.greedysnake.ui

import android.graphics.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.abs
import kotlin.math.max

/**
 *@author: Chen
 *@createTime: 2021/8/4 14:29
 *@description:
 **/
enum class Direction(val d: Int) {
    Left(1), Top(2), Right(-1), Bottom(2)
}

enum class SnackLife {
    Live, Dead
}
typealias EatCallBack = suspend () -> Unit

class Snack private constructor() {
    private val map: Point = Point()
    private var eatCallBack: EatCallBack? = null
    private var head = Point(0, 0)
    var food: Point? = null
    private var body: MutableList<Point> = mutableListOf()
    var direction: Direction = Direction.Right
        set(value) {
            if (abs(value.d) == abs(field.d)) {
                return
            }
            field = value
        }
    val data = MutableStateFlow(head + body)
    val life = MutableStateFlow(SnackLife.Live)
    var speed = 400L
        private set

    //    val score = MutableStateFlow(0)
    suspend fun move() {
        if (life.value == SnackLife.Dead) return
        var temp = head
        val new = when (direction) {
            Direction.Right -> {
                Point(head.x + 1, head.y)
            }
            Direction.Left -> {
                Point(head.x - 1, head.y)
            }
            Direction.Top -> {
                Point(head.x, head.y - 1)
            }
            Direction.Bottom -> {
                Point(head.x, head.y + 1)
            }
        }
        if (new.x !in 0 until map.x || new.y !in 0 until map.y) {
            life.emit(SnackLife.Dead)
            return
        }
        if (body.contains(new)) {
            life.emit(SnackLife.Dead)
            return
        }
        head = new
        body.forEach {
            val t = it.copy()
            it.x = temp.x
            it.y = temp.y
            temp = t
        }
        food?.let {
            if (head == it) eat()
        }
        data.emit(head + body)
    }

    private suspend fun eat() {
        this.food = null
        eatCallBack?.invoke()
        grow()
    }

    private fun grow() {
        val last = body.last()
        val last1 = body[body.lastIndex - 1]
        val new = when {
            //up
            last.y < last1.y -> Point(last.x, last.y + 1)
            //down
            last.y > last1.y -> Point(last.x, last.y - 1)
            //left
            last.x < last1.x -> Point(last.x - 1, last.y)
            //right
            else -> Point(last.x + 1, last.y)
        }
        body.add(new)
        speed = max((speed * 0.9f).toLong(), MAX_SPEED)
    }

    suspend fun reset() {
        body = mutableListOf()
        direction = Direction.Right
        head = Point((3..map.x / 2).random(), (0..map.y / 2).random())
        body.add(Point(head.x - 1, head.y))
        body.add(Point(head.x - 2, head.y))
        body.add(Point(head.x - 3, head.y))
        data.emit(head + body)
        life.emit(SnackLife.Live)
        speed = MIN_SPEED
    }

    private constructor(map: Point, eatCallBack: EatCallBack) : this() {
        head = Point((3..map.x / 2).random(), (0..map.y / 2).random())
        body.add(Point(head.x - 1, head.y))
        body.add(Point(head.x - 2, head.y))
        body.add(Point(head.x - 3, head.y))
        this.map.x = map.x
        this.map.y = map.y
        this.eatCallBack = eatCallBack
    }

    companion object {
        fun create(map: Point, eatCallBack: EatCallBack) = Snack(map, eatCallBack)
        private const val MAX_SPEED = 100L
        private const val MIN_SPEED = 300L
    }
}

private fun Point.copy() = Point(this.x, this.y)

private operator fun Point.plus(body: List<Point>): List<Point> {
    return listOf(this) + body
}
