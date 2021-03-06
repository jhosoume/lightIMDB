package controllers

import play.api.mvc._
import models.Movie
import models.MovieDAO
import models.UserDAO
import models.RatingDAO
import models.CommentDAO
import javax.inject.Inject
import play.api.data._
import play.api.data.Forms._
import javax.inject.Singleton
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import controllers.Secured
import controllers.CommentVO
import controllers.RatingVO

/**
 * A classe que processa as requisicoes do usuario , cda
 * relacionadas a filmes e interage com o meio de 
 * persitencia. 
 * 
 * Utiliza o mecanismo de injecao de dependencia do 
 * PlayFramework para ter acesso ao DAO FilmeDAO.  
 */
@Singleton
class MovieController @Inject()(override val dao: UserDAO, mdao: MovieDAO, ratingdao: RatingDAO, commentdao: CommentDAO, val messagesApi: MessagesApi, cdao: CommentDAO, rdao: RatingDAO) extends Controller with Secured with I18nSupport {
  
  def list = Action {
    var movies = mdao.list
    Ok(views.html.movies.listing(movies))
  }

  def listAdm = Action {
    var movies = mdao.list
    Ok(views.html.movies.listingAdm(movies))
  }

  def show(id: Int) = withAuth { username => implicit request =>
    val avgRating = ratingdao.avgByMovie(id)
    val comments = commentdao.findByMovie(id)
    println(comments)
    Ok(views.html.movies.single((mdao.findById(id)), commentForm, ratingForm, comments, avgRating)).withCookies(Cookie("movie", s"$id"))
  }
  
  def newMovie = withAuth { username => implicit request =>
    Ok(views.html.movies.newMovie(movieForm))
  }

  def delete(id: Int) = Action {implicit request =>
    println("uhuuuuhsauhsa")
    mdao.delete(id)
    var movies = mdao.list
    Ok(views.html.movies.listingAdm(movies))
  }
  
  def newMovieSubmission = Action { implicit request =>
    movieForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.movies.newMovie(formWithErrors))
      },
      movie => {
        val newMovie = Movie(0, movie.title, movie.director, movie.year)
        mdao.save(newMovie)
        var movies = mdao.list
        Created(views.html.movies.listingAdm(movies))
      }
    )
  }

  def user() = withUser { user => implicit request =>
    val username = user.email
    Ok(views.html.index())
  }
  
  val movieForm = Form(
    mapping(
      "Title"  -> nonEmptyText,
      "Director" -> nonEmptyText,
      "Year" -> number(min=1956, max=2050)
    )(MovieVO.apply)(MovieVO.unapply)    
  )
   val commentForm = Form(
     mapping(
       "Message"  -> nonEmptyText
     )(CommentVO.apply)(CommentVO.unapply)
   )

  val ratingForm = Form(
    mapping(
      "Stars"  -> number
    )(RatingVO.apply)(RatingVO.unapply)
  )

}

case class MovieVO(title: String, director: String, year: Int)
