package com.example.madlevel4task2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.example.madlevel4task2.model.Game
import com.example.madlevel4task2.repository.GameRepository
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_game.ivComputer
import kotlinx.android.synthetic.main.item_game.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

const val HISTORY_CLEAR_CODE = 100

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GameFragment : Fragment() {

    private lateinit var gameRepository: GameRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        gameRepository = GameRepository(requireContext())

        ivRock.setOnClickListener {
            startGame(Game.Choice.ROCK)
        }
        ivPaper.setOnClickListener {
            startGame(Game.Choice.PAPER)
        }
        ivScissors.setOnClickListener {
            startGame(Game.Choice.SCISSORS)
        }
    }

    private fun startGame(userChoice: Game.Choice) {
        val computerInput = Game.Choice.values()[(Game.Choice.values().indices).random()]
        val game = Game(
            getImageId(userChoice),
            getImageId(computerInput),
            checkResult(userChoice, computerInput),
            Date()
        )
        ivComputer.setImageDrawable(getDrawable(requireContext(), game.computerChoice))
        ivYou.setImageDrawable(getDrawable(requireContext(), game.playerChoice))

        if (game.result == Game.Result.LOSS) {
            tvResult.text = getString(R.string.you_lose)
        }
        else if (game.result == Game.Result.DRAW) {
            tvResult.text = getString(R.string.draw)
        }
        else if (game.result == Game.Result.WIN) {
            tvResult.text = getString(R.string.you_win)
        }
        addGameToDatabase(game)
    }

    private fun getImageId(choice: Game.Choice): Int {

        var imageId = 0

        if (choice == Game.Choice.ROCK) {
            imageId = R.drawable.rock
        } else if (choice == Game.Choice.PAPER) {
            imageId = R.drawable.paper
        } else if (choice == Game.Choice.SCISSORS) {
            imageId = R.drawable.scissors
        }
        return imageId
    }

    private fun checkResult(userChoice: Game.Choice, computerInput: Game.Choice): Game.Result {

        var result: Game.Result = Game.Result.DRAW

        if (userChoice == computerInput) {
            result = Game.Result.DRAW
        } else if (userChoice == Game.Choice.ROCK) {
            if (computerInput == Game.Choice.SCISSORS) {
                result = Game.Result.WIN
            } else {
                result = Game.Result.LOSS
            }
        } else if (userChoice == Game.Choice.SCISSORS) {
            if (computerInput == Game.Choice.PAPER) {
                result = Game.Result.WIN
            } else {
                result = Game.Result.LOSS
            }
        } else if (userChoice == Game.Choice.PAPER) {
            if (computerInput == Game.Choice.ROCK) {
                result = Game.Result.WIN
            } else {
                result = Game.Result.LOSS
            }
        }
        return result
    }
    private fun addGameToDatabase(game: Game) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
            getStatisticsFromDatabase()
        }
    }
    private fun getStatisticsFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            var wins = 0
            var draws = 0
            var losses = 0
            withContext(Dispatchers.IO) {
                wins = gameRepository.getNumberOfWins()
                draws = gameRepository.getNumberOfDraws()
                losses = gameRepository.getNumberOfLosses()
            }
            tvStatistics.text = getString(R.string.statistics, wins, draws, losses)
        }
    }
}
