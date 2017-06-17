package controllers

import play.api.mvc._

class UsuarioController extends Controller {
  
  def login(email: String, password:String) = Action {
    Ok("foo")
  }
}
