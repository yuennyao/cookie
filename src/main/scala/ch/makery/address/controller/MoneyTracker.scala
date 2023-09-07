package ch.makery.address.controller

import ch.makery.address.model.{Expend, Income, TotalExpend, TotalIncome}
import ch.makery.address.Main
import scalafx.scene.control.{TableView, TableColumn, Label, Alert, TextField}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{StringProperty, DoubleProperty}
import scalafx.Includes._
import ch.makery.address.util.DateUtil._
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType

/*
* Reference from ScalaFX Address App (Program that was created during Practical CLasses)
* by Dr Chin Teck Min
*/

@sfxml
class MoneyTracker (
    //Expend
    private val expendTable : TableView[Expend],
    private val categoryColumn : TableColumn[Expend, String],
    private val expendAmountColumn : TableColumn[Expend, String],
    private val dateColumn : TableColumn[Expend, String],
    private val categoryLabel : Label,
    private val expendAmountLabel : Label,
    private val dateLabel : Label,
    private val remarkLabel :  Label,

    //Income
    private val incomeTable : TableView[Income],
    private val category2Column : TableColumn[Income, String],
    private val incomeAmount2Column : TableColumn[Income, String],
    private val date2Column : TableColumn[Income, String],
    private val category2Label : Label,
    private val incomeAmount2Label : Label,
    private val date2Label : Label,
    private val remark2Label :  Label,

    //Total Expend
    private val totalExpendTable : TableView[TotalExpend],
    private val totalExpendColumn : TableColumn[TotalExpend, String],
    private var totalExpend1 :  Double,
    private val totalExpendLabel:  Label,

    //Total Income
    private val totalIncomeTable : TableView[TotalIncome],
    private val totalIncomeColumn : TableColumn[TotalIncome, String],
    private var totalIncome1 :  Double,
    private val totalIncomeLabel:  Label

) {

    if (totalExpendTable.items == null){
        handleNewTotalExpend(0.0)
    }

    if (totalIncomeTable.items == null){
        handleNewTotalIncome(0.0)
    }


    //Total Income
    totalIncomeTable.items = TotalIncome.totalIncomeData
    totalIncomeColumn.cellValueFactory = {_.value.totalIncomeAmount.asString}
    totalIncomeLabel.text = totalIncomeColumn.getCellData(0)

    //Total Expend
    totalExpendTable.items = TotalExpend.totalExpendData
    totalExpendColumn.cellValueFactory = {_.value.totalExpendAmount.asString}
    totalExpendLabel.text = totalExpendColumn.getCellData(0)

    //Expend
    expendTable.items = Expend.expendData
    categoryColumn.cellValueFactory = {_.value.category}
    expendAmountColumn.cellValueFactory  = {_.value.expendAmount.asString} 
    dateColumn.cellValueFactory  = {_.value.date.asString} 

    //Income
    incomeTable.items = Income.incomeData
    category2Column.cellValueFactory = {_.value.category}
    incomeAmount2Column.cellValueFactory  = {_.value.incomeAmount.asString} 
    date2Column.cellValueFactory  = {_.value.date.asString} 
 
    def handleDeleteTotalExpend() = {
        totalExpendTable.items().remove(0).delete()
    }

    def handleNewTotalExpend(totalExpend1: Double) = {
        val totalExpend = new TotalExpend(totalExpend1)
        TotalExpend.totalExpendData += totalExpend
        totalExpend.save() 
    }  

    def handleDeleteTotalIncome() = {
        totalIncomeTable.items().remove(0).delete()
    }

    def handleNewTotalIncome(totalIncome1: Double) = {
        val totalIncome = new TotalIncome(totalIncome1)
        TotalIncome.totalIncomeData += totalIncome
        totalIncome.save() 
    } 

    //Expend
    private def showExpendDetails (expend : Option[Expend]) = {
        expend match {
            case Some(expend) =>
                categoryLabel.text <== expend.category
                expendAmountLabel.text   = expend.expendAmount.value.toString
                dateLabel.text     = expend.date.value.asString
                remarkLabel.text   <== expend.remark
            
            case None =>
                categoryLabel.text  = ""
                expendAmountLabel.text    = ""
                dateLabel.text      = ""
                remarkLabel.text    = ""
        }    
    }

    showExpendDetails(None) 
    showExpendDetails(None);
    
    expendTable.selectionModel().selectedItem.onChange(
        (_, _, newValue) => showExpendDetails(Some(newValue))
    )

    def handleDeleteExpend(action : ActionEvent) = {
      val selectedIndex = expendTable.selectionModel().selectedIndex.value

      if (selectedIndex >= 0) {
            if (totalExpendTable.items != null){
                totalExpend1 = totalExpendColumn.getCellData(0).toDouble
            }
            totalExpend1 -= expendAmountColumn.getCellData(selectedIndex).toDouble
            if (totalExpend1 < 0){
                totalExpend1 = 0.0
            }
            handleDeleteTotalExpend()
            handleNewTotalExpend(totalExpend1)
            expendTable.items().remove(selectedIndex).delete()
            totalExpendLabel.text = totalExpendColumn.getCellData(0)
        } 

      else {
            val alert = new Alert(AlertType.Warning){
            initOwner(Main.stage)
            title       = "No Selection"
            headerText  = "No Expend Selected"
            contentText = "Please select an expend in the table."
            }.showAndWait()
        }
    }

    def handleNewExpend(action : ActionEvent) = {
        val expend = new Expend("",0.0)
        val okClicked = Main.showExpendEditDialog(expend);

        if (okClicked) {
            Expend.expendData +=expend
            if (totalExpendTable.items == null){
                handleNewTotalExpend(0.0)
            }
            if (totalExpendTable.items != null){
                totalExpend1 = totalExpendColumn.getCellData(0).toDouble
            }
            totalExpend1 += expend.expendAmount.value.toString.toDouble
            if (totalExpendTable.items != null){
                handleDeleteTotalExpend()
            }
            handleNewTotalExpend(totalExpend1)
            expend.save()
            totalExpendLabel.text = totalExpendColumn.getCellData(0)
        }
    }   

    //Income
    private def showIncomeDetails (income : Option[Income]) = {
        income match {
            case Some(income) =>
                category2Label.text <== income.category
                incomeAmount2Label.text   = income.incomeAmount.value.toString
                date2Label.text     = income.date.value.asString
                remark2Label.text   <== income.remark;
            
            case None =>
                category2Label.text  = ""
                incomeAmount2Label.text    = ""
                date2Label.text      = ""
                remark2Label.text    = ""
        }    
    }

    showIncomeDetails(None) 
    showIncomeDetails(None);
    
    incomeTable.selectionModel().selectedItem.onChange(
        (_, _, newValue) => showIncomeDetails(Some(newValue))
    )

    def handleDeleteIncome(action : ActionEvent) = {
      val selectedIndex = incomeTable.selectionModel().selectedIndex.value

      if (selectedIndex >= 0) {
            if (totalIncomeTable.items != null){
                totalIncome1 = totalIncomeColumn.getCellData(0).toDouble
            }
            totalIncome1 -= incomeAmount2Column.getCellData(selectedIndex).toDouble
            if (totalIncome1 < 0){
                totalIncome1 = 0.0
            }
            handleDeleteTotalIncome()
            handleNewTotalIncome(totalIncome1)
            incomeTable.items().remove(selectedIndex).delete()
            totalIncomeLabel.text = totalIncomeColumn.getCellData(0)

        } 

      else {
            val alert = new Alert(AlertType.Warning){
            initOwner(Main.stage)
            title       = "No Selection"
            headerText  = "No Income Selected"
            contentText = "Please select an income in the table."
            }.showAndWait()
        }
    }
     
    def handleNewIncome(action : ActionEvent) = {
        val income = new Income("",0.0)
        val okClicked = Main.showIncomeEditDialog(income);

        if (okClicked) {
            Income.incomeData +=income
            if (totalExpendTable.items == null){
                handleNewTotalIncome(0.0)
            }
            if (totalIncomeTable.items != null){
              totalIncome1 = totalIncomeColumn.getCellData(0).toDouble
            }
            totalIncome1 += income.incomeAmount.value.toString.toDouble
            if (totalIncomeTable.items != null){
                handleDeleteTotalIncome()
            }
            handleNewTotalIncome(totalIncome1)
            income.save()
            totalIncomeLabel.text = totalIncomeColumn.getCellData(0)
        }
    }

    def mainMenu{
        Main.showMainMenu()
    }
}
