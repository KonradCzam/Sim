package com.example.Sim;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Config.SimConfig;
import javafx.application.Application;

import javafx.stage.Stage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@EnableConfigurationProperties
public class Main extends Application {


        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage stage) throws Exception {
            ApplicationContext context = new AnnotationConfigApplicationContext(SimConfig.class);
            ScreensConfiguration screens = context.getBean(ScreensConfiguration.class);


            screens.setPrimaryStage(stage);
            screens.loginDialog().show();
        }
    }
