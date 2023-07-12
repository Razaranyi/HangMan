package com.example.q2_mmn13;
/*This class implements hang man game user interface and graphics
The user will be able to guess the next letter of a given word using the buttons.
With each correct guess the button will turn green and the letter will be written in its places in the word
with each wrong guess the button wil; turn red and another body part if the hang man will be drawn
*/

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

import java.util.Objects;
import java.util.Optional;

public class Hang_manController {

    @FXML
    private Canvas canvas;

    @FXML
    private GridPane grid;
    @FXML
    private Label lbl;
    private Button[] btns;
    private GraphicsContext gc;
    private GameLogic gl;
    private String word;

    final int SIZE = 26;
    final int ROWS = 2;
    final int COLUMNS = 13;


    public void initialize() {//initialize graphics
        gl = new GameLogic();
        gc = canvas.getGraphicsContext2D();
        startNewGame();
    }

    private void startNewGame() {//setting game graphics
        if (Objects.equals(gl.getPickedWord(), "-1")) {
            errorWithFile();
        } else {
            word = gl.getRedactedWord();
            generateBtns();
            lbl.setContentDisplay(ContentDisplay.CENTER);
            lbl.setText(word);
        }

    }

    private void generateBtns() {//generates buttons in their place and call handleBynAction func to set functionality
        btns = new Button[SIZE];
        for (int i = 0; i < SIZE; i++) {
            btns[i] = new Button(Character.toString('a' + i));
            btns[i].setPrefSize(grid.getPrefWidth() / COLUMNS, grid.getPrefHeight() / ROWS);
            grid.add(btns[i], i % COLUMNS, i / COLUMNS);
            btns[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    handleBtnAction(actionEvent);
                }
            });
        }
    }

    private void handleBtnAction(ActionEvent event) { // implements buttons functionality in case of wrong/right guess according to the game logic class
        Button tmp = (Button) event.getSource();
        tmp.setDisable(true);
        tmp.setWrapText(true);
        String s = tmp.getText();
        char label = s.charAt(0);
        if (gl.isGoodGuess(label)) {
            word = gl.computeNext(label);
            lbl.setText(word);
            tmp.setTextFill(Paint.valueOf("GREEN"));

            if (gl.isGameWon()) {
                gameWon();
            }
        } else {
            tmp.setTextFill(Paint.valueOf("RED"));

            drawWrongGuess(gl.getNumberOfWrongGuess());
            if (gl.isGameOver()) {
                gameOver();
            }

        }
    }

    private void gameWon() {//change text to "well done" and execute newGame func with the matching message content
        String s = "The word was: " + gl.getPickedWord() + "\nWell Done!";
        lbl.setText(s);
        newGame("You Won!");

    }

    private void gameOver() {//change text to "too bad", reveal  the word and execute newGame func with the matching message content
        String s = "Too bad! \n The word was: " + gl.getPickedWord();
        lbl.setText(s);
        newGame("You Lost");
    }


    private void newGame(String msg) { //will let the user start a new game or quit by his choice
        if (gl.isMoreWords()) {
            Alert.AlertType type = Alert.AlertType.CONFIRMATION;
            Alert alert = new Alert(type, "");
            alert.setHeaderText(msg);
            alert.setTitle("New game");
            alert.getDialogPane().setContentText("Would you like to play again?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                gl.restart();
                gc.clearRect(0, 0, 500, 500);
                startNewGame();
            }
            if (result.get() == ButtonType.CANCEL) {
                Platform.exit();
            }
        }
    }

    private void errorWithFile() {//print error message in case of file error
        Alert.AlertType type = Alert.AlertType.ERROR;
        Alert alert = new Alert(type, "");
        alert.setHeaderText("Error with finding file");
        alert.setTitle("Error");
        alert.showAndWait();

    }

    private void drawWrongGuess(int wrongGuessNum) {//draw hang man body part by the number of giver error
        switch (wrongGuessNum) {
            case 1:
                gc.strokeLine(200, 50, 200, 350); // draw a gallows
                gc.strokeLine(200, 50, 300, 50);
                gc.strokeLine(300, 50, 300, 100);
                break;
            case 2:
                gc.strokeOval(278, 150 - 50, 50, 50); //draw head

                break;
            case 3:
                gc.strokeLine(300, 150, 300, 200); //draw body

                break;
            case 4:
                gc.strokeLine(300, 200, 275, 225);//draw legs
                gc.strokeLine(300, 200, 325, 225);
                break;
            case 5:
                gc.strokeLine(300, 175, 275, 155);//draw hands
                gc.strokeLine(300, 175, 325, 155);

                break;
            case 6:
                gc.strokeOval(293, 160 - 50, 5, 5);//draw eyes
                gc.strokeOval(308, 160 - 50, 5, 5);
                break;
            case 7:
                gc.strokeLine(300, 120, 300, 127);//draw nose
                break;
            case 8:
                gc.strokeLine(290, 135, 310, 135);//draw mouth
                break;

            default:


        }
    }


}
