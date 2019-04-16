package com.example.snaky.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.webkit.CookieManager
import com.example.snaky.core.Score
import org.w3c.dom.Document
import java.net.URL
import java.util.ArrayList
import javax.net.ssl.HttpsURLConnection


class ScoreService: Service() {

    private val CHANNEL_ID = 1
    private val BASE_URL = "https://snake.struct-it.fr"

    companion object {
        val ACTION_LOGIN = "LOGIN"
        val ACTION_REGISTER_SCORE = "REGISTER_SCORE"
        val ACTION_GET_SCORES = "GET_SCORES"
    }

    override fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID.toString(), "Score service", importance)
            mChannel.description = "This is a score service"

            val notificationManager = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

            val notificationBuilder = Notification.Builder(this, CHANNEL_ID.toString())
            val notification = notificationBuilder.build()

            startForeground(CHANNEL_ID, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.getStringExtra("ACTION")
        when (action) {
            ACTION_LOGIN -> login()
            ACTION_GET_SCORES -> getScores()
            ACTION_REGISTER_SCORE -> registerScore(intent.getParcelableExtra("SCORE"))
        }
        return START_STICKY
    }

    private fun getScores() {
        val getScoresUrl = URL("$BASE_URL/score?list")
        val getScoresTask = RequestTask(object: RequestHandler {
            override fun onResponse(result: Boolean, httpsURLConnection: HttpsURLConnection, document: Document?) {
                val intent = Intent(ACTION_GET_SCORES)
                val scores = ArrayList<Score>()
                val items = document!!.getElementsByTagName("score")
                for (i in 0..items.length - 1) {
                    val user = items.item(i).attributes.getNamedItem("player")
                    val value = items.item(i).attributes.getNamedItem("value")
                    scores.add(Score(user.nodeValue, value.nodeValue.toInt()))
                }
                intent.putExtra("SCORES", scores)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
        })
        getScoresTask.execute(getScoresUrl)
    }

    private fun registerScore(score: Score) {
        val getScoresUrl = URL("$BASE_URL/score?player=${score.user}&value=${score.value}")
        val getScoresTask = RequestTask(object: RequestHandler {
            override fun onResponse(result: Boolean, httpsURLConnection: HttpsURLConnection, document: Document?) {
                val intent = Intent(ACTION_REGISTER_SCORE)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
        })
        getScoresTask.execute(getScoresUrl)
    }

    private fun login() {
        val loginUrl = URL("$BASE_URL/login.php?user=snake&pwd=test")
        val loginTask = RequestTask(object: RequestHandler {
            override fun onResponse(result: Boolean, httpsURLConnection: HttpsURLConnection, document: Document?) {
                if (result) {
                    val cookieHeader = httpsURLConnection.headerFields.get("Cookie")
                    cookieHeader?.let {
                        for (cookie in it) {
                            val cookieManager = CookieManager.getInstance()
                            cookieManager.setCookie("Snake", cookie)
                        }
                    }
                }
            }
        })
        loginTask.execute(loginUrl)
    }

    override fun onBind(intent: Intent?): IBinder? { return null }

}