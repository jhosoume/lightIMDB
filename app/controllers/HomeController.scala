package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.Secured

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(override val dao: UserDAO) extends Controller with Secured {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = withAuth { username => implicit request =>
    Ok(views.html.index(username))
  }

  def user() = withUser { user => implicit request =>
    val username = user.username
    Ok(views.html.user(user))
  }
}
