package se.callista.cadec

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CadecSimulation extends Simulation {

	val testpersonnummer = csv("testpersonnummer_skatteverket.cvs").circular

	val scn = scenario("CadecSimulation")
		.repeat(5) {
			feed(testpersonnummer)
			.exec(Login.loginAs("${personNr}"))
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
		}
	setUp(scn.inject(rampUsers(500) over (10 seconds))).protocols(Configuration.httpConf)
}