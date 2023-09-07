package ch.makery.address.util

import scalikejdbc._
import ch.makery.address.model.{Card, Income, Expend, TotalExpend, TotalIncome, Password}

/*
* Reference from ScalaFX Address App (Program that was created during Practical CLasses)
* by Dr Chin Teck Min
*/

trait Database {
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB;create=true;";
  Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "me", "mine")
  implicit val session = AutoSession
}

object Database extends Database{
  def setupDB() = {
  	if (!hasDBInitialize_C)
  		Card.initializeTable()

    if (!hasDBInitialize_I)
      Income.initializeTable()

    if (!hasDBInitialize_E)
      Expend.initializeTable()

    if (!hasDBInitialize_TE)
      TotalExpend.initializeTable()

    if (!hasDBInitialize_TI)
      TotalIncome.initializeTable()

    if (!hasDBInitialize_P)
      Password.initializeTable()
}

  def hasDBInitialize_C : Boolean = {
    DB getTable "Card" match {
      case Some(x) => true
      case None => false 
    }   
  }

  def hasDBInitialize_I : Boolean = {
    DB getTable "Income" match {
      case Some(x) => true
      case None => false
    }
  }

  def hasDBInitialize_E : Boolean = {
    DB getTable "Expend" match {
      case Some(x) => true
      case None => false
    }
  }

  def hasDBInitialize_TE : Boolean = {
    DB getTable "TotalExpend" match {
      case Some(x) => true
      case None => false
    }
  }

  def hasDBInitialize_TI : Boolean = {
    DB getTable "TotalIncome" match {
      case Some(x) => true
      case None => false
    }
  }

  def hasDBInitialize_P : Boolean = {
    DB getTable "Password" match {
      case Some(x) => true
      case None => false
    }
  }
}
