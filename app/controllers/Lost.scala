package controllers

import play.api.mvc.{Action, Controller}
import play.api.data.{Form}
import play.api.data.Forms._
import anorm.{Pk, NotAssigned}
import play.api.data.format.Formats._
import models.{LostRequestImg, LostRequest}
import java.util.Date
import java.io.File
import services.FileService
import scala.util.Random

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
      //,"picture" -> ignored( play.api.mvc.MultipartFormData.FilePart[play.api.libs.Files.TemporaryFile] ).verifying("Pildi fail on valimata!", _.isDefined)
    )(LostRequest.apply)(LostRequest.unapply)
  )

  def create = Action{ implicit  request =>
    Ok( views.html.lost.create(lostForm) )
  }

  def insert = Action(parse.multipartFormData){implicit request =>
    lostForm.bindFromRequest().fold(
      formWithErrors => BadRequest( views.html.lost.create(formWithErrors) ),
      lostReq =>{
        //request.body.file("picture").get.ref.moveTo(new File("/tmp/images/"+new Random().nextLong() +".jpg"))
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

  def update( id: Long) = Action{implicit request =>
    lostForm.bindFromRequest().fold(
      formWithErrors => BadRequest( views.html.lost.update(id, formWithErrors) ),
      lostReq =>{
        LostRequest.update(id, lostReq )
        Redirect( routes.Lost.showUpdate(id)).flashing("success" -> "Teade on edukalt muudetud")
      }
    )
  }

  def uploadImage(id:Long) = Action(parse.multipartFormData) { implicit request =>
    println("Uploading")
    request.body.file("picture").map { file =>
      val filename = new FileService().persistFile( file, id)
      println("persisted as "+filename)
      val imgId = LostRequestImg.insert( new LostRequestImg( reqId=id, filename=filename) )
      println("inserted as "+imgId)
      Redirect( routes.Lost.showUpdate(id)).flashing("success" -> "Pilt on lisatud")
    }.getOrElse(
      Redirect( routes.Lost.showUpdate(id)).flashing("success" -> "Pilti ei ole")
    )
  }
}