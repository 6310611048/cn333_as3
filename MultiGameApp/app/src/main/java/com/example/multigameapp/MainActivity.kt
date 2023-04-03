package com.example.multigameapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.multigameapp.ui.theme.MultiGameAppTheme

sealed class Screen(val route: String){
    object Home: Screen("home")
    object QuizScreen: Screen("quizScreen")
    object QuizHomeScreen: Screen("quizHomeScreen")
    object GameOver: Screen("gameOver")
    object GuessingNumberGame: Screen("guessingNumberGame")
    object MathHomeGame: Screen("mathHomeGame")
    object MathGame: Screen("mathGame")
}

class MainActivity : ComponentActivity() {
    private val viewModel: QuizViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultiGameAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val question: Quiz? by viewModel.question.observeAsState(null)
                    val score: Int? by viewModel.score.observeAsState(null)
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "home") {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                navController = navController,
                                onQuizGameClick = { navController.navigate(Screen.QuizHomeScreen.route) },
                                onGuessingGameClick = { navController.navigate(Screen.GuessingNumberGame.route) },
                                onMathGameClick = { navController.navigate(Screen.MathHomeGame.route) }
                            )
                        }
                        composable(Screen.QuizHomeScreen.route) {
                            QuizHomeScreen(
                                navController = navController,
                                onBackClick = { navController.navigate(Screen.Home.route) })
                        }
                        composable(Screen.QuizScreen.route) {
                            QuizScreen(
                                navController = navController, question,score ?: 0,
                            ){ answerIndex ->
                                viewModel.Answer(answerIndex)
                            }
                        }
                        composable(Screen.GameOver.route) {
                            GameOverScreen(navController = navController, score ?: 0,onBackClick = { navController.navigate(Screen.Home.route) })
                        }
                        composable(Screen.GuessingNumberGame.route) {
                            GuessingNumberGame(onBackClick = { navController.navigate(Screen.Home.route) })
                        }
                        composable(Screen.MathHomeGame.route) {
                            MathGame(navController = navController,
                                    onBackClick = { navController.navigate(Screen.Home.route) })
                        }

                    }
                }
            }
        }
    }
    @Composable
    fun HomeScreen(navController: NavController, modifier: Modifier = Modifier,
                   onQuizGameClick: () -> Unit,
                   onGuessingGameClick: () -> Unit,
                   onMathGameClick: () -> Unit
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF5EA7E))
        )
        Text(
            text = stringResource(R.string.gameName),
            fontSize = 50.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.Center)
                .padding(top = 250.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.Center)

        ) {
            Button(
                onClick = onGuessingGameClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center)
                    .padding(top = 360.dp),
            ) {
                Text("Guessing Number Game")
            }
            Button(
                onClick = onQuizGameClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center)
                    .padding(top = 25.dp),) {
                Text("Quiz Game")
            }
            Button(
                onClick = onMathGameClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center)
                    .padding(top = 25.dp),) {
                Text("Math Game")
            }
        }
    }
    @Composable
    fun QuizHomeScreen(navController: NavController, modifier: Modifier = Modifier,
                       onBackClick: () -> Unit
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF5EA7E))
        )
        Text(
            text = stringResource(R.string.gameNamee),
            fontSize = 50.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.Center)
                .padding(top = 200.dp),

            )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.Center)

        ) {
            Button(
                onClick = { navController.navigate("quizScreen")},
                modifier = Modifier.padding(top = 300.dp)
            ) {
                Text(
                    text = "Start Game"
                )
            }
            Button(
                onClick = onBackClick,
            ) {
                Text(
                    text = "Back to Home"
                )
            }
        }
    }

    @Composable
    fun QuizScreen(navController: NavController, question: Quiz?, score: Int, modifier: Modifier = Modifier, onAnswerSelected: (Int) -> Unit) {
        var currentQuestion by remember { mutableStateOf(0) }
        val totalQuestions = 10
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF5EA7E))
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(Color(0xFF5F5E56))
                .size(48.dp)
                .padding(8.dp),

            ) {
            Text(
                text = "Question $currentQuestion of $totalQuestions",
                fontSize = 22.sp,
                color = Color.White
            )
            Text(
                modifier = modifier
                    .padding(start = 140.dp),
                text = "Score: $score",
                fontSize = 22.sp,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (question != null) {
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                val shuffledOptions = question.options.shuffled() // Shuffle the options
                shuffledOptions.forEachIndexed { index, option ->
                    AnswerOption(
                        option = option,
                        isSelected = false,
                        isEnabled = true,
                        onClick = {
                            onAnswerSelected(question.options.indexOf(option))
                            currentQuestion++
                            viewModel.click()
                            if (viewModel.gameOver()) {
                                navController.navigate("gameOver")
                            }
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun GameOverScreen(navController: NavController, score: Int,onBackClick: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF5EA7E))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Game Over!",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Your score is $score",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = {
                    viewModel.resetGame()
                    navController.navigate("quizHomeScreen")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Play Again")
            }
            Button(
                onClick = onBackClick,
            ) {
                Text(
                    text = "Back to Home"
                )
            }
            Button(
                onClick = {
                    finish()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Quit")
            }
        }
    }

    @Composable
    fun AnswerOption(option: String, isSelected: Boolean, isEnabled: Boolean, onClick: () -> Unit) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = null,
                modifier = Modifier.clickable(enabled = isEnabled, onClick = onClick)
            )
            Text(
                text = option,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}


@Composable
fun GuessingNumberGame(
    onBackClick: () -> Unit
) {
    var random by remember { mutableStateOf((1..1000).random()) }
    var text by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var guessCount by remember { mutableStateOf(0) }
    Text(
        text = "Number Guessing Game",
        fontSize = 20.sp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6200ED))
            .padding(15.dp)
    )
    Column(
        modifier = Modifier.padding(40.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = stringResource(R.string.header),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.your_name)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                guessCount++
                result = ""
                val userGuess = text.toInt()

                if (userGuess != null) {
                    if (userGuess == random) {
                        result = "Correct! You guessed the number in $guessCount time!"
                        text = ""
                        random = (1..1000).random()
                        guessCount = 0

                    } else if (userGuess > random) {
                        result = "Hint: It's Lower!"
                        text = ""

                    } else {
                        result = "Hint: It's Higher!"
                        text = ""
                    }
                }else {
                    result = "Please enter a valid number"
                }
            }
        ) {
            Text(text = "play again")
        }
        Button(
            onClick = onBackClick,
        ) {
            Text(
                text = "Back to Home"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = result,
            color = androidx.compose.ui.graphics.Color.Gray,
            fontSize = 20.sp,
        )
    }
}

@Composable
fun MathGame(navController: NavController, modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    var num1 by remember { mutableStateOf(0) }
    var num2 by remember { mutableStateOf(0) }
    var operator by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var questionCount by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F06A))
    )
    if (gameOver) {
        Spacer(modifier = Modifier.height(60.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(110.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Game Over!",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                "Score: $score",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    num1=0
                    num2=0
                    operator=""
                    answer=""
                    score=0
                    questionCount=0
                    gameOver = false
                          },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Play Again")
            }

            Button(
                onClick = onBackClick,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Back to Home")

            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(Color(0xFFF0E8A0))
                .size(50.dp)
                .padding(8.dp),
        ){
            Text(
                text = "Question $questionCount of 10",
                fontSize = 22.sp,
                color = Color.Black
            )
            Text(
                "Score: $score",
                style = MaterialTheme.typography.h5,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 140.dp),
            )
        }
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (questionCount < 10) {
                LaunchedEffect(Unit) {
                    num1 = (1..10).random()
                    num2 = (1..10).random()
                    operator = listOf("+", "-", "*", "/").random()
                    answer = ""
                }
                Spacer(modifier = Modifier.height(65.dp))
                Text(
                    "$num1 $operator $num2 ",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text("Your Answer") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (checkAnswer(num1, num2, operator, answer.toIntOrNull())) {
                            score++
                            num1 = (1..100).random()
                            num2 = (1..100).random()
                            operator = listOf("+", "-", "*", "/").random()
                            answer = ""
                            questionCount++
                        } else {
                            num1 = (1..100).random()
                            num2 = (1..100).random()
                            operator = listOf("+", "-", "*", "/").random()
                            answer = ""
                            questionCount++
                        }

                        if (questionCount == 10) {
                            gameOver = true
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Next")
                }
                if (questionCount == 0) {
                    Button(
                        onClick = onBackClick,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(text = "Back to Home")

                    }
                }

            }
        }
    }

}

fun checkAnswer(num1: Int, num2: Int, operator: String, userAnswer: Int?): Boolean {
    val answer = when (operator) {
        "+" -> num1 + num2
        "-" -> num1 - num2
        "*" -> num1 * num2
        "/" -> num1 / num2
        else -> 0
    }
    return answer == userAnswer
}
