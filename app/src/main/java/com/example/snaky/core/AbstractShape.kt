package com.example.snaky.core

abstract class AbstractShape() {

    var position = Position(0, 0)
    var direction = Direction.Down
    var countMoveTile = 1

    fun move() {
        position = nextPosition(position, direction, countMoveTile)
    }

    companion object {

        fun nextPosition(position: Position, direction: Direction, increment: Int = 1): Position {
            when (direction) {
                Direction.Up -> {
                    position.y -= increment
                }
                Direction.Down -> {
                    position.y += increment
                }
                Direction.Left -> {
                    position.x -= increment
                }
                Direction.Right -> {
                    position.x += increment
                }
            }
            return position
        }
    }
}