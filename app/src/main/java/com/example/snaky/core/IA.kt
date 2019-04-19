package com.example.snaky.core

import android.util.Log
import com.example.snaky.view.SnakeGrid
import kotlin.math.absoluteValue

class IA (val snake: Snake) {

    var directions = Direction.values().toMutableList()

    fun play(grid: SnakeGrid) {
        val nextDistances = HashMap<Direction, Int>()
        for (i in 0 until directions.count()) {
            getDistance(grid, directions[i])?.let { distance ->
                nextDistances[directions[i]] = distance.first.absoluteValue + distance.second.absoluteValue
            }
        }
        val minDist = nextDistances.minBy { it.value }
        minDist?.let {
            snake.direction = it.key
        }
    }

    private fun getDistance(grid: SnakeGrid, direction: Direction): Pair<Int, Int>? {
        val nextPosition = AbstractShape.nextPosition(Position(snake.position.x, snake.position.y), direction)
        val nextTile = grid.getTileValue(nextPosition)

        if (nextTile == grid.TILE_EMPTY || nextTile == grid.TILE_FOOD) {
            snake.direction = direction
            /*val index = directions.indexOf(direction)
            directions.removeAt(index)
            directions.add(direction)*/

            return grid.getDistance(
                nextPosition,
                grid.getPositionsForValue(grid.TILE_FOOD)[0]
            )
        }

        return null
    }

}