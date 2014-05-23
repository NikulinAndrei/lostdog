package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import anorm.~
import play.api.Play.current

/**
 * Created with IntelliJ IDEA.
 * User: AndreiN
 * Date: 23.05.14
 */
case class LostRequestImg(id: Pk[Long] = NotAssigned,
                          reqId: Long,
                          filename: String)
object LostRequestImg {

  val simple = {
    get[Pk[Long]]("lost_req_img.id")~
      get[Long]("lost_req_img.req_id")~
      get[String]("lost_req_img.location") map{
      case id~reqId~filename => LostRequestImg(id, reqId,filename)
    }
  }


  def insert(img: LostRequestImg) ={
    DB.withConnection{ implicit connection =>
      SQL("insert into lost_req_img(id, req_id, location) values( lost_req_seq.nextval, " +
        "{rid}, {loc} )").
        on('rid->img.reqId,
        'loc -> img.filename
      ).executeInsert( scalar[Long] single )
    }
  }
}
