package ch.makery.address.model

import scalafx.beans.property.{StringProperty, DoubleProperty, ObjectProperty}
import scalafx.scene.control.{TextField, TableColumn, Label, Alert}
import java.time.LocalDate
import ch.makery.address.util.Database
import ch.makery.address.util.DateUtil._
import scalikejdbc._
import scala.util.{ Try, Success, Failure }
import scalafx.collections.ObservableBuffer

abstract class Money (val catS: String, val amountS : Double) extends Database {
	def this()     = this(null, 0.0)
}

/*
* Reference from ScalaFX Address App (Program that was created during Practical CLasses)
* by Dr Chin Teck Min
*/

class Expend (categoryS: String, expendAmountS : Double) extends Money(categoryS,expendAmountS) {
	def this()     = this(null, 0.0)
	var category   = new StringProperty(categoryS)
	var expendAmount  = DoubleProperty(expendAmountS)
	var remark     = new StringProperty("Add remark")
	var date       = ObjectProperty[LocalDate](LocalDate.of(2022, 7, 17))

	def save() : Try[Int] = {
		if (!(isExist)) {
			Try(DB autoCommit { implicit session => 
				sql"""
				insert into expend (category, expendAmount,
				remark, date) values 
				(${category.value}, ${expendAmount.value}, ${remark.value}, ${date.value.asString})
				""".update.apply()
			})
		} 

		else {
			Try(DB autoCommit { implicit session => 
				sql"""
				update expend 
				set 
				category  = ${category.value} ,
				expendAmount   = ${expendAmount.value},
				remark     = ${remark.value},
				date       = ${date.value.asString}
				where category = ${category.value} and
				expendAmount = ${expendAmount.value} and date = ${date.value.asString}
				""".update.apply()
			})
		}
			
	}

	def delete() : Try[Int] = {
		if (isExist) {
			Try(DB autoCommit { implicit session => 
			sql"""
				delete from expend where  
					category = ${category.value} and
				    expendAmount = ${expendAmount.value} and date = ${date.value.asString}
				""".update.apply()
			})
		} 
		
		else 
			throw new Exception("Expend billing details not exists in Database")		
	}
	
	def isExist : Boolean =  {
		DB readOnly { implicit session =>
			sql"""
				select * from expend where 
				category = ${category.value} and
				expendAmount = ${expendAmount.value} and date = ${date.value.asString}
			""".map(rs => rs.string("category")).single.apply()
		} match {
			case Some(x) => true
			case None => false
		}

	}
}

object Expend extends Database{
	val expendData = new ObservableBuffer[Expend]()
  	Database.setupDB()
  	expendData ++= Expend.getAllExpends

	def apply (
		categoryS : String, 
		expendAmountS : Double,
		remarkS : String,
		dateS : String
		) : Expend = {

		new Expend(categoryS, expendAmountS) {
			remark.value     = remarkS
		    date.value       = dateS.parseLocalDate
		}
	}

	def initializeTable() = {
		DB autoCommit { implicit session => 
			sql"""
			create table expend (
			  id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
			  category varchar(64), 
			  remark varchar(64),
			  expendAmount double,
			  date varchar(64)
			)
			""".execute.apply()
		}
	}
	
	def getAllExpends : List[Expend] = {
		DB readOnly { implicit session =>
			sql"select * from expend".map(rs => Expend(rs.string("category"), rs.double("expendAmount"),
				rs.string("remark"), rs.string("date") )).list.apply()
		} 
	}
}
