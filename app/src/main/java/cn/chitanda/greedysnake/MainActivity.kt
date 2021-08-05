package cn.chitanda.greedysnake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import cn.chitanda.greedysnake.theme.GreedySnakeTheme
import cn.chitanda.greedysnake.ui.GameMap
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            GreedySnakeTheme {
                ProvideWindowInsets {

                    // A surface container using the 'background' color from the theme
                    val viewModel by viewModels<GameViewModel>()
                    val p by viewModel.point.collectAsState()
                    val gameStatus by viewModel.gameStatus.collectAsState()
                    val snack by viewModel.snack.collectAsState()
                    Surface(
                        color = MaterialTheme.colors.background
                    ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(
                                    rememberInsetsPaddingValues(
                                        insets = LocalWindowInsets.current.systemBars,
                                        applyBottom = true,
                                        applyTop = true
                                    )
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Score: ${snack.size - 4}",
                                modifier = Modifier
                                    .weight(3f)
                                    .fillMaxSize(),
                                fontSize = 20.sp, color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            GameMap(Modifier.weight(9f)) {
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    val topLeft =
                                        Offset(
                                            p.x * itemSize + boardStart,
                                            p.y * itemSize + boardTop
                                        )
                                    drawRect(Color.Red, topLeft, Size(itemSize, itemSize))
                                    snack.forEach {
                                        val offset =
                                            Offset(
                                                it.x * itemSize + boardStart,
                                                it.y * itemSize + boardTop
                                            )
                                        drawRect(Color.Blue, offset, Size(itemSize, itemSize))
                                    }
                                }
                            }
                            if (gameStatus == Game.Status.Close) {
                                TextButton(
                                    onClick = { viewModel.startGame() },
                                    Modifier.weight(2f)
                                ) {
                                    Text(text = "Start")
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(2f))
                            }
                            DirectionController(
                                viewModel = viewModel, modifier = Modifier
                                    .weight(4f)
                                    .aspectRatio(1f, true)
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DirectionController(viewModel: GameViewModel, modifier: Modifier) {
    val gameStatus by viewModel.gameStatus.collectAsState()
    Column(
        modifier = modifier
    ) {
        Row(Modifier.weight(1f)) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                onClick = { viewModel.up() },
                enabled = gameStatus == Game.Status.Running
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_up),
                    contentDescription = "up"
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
        }
        Row(Modifier.weight(1f)) {
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                onClick = { viewModel.left() },
                enabled = gameStatus == Game.Status.Running
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_left),
                    contentDescription = "left"
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                onClick = { viewModel.right() },
                enabled = gameStatus == Game.Status.Running
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_right),
                    contentDescription = "right"
                )
            }
        }
        Row(Modifier.weight(1f)) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                onClick = { viewModel.down() },
                enabled = gameStatus == Game.Status.Running
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_down),
                    contentDescription = "down"
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
        }
    }
}