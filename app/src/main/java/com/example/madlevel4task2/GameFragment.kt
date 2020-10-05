package com.example.madlevel4task2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.example.madlevel4task2.model.Game
import com.example.madlevel4task2.repository.GameRepository
import kotlinx.android.synthetic.main.fragment_game.*
import java.util.*

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
//        ivComputer.setImageDrawable(getDrawable(getImageId(computerInput)))
    }

//    private fun setResult(game:Game){
//        ivComputer.setImageDrawable(getDrawable(game.computerChoice))
//    }

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
}
