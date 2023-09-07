package ch.makery.address

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import ch.makery.address.util.{DateUtil, Database}
import ch.makery.address.model.{Card,Income,Expend}
import ch.makery.address.controller.{CardEditDialog, MoneyTracker, IncomeEditDialog, ExpendEditDialog, MainMenu}
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import scalafx.scene.image.Image
import scalafx.collections.ObservableBuffer
import javafx.scene.control.{Tab, TabPane, MenuBar}
import javafx.{scene => jfxs}
import scalafx.stage.{Stage, Modality}

/*
* Reference from ScalaFX Address App (Program that was created during Practical CLasses)
* by Dr Chin Teck Min
*/

object Main extends JFXApp {
  Database.setupDB()

  val cardData = new ObservableBuffer[Card]()
  val incomeData = new ObservableBuffer[Income]()
  val expendData = new ObservableBuffer[Expend]()

  cardData ++= Card.getAllCards
  incomeData ++= Income.getAllIncomes
  expendData ++= Expend.getAllExpends

  val rootResource = getClass.getResource("view/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load();
  val roots = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage {
    title = "Cookie! My Money Tracking System"
    icons += new Image(getClass.getResourceAsStream("images/cookie_icon.png"))
    scene = new Scene {
      root = roots
    }
  }

  //Main Menu
  def showMainMenu() = {
    val resource = getClass.getResource("view/MainMenu.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  } 

  //Card Wallet
  def showCardOverview() = {
    val resource = getClass.getResource("view/CardWallet.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  } 

  //Login
  def showLogin() = {
    val resource = getClass.getResource("view/Login.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
    //this.roots.hide()
  } 

  //Display login page when the program runs
  showLogin()

  //Card Edit Dialog
  def showCardEditDialog(card: Card): Boolean = {
    val resource = getClass.getResource("view/CardEditDialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[CardEditDialog#Controller]

    val dialog = new Stage() {
      title = "Cookie! Card Edit Dialog"
      icons += new Image(getClass.getResourceAsStream("images/cookie_icon.png"))
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    
    control.dialogStage = dialog
    control.card = card
    dialog.showAndWait()
    control.okClicked
  } 

  //Money Tracker
  def showMoneyTracker() = {
    val resource = getClass.getResource("view/MoneyTracker.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  } 

  //Income Edit Dialog
  def showIncomeEditDialog(income: Income): Boolean = {
    val resource = getClass.getResource("view/IncomeEditDialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[IncomeEditDialog#Controller]

    val dialog = new Stage() {
      title = "Cookie! Income Edit Dialog"
      icons += new Image(getClass.getResourceAsStream("images/cookie_icon.png"))
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    
    control.dialogStage = dialog
    control.income = income
    dialog.showAndWait()
    control.okClicked
  } 

   //Expend Edit Dialog
  def showExpendEditDialog(expend: Expend): Boolean = {
    val resource = getClass.getResource("view/ExpendEditDialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[ExpendEditDialog#Controller]

    val dialog = new Stage() {
      title = "Cookie! Expend Edit Dialog"
      icons += new Image(getClass.getResourceAsStream("images/cookie_icon.png"))
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    
    control.dialogStage = dialog
    control.expend = expend
    dialog.showAndWait()
    control.okClicked
  } 

}
