package se.callista.cadec

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object Configuration {

	val baseUrl = System.getProperty("baseUrl", "http://localhost:9090" )
	val httpConf = http
			.baseURL(baseUrl)

}