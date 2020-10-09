package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.Achievement;
import com.example.Sim.Services.AchievementService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Service
public class AchievementsController  {

    public AchievementsController() {
    }

    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    @FXML
    private GridPane AchievmentGrid;
    @Resource
    private AchievementService achievementService;

    public AchievementsController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }


    public void initialize(URL location, ResourceBundle resources) {
        initGrid();
    }
    public void initGrid() {
        Map<String,Achievement> achievementMap = achievementService.getAchievementList();
        List<Achievement> achievementList = new ArrayList<Achievement>(achievementMap.values());

        Integer index = 0;
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 9; column++) {
                if (index < achievementList.size()) {
                    Boolean finished = achievementList.get(index).getFinished();
                    String imagePath;
                    Achievement curr_achievment = achievementList.get(index);
                    if (finished)
                        imagePath  = curr_achievment.getPathFinished();
                    else
                        imagePath = curr_achievment.getPathUnFinished();

                    Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    ImageView imageView = new ImageView(image);

                    Tooltip tooltip = new Tooltip(curr_achievment.getTooltip());
                    Tooltip.install(imageView,tooltip);
                    imageView.setFitHeight(120);
                    imageView.setFitWidth(120);

                    AchievmentGrid.add(imageView, column, row);
                    index++;
                } else {
                    String imagePath = "/UI/AchievmentNo.png";
                    Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(120);
                    imageView.setFitWidth(120);
                    AchievmentGrid.add(imageView, column, row);
                }
            }
        }
    }
    public void goToStart(){
        dialog.close();
       // screens.startDialog().show();
    }
}
