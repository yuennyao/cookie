package ch.makery.address.model

import scalafx.beans.property.{StringProperty, DoubleProperty, ObjectProperty}
import ch.makery.address.util.Database
import ch.makery.address.util.DateUtil._
import scalikejdbc._
import scala.util.{ Try, Success, Failure }
import scalafx.collections.ObservableBuffer

/*
* Reference from ScalaFX Address App (Program that was created during Practical CLasses)
* by Dr Chin Teck Min
*/

class Card (val cardNameS : String, val cardTypeS : String) extends Database {
	def this()     = this(null, null)
	var cardName  = new StringProperty(cardNameS)
	var cardType   = new StringProperty(cardTypeS)
	var balance = DoubleProperty(0.0)

	def save() : Try[Int] = {
		if (!(isExist)) {
			Try(DB autoCommit { implicit session => 
				sql"""
				insert into card (cardName, cardType,
				balance) values 
				(${cardName.value}, ${cardType.value},
				${balance.value})
				""".update.apply()
			})
		} 

		else {
			Try(DB autoCommit { implicit session => 
				sql"""
				update card
				set 
				cardName  = ${cardName.value} ,
				cardType   = ${cardType.value},
				balance = ${balance.value},
				where cardName = ${cardName.value} and
				cardType = ${cardType.value}
				""".update.apply()
			})
		}		
	}

	def delete() : Try[Int] = {
		if (isExist) {
			Try(DB autoCommit { implicit session => 
			sql"""
				delete from card where  
					cardName = ${cardName.value} and cardType = ${cardType.value}
				""".update.apply()
			})
		} 
		
		else 
			throw new Exception("Card not exist in Database")		
	}

	def isExist : Boolean =  {
		DB readOnly { implicit session =>
			sql"""
				select * from card where 
				cardName = ${cardName.value} and cardType = ${cardType.value}
			""".map(rs => rs.string("cardName")).single.apply()
		} match {
			case Some(x) => true
			case None => false
		}
	}
}

object Card extends Database{
	val cardData = new ObservableBuffer[Card]()
  	Database.setupDB()
  	cardData ++= Card.getAllCards

	def apply (
		cardNameS : String, 
		cardTypeS : String,
		balanceD : Double
		) : Card = {

		new Card(cardNameS, cardTypeS) {
			balance.value = balanceD
		}
		
	}

	def initializeTable() = {
		DB autoCommit { implicit session => 
			sql"""
			create table card (
			  id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
			  cardName varchar(64), 
			  cardType varchar(64), 
			  balance double
			)
			""".execute.apply()
		}
	}
	
	def getAllCards : List[Card] = {
		DB readOnly { implicit session =>
			sql"select * from card".map(rs => Card(rs.string("cardName"),
				rs.string("cardType"), 
				rs.double("balance") )).list.apply()
		} 
	}
}
