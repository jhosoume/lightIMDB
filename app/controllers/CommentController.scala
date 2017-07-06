package controllers

import play.api.mvc._
import models.Movie
import models.MovieDAO
import models.UserDAO
import models.Comment
import models.CommentDAO
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
class CommentController @Inject()(override val dao: UserDAO, moviedao: MovieDAO, commentdao: CommentDAO, ratingdao: RatingDAO, val messagesApi: MessagesApi) extends Controller with Secured with I18nSupport {
  
  def list = Action {
    Ok(views.html.comments.listing(commentdao.list))
  }

  def show(id: Int) = withAuth { username => implicit request =>
    Ok(views.html.comments.single(commentdao.findById(id)))
  }
  
  def newComment = withAuth { username => implicit request =>
    Ok(views.html.comments.newComment(commentForm))
  }
  
  def newCommentSubmission = Action { implicit request =>
    commentForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.comments.newComment(formWithErrors))
      },
      comment => {
        val user_email = request.session.get(Security.username).get
        val user = dao.searchByEmail(user_email).get
        val movie_id =  request.cookies.get("movie").get.value.toInt
        val newComment = Comment(0, comment.message, user.id, movie_id)
        val avgRating = ratingdao.avgByMovie(movie_id)
        val comments = commentdao.findByMovie(movie_id)
        commentdao.save(newComment)
        Created(views.html.movies.single((moviedao.findById(movie_id)), commentForm, ratingForm,comments, avgRating)).withCookies(Cookie("movie", s"$movie_id"))
      }
    )
  }

  def user() = withUser { user => implicit request =>
    val username = user.email
    Ok(views.html.index())
  }
  
  val commentForm = Form(
    mapping(
      "Message"  -> nonEmptyText
    )(CommentVO.apply)(CommentVO.unapply)    
  )

  val ratingForm = Form(
    mapping(
      "Rating"  -> number(min = 0, max = 5)
    )(RatingVO.apply)(RatingVO.unapply)
  )



}

case class CommentVO(message: String)
