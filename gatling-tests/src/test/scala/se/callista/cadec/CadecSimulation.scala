package se.callista.cadec

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CadecSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://localhost:9090")

	val scn = scenario("CadecSimulation")
		.exec(http("LogIn")
			.get("/logIn"))
		.pause(2 seconds)
		.exec(http("DoThis")
			.get("/doThis")
		)
		.pause(5 seconds)
		.exec(http("DoThat")
			.get("/doThat")
		)
		.pause(5 seconds)
		.exec(http("LogOut")
			.get("/logOut")
		)
	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}