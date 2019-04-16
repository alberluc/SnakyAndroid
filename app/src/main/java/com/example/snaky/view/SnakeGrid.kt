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

    private val TILE_WALL: Int = 0;
    private val TILE_SNAKE_HEAD: Int = 1;
    private val TILE_SNAKE_PART: Int = 2;
    private val TILE_SNAKE_IA_HEAD: Int = 3;
    private val TILE_SNAKE_IA_PART: Int = 4;
    private val TILE_FOOD: Int = 5;

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) {
        // initTiles()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        // initTiles()
    }

    override fun initTiles() {
        setFocusable(true)
        val r: Resources = this.context.resources
        resetTileList(TILE_FOOD + 1)
        loadTile(TILE_WALL, ResourcesCompat.getDrawable(r, R.drawable.ic_frame_wall_1, null)!!)
        loadTile(TILE_SNAKE_HEAD, ResourcesCompat.getDrawable(r, R.drawable.ic_snake, null)!!)
        loadTile(TILE_SNAKE_PART, ResourcesCompat.getDrawable(r, R.drawable.ic_snake_part, null)!!)
        loadTile(TILE_FOOD, ResourcesCompat.getDrawable(r, R.drawable.ic_apple, null)!!)

        Snake.posX = (nbTileX / 2) - 3
        Snake.posY = (nbTileY / 2) - 3

        setTile(TILE_SNAKE_HEAD, Snake.posX, Snake.posY)
        setTile(TILE_FOOD, (nbTileX / 2) + 3, (nbTileY / 2) + 3)

        for (x in 0..nbTileX - 1) {
            setTile(TILE_WALL, x, 0)
            setTile(TILE_WALL, x, nbTileY - 1)
        }
        for (y in 0..nbTileY - 1) {
            setTile(TILE_WALL, 0, y)
            setTile(TILE_WALL, nbTileX -1, y)
        }
    }

    fun updateTiles() {
        // Clear and draw last head positions in snake part
        changeTilesValue(TILE_SNAKE_PART, TILE_EMPTY)
        for (position in Snake.getHeadPositions()) {
            setTile(TILE_SNAKE_PART, position.x, position.y)
        }
        // Save current head position
        val headPosition =
            Position(Snake.posX, Snake.posY)
        Snake.addHeadPosition(headPosition)
        // Clear and draw head position and handle the tile touched
        changeTilesValue(TILE_SNAKE_HEAD, TILE_EMPTY)
        val tileTouched = setTile(TILE_SNAKE_HEAD, Snake.posX, Snake.posY)
        when (tileTouched) {
            TILE_SNAKE_HEAD, TILE_WALL, TILE_SNAKE_PART -> {
                Game.delegate?.onGameLose()
            }
            TILE_FOOD -> {
                Snake.addPart()
                changeTilesValue(TILE_FOOD, TILE_SNAKE_PART)
                setRandomTile(TILE_FOOD)
            }
        }
    }
}