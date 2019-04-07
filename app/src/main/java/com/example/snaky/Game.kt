package com.example.snaky

import com.structit.snake.RedrawHandler
import com.structit.snake.RedrawHandlerDelegate

interface GameDelegate {
    fun onGameStart()
    fun onGameInit()
    fun onGameLose()
    fun onGameUpdate()
}

object Game: RedrawHandlerDelegate {

    val STATE_RUN: Int = 0
    val STATE_INIT: Int = 1
    val REDRAW_INTERVAL_MS = 400

    var delegate: GameDelegate? = null
    var handler = RedrawHandler()

    private var state: Int = STATE_INIT

    fun toggleState() {
        val reverse = if (state == STATE_INIT) STATE_RUN else STATE_INIT
        setState(reverse)
    }

    fun setState(state: Int) {
        this.state = state
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
        Snake.move();
        delegate?.onGameUpdate()
    }
}