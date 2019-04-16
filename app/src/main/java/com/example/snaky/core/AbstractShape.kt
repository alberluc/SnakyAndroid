package com.example.snaky.core

abstract class AbstractShape() {

    enum class Direction {
        Up, Down, Left, Right
    }

    var posX: Int = 0
    var posY: Int = 0
    var direction = Direction.Down

    var countMoveTile = 1

    fun move() {
        when (direction) {
            Direction.Up -> {
                posY -= countMoveTile
            }
            Direction.Down -> {
                posY += countMoveTile
            }
            Direction.Left -> {
                posX -= countMoveTile
            }
            Direction.Right -> {
                posX += countMoveTile
            }
        }
    }

}