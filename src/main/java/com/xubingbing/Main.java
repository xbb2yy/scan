package com.xubingbing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("靓号助手");
        Parent root = FXMLLoader.load(getClass().getResource("/app.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(new FileInputStream("H:\\workspace\\scan\\src\\main\\resources\\ico.jpg")));
        primaryStage.show();
    }
}
