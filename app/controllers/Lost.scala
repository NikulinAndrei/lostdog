package controllers

import play.api.mvc.{Action, Controller}
import play.api.data.{Form}
import play.api.data.Forms._
import anorm.{Pk, NotAssigned}
import play.api.data.format.Formats._
import models.LostRequest
import java.util.Date

/**
 * Created with IntelliJ IDEA.
 * User: AndreiN
 * Date: 20.05.14
 */
object Lost extends Controller {
  /** Describe the forms (used in both edit and create screens). */
  val lostForm = Form(
    mapping(
      "id" -> ignored(NotAssigned:Pk[Long]),
      "title" -> nonEmptyText,
      "descr" -> nonEmptyText,
      "lostAt" -> of[Date]
    )(LostRequest.apply)(LostRequest.unapply)
  )

  def create = Action{ implicit  request =>
    Ok( views.html.lost.create(lostForm) )
  }

  def insert = Action{implicit request =>
    lostForm.bindFromRequest().fold(
      formWithErrors => BadRequest( views.html.lost.create(formWithErrors) ),
      lostReq =>{
        val id: Long = LostRequest.insert( lostReq )
        Redirect( routes.Lost.showUpdate(id)).flashing("success" -> "Uus teade on edukalt sisestatud")
      }
    )
  }

  def showUpdate( id: Long) = Action{ implicit  request =>
    LostRequest.getById( id ).map{ o =>
      Ok(views.html.lost.update(id, lostForm.fill( o )))
    }.getOrElse( NotFound )
  }

  def update( id: Long) = TODO
}