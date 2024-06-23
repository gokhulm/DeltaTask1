package com.example.deltaactual

import android.util.Log
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }

    @Preview
    @Composable
    private fun HomeScreen() {
        var showDialog by remember { mutableStateOf(false) }
        val height = LocalConfiguration.current.screenHeightDp.dp
        val width = LocalConfiguration.current.screenWidthDp.dp
        var showWinTrackerDialog by remember { mutableStateOf(false) }
        val sharedPreferences = LocalContext.current.getSharedPreferences("player_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val keysToRemove = mutableListOf<String>()
        var statsUpdated by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            snapshotFlow {
                sharedPreferences.getBoolean("stats_updated", false)
            }.collect { updated ->
                statsUpdated = updated
                Log.d("HomeScreen", "Stats updated flag received: $updated")

            }
        }
        val allPlayerNames = remember(statsUpdated) {
            val playerNames = mutableListOf<String>()
            sharedPreferences.all.keys.forEach { key ->
                if (key.endsWith("/games")) {
                    playerNames.add(key.substringBefore("/")) // Only name taken
                }
            }
            playerNames
        }
        val playerDataList = remember(statsUpdated) {
            allPlayerNames.map { playerName ->
                val games = sharedPreferences.getInt("$playerName/games", 0)
                val wins = sharedPreferences.getInt("$playerName/wins", 0)
                PlayerStats(playerName, games, wins)
            }.sortedByDescending { it.wins }
        }
        sharedPreferences.all.keys.forEach { key ->
            if (key.endsWith("/games")) {
                val gamesPlayed = sharedPreferences.getInt(key, 0)
                if (gamesPlayed == 0) {
                    keysToRemove.add(key.substringBefore("/"))
                }
            }
        }
        keysToRemove.forEach { playerName ->
            editor.remove("$playerName/games")
            editor.remove("$playerName/wins")
        }
        editor.apply()
        LaunchedEffect(statsUpdated) {  }
        Log.d("HomeScreen", "Player Data List: $playerDataList")
        playerDataList.forEach { player ->
            Log.d("HomeScreen", "Games for ${player.playerName}: ${player.games}")
            Log.d("HomeScreen", "Wins for ${player.playerName}: ${player.wins}")
        }
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
                    .padding(0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(height / 8))
                Text(
                    text = "COLOR\nCONQUEST",
                    fontSize = 64.sp,
                    style = TextStyle(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(R.color.grad_purp),
                                colorResource(R.color.grad_pink)
                            )
                        )
                    ),
                    fontFamily = FontFamily(Font(R.font.titlefont, FontWeight.Normal)),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(height / 15))
                Image(
                    painter = painterResource(R.drawable.players),
                    contentDescription = "2 players",
                    modifier = Modifier.size(330.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.Absolute.Left,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            showWinTrackerDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.question_vio)),
                        modifier = Modifier
                            .padding(8.dp)
                            .offset(0.dp, (-2.5f).dp)
                            .shadow(0.5f.dp, shape = CircleShape)
                            .offset(0.dp, (-1).dp)
                            .height(60.dp)
                            .width(60.dp),
                        shape = CircleShape
                    ) {
                        Text(
                            "\uD83C\uDFC6",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.align(Alignment.CenterVertically)
                                .offset((-15).dp)
                        )
                    }
                    Button(
                        onClick = {
                            val newIntent = Intent(applicationContext, PlayerCreationActivity::class.java)
                            startActivity(newIntent)

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.dark_blue)),
                        modifier = Modifier
                            .height(height / 12)
                            .width(width / 2.3f)
                            .padding(5.dp)
                            .offset(width / 4-60.dp)
                            .shadow(0.5f.dp, shape = RoundedCornerShape(30.dp))
                            .offset(0.dp, (-1).dp)
                    ) {
                        Text(
                            "PLAY",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    Button(
                        onClick = {
                            showDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.question_vio)),
                        modifier = Modifier
                            .padding(8.dp)
                            .offset(width / 3-60.dp)
                            .shadow(0.5f.dp, shape = CircleShape)
                            .offset(0.dp, (-1).dp),
                        shape = CircleShape
                    ) {
                        Text(
                            "?",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(16.dp)
                        .width(width/1.5f)
                        .height(height/1.5f)
                ) {
                    Column(
                        modifier = Modifier
                            .background(colorResource(R.color.white))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Rules",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                                .height(50.dp)
                        )
                        LazyColumn(modifier = Modifier.height(height/2-35.dp)) {
                            item {
                                Text(
                                    text = "Game Logic",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text("1. 1st Turn of each player: Players can choose any tile on the grid on this turn only. Clicking a tile assigns your colour to it and awards you 3 points on that tile.")
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("2. Subsequent Turns: After the first turn, players can only click on tiles that already have their own colour. Clicking a tile with your colour adds 1 point to that tile. The background colour indicates the next player.")
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("3. Conquest and Expansion: When a tile with your colour reaches 4 points, it triggers an expansion:")
                                Text("    a) Your colour spreads to the four surrounding squares in a plus shape (up, down, left, right).")
                                Text("    b) Each of the four surrounding squares gains 1 point with your colour.")
                                Text("    c) If any of the four has your opponent’s colour, you conquer the opponent's points on that tile while adding a point to it, completely erasing theirs.")
                                Text("    d) The expansion is retriggered if the neighbouring tile as well reaches 4 points this way.")
                                Text("    e) Players take turns clicking on tiles and the objective is to eliminate your opponent's colour entirely from the screen.")
                            }
                        }
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.padding(top = 16.dp)
                                .size(width/4, height/20)
                        ) {
                            Text("Close")
                        }
                    }
                }
            }
        }
        if (showWinTrackerDialog) {
            Dialog(
                onDismissRequest = { showWinTrackerDialog = false },
            ){
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(16.dp)
                        .width(width/1.5f)
                        .height(height/1.5f)
                ) {
                    Column(
                        modifier = Modifier
                            .background(colorResource(R.color.white))
                            .padding(16.dp)
                            .width(width/2)
                            .height(height/2),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Leaderboard",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        LazyColumn(modifier = Modifier
                            .border(1.dp, Color.Black)
                            .padding(8.dp)
                        ) {
                            item {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .border(1.dp, Color.Black),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text("Player", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                                    Text("Games", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                    Text("Wins", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                    Text("Win %", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                }
                            }
                            items(playerDataList) { player ->
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .border(1.dp, Color.Black),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(player.playerName, modifier = Modifier.weight(1f))
                                    Text(player.games.toString(), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                    Text(player.wins.toString(), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                    Text(player.winPercentage, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                }
                            }
                        }
                        Button(
                            onClick = { showDialog = false
                                showWinTrackerDialog=false},
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }
}


