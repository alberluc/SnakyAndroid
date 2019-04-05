package com.example.snaky

object Snake: AbstractShape() {

    var parts: ArrayList<SnakePart> = ArrayList();

    fun addPart() {
        parts.add(SnakePart())
    }

}