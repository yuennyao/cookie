package ch.makery.address.controller

import ch.makery.address.model.Card
import scalafx.scene.control.{TextField, ComboBox, TableColumn, Label, Alert}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import scalafx.event.ActionEvent

/*
* Reference from ScalaFX Address App (Program that was created during Practical CLasses)
* by Dr Chin Teck Min
*/

@sfxml
class CardEditDialog (
    private val   comboBox1 : ComboBox[String],
    private val   cardNameField : TextField,
    private val   cardTypeField : TextField,
    private val   balanceField : TextField
){
    var         dialogStage : Stage  = null
    private var _card       : Card   = null
    var         okClicked            = false

    comboBox1.getItems().addAll( "Credit Card","Reload/Top Up Card","Debit Card");

    comboBox1.onAction = (action: ActionEvent) =>{
        cardTypeField.text = comboBox1.value.apply
    }

    def card = _card
    def card_= (x : Card) {
        _card = x

        cardNameField.text = _card.cardName.value
        cardTypeField.text  = _card.cardType.value
        balanceField.text= _card.balance.value.toString
    }

    def handleOk(action :ActionEvent){
        if (isInputValid()) {
            _card.cardName <== cardNameField.text
            _card.cardType  <== cardTypeField.text
            _card.balance.value = balanceField.getText().toDouble

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

        if (nullChecking(cardNameField.text.value))
            errorMessage += "No valid card name!\n"
        if (nullChecking(cardTypeField.text.value))
            errorMessage += "No valid card type!\n"
        if (nullChecking(balanceField.text.value))
            errorMessage += "No valid balance!\n"
        else {
            try {
                (balanceField.getText()).toDouble;
            } 
            catch {
                case e : NumberFormatException =>
                errorMessage += "No valid balance Amount (must be a double)!\n"
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