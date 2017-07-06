package controllers

import play.api.mvc._
import models.Movie
import models.MovieDAO
import models.UserDAO
import models.Rating
import models.RatingDAO
import javax.inject.Inject
import play.api.data._
import play.api.data.Forms._
import javax.inject.Singleton
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import controllers.Secured

/**
 * A classe que processa as requisicoes do usuario 
 * relacionadas a filmes e interage com o meio de 
 * persitencia. 
 * 
 * Utiliza o mecanismo de injecao de dependencia do 
 * PlayFramework para ter acesso ao DAO FilmeDAO.  
 */
@Singleton
class RatingController @Inject()(override val dao: UserDAO, moviedao: MovieDAO, ratingdao: RatingDAO, val messagesApi: MessagesApi) extends Controller with Secured with I18nSupport {
  
  def list = Action {
    Ok(views.html.ratings.listing(ratingdao.list))
  }

  def show(id: Int) = withAuth { username => implicit request =>
    Ok(views.html.ratings.single(ratingdao.findById(id)))
  }
  
  def newRating = withAuth { username => implicit request =>
    Ok(views.html.ratings.newRating(ratingForm))
  }
  
  def newRatingSubmission = Action { implicit request =>
    ratingForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.ratings.newRating(formWithErrors))
      },
      rating => {
        val user_email = request.session.get(Security.username).get
        val user = dao.searchByEmail(user_email).get
        val movie_id =  request.cookies.get("movie").get.value.toInt
        val newRating = Rating(0, rating.stars, user.id, movie_id)
        ratingdao.save(newRating)
        Created(views.html.movies.single((moviedao.findById(movie_id)), commentForm, ratingForm)).withCookies(Cookie("movie", s"$movie_id"))
      }
    )
  }

  def user() = withUser { user => implicit request =>
    val username = user.email
    Ok(views.html.index())
  }
  
  val ratingForm = Form(
    mapping(
      "Stars"  -> number
    )(RatingVO.apply)(RatingVO.unapply)    
  )

  val commentForm = Form(
    mapping(
      "Message"  -> nonEmptyText
    )(CommentVO.apply)(CommentVO.unapply)
  )
}

case class RatingVO(stars: Int)
