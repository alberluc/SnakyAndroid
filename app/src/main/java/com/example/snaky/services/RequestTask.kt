package com.example.snaky.services

import android.os.AsyncTask
import android.os.Build
import android.support.annotation.RequiresApi
import android.webkit.CookieManager
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import javax.xml.parsers.DocumentBuilderFactory

class RequestTask(private val requestHandler: RequestHandler): AsyncTask<URL, Void, Boolean>() {

    private var document: Document? = null
    private var httpsURLConnection: HttpsURLConnection? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun doInBackground(vararg urls: URL?): Boolean {
        httpsURLConnection = urls[0]?.openConnection() as HttpsURLConnection
        httpsURLConnection?.let {
            it.requestMethod = "GET"
            it.doOutput = true
            it.connectTimeout = 10000
            it.readTimeout = 5000
            val cookieManager = CookieManager.getInstance()
            val cookieVal = cookieManager.getCookie("Snake")
            it.setRequestProperty("Cookie", cookieVal)
            it.connect()
            if (it.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(it.inputStream) as Reader)
                val buffer = StringBuffer()
                for (line in reader.lines()) {
                    buffer.append(line)
                }
                reader.close()
                val documentBuilderFactory = DocumentBuilderFactory.newInstance()
                val documentBuilder = documentBuilderFactory.newDocumentBuilder()
                val inputSource = InputSource(StringReader(buffer.toString()))
                document = documentBuilder.parse(inputSource)
                return true
            }
        }
        return false
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        requestHandler.onResponse(result!!, httpsURLConnection!!, document)
    }
}