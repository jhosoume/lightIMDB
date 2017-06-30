package models

import anorm.SQL
import anorm.SqlQuery
import anorm.RowParser
import anorm.Macro
import anorm.SqlStringInterpolation
import anorm.SqlParser
import play.api.db.DB
import play.api.Play.current
import javax.inject.Inject
import play.api.db.Database
import javax.inject.Singleton

case class User(id: Int, email: String, password: String, admin: Boolean = false)

class UserDAO @Inject() (database: Database) {
  val parser : RowParser[models.User] = Macro.namedParser[models.User]
  //val simple = {
    //get[String]("tb_user.email") ~
    //get[String]("tb_user.password") map {
      //case email~password => User(0, email, password, false)
    //}
  //} 

  def save(user: User) = database.withConnection { implicit connection =>
    val id: Option[Long] = SQL(
      """INSERT INTO TB_USER(EMAIL, PASSWORD, ADMIN)
          values ({email}, {password}, {admin})""")
     .on('email -> user.email,
         'password -> user.password,
         'admin -> user.admin).executeInsert()
  }

  def list = database.withConnection { implicit connection =>
    SQL"SELECT ID, EMAIL, PASSWORD, ADMIN FROM TB_USER".as(parser.*)
  }

  def findById(id: Int): Option[User] = database.withConnection { 
    implicit connection =>
    SQL("""SELECT * FROM TB_USER 
           WHERE ID = {id} LIMIT 1""")
    .on('id -> id).as(parser.singleOpt)
  }

  def searchByEmail(email: String): Option[User] = database.withConnection { 
  implicit connection => 
    SQL("""SELECT * FROM TB_USER
           WHERE EMAIL = {email} LIMIT 1""")
    .on('email -> email).as(parser.singleOpt)
  }
}
