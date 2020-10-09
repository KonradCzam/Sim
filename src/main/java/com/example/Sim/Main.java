package com.example.Sim;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Config.SimConfig;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


@EnableConfigurationProperties
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static void showError(Thread t, Throwable e) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        String filename = sdf.format(cal.getTime()) + ".txt";

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filename, "UTF-8");
            writer.println(e.getClass() + ": " + e.getMessage());
            for (int i = 0; i < e.getStackTrace().length; i++) {
                writer.println(e.getStackTrace()[i].toString());

            }
            writer.println(e.getCause().getClass() + ": " + e.getCause().getMessage());
            for (int i = 0; i < e.getCause().getStackTrace().length; i++) {
                writer.println(e.getCause().getStackTrace()[i].toString());

            }

        } catch (FileNotFoundException | UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } finally {
            if (writer != null)
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

    @Override
    public void start(Stage stage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(Main::showError);


        ApplicationContext context = new AnnotationConfigApplicationContext(SimConfig.class);
        Pane startPane = (Pane)context.getBean("startPane");
        Scene scene = new Scene(startPane);
        context.getBean(ScreensConfiguration.class).setPrimaryStage(stage);
        stage.setScene(scene);
        stage.show();

    }
}

