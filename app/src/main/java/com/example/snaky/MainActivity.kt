package com.example.snaky

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*

class MainActivity : AppCompatActivity() {

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                this.menu?.getItem(0)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
