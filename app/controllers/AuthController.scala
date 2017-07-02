package controllers

import javax.inject._
import javax.inject.Singleton
import javax.inject.Inject
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.i18n.Messages.Implicits._
import play.api.i18n.Messages._
import models.User
import models.UserDAO


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class AuthController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
  
  val loginForm = Form(
    tuple(
      "Email" -> text,
      "Password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => check(email, password)
    })
  )

  def check(email: String, password: String) = {
    (email == "admin@admin" && password == "1234")
  }

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => Redirect(routes.HomeController.index).withSession(Security.username -> user._1)
    )
  }

  def logout = Action {
    Redirect(routes.AuthController.login).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }
}

trait Secured {
  val dao:  UserDAO

  def username(request: RequestHeader) = request.session.get(Security.username)

  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.AuthController.login)

  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }

  /**
   * This method shows how you could wrap the withAuth method to also fetch your user
   * You will need to implement UserDAO.findOneByUsername
   */
  def withUser(f: User => Request[AnyContent] => Result) = withAuth { username => implicit request =>
    dao.searchByEmail(username).map { user =>
      f(user)(request)
    }.getOrElse(onUnauthorized(request))
  }
}
