package ch.makery.address.controller

import ch.makery.address.model.Expend
import scalafx.scene.control.{TextField, ComboBox, TableColumn, Label, Alert, DatePicker}
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
class ExpendEditDialog (
    private val   comboBox1 : ComboBox[String],
    private val   categoryField : TextField,
    private val   expendAmountField : TextField,
    private val   remarkField : TextField,
    private val   dateField : TextField,
){
    var         dialogStage : Stage  = null
    private var _expend     : Expend = null
    var         okClicked            = false


    comboBox1.getItems().addAll( "Dining","Snacks","Necessities","Food/Beverages","Clothing","Accessories","Beauty/Cosmetic","Games/Entertainment",
    "Shopping","Water/Electricity Bill","Housing Rent","Books/Learning", "Tuition Fee","Medical","Donate","Communication","Family","Other");

    comboBox1.onAction = (action: ActionEvent) => {
        categoryField.text = comboBox1.value.apply
    }

    def expend = _expend
    def expend_= (x : Expend) {
        _expend = x

        categoryField.text = _expend.category.value
        remarkField.text  = _expend.remark.value
        expendAmountField.text = _expend.expendAmount.value.toString
        dateField.text  = _expend.date.value.asString
        dateField.setPromptText("dd.mm.yyyy");
    }

    def handleOk(action :ActionEvent){
        if (isInputValid()) {
            _expend.category <== categoryField.text
            _expend.remark  <== remarkField.text
            _expend.expendAmount.value = expendAmountField.getText().toDouble
            _expend.date.value         = dateField.text.value.parseLocalDate;

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
        if (nullChecking(expendAmountField.text.value))
            errorMessage += "No valid expend amount!\n"
        else {
            try {
                (expendAmountField.getText()).toDouble;
            } 
            catch {
                case e : NumberFormatException =>
                errorMessage += "No valid expend Amount (must be a double)!\n"
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
