package com.oteeninga.portal.api.endpoints

import com.oteeninga.portal.api.model.{Configuration, ResultCtx}

import javax.ws.rs.{POST, Path}
import scala.concurrent.Future

@Path("game")
case class GameRestApi() {

  @POST
  @Path("/simulate")
  def simulate(config: Configuration): Future[ResultCtx] = {

  }

}
