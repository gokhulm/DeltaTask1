package com.example.deltaactual

import android.util.Log
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview

data class ButtonState(var value: Int = 0, var blue: Boolean = false, var clicked: Boolean = false)

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val player1Name = intent.getStringExtra("PLAYER1_NAME") ?: "Gokhul"
        val player2Name = intent.getStringExtra("PLAYER2_NAME") ?: "Manikandan"
        Log.d("GameActivity", "Received Player 1: $player1Name, Player 2: $player2Name")
        setContent {
            MyApp(player1Name,player2Name)
        }
    }
    @Preview
    @Composable
    fun MyApp(player1Name:String="hi",player2Name:String="lol") {
        var isBlue by remember { mutableStateOf(false) }
        var firstTurn by remember { mutableIntStateOf(2) }
        val buttonStates = remember { Array(5) { Array(5) { ButtonState() } } }
        val backgroundColor = if (isBlue) R.color.light_blue else R.color.light_red
        var redScore by remember { mutableIntStateOf(0) }
        var blueScore by remember { mutableIntStateOf(0) }
        var showDialog by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(backgroundColor))
                .padding(top = 50.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            Row {
                val score = blueCount(buttonStates).toString()
                val pcolour = colorResource(R.color.light_blue)
                ScoreAndPlayer(score, player2Name, true, pcolour)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (i in 0 until 5) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (j in 0 until 5) {
                                val buttonState = buttonStates[i][j]
                                when (firstTurn) {
                                    2 -> {
                                        Button(
                                            onClick = {
                                                buttonState.blue = isBlue
                                                isBlue = !isBlue
                                                firstTurn -= 1
                                                buttonState.clicked = true
                                                buttonState.value = 3
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .padding(2.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.White.copy(alpha = 0.55F)
                                            ),
                                            shape = RoundedCornerShape(15.dp),
                                        ) {
                                            if (buttonState.clicked) {
                                                ValueCell(buttonState.blue, buttonState.value)
                                            }
                                        }
                                    }

                                    1 -> {
                                        if (!buttonState.clicked) {
                                            Button(
                                                onClick = {
                                                    buttonState.blue = isBlue
                                                    isBlue = !isBlue
                                                    firstTurn -= 1
                                                    buttonState.clicked = true
                                                    buttonState.value = 3
                                                },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .aspectRatio(1f)
                                                    .padding(2.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.White.copy(alpha = 0.55F)
                                                ),
                                                shape = RoundedCornerShape(15.dp),
                                            ) {
                                                if (buttonState.clicked) {
                                                    ValueCell(buttonState.blue, buttonState.value)
                                                }
                                            }
                                        } else {
                                            Button(
                                                onClick = { },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .aspectRatio(1f)
                                                    .padding(2.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.White.copy(alpha = 0.55F)
                                                ),
                                                shape = RoundedCornerShape(15.dp),
                                            ) {
                                                if (buttonState.clicked) {
                                                    ValueCell(buttonState.blue, buttonState.value)
                                                }
                                            }
                                        }
                                    }

                                    else -> {
                                        if (buttonState.clicked) {
                                            if (buttonState.blue == isBlue) {
                                                Button(
                                                    onClick = {
                                                        buttonState.blue = isBlue
                                                        isBlue = !isBlue
                                                        firstTurn = 0
                                                        buttonState.clicked = true
                                                        if (buttonState.value < 3) {
                                                            buttonState.value += 1
                                                        } else {
                                                            explosion(buttonStates, i, j)
                                                            buttonState.clicked = false
                                                            buttonState.value = 0
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .aspectRatio(1f)
                                                        .padding(2.dp),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color.White.copy(alpha = 0.55F)
                                                    ),
                                                    shape = RoundedCornerShape(15.dp),
                                                )
                                                {
                                                    if (buttonState.clicked) {
                                                        ValueCell(
                                                            buttonState.blue,
                                                            buttonState.value
                                                        )
                                                    }
                                                }
                                            } else {
                                                Button(
                                                    onClick = {},
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .aspectRatio(1f)
                                                        .padding(2.dp),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color.White.copy(alpha = 0.55F)
                                                    ),
                                                    shape = RoundedCornerShape(15.dp),
                                                )
                                                {
                                                    if (buttonState.clicked) {
                                                        ValueCell(
                                                            buttonState.blue,
                                                            buttonState.value
                                                        )
                                                    }
                                                }
                                            }
                                        } else {
                                            Button(
                                                onClick = {},
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .aspectRatio(1f)
                                                    .padding(2.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.White.copy(alpha = 0.55F)
                                                ),
                                                shape = RoundedCornerShape(15.dp),
                                            )
                                            {
                                                if (buttonState.clicked) {
                                                    ValueCell(buttonState.blue, buttonState.value)
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Row {
                val score = redCount(buttonStates).toString()
                val pcolour = colorResource(R.color.light_red)
                ScoreAndPlayer(score, player1Name, false, pcolour)
            }
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.Left) {
                Button(
                    onClick = {
                        for (i in 0 until 5) {
                            for (j in 0 until 5) {
                                buttonStates[i][j].value = 0
                                buttonStates[i][j].clicked = false
                                buttonStates[i][j].blue = false
                            }
                        }
                        showDialog = false
                        isBlue = false
                        redScore = 0
                        blueScore = 0
                        firstTurn = 2
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .padding(0.dp)
                        .offset(5.dp)
                        .height(50.dp)
                        .width(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "Reset", fontSize = 24.sp, color = Color.Black, modifier = Modifier.offset(0.dp,(-3).dp))
                }
                Button(
                    onClick = {
                        Intent(applicationContext, MainActivity::class.java).also {
                            startActivity(it)
                        }
                    },
                    modifier = Modifier
                        .padding(0.dp)
                        .offset(LocalConfiguration.current.screenWidthDp.dp/2-130.dp)
                        .size(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f)),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "✖", fontSize = 24.sp, color = Color.Black, modifier = Modifier.offset(0.dp,(-3).dp))
                }
            }
            redScore = redCount(buttonStates)
            blueScore = blueCount(buttonStates)
            if ((redScore == 0 || blueCount(buttonStates) == 0) && firstTurn == 0) {
                showDialog = true
            }
            if (showDialog) {
                val winner = if (blueScore == 0) player1Name else player2Name
                AlertDialog(
                    onDismissRequest = {
                        for (i in 0 until 5) {
                            for (j in 0 until 5) {
                                buttonStates[i][j].value = 0
                                buttonStates[i][j].clicked = false
                                buttonStates[i][j].blue = false
                            }
                        }
                        showDialog = false
                        isBlue = false
                        redScore = 0
                        blueScore = 0
                        firstTurn = 2
                        val sharedPreferences = getSharedPreferences("player_prefs",Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        val winnerKey = if (winner == player1Name) player1Name else player2Name
                        val loserKey = if (winner == player1Name) player2Name else player1Name
                        Log.d("GameActivity", "Winner key: $winnerKey")
                        Log.d("GameActivity", "Loser key: $loserKey")
                        val winnerWind = sharedPreferences.getInt("$winnerKey/wins", 0)
                        Log.d("GameActivity", "Winner wins before increment: $winnerWind")
                        val loserGamed = sharedPreferences.getInt("$loserKey/wins", 0)
                        Log.d("GameActivity", "Loser games before increment: $loserGamed")
                        val winnerGamed = sharedPreferences.getInt("$loserKey/wins", 0)
                        Log.d("GameActivity", "Winner games before increment: $winnerGamed")
                        val winnerGames = sharedPreferences.getInt("$winnerKey/games", 0) + 1
                        val winnerWins = sharedPreferences.getInt("$winnerKey/wins", 0) + 1
                        val loserGames = sharedPreferences.getInt("$loserKey/games", 0) + 1
                        editor.putInt("$winnerKey/games", winnerGames)
                        editor.putInt("$winnerKey/wins", winnerWins)
                        editor.putInt("$loserKey/games", loserGames)
                        editor.putBoolean("stats_updated", true)
                        editor.apply()
                        Log.d("GameActivity", "Winner wins after increment: ${sharedPreferences.getInt("$winnerKey/wins", 0)}")
                        Log.d("GameActivity", "Loser games: ${sharedPreferences.getInt("$loserKey/games", 0)}")
                        Log.d("GameActivity", "Winner games: ${sharedPreferences.getInt("$winnerKey/games", 0)}")


                        Log.d("GameActivity", "Stats updated flag set to true")
                        Log.d("GameActivity", "Updated stats for winner: $winner")

                        Intent(applicationContext, MainActivity::class.java).also {
                            startActivity(it)
                        }
                    },
                    title = { Text(text = "Winner: $winner") },
                    confirmButton = {
                        Button(
                            onClick = {
                                // Reset the game
                                for (i in 0 until 5) {
                                    for (j in 0 until 5) {
                                        buttonStates[i][j].value = 0
                                        buttonStates[i][j].clicked = false
                                        buttonStates[i][j].blue = false
                                    }
                                }
                                showDialog = false
                                isBlue = false
                                redScore = 0
                                blueScore = 0
                                firstTurn = 2
                                val sharedPreferences = getSharedPreferences("player_prefs",Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                val winnerKey = if (winner == player1Name) player1Name else player2Name
                                val loserKey = if (winner == player1Name) player2Name else player1Name
                                Log.d("GameActivity", "Winner key: $winnerKey")
                                Log.d("GameActivity", "Loser key: $loserKey")
                                val winnerWind = sharedPreferences.getInt("$winnerKey/wins", 0)
                                Log.d("GameActivity", "Winner wins before increment: $winnerWind")
                                val loserGamed = sharedPreferences.getInt("$loserKey/wins", 0)
                                Log.d("GameActivity", "Loser games before increment: $loserGamed")
                                val winnerGamed = sharedPreferences.getInt("$loserKey/wins", 0)
                                Log.d("GameActivity", "Winner games before increment: $winnerGamed")
                                val winnerGames = sharedPreferences.getInt("$winnerKey/games", 0) + 1
                                val winnerWins = sharedPreferences.getInt("$winnerKey/wins", 0) + 1
                                val loserGames = sharedPreferences.getInt("$loserKey/games", 0) + 1
                                editor.putInt("$winnerKey/games", winnerGames)
                                editor.putInt("$winnerKey/wins", winnerWins)
                                editor.putInt("$loserKey/games", loserGames)
                                editor.putBoolean("stats_updated", true)
                                editor.apply()
                                Log.d("GameActivity", "Winner wins after increment: ${sharedPreferences.getInt("$winnerKey/wins", 0)}")
                                Log.d("GameActivity", "Loser games: ${sharedPreferences.getInt("$loserKey/games", 0)}")
                                Log.d("GameActivity", "Winner games: ${sharedPreferences.getInt("$winnerKey/games", 0)}")


                                Log.d("GameActivity", "Stats updated flag set to true")
                                Log.d("GameActivity", "Updated stats for winner: $winner")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text(text = "Restart")
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun ScoreAndPlayer(
        score: String = "9",
        player: String = "Test",
        isFlip: Boolean = false,
        pcolour: Color = Color.Blue
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isFlip) {
                        Modifier.graphicsLayer(rotationZ = 180f)
                    } else {
                        Modifier
                    }
                ),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .offset(x = LocalConfiguration.current.screenWidthDp.dp * 0.3f)
                    .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(25.dp))
                    .padding(top = 2.dp, bottom = 2.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(pcolour),
            ) {
                Text(
                    text = player,
                    style = TextStyle(
                        fontSize = 23.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(10.dp)
                )
            }
            Box(
                modifier = Modifier
                    .offset(x = LocalConfiguration.current.screenWidthDp.dp * 0.4f)
                    .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(25.dp))
                    .fillMaxWidth(0.2f)
                    .padding(top = 2.dp, bottom = 2.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(color = pcolour),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = score,
                    style = TextStyle(
                        fontSize = 23.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }

    @Composable
    fun ValueCell(isBlue: Boolean = true, value: Int = 1) {
        val ccolor =
            if (isBlue) colorResource(R.color.dark_blue) else colorResource(R.color.dark_red)
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(100.dp)) {
                drawCircle(
                    color = ccolor,
                    radius = size.height / 2,
                    center = androidx.compose.ui.geometry.Offset(
                        x = size.width / 2f,
                        y = size.height / 2f + 3f
                    )
                )
            }
            Text(
                text = value.toString(),
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 23.sp,
                    color = Color.White
                )
            )
        }
    }

    private fun explosion(buttonStates: Array<Array<ButtonState>>, i: Int, j: Int) {
        val buttonState = buttonStates[i][j]
        buttonState.value = 0
        buttonState.clicked = false

        val adjacentCoordinates = listOf(
            i - 1 to j,
            i + 1 to j,
            i to j - 1,
            i to j + 1
        )

        for ((adjX, adjY) in adjacentCoordinates) {
            if (adjX in 0..4 && adjY in 0..4) {
                val adjButtonState = buttonStates[adjX][adjY]
                adjButtonState.blue = buttonState.blue
                adjButtonState.clicked = true
                if (adjButtonState.value < 3) {
                    adjButtonState.value += 1
                } else {
                    explosion(buttonStates, adjX, adjY)
                }
            }
        }
    }

    private fun redCount(buttonStates: Array<Array<ButtonState>>): Int {
        var redScore = 0
        for (i in 0 until 5) {
            for (j in 0 until 5) {
                val buttonState = buttonStates[i][j]
                if (!buttonState.blue) {
                    redScore += buttonState.value
                }
            }
        }
        return redScore
    }

    private fun blueCount(buttonStates: Array<Array<ButtonState>>): Int {
        var blueScore = 0
        for (i in 0 until 5) {
            for (j in 0 until 5) {
                val buttonState = buttonStates[i][j]
                if (buttonState.blue) {
                    blueScore += buttonState.value
                }
            }
        }
        return blueScore

    }

}
