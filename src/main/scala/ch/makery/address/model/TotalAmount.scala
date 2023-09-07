package ch.makery.address.model

import scalafx.beans.property.{StringProperty, DoubleProperty, ObjectProperty}
import scalafx.scene.control.{TextField, TableColumn, Label, Alert}
import ch.makery.address.util.Database
import ch.makery.address.util.DateUtil._
import scalikejdbc._
import scala.util.{ Try, Success, Failure }
import scalafx.collections.ObservableBuffer

abstract class TotalAmount (val totalAmountS : Double) extends Database {
	def this()     = this(0.0)
}

//Expend
class TotalExpend (val totalExpendAmountS : Double) extends TotalAmount(totalExpendAmountS) {
	def this()     = this(0.0)
	var totalExpendAmount  = DoubleProperty(totalExpendAmountS)

	def save() : Try[Int] = {
		if (!(isExist)) {
			Try(DB autoCommit { implicit session => 
				sql"""
				insert into totalExpend (totalExpendAmount) values 
				(${totalExpendAmount.value})
				""".update.apply()
			})
		} 

		else {
			Try(DB autoCommit { implicit session => 
				sql"""
				update totalExpend 
				set 
				totalExpendAmount  = ${totalExpendAmount.value},
				where totalExpendAmount   = ${totalExpendAmount.value}
				""".update.apply()
			})
		}	
	}

	def delete() : Try[Int] = {
		if (isExist) {
			Try(DB autoCommit { implicit session => 
			sql"""
				delete from totalExpend where  
					totalExpendAmount = ${totalExpendAmount.value}
				""".update.apply()
			})
		} 
	
	else 
		throw new Exception("Expend Amount not exists in Database")		
	}
	
	def isExist : Boolean =  {
		DB readOnly { implicit session =>
			sql"""
				select * from totalExpend where 
				totalExpendAmount = ${totalExpendAmount.value}
			""".map(rs => rs.string("totalExpendAmount")).single.apply()
		} match {
			case Some(x) => true
			case None => false
		}

	}
}


object TotalExpend extends Database{
	val totalExpendData = new ObservableBuffer[TotalExpend]()
  	Database.setupDB()
  	totalExpendData ++= TotalExpend.getAllTotalExpends

	def apply (
		totalExpendAmountS : Double
		) : TotalExpend = {

		new TotalExpend(totalExpendAmountS) {
		}
	}

	def initializeTable() = {
		DB autoCommit { implicit session => 
			sql"""
			create table totalExpend (
			  id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
			  totalExpendAmount double
			)
			""".execute.apply()
		}
	}
	
	def getAllTotalExpends : List[TotalExpend] = {
		DB readOnly { implicit session =>
			sql"select * from totalExpend".map(rs => TotalExpend(rs.double("totalExpendAmount"))).list.apply()
		} 
	}
}

//Income
class TotalIncome (val totalIncomeAmountS : Double) extends TotalAmount(totalIncomeAmountS) {
	def this()     = this(0.0)
	var totalIncomeAmount  = DoubleProperty(totalIncomeAmountS)

	def save() : Try[Int] = {
		if (!(isExist)) {
			Try(DB autoCommit { implicit session => 
				sql"""
				insert into totalIncome (totalIncomeAmount) values 
				(${totalIncomeAmount.value})
				""".update.apply()
			})
		} 

		else {
			Try(DB autoCommit { implicit session => 
				sql"""
				update totalIncome 
				set 
				totalIncomeAmount  = ${totalIncomeAmount.value},
				where totalIncomeAmount   = ${totalIncomeAmount.value}
				""".update.apply()
			})
		}
			
	}

	def delete() : Try[Int] = {
		if (isExist) {
			Try(DB autoCommit { implicit session => 
			sql"""
				delete from totalIncome where  
					totalIncomeAmount = ${totalIncomeAmount.value}
				""".update.apply()
			})
		} 
		
		else 
			throw new Exception("Income Amount not exists in Database")		
	}

	def isExist : Boolean =  {
		DB readOnly { implicit session =>
			sql"""
				select * from totalIncome where 
				totalIncomeAmount = ${totalIncomeAmount.value}
			""".map(rs => rs.string("totalIncomeAmount")).single.apply()
		} match {
			case Some(x) => true
			case None => false
		}

	}
}

object TotalIncome extends Database{
	val totalIncomeData = new ObservableBuffer[TotalIncome]()
  	Database.setupDB()
  	totalIncomeData ++= TotalIncome.getAllTotalIncomes

	def apply (
		totalIncomeAmountS : Double
		) : TotalIncome = {

		new TotalIncome(totalIncomeAmountS) {
		}
	}

	def initializeTable() = {
		DB autoCommit { implicit session => 
			sql"""
			create table totalIncome (
			  id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
			  totalIncomeAmount double
			)
			""".execute.apply()
		}
	}
	
	def getAllTotalIncomes : List[TotalIncome] = {
		DB readOnly { implicit session =>
			sql"select * from totalIncome".map(rs => TotalIncome(rs.double("totalIncomeAmount"))).list.apply()
		} 
	}
}
