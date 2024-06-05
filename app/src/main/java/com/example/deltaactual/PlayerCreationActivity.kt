package com.example.deltaactual

import android.util.Log
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deltaactual.ui.theme.DeltaActualTheme

data class PlayerStats(
    val playerName: String,
    var games: Int = 0,
    var wins: Int = 0
) {
    val winPercentage: String
        get() = if (games > 0) {
            (wins.toFloat() / games * 100).toInt().toString()+ "%"
        }else {
            "N/A"
        }
}

class PlayerCreationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeltaActualTheme {
                PlayerCreationScreen()
            }
        }
    }
}

@Preview
@Composable
fun PlayerCreationScreen() {
    val lightblue: Color = colorResource(R.color.light_blue)
    val lightred: Color = colorResource(R.color.light_red)
    var player1Name by remember { mutableStateOf(TextFieldValue()) }
    var player2Name by remember { mutableStateOf(TextFieldValue()) }
    var isPlayer1Focused by remember { mutableStateOf(false) }
    var isPlayer2Focused by remember { mutableStateOf(false) }
    val height = LocalConfiguration.current.screenHeightDp.dp
    val width = LocalConfiguration.current.screenWidthDp.dp
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("player_prefs", Context.MODE_PRIVATE)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(R.color.grad_yel),
                        colorResource(R.color.grad_red)
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorResource(R.color.grad_yel),
                            colorResource(R.color.grad_red)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .border(
                        1.dp,
                        color = colorResource(R.color.border_grey),
                        CutCornerShape(35.dp)
                    )
                    .graphicsLayer {
                        shadowElevation = 10f
                        shape = CutCornerShape(32.dp)
                        clip = true
                    }
                    .background(colorResource(R.color.peach)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "PLAYER INFORMATION",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            OutlinedTextField(
                value = player1Name,
                onValueChange = { player1Name = it },
                label = {
                    if(!isPlayer1Focused&&player1Name.text.isEmpty()){
                        Text(
                            "Enter Player-1 Name",
                            color = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier
                                .offset(width / 4 - 28.dp)
                                .drawBehind {
                                    drawLine(
                                        color = lightred,
                                        strokeWidth = 6f,
                                        start = Offset(-4f, size.height + 10),
                                        end = Offset(size.width + 10f, size.height + 10),
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(20f, 10f),
                                            0f
                                        )
                                    )
                                }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .onFocusChanged { isPlayer1Focused = it.isFocused },
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.question_vio),
                    unfocusedContainerColor = colorResource(R.color.question_vio),
                    disabledContainerColor = colorResource(R.color.question_vio),
                )
            )
            OutlinedTextField(
                value = player2Name,
                onValueChange = { player2Name = it },
                label = {
                    if(!isPlayer2Focused&&player2Name.text.isEmpty()){
                        Text(
                            "Enter Player-2 Name",
                            color = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier
                                .offset(width / 4 - 28.dp)
                                .drawBehind {
                                    drawLine(
                                        color = lightblue,
                                        strokeWidth = 6f,
                                        start = Offset(-4f, size.height + 10),
                                        end = Offset(size.width + 10f, size.height + 10),
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(20f, 10f),
                                            0f
                                        )
                                    )
                                }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .onFocusChanged { isPlayer2Focused = it.isFocused },
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.question_vio),
                    unfocusedContainerColor = colorResource(R.color.question_vio),
                    disabledContainerColor = colorResource(R.color.question_vio),
                )
            )
            Image(
                painter = painterResource(R.drawable.players),
                contentDescription = "2 players",
                modifier = Modifier
                    .width(330.dp)
                    .height(150.dp)
            )
            Button(
                onClick = {
                    val player1NameText = player1Name.text.trim()
                    val player2NameText = player2Name.text.trim()

                    if (player1NameText.isEmpty() || player2NameText.isEmpty()) {
                        Toast.makeText(context, "Please enter both player names", Toast.LENGTH_SHORT).show()
                    }
                    val player1ExistingGames = sharedPreferences.getInt("${player1NameText}/games", 0)
                    val player1ExistingWins = sharedPreferences.getInt("${player1NameText}/wins", 0)
                    val player2ExistingGames = sharedPreferences.getInt("${player2NameText}/games", 0)
                    val player2ExistingWins = sharedPreferences.getInt("${player2NameText}/wins", 0)

                    // Store (or update) player data
                    savePlayerData(context, player1NameText, player1ExistingGames, player1ExistingWins)
                    savePlayerData(context, player2NameText, player2ExistingGames, player2ExistingWins)

                    val intent = Intent(context, GameActivity::class.java).apply {
                        putExtra("PLAYER1_NAME", player1NameText)
                        putExtra("PLAYER2_NAME", player2NameText)
                    }
                    Log.d("PlayerCreation", "Starting GameActivity with Player 1: $player1NameText, Player 2: $player2NameText")
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.dark_blue)),
                modifier = Modifier
                    .height(height / 12)
                    .width(width / 2.3f)
                    .padding(5.dp)
                    .shadow(0.5f.dp, shape = RoundedCornerShape(30.dp))
            ) {
                Text(
                    "START", fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

fun savePlayerData(context: Context, playerName: String, games: Int, wins: Int) {
    val sharedPreferences = context.getSharedPreferences("player_prefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putInt("$playerName/games", games)
        putInt("$playerName/wins", wins)
        apply()
    }
}


