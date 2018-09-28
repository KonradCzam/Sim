package com.example.Sim;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Config.SimConfig;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


@EnableConfigurationProperties
public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(Main::showError);


        ApplicationContext context = new AnnotationConfigApplicationContext(SimConfig.class);
        ScreensConfiguration screens = context.getBean(ScreensConfiguration.class);


        screens.setPrimaryStage(stage);
        screens.startDialog().show();

    }

    private static void showError(Thread t, Throwable e) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        String filename = sdf.format(cal.getTime())+".txt";

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filename, "UTF-8");
            writer.println(e.getClass() + ": " + e.getMessage());
            for (int i = 0; i < e.getStackTrace().length; i++) {
                writer.println(e.getStackTrace()[i].toString());
            }

        } catch (FileNotFoundException | UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }finally {
            if(writer!= null)
                writer.close();
        }
        if (Platform.isFxApplicationThread()) {
            showErrorDialog(e);
        } else {
            System.err.println("An unexpected error occurred in " + t);

        }
    }

    private static void showErrorDialog(Throwable e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getMessage() + "\n\n" + e.getStackTrace().toString());
        alert.showAndWait();
        e.printStackTrace();
    }
}

