package ch.makery.address.controller

import ch.makery.address.model.Income
import scalafx.scene.control.{TextField, ComboBox, TableColumn, Label, Alert}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import ch.makery.address.util.DateUtil._
import scalafx.event.ActionEvent

/*
* Reference from ScalaFX Address App (Program that was created during Practical CLasses)
* by Dr Chin Teck Min
*/

@sfxml
class IncomeEditDialog (
    private val   comboBox1 : ComboBox[String],
    private val   categoryField : TextField,
    private val   incomeAmountField : TextField,
    private val   remarkField : TextField,
    private val   dateField : TextField,
){
    var         dialogStage : Stage  = null
    private var _income     : Income = null
    var         okClicked            = false

    comboBox1.getItems().addAll("Salary","Part-Time","Freelance","Bonus","Red Packet","Refund","Dividend","PTPTN","Scholarship");

    comboBox1.onAction = (action: ActionEvent) =>{
        categoryField.text = comboBox1.value.apply
    }

    def income = _income
    def income_= (x : Income) {
        _income = x

        categoryField.text = _income.category.value
        remarkField.text  = _income.remark.value
        incomeAmountField.text= _income.incomeAmount.value.toString
        dateField.text  = _income.date.value.asString
        dateField.setPromptText("dd.mm.yyyy");
    }

    def handleOk(action :ActionEvent){
        if (isInputValid()) {
            _income.category <== categoryField.text
            _income.remark  <== remarkField.text
            _income.incomeAmount.value = incomeAmountField.getText().toDouble
            _income.date.value       = dateField.text.value.parseLocalDate;

            okClicked = true; 
            dialogStage.close() 
        }
    }

    def handleCancel(action :ActionEvent) {
        dialogStage.close();
    }

    def nullChecking (x : String) = x == null || x.length == 0

    def isInputValid() : Boolean = {
        var errorMessage = ""

        if (nullChecking(categoryField.text.value))
            errorMessage += "No valid category!\n"
        if (nullChecking(remarkField.text.value))
            errorMessage += "No valid remarks!\n"
        if (nullChecking(incomeAmountField.text.value))
            errorMessage += "No valid income amount!\n"
        else {
            try {
                (incomeAmountField.getText()).toDouble;
            } 
            catch {
                case e : NumberFormatException =>
                errorMessage += "No valid income Amount (must be a double)!\n"
            }
        }
        if (nullChecking(dateField.text.value))
            errorMessage += "No valid date!\n"
        else {
            if (!dateField.text.value.isValid) {
                errorMessage += "No valid date. Use the format dd.mm.yyyy!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } 
        else {
            val alert = new Alert(Alert.AlertType.Error){
                initOwner(dialogStage)
                title = "Invalid Fields"
                headerText = "Please correct invalid fields"
                contentText = errorMessage
            }.showAndWait()

            return false;
        }
    }
} 