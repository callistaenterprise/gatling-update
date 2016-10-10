package se.callista.cadec

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object Login {

	def loginAs(login: String) = {
		exec(http("LogIn")
			.get("/logIn?userName="+login)
			.check(jsonPath("$.status").is("loggedIn "+login))
		)
	}

}