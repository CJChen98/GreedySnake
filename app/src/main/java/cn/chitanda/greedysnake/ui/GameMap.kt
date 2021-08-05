package cn.chitanda.greedysnake.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import cn.chitanda.greedysnake.theme.LineColor
import kotlin.math.abs
import kotlin.math.min

/**
 *@author: Chen
 *@createTime: 2021/8/2 10:32
 *@description:
 **/
@Composable
fun GameMap(modifier: Modifier = Modifier, content: @Composable GameMapScope.() -> Unit) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (size != Size.Zero) {
                GameMapScopeInstance.itemSize = min(size.width, size.height) / 30f
                GameMapScopeInstance.boardTop =
                    abs(size.height - GameMapScopeInstance.itemSize * 30) / 2f
                GameMapScopeInstance.boardStart =
                    abs(size.width - GameMapScopeInstance.itemSize * 30) / 2f
                GameMapScopeInstance.boardEnd = size.width - GameMapScopeInstance.boardStart
                GameMapScopeInstance.boardBottom = size.height - GameMapScopeInstance.boardTop
                //竖线
                repeat(30 + 1) {
                    val offset =
                        GameMapScopeInstance.itemSize * it + GameMapScopeInstance.boardStart
                    val start = Offset(offset, GameMapScopeInstance.boardTop)
                    val end = Offset(offset, GameMapScopeInstance.boardBottom)
                    drawLine(LineColor, start = start, end = end, strokeWidth = 1.dp.toPx())
                }
                //横线
                repeat(30 + 1) {
                    val offset = GameMapScopeInstance.boardTop + it * GameMapScopeInstance.itemSize
                    val start = Offset(GameMapScopeInstance.boardStart, offset)
                    val end = Offset(GameMapScopeInstance.boardEnd, offset)
                    drawLine(LineColor, start = start, end = end, strokeWidth = 1.dp.toPx())
                }
            }
        }
        GameMapScopeInstance.content()
    }
}

interface GameMapScope {
    //    @Stable
    var itemSize: Float

    //    @Stable
    var boardStart: Float

    //    @Stable
    var boardEnd: Float

    //    @Stable
    var boardTop: Float

    //    @Stable
    var boardBottom: Float
}

private object GameMapScopeInstance : GameMapScope {
    override var itemSize: Float = 0f
    override var boardStart: Float = 0f
    override var boardEnd: Float = 0f
    override var boardTop: Float = 0f
    override var boardBottom: Float = 0f
}


