package com.xubingbing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    private final static Logger LOG = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("靓号助手");
        Parent root = FXMLLoader.load(getClass().getResource("/app.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/scan.ico")));
        primaryStage.show();
    }

    public static void main(String[] args){
        try {
            launch(args);
            LOG.error("afdsafsaffdasf");
            throw new RuntimeException();
        } catch(Exception e) {
        }
    }


}
