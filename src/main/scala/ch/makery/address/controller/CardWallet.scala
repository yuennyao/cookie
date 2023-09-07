package ch.makery.address.controller

import ch.makery.address.model.Card
import ch.makery.address.Main
import scalafx.scene.control.{TableView, TableColumn, Label, Alert}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{StringProperty, DoubleProperty}
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType

/*
* Reference from ScalaFX Address App (Program that was created during Practical CLasses)
* by Dr Chin Teck Min
*/

@sfxml
class CardWallet(
    private val cardTable : TableView[Card],
    private val cardNameColumn : TableColumn[Card, String],
    private val cardTypeColumn : TableColumn[Card, String],
    private val cardNameLabel : Label,
    private val cardName2Label : Label,
    private val cardTypeLabel : Label,
    private val balanceLabel : Label,
) {

    cardTable.items = Card.cardData

    cardNameColumn.cellValueFactory = {_.value.cardName}
    cardTypeColumn.cellValueFactory  = {_.value.cardType} 

    private def showCardDetails (card : Option[Card]) = {
        card match {
            case Some(card) =>
                cardName2Label.text <== card.cardName
                cardNameLabel.text <== card.cardName
                cardTypeLabel.text   <== card.cardType
                balanceLabel.text   = card.balance.value.toString;
                
            
            case None =>
                cardNameLabel.text  = ""
                cardTypeLabel.text    = ""
                balanceLabel.text      = ""
        }    
    }

    showCardDetails(None) 
    showCardDetails(None);
    
    cardTable.selectionModel().selectedItem.onChange(
        (_, _, newValue) => showCardDetails(Some(newValue))
    )

    def handleDeleteCard(action : ActionEvent) = {
      val selectedIndex = cardTable.selectionModel().selectedIndex.value

      if (selectedIndex >= 0) {
            cardTable.items().remove(selectedIndex).delete()
        } 

      else {
            val alert = new Alert(AlertType.Warning){
            initOwner(Main.stage)
            title       = "No Selection"
            headerText  = "No Card Selected"
            contentText = "Please select a card in the table."
            }.showAndWait()
        }
    }
     

    def handleNewCard(action : ActionEvent) = {
        val card = new Card("","")
        val okClicked = Main.showCardEditDialog(card);
        if (okClicked) {
            Card.cardData +=card
            card.save() 
        }
    }


    def handleEditCard(action : ActionEvent) = {
        val selectedCard = cardTable.selectionModel().selectedItem.value
        if (selectedCard != null) {
            val okClicked = Main.showCardEditDialog(selectedCard)

            if (okClicked) {
                showCardDetails(Some(selectedCard))
                selectedCard.save()
            }
        } 

        else {
            // Nothing selected.
            val alert = new Alert(Alert.AlertType.Warning){
                initOwner(Main.stage)
                title       = "No Selection"
                headerText  = "No Card Selected"
                contentText = "Please select a card in the table."
            }.showAndWait()
        }
    }

    def mainMenu{
        Main.showMainMenu()
    }

}
