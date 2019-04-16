package com.example.snaky.services

import org.w3c.dom.Document
import javax.net.ssl.HttpsURLConnection

interface RequestHandler {

    fun onResponse(result: Boolean, httpsURLConnection: HttpsURLConnection, document: Document?)

}