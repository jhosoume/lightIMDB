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

case class User(id: Int, email: String, password: String, admin: Boolean)

class UserDAO @Inject() (database: Database) {
  val parser : RowParser[models.User] = Macro.namedParser[models.User]

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


  //def searchByEmail(email : String) = products.filter(u => u.email == email).toList(0)  
}
