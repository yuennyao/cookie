package ch.makery.address.model

import scalafx.beans.property.{StringProperty, DoubleProperty, ObjectProperty}
import java.time.LocalDate
import ch.makery.address.util.Database
import ch.makery.address.util.DateUtil._
import scalikejdbc._
import scala.util.{ Try, Success, Failure }
import scalafx.collections.ObservableBuffer

/*
* Reference from ScalaFX Address App (Program that was created during Practical CLasses)
* by Dr Chin Teck Min
*/

class Income (val categoryS: String, val incomeAmountS : Double) extends Money(categoryS,incomeAmountS) {
	def this()     = this(null, 0.0)
	var category   = new StringProperty(categoryS)
	var incomeAmount  = DoubleProperty(incomeAmountS)
	var remark     = new StringProperty("Add remark")
	var date       = ObjectProperty[LocalDate](LocalDate.of(2022, 7, 17))

	def save() : Try[Int] = {
		if (!(isExist)) {
			Try(DB autoCommit { implicit session => 
				sql"""
				insert into income (category, incomeAmount,
				remark, date) values 
				(${category.value}, ${incomeAmount.value}, ${remark.value}, ${date.value.asString})
				""".update.apply()
			})
		} 

		else {
			Try(DB autoCommit { implicit session => 
				sql"""
				update income 
				set 
				category  = ${category.value} ,
				incomeAmount   = ${incomeAmount.value},
				remark     = ${remark.value},
				date       = ${date.value.asString}
				where category = ${category.value} and
				incomeAmount = ${incomeAmount.value} and date = ${date.value.asString}
				""".update.apply()
			})
		}
			
	}

	def delete() : Try[Int] = {
		if (isExist) {
			Try(DB autoCommit { implicit session => 
			sql"""
				delete from income where  
					category = ${category.value} and
				    incomeAmount = ${incomeAmount.value} and date = ${date.value.asString}
				""".update.apply()
			})
		} 
		
		else 
			throw new Exception("Income billing details not exists in Database")		
	}
	def isExist : Boolean =  {
		DB readOnly { implicit session =>
			sql"""
				select * from income where 
				category = ${category.value} and
				incomeAmount = ${incomeAmount.value} and date = ${date.value.asString}
			""".map(rs => rs.string("category")).single.apply()
		} match {
			case Some(x) => true
			case None => false
		}

	}
}

object Income extends Database{
	val incomeData = new ObservableBuffer[Income]()
  	Database.setupDB()
  	incomeData ++= Income.getAllIncomes

	def apply (
		categoryS : String, 
		incomeAmountS : Double,
		remarkS : String,
		dateS : String
		) : Income = {

		new Income(categoryS, incomeAmountS) {
			remark.value     = remarkS
		    date.value       = dateS.parseLocalDate
		}
	}

	def initializeTable() = {
		DB autoCommit { implicit session => 
			sql"""
			create table income (
			  id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
			  category varchar(64), 
			  remark varchar(64),
			  incomeAmount double,
			  date varchar(64)
			)
			""".execute.apply()
		}
	}
	
	def getAllIncomes : List[Income] = {
		DB readOnly { implicit session =>
			sql"select * from income".map(rs => Income(rs.string("category"), rs.double("incomeAmount"),
				rs.string("remark"), rs.string("date") )).list.apply()
		} 
	}
}