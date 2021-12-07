package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        //UI part
        // Text area (text part of content) e.g. "Number received from client ot check prime number is: #"
        TextArea ta = new TextArea();
        // Create a scene (has all content); width: 450, height: 200
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        //set a title of the window.
        primaryStage.setTitle("Server");
        //set all of content on the window.
        primaryStage.setScene(scene);

        //Display an window.
        primaryStage.show();
        //end UI part

        //Make a connection with client.
        new Thread( () -> {
            try {
                // Create a connection with client using port number 8000 (the same as client port)
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() ->
                        ta.appendText("Server started at " + new Date() + '\n'));

                // Listen for a connection request
                Socket socket = serverSocket.accept();

                //Send data to client.
                DataOutputStream sendClient = new DataOutputStream(socket.getOutputStream());
                //Receive data from client.
                DataInputStream receiveClient = new DataInputStream(socket.getInputStream());


                while (true) {
                    // Receive a number from the client
                    int number = receiveClient.readInt();
                    System.out.println("Number received at this point: " + number);

                    //check whether or not the number is prime number
                    boolean isPrime = checkPrime(number);

                    // Send the result (is prime or not) back to the client
                    sendClient.writeBoolean(isPrime);

                    //Display on text area
                    Platform.runLater(() -> {
                        ta.appendText("Number received from client ot check prime number is: " + number + '\n');
                    });
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //Check whether or not the number is prime
    public static boolean checkPrime(int number) {
        //1 is not a prime number
        if (number == 1) {
            return false;
        }

        for(int i = 2; i < number; i++) {
            //if the number is divisible by other number, it's not a prime.
            if(number % i == 0) {
                return false;
            }
        }
        return true;
    }//end isPrime

    public static void main(String[] args) {
        launch(args);
    }
}
