package ch.makery.address.controller

import scalafxml.core.macros.sfxml
import ch.makery.address.Main
import scalafx.scene.control.{TableView, TableColumn, Label, Alert}
import scalafx.beans.property.{StringProperty} 
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType

@sfxml
class MenuBar {
    def logout()  {
        Main.showLogin()
    }

    def changePassword()  {
        Main.showLogin()
    }

    def terminateSystem()  {
        System.exit(0);
    }
}
