package controllers

import play.api.mvc._
import models.Movie
import models.MovieDAO
import javax.inject.Inject
import play.api.data._
import play.api.data.Forms._
import javax.inject.Singleton
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi

/**
 * A classe que processa as requisicoes do usuario 
 * relacionadas a filmes e interage com o meio de 
 * persitencia. 
 * 
 * Utiliza o mecanismo de injecao de dependencia do 
 * PlayFramework para ter acesso ao DAO FilmeDAO.  
 */
@Singleton
class MovieController @Inject()(dao: MovieDAO, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  
  def list = Action {
    var movies = dao.list
    Ok(views.html.movies.listing(movies))
  }
  
  def newMovie = Action {
    Ok(views.html.movies.newMovie(movieForm))
  }
  
  def newMovieSubmission = Action { implicit request =>
    movieForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.movies.newMovie(formWithErrors))
      },
      movie => {
        val newMovie = Movie(0, movie.title, movie.director, movie.year)
        dao.save(newMovie)
        var movies = dao.list
        Created(views.html.movies.listing(movies))
    }
  )
    
  }
  
  val movieForm = Form(
    mapping(
      "Title"  -> nonEmptyText,
      "Director" -> nonEmptyText,
      "Year" -> number(min=1950, max=2050)
    )(MovieVO.apply)(MovieVO.unapply)    
  )
}

case class MovieVO(title: String, director: String, year: Int)
