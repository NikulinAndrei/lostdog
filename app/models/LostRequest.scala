package models

import java.util.Date

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

/**
 * Created with IntelliJ IDEA.
 * User: AndreiN
 * Date: 20.05.14
 */
case class LostRequest(id: Pk[Long] = NotAssigned,
                       title: String,
                       descr: String,
                       lostAt: Date)

object LostRequest{

  val simple = {
      get[Pk[Long]]("lost_req.id")~
      get[String]("lost_req.title")~
      get[String]("lost_req.descr") ~
      get[Date]("lost_req.lost_at") map{
      case id~title~description~lostAt => LostRequest(id,title,description,lostAt)
    }
  }


  def getById( id:Long) = {
    DB.withConnection { implicit connection =>
      SQL( "select * from lost_req where id={id} ").on( 'id-> id).as(simple.singleOpt)
    }
  }


  def insert(lost: LostRequest) ={
    DB.withConnection{ implicit connection =>
      SQL("insert into lost_req(id, title, descr, lost_at) values( lost_req_seq.nextval, " +
        "{title}, {descr}, {when} )").
        on('title->lost.title,
           'descr -> lost.descr,
           'when -> lost.lostAt
      ).executeInsert( scalar[Long] single )
    }
  }

  def update(id: Long, lost: LostRequest) ={
    DB.withConnection{ implicit connection =>
      SQL("update lost_req set title={title}, descr={descr}, lost_at={when}  where id={id}").
        on('title->lost.title,
        'descr -> lost.descr,
        'when -> lost.lostAt,
        'id -> id
      ).executeUpdate
    }
  }

  def list(cnt: Int) ={
    DB.withConnection{ implicit connection =>
      SQL(
        """
           select l.id, l.title, l.descr, l.lost_at
           from lost_req l
           order by created_at desc
           limit {cnt}
        """).on( 'cnt -> cnt). as( simple * )
    }
  }
}
