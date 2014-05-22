package controllers

import play.api.mvc._
import models.LostRequest

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index( LostRequest.list( 10 )) )
  }

}