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

/**
 * A definicao da classe Filme, que corresponde 
 * a uma entidade cujo estado deve ser salvo no 
 * banco de dados. 
 */
case class Comment(id: Int, message: String, user_id: Int, movie_id: Int) 

/**
 * Um DAO para a classe de entidade Filme. 
 */
class CommentDAO @Inject() (database: Database){
  val parser : RowParser[models.Comment] = Macro.namedParser[models.Comment]
  
  def save(comment: Comment) = database.withConnection { implicit connection => 
    val id: Option[Long] = SQL(
        """INSERT INTO TB_COMMENT(MESSAGE, USERID, MOVIEID) 
            values ({message}, {userid}, {movieid})""")
     .on('message -> comment.message, 
         'userid -> comment.user_id, 
         'movieid -> comment.user_id).executeInsert()
  }
  
  def list = database.withConnection { implicit connection => 
    SQL("""SELECT ID, MESSAGE, USERID as user_id, MOVIEID AS movie_id 
           FROM TB_COMMENT""")
    .as(parser.*)
  }

  def findById(id: Int): Option[Comment] = database.withConnection { 
    implicit connection =>
    SQL("""SELECT ID, MESSAGE, USERID AS user_id, MOVIEID AS movie_id 
           FROM TB_COMMENT
           WHERE ID = {id} LIMIT 1""")
    .on('id -> id).as(parser.singleOpt)
  }

  def findByUser(user_id: Int) = database.withConnection { implicit connection =>
    SQL("""SELECT COMMENT.ID AS id, COMMENT.MESSAGE AS message, COMMENT.USERID AS user_id, COMMENT.MOVIEID AS movie_id
           FROM TB_COMMENT AS COMMENT
           INNER JOIN TB_MOVIE MOVIE ON MOVIE.ID = movie_id
           WHERE COMMENT.USERID = {user_id}""")
    .on('user_id -> user_id).as(parser.*)
  }

  def findByMovie(movie_id: Int) = database.withConnection { implicit connection =>
    SQL("""SELECT COMMENT.ID AS id, COMMENT.MESSAGE AS stars, COMMENT.USERID AS user_id, COMMENT.MOVIEID AS movie_id
           FROM TB_COMMENT AS COMMENT
           INNER JOIN  TB_MOVIE MOVIE ON MOVIE.ID = COMMENT.MOVIEID
           WHERE COMMENT.MOVIEID = {movie_id}""")
    .on('movie_id -> movie_id).as(parser.*)
  }



  //def searchByRating(rate: Int) = database.withConnection { implicit connection =>
    //SQL("""SELECT * FROM TB_MOVIE
           //WHERE EMAIL = {email} LIMIT 1""")
    //.on('email -> email).as(parser.*)
  //}
}
