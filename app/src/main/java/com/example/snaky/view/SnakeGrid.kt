package com.example.snaky.view

import android.content.Context
import android.content.res.Resources
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import com.example.snaky.core.Game
import com.example.snaky.core.Position
import com.example.snaky.R
import com.example.snaky.core.Snake

class SnakeGrid: GridView {

    val TILE_WALL: Int = 0
    val TILE_SNAKE_HEAD: Int = 1
    val TILE_SNAKE_PART: Int = 2
    val TILE_SNAKE_IA_HEAD: Int = 3
    val TILE_SNAKE_IA_PART: Int = 4
    val TILE_FOOD: Int = 5

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {}

    override fun initTiles() {
        if (nbTileX == 0 || nbTileX == 0) return

        setFocusable(true)
        val r: Resources = this.context.resources
        resetTileList(TILE_FOOD + 1)

        loadTile(TILE_WALL, ResourcesCompat.getDrawable(r, R.drawable.ic_frame_wall_1, null)!!)
        loadTile(TILE_SNAKE_HEAD, ResourcesCompat.getDrawable(r, R.drawable.ic_snake, null)!!)
        loadTile(TILE_SNAKE_PART, ResourcesCompat.getDrawable(r, R.drawable.ic_snake_part, null)!!)
        loadTile(TILE_SNAKE_IA_HEAD, ResourcesCompat.getDrawable(r, R.drawable.ic_snake_ia, null)!!)
        loadTile(TILE_SNAKE_IA_PART, ResourcesCompat.getDrawable(r, R.drawable.ic_snake_part_ia, null)!!)
        loadTile(TILE_FOOD, ResourcesCompat.getDrawable(r, R.drawable.ic_apple, null)!!)

        setTile(TILE_FOOD, (nbTileX / 2) + 3, (nbTileY / 2) + 3)

        for (x in 0..nbTileX - 1) {
            setTile(TILE_WALL, x, 0)
            setTile(TILE_WALL, x, nbTileY - 1)
        }
        for (y in 0..nbTileY - 1) {
            setTile(TILE_WALL, 0, y)
            setTile(TILE_WALL, nbTileX -1, y)
        }

        initSnake(Game.userSnake, TILE_SNAKE_HEAD)
        Game.botsSnake.forEach { initSnake(it, TILE_SNAKE_IA_HEAD) }
    }

    fun initSnake(snake: Snake, headTile: Int) {
        val position = this.setRandomTile(headTile)
        snake.position = position

        setTile(headTile, snake.position.x, snake.position.y)
    }

    fun updateTiles() {
        updateSnake(Game.userSnake, TILE_SNAKE_HEAD, TILE_SNAKE_PART)
        Game.botsSnake.forEach { bot -> updateSnake(bot, TILE_SNAKE_IA_HEAD, TILE_SNAKE_IA_PART) }
    }

    fun updateSnake(snake: Snake, headTile: Int, bodyTile: Int) {
        // Clear and draw last head positions in snake part
        changeTilesValue(bodyTile, TILE_EMPTY)
        for (position in snake.getHeadPositions()) {
            setTile(bodyTile, position.x, position.y)
        }
        // Save current head position
        val headPosition = Position(snake.position.x, snake.position.y)
        snake.addHeadPosition(headPosition)
        // Clear and draw head position and handle the tile touched
        changeTilesValue(headTile, TILE_EMPTY)
        val tileTouched = setTile(headTile, snake.position.x, snake.position.y)
        when (tileTouched) {
            TILE_SNAKE_HEAD, TILE_SNAKE_IA_HEAD, TILE_WALL, TILE_SNAKE_PART, TILE_SNAKE_IA_PART -> {
                Game.delegate?.onGameLose()
            }
            TILE_FOOD -> {
                snake.addPart()
                changeTilesValue(TILE_FOOD, TILE_SNAKE_PART)
                setRandomTile(TILE_FOOD)
            }
        }
    }

    fun onGamePreUpdate() {
        Game.botsSnake.forEach { bot -> bot.ia.play(this) }
    }
}