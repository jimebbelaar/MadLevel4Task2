package com.example.madlevel4task2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel4task2.adapter.GameAdapter
import com.example.madlevel4task2.model.Game
import com.example.madlevel4task2.repository.GameRepository
import kotlinx.android.synthetic.main.fragment_game_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class GameHistoryFragment : Fragment() {

    private val games = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(games)

    private lateinit var gameRepository: GameRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        gameRepository = GameRepository(requireContext())

        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_gameHistoryFragment_to_GameFragment)
        }
    }

    private fun initViews() {
        rvGames.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        rvGames.adapter = gameAdapter
        rvGames.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        getGamesFromDatabase()
    }

    private fun getGamesFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val games = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }
            this@GameHistoryFragment.games.clear()
            this@GameHistoryFragment.games.addAll(games)
            this@GameHistoryFragment.gameAdapter.notifyDataSetChanged()
        }
    }


}