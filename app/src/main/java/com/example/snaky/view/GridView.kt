package com.example.snaky.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View

import android.widget.GridView
import com.example.snaky.R
import com.example.snaky.core.Position
import java.lang.Error

abstract class GridView: View {

    private val LOG_TAG = GridView::class.java.name
    private val TILE_DEFAULT_SIZE = 56

    val TILE_EMPTY = -1

    private var mPaint: Paint? = null
    private var mTileList: Array<Bitmap?>? = null
    private var mGrid: Array<IntArray>? = null

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        mPaint = Paint()
        val styledAttributes = context.obtainStyledAttributes(
            attrs,
            R.styleable.GridView
        )
        mTileSize = styledAttributes.getDimensionPixelSize(
            R.styleable.GridView_tileSize,
            TILE_DEFAULT_SIZE
        )
        Log.d(LOG_TAG, "Tile size: $mTileSize")
        styledAttributes.recycle()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mPaint = Paint()
        val styledAttributes = context.obtainStyledAttributes(
            attrs,
            R.styleable.GridView
        )
        mTileSize = styledAttributes.getDimensionPixelSize(
            R.styleable.GridView_tileSize,
            TILE_DEFAULT_SIZE
        )
        Log.d(LOG_TAG, "Tile size: $mTileSize")
        styledAttributes.recycle()
    }


    protected fun resetTileList(nbTiles: Int) {
        mTileList = arrayOfNulls(nbTiles)
    }

    fun loadTile(key: Int, tile: Drawable) {
        val bitmap = Bitmap.createBitmap(
            mTileSize, mTileSize,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        tile.setBounds(0, 0,
            mTileSize,
            mTileSize
        )
        tile.draw(canvas)
        mTileList?.let { tileList ->
            tileList[key] = bitmap
        }
    }

    public override fun onDraw(canvas: Canvas) {
        Log.d(LOG_TAG, "Drawing...")
        super.onDraw(canvas)
        for (x in 0 until nbTileX) {
            for (y in 0 until nbTileY) {
                if (mGrid!![x][y] > TILE_EMPTY) {
                    canvas.drawBitmap(
                        mTileList!![mGrid!![x][y]],
                        (mOffsetX + x * mTileSize).toFloat(),
                        (mOffsetY + y * mTileSize).toFloat(),
                        mPaint
                    )
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        nbTileX = Math.floor((w / mTileSize).toDouble()).toInt()
        nbTileY = Math.floor((w / mTileSize).toDouble()).toInt()

        Log.d(LOG_TAG, "Nb tile - X: $nbTileX")
        Log.d(LOG_TAG, "Nb tile - Y: $nbTileY")

        mOffsetX = (w - mTileSize * nbTileX) / 2
        mOffsetY = (h - mTileSize * nbTileY) / 2

        mGrid = Array(nbTileX) { IntArray(nbTileY) }

        clearTiles()
        this.invalidate()
    }

    fun clearTiles() {
        for (x in 0 until nbTileX) {
            for (y in 0 until nbTileY) {
                setTile(TILE_EMPTY, x, y)
            }
        }
        this.initTiles()
    }

    protected abstract fun initTiles()

    fun setTile(value: Int, x: Int, y: Int): Int? {
        if (mGrid != null) {
            val lastValue = mGrid!![x][y]
            mGrid!![x][y] = value
            return lastValue
        }
        return null
    }

    fun getTileValue(position: Position): Int? {
        return getTileValue(position.x, position.y)
    }

    fun getTileValue(x: Int, y: Int): Int? {
        return mGrid?.let {
            it.getOrNull(x)?.getOrNull(y)
        }
    }

    fun changeTilesValue(currentValue: Int, newValue: Int) {
        for (x in 0 until nbTileX) {
            for (y in 0 until nbTileY) {
                if (mGrid!![x][y] == currentValue) {
                    mGrid!![x][y] = newValue
                }
            }
        }
    }

    fun getPositionsForValue(value: Int): ArrayList<Position> {
        val positions: ArrayList<Position> = ArrayList()
        for (x in 0 until nbTileX) {
            for (y in 0 until nbTileY) {
                if (mGrid!![x][y] == value) {
                    positions.add(Position(x, y))
                }
            }
        }
        return positions
    }

    fun isEmpty(x: Int, y: Int): Boolean {
        return mGrid!![x][y] == TILE_EMPTY
    }

    fun setRandomTile(value: Int): Position {
        var tileValue: Int?
        var randX: Double
        var randY: Double
        var x: Int
        var y: Int
        do {
            x = (0..nbTileX).random()
            y = (0..nbTileY).random()
            tileValue = getTileValue(x, y)
        } while (tileValue != TILE_EMPTY)
        setTile(value, x, y)

        return Position(x, y)
    }

    fun getDistance(value1: Int, value2: Int): Pair<Int, Int> {
        val pos1 = getPositionsForValue(value1)
        val pos2 = getPositionsForValue(value2)

        return getDistance(pos1[0], pos2[0])
    }

    fun getDistance(value1: Position, value2: Position): Pair<Int, Int> {
        return Pair<Int, Int>(
            value2.x - value1.x,
            value2.y - value1.y
        )
    }

    companion object {

        private var mTileSize: Int = 0
        private var mOffsetX: Int = 0
        private var mOffsetY: Int = 0

        var nbTileX: Int = 0
            protected set
        var nbTileY: Int = 0
            protected set

        fun getTileX(x: Float): Double {
            return Math.floor(((x - mOffsetX) / mTileSize).toDouble())
        }

        fun getTileY(y: Float): Double {
            return Math.floor(((y - mOffsetY) / mTileSize).toDouble())
        }
    }
}