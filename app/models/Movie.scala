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
case class Movie(id: Int, title: String, director: String, productionYear: Int) 

/**
 * Um DAO para a classe de entidade Filme. 
 */
class MovieDAO @Inject() (database: Database){
  val parser : RowParser[models.Movie] = Macro.namedParser[models.Movie]
  
  def save(movie: Movie) = database.withConnection { implicit connection => 
    val id: Option[Long] = SQL(
        """INSERT INTO TB_MOVIE(TITLE, DIRECTOR, PRODUCTION_YEAR) 
            values ({title}, {director}, {productionYear})""")
     .on('title -> movie.title, 
         'director -> movie.director, 
         'productionYear -> movie.productionYear).executeInsert()
  }
  
  def list = database.withConnection { implicit connection => 
    SQL"SELECT ID, TITLE, DIRECTOR, PRODUCTION_YEAR AS productionYear FROM TB_MOVIE".as(parser.*)
  }

}
