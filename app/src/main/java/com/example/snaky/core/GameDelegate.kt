package com.example.snaky.core

interface GameDelegate {
    fun onGameStart()
    fun onGameInit()
    fun onGameLose()
    fun onGameUpdate()
}