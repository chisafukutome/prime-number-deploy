package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Application {
    //variable declarations
    DataInputStream receiveServer = null;
    DataOutputStream sendServer = null;
    @Override
    public void start(Stage primaryStage) throws Exception{
        //UI Part
        // A hold for the text area.
        BorderPane textPane = new BorderPane();
        //the location for the text area
        textPane.setPadding(new Insets(5, 5, 5, 5));
        //Border color for the text area
        textPane.setStyle("-fx-border-color: green");
        //prompt for the input
        textPane.setLeft(new Label("Enter a number to check prime: "));

        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        textPane.setCenter(tf);

        //Main area
        BorderPane mainPane = new BorderPane();
        // Text area to display contents
        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(textPane);

        // Create a scene (has all content); width: 450, height: 200
        Scene scene = new Scene(mainPane, 450, 200);
        //set a title of the window.
        primaryStage.setTitle("Client");
        //set all of content on the window.
        primaryStage.setScene(scene);

        //Display an window.
        primaryStage.show();
        //end UI part

        tf.setOnAction(e -> {
            try {
                // Get the number to check whether or not it's a prime number.
                int number = Integer.parseInt(tf.getText().trim());

                // Send the user input (number) to the server
                sendServer.writeInt(number);
                sendServer.flush();

                // Get true or false (prime number or not)
                boolean isPrime = receiveServer.readBoolean();

                // Display to the text area
                ta.appendText("number is " + number + "\n");

                //if the number is prime number
                if (isPrime) {
                    ta.appendText(number + " is prime." + '\n');
                } else {
                    //if not
                    ta.appendText(number + " is not prime." + '\n');
                }

            }
            catch (IOException ex) {
                System.err.println(ex);
            }
        });

        //Connection between server and client.
        try {
            // Make a connection with server side using port number 8000.
            Socket socket = new Socket("localhost", 8000);

            // Receive data from the server.
            receiveServer = new DataInputStream(socket.getInputStream());
            // Send data to the server.
            sendServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
