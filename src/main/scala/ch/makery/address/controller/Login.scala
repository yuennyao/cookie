package ch.makery.address.controller

import scalafxml.core.macros.sfxml
import ch.makery.address.Main
import ch.makery.address.model.Password
import scalafx.scene.control.{PasswordField, TableColumn, TableView, TextField, Label, Alert}
import scalafx.scene.control.Alert.AlertType
import scalafx.beans.property.{StringProperty} 
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.paint.Color

@sfxml
class Login(
    private var loginPassword: String,
    private val passwordField : PasswordField,
    private val statusLabel : Label,
    private val passwordLabel : Label,
    
    private val passwordTable : TableView[Password],
    private val passwordColumn : TableColumn[Password, String],
    private var currentPassword: String,
    private var newPassword: String,
    private val currentPasswordField: TextField,
    private val newPasswordField : TextField,
    private val passwordStatusLabel : Label,
) {

    if (passwordTable.items == null){
        handleAddNewPassword("113")
    }

    passwordTable.items = Password.passwordData
    passwordColumn.cellValueFactory = {_.value.loginPassword}

      //Delete old password
    def handleDeleteOldPassword() {
        passwordTable.items().remove(0).delete()
    }

    //Store and reset password
    def handleAddNewPassword(newPassword: String) = {
        var currentPassword = new Password(newPassword)
        Password.passwordData += currentPassword
        currentPassword.save()
    }

    //To clear the inputs in the text fields
    def clearInput() {
        newPasswordField.clear()
        currentPasswordField.clear()
    }

    try {passwordLabel.text = passwordColumn.getCellData(0).toString}

    catch{
        case e: IndexOutOfBoundsException => println("Couldn't find that index.")

    }

    //Click confirm to reset password
    def handleConfirm() {
        if (passwordTable.items != null){
            currentPassword = passwordColumn.getCellData(0).toString
        }

        if ((currentPasswordField.getText == currentPassword) && (newPasswordField.getText != currentPassword)){
            newPassword = newPasswordField.getText
            handleDeleteOldPassword()
            handleAddNewPassword(newPassword)
            passwordStatusLabel.setText("Status: Password has been successfully changed.")
            passwordStatusLabel.setTextFill(Color.GREEN)
            passwordLabel.text = passwordColumn.getCellData(0).toString
            clearInput()
        }

        else if ((currentPasswordField.getText != currentPassword)) {
            passwordStatusLabel.setText("Status: Wrong current password. Couldn't change password.")
            passwordStatusLabel.setTextFill(Color.RED)
            clearInput()
        }

        else if ((currentPasswordField.getText == currentPassword) && (newPasswordField.getText == currentPassword)) {
            passwordStatusLabel.setText("Status: New password same as current password.")
            passwordStatusLabel.setTextFill(Color.BLUE)
            clearInput()
        }

        else {
            passwordStatusLabel.setText("Status: Couldn't change password.")
            passwordStatusLabel.setTextFill(Color.RED)
            clearInput()
        }
    }
    
    //Login 
    def handleLogin() {
        loginPassword = passwordColumn.getCellData(0).toString
        //Correct password
        if (passwordField.getText == loginPassword){
            Main.showMainMenu()
        }

        //Incorrect password
        else {
            statusLabel.setText("Login Failed. Enter The Correct Password.")
            statusLabel.setTextFill(Color.RED)
        }
    }
}

