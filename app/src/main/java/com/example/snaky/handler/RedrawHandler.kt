package com.structit.snake

import android.os.Handler
import android.os.Message
import com.example.snaky.core.Game

interface RedrawHandlerDelegate {

    fun onLoopExecute()

}

class RedrawHandler : Handler() {

    private val game: Game? = null
    private var interval: Int = 0
    private var isStop = false

    var delegate: RedrawHandlerDelegate? = null

    init {
        this.interval = -1
    }

    fun setInterval(interval: Int) {
        this.interval = interval
    }

    override fun handleMessage(msg: Message) {
        delegate?.onLoopExecute()
        if(!isStop) request()
    }

    fun request() {
        this.removeMessages(0)
        sendMessageDelayed(obtainMessage(0), this.interval.toLong())
    }

    fun stop() {
        isStop = true
    }

    fun start() {
        isStop = false
        request()
    }
}