package models

case class User(email: String, password: String)

object User {
  var products = Set(
        User("foo", "foo"), 
        User("blah", "blah")
      )
      
   def searchByEmail(email : String) = products.filter(u => u.email == email).toList(0)  
}
