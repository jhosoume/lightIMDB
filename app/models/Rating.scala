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
case class Rating(id: Int, stars: Float, user_id: Int, movie_id: Int) 
case class averageMovie(id: Int, avg_stars: Float, user_id: Int, movie_id: Int)

/**
 * Um DAO para a classe de entidade Filme. 
 */
class RatingDAO @Inject() (database: Database){
  val parser : RowParser[models.Rating] = Macro.namedParser[models.Rating]
  val avgParser : RowParser[models.averageMovie] = Macro.namedParser[models.averageMovie]
  
  def save(rating: Rating) = database.withConnection { implicit connection => 
    val id: Option[Long] = SQL(
        """INSERT INTO TB_RATING(STARS, USERID, MOVIEID) 
            values ({stars}, {userid}, {movieid})""")
     .on('stars -> rating.stars, 
         'userid -> rating.user_id, 
         'movieid -> rating.movie_id).executeInsert()
  }
  
  def list = database.withConnection { implicit connection => 
    SQL("""SELECT ID, STARS, USERID AS user_id, MOVIEID AS movie_id 
           FROM TB_RATING""")
    .as(parser.*)
  }

   def findById(id: Int): Option[Rating] = database.withConnection { 
    implicit connection =>
    SQL("""SELECT ID, STARS, USERID AS user_id, MOVIEID AS movie_id 
           FROM TB_RATING 
           WHERE ID = {id} LIMIT 1""")
    .on('id -> id).as(parser.singleOpt)
  }

  def findByUser(user_id: Int) = database.withConnection { implicit connection =>
    SQL("""SELECT RATE.ID AS id, RATE.STARS AS stars, RATE.USERID AS user_id, MOVIEID AS movie_id
           FROM TB_RATING AS RATE
           INNER JOIN  TB_MOVIE MOVIE ON MOVIE.ID == movie_id
           WHERE RATE.USERID = {user_id}""")
    .on('user_id -> user_id).as(parser.*)
  }

  def findByMovie(movie_id: Int) = database.withConnection { implicit connection =>
    SQL("""SELECT RATE.ID AS id, RATE.STARS AS stars, RATE.USERID AS user_id, RATE.MOVIEID AS movie_id
           FROM TB_RATING AS RATE
           INNER JOIN  TB_MOVIE MOVIE ON MOVIE.ID == movie_id
           WHERE RATE.MOVIEID = {movie_id}""")
    .on('movie_id -> movie_id).as(parser.*)
  }

  def avgByMovie(movie_id: Int)  = database.withConnection { implicit connection =>
    SQL("""SELECT RATE.ID AS id, AVG(RATE.STARS) AS avg_stars, RATE.USERID AS user_id, RATE.MOVIEID AS movie_id
           FROM TB_RATING AS RATE
           INNER JOIN TB_MOVIE MOVIE ON MOVIE.ID == movie_id
           WHERE RATE.MOVIEID = {movie_id}""")
    .on('movie_id -> movie_id).as(avgParser.*)
  }

  //def searchByRating(rate: Int) = database.withConnection { implicit connection =>
    //SQL("""SELECT * FROM TB_MOVIE
           //WHERE EMAIL = {email} LIMIT 1""")
    //.on('email -> email).as(parser.*)
  //}
}

