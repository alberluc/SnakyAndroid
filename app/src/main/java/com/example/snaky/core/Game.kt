package com.example.snaky.core

import com.structit.snake.RedrawHandler
import com.structit.snake.RedrawHandlerDelegate

object Game: RedrawHandlerDelegate {

    val STATE_RUN: Int = 0
    val STATE_INIT: Int = 1
    val REDRAW_INTERVAL_MS = 400

    var delegate: GameDelegate? = null
    var handler = RedrawHandler()

    val userSnake = Snake()
    val botsSnake = arrayListOf(Snake())

    val snakes: List<Snake>
        get() = arrayListOf(userSnake) + botsSnake

    private var state: Int = STATE_INIT

    fun toggleState() {
        val reverse = if (state == STATE_INIT) STATE_RUN else STATE_INIT
        setState(reverse)
    }

    fun setState(state: Int) {
        Game.state = state
        when (state) {
            STATE_RUN -> {
                handler.start()
                delegate?.onGameStart()
            }
            STATE_INIT -> {
                handler.stop()
                delegate?.onGameInit()
            }
        }
    }

    fun setup() {
        handler.delegate = this
        setState(STATE_INIT)
        handler.setInterval(REDRAW_INTERVAL_MS)
    }

    fun getState(): Int {
        return state
    }

    override fun onLoopExecute() {
        delegate?.onGamePreUpdate()
        // userSnake.move()
        botsSnake.forEach { it.move() }
        delegate?.onGameUpdate()
    }
}