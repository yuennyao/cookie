package ch.makery.address.model

import scalafx.beans.property.{StringProperty}
import ch.makery.address.util.Database
import scalikejdbc._
import scala.util.{ Try, Success, Failure }
import scalafx.collections.ObservableBuffer

/*
* Reference from ScalaFX Address App (Program that was created during Practical CLasses)
* by Dr Chin Teck Min
*/

class Password (val loginPasswordS: String) extends Database {
	def this()     = this(null)
	var loginPassword   = new StringProperty(loginPasswordS)

	def save() : Try[Int] = {
		if (!(isExist)) {
			Try(DB autoCommit { implicit session => 
				sql"""
				insert into password (loginPassword) values 
				(${loginPassword.value})
				""".update.apply()
			})
		} 

		else {
			Try(DB autoCommit { implicit session => 
				sql"""
				update password 
				set 
				loginPassword  = ${loginPassword.value}
				where loginPassword  = ${loginPassword.value}
				""".update.apply()
			})
		}
			
	}

	def delete() : Try[Int] = {
		if (isExist) {
			Try(DB autoCommit { implicit session => 
			sql"""
				delete from password where  
					loginPassword  = ${loginPassword.value}
				""".update.apply()
			})
		} 
		
		else 
			throw new Exception("Password not exists in Database")		
	}

	def isExist : Boolean =  {
		DB readOnly { implicit session =>
			sql"""
				select * from password where 
				loginPassword  = ${loginPassword.value}
			""".map(rs => rs.string("loginPassword")).single.apply()
		} match {
			case Some(x) => true
			case None => false
		}

	}
}

object Password extends Database{
	val passwordData = new ObservableBuffer[Password]()
  	Database.setupDB()
  	passwordData ++= Password.getAllPasswords

	def apply (
		loginPasswordS : String, 
		) : Password = {

		new Password(loginPasswordS) {

		}
	}

	def initializeTable() = {
		DB autoCommit { implicit session => 
			sql"""
			create table password (
			  id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
			  loginPassword varchar(64)
			)
			""".execute.apply()
		}
	}
	
	def getAllPasswords : List[Password] = {
		DB readOnly { implicit session =>
			sql"select * from password".map(rs => Password(rs.string("loginPassword"))).list.apply()
		} 
	}
}