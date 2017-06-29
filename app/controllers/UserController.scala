package controllers

import play.api.mvc._
import models.User
import models.UserDAO
import javax.inject.Inject
import play.api.data._
import play.api.data.Forms._
import javax.inject.Singleton
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi

@Singleton
class UserController @Inject()(dao: UserDAO, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  
  def login(email: String, password:String) = TODO

  def show(id: Int) = Action {
    Ok(views.html.users.single(dao.findById(id)))
  }

  def list = Action {
    var users = dao.list
    Ok(views.html.users.listing(users))
  }

  def newUser = Action {
    Ok(views.html.users.newUser(userForm))
  }

  def newUserSubmission = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.users.newUser(formWithErrors))
      },
      user => {
        val newUser = User(0, user.email, user.password, false)
        dao.save(newUser)
        var users = dao.list
        Created(views.html.users.listing(users))
      }
  )}

  val userForm = Form(
    mapping(
      "Email" -> nonEmptyText,
      "Password" -> nonEmptyText
    )(UserVO.apply)(UserVO.unapply)
  )
}

case class UserVO(email: String, password: String)
