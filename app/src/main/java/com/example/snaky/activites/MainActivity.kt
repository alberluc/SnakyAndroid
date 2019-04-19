package com.example.snaky.activites

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.LocalBroadcastManager
import android.view.*
import com.example.snaky.view.SnakeGrid
import android.support.v4.view.GestureDetectorCompat
import com.example.snaky.listeners.DetectSwipeGestureListener
import android.view.MotionEvent
import com.example.snaky.*
import com.example.snaky.core.*
import com.example.snaky.dialog_fragment.MenuDialogFragment
import com.example.snaky.services.ScoreService
import java.util.*


class MainActivity : AppCompatActivity(), GameDelegate {

    private var menu: Menu? = null
    private var snakeGrid: SnakeGrid? = null
    private var menuDialog: MenuDialogFragment? = null

    private var gestureDetectorCompat: GestureDetectorCompat? = null

    fun addScore(score: Score) {
        val scoreIntent = Intent(this, ScoreService::class.java)
        scoreIntent.also {
            it.putExtra("ACTION", ScoreService.ACTION_REGISTER_SCORE)
            it.putExtra("SCORE", score)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(scoreIntent)
        } else {
            startService(scoreIntent)
        }
    }

    fun triggerScoreService(action: String) {
        val scoreIntent = Intent(this, ScoreService::class.java)
        scoreIntent.also {
            it.putExtra("ACTION", action)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(scoreIntent)
        } else {
            startService(scoreIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        snakeGrid = findViewById(R.id.snake)

        val gestureListener = DetectSwipeGestureListener()
        gestureListener.activity = this
        gestureDetectorCompat = GestureDetectorCompat(this, gestureListener)

        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                menuDialog?.scoreAdapter?.clear()
                menuDialog?.scoreAdapter?.addAll(intent!!.getParcelableArrayListExtra<Score>("SCORES"))
                menuDialog?.scoreAdapter?.notifyDataSetChanged()
            }
        }, IntentFilter(ScoreService.ACTION_GET_SCORES))

        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                triggerScoreService(ScoreService.ACTION_GET_SCORES)
            }
        }, IntentFilter(ScoreService.ACTION_REGISTER_SCORE))

        Game.delegate = this
        Game.setup()
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.WAKE_LOCK,
                android.Manifest.permission.FOREGROUND_SERVICE
            ), 0)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.WAKE_LOCK
            ), 0)
        }
        triggerScoreService(ScoreService.ACTION_LOGIN)
    }

    override fun onStop() {
        super.onStop()
        val scoreIntent = Intent(this, ScoreService::class.java)
        stopService(scoreIntent)
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetectorCompat!!.onTouchEvent(event)
        return true
    }

    fun onSwipe(direction: Direction) {
        Game.userSnake.direction = direction
    }

    override fun onGameInit() {
        val itemStateGame = this.menu?.getItem(0)
        itemStateGame?.setIcon(R.drawable.ic_game)
        Game.userSnake.init()
        Game.botsSnake.forEach { it.init() }
        snakeGrid?.clearTiles()
    }

    override fun onGameStart() {
        val itemStateGame = this.menu?.getItem(0)
        itemStateGame?.setIcon(R.drawable.ic_restart)
    }

    override fun onGameLose() {
        Game.handler.stop()
        Game.snakes.forEach { it.countMoveTile = 0 }
        triggerScoreService(ScoreService.ACTION_GET_SCORES)

        menuDialog = MenuDialogFragment(this)
        menuDialog!!.show(supportFragmentManager, "MenuDialogFragment")
    }

    override fun onGamePreUpdate() {
        snakeGrid?.onGamePreUpdate()
    }

    override fun onGameUpdate() {
        snakeGrid?.updateTiles()
        snakeGrid?.invalidate()
    }
}
