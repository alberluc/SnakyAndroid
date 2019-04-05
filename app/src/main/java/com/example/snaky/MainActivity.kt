package com.example.snaky

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MotionEventCompat
import android.util.Log
import android.view.*
import com.example.snaky.view.SnakeGrid
import android.support.v4.view.GestureDetectorCompat
import com.example.snaky.listeners.DetectSwipeGestureListener
import android.text.method.Touch.onTouchEvent
import android.view.MotionEvent
import com.example.snaky.dialog_fragment.MenuDialogFragment

class MainActivity : AppCompatActivity(), GameDelegate {

    private var menu: Menu? = null
    private var snakeGrid: SnakeGrid? = null

    private var gestureDetectorCompat: GestureDetectorCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        snakeGrid = findViewById(R.id.snake)

        val gestureListener = DetectSwipeGestureListener()
        gestureListener.activity = this
        gestureDetectorCompat = GestureDetectorCompat(this, gestureListener)

        Game.delegate = this
        Game.setup()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetectorCompat!!.onTouchEvent(event)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.game_menu, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.start_game -> {
                Game.toggleState()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onGameStart() {
        val itemStateGame = this.menu?.getItem(0)
        itemStateGame?.setIcon(R.drawable.ic_restart)
        Snake.countMoveTile = 1
    }

    override fun onGameInit() {
        val itemStateGame = this.menu?.getItem(0)
        itemStateGame?.setIcon(R.drawable.ic_game)
        snakeGrid?.clearTiles()
    }

    override fun onGameLose() {
        Game.handler.stop()
        Snake.countMoveTile = 0
        val dialog = MenuDialogFragment()
        dialog.show(supportFragmentManager, "MenuDialogFragment")
    }

    override fun onUpdateAnimate() {
        snakeGrid?.updateTiles()
        snakeGrid?.invalidate()
    }

    fun onSwipe(direction: AbstractShape.Direction) {
        Snake.direction = direction
    }
}
