package com.oteeninga.portal.api

import sun.net.www.http.HttpClient

class GameApiModule {
  lazy val apiHttpClient: HttpClient = JettyU

}
