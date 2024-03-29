package com.example.snaky.core

class Snake: AbstractShape() {

    private var headPositions: ArrayList<Position> = ArrayList()
    private var countParts: Int = 0

    var ia = IA(this)

    fun addPart() {
        countParts++
    }

    fun init() {
        countMoveTile = 1
        direction = Direction.Down
        countParts = 0
        headPositions = ArrayList()
    }

    fun getHeadPositions(): ArrayList<Position> {
        return headPositions
    }

    fun addHeadPosition(position: Position) {
        headPositions.add(position)
        if (headPositions.size > countParts) {
            headPositions.removeAt(0)
        }
    }
}