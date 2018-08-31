package com.example.Sim.screens.girlDetails;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ResourceBundle;

@Service
public class GirlDetailsController implements Initializable, DialogController {
    @FXML
    private GridPane EqGrid;

    @FXML
    private GridPane PlayerEqGrid;

    @FXML
    private GridPane GirlEqGrid;

    @FXML
    private ImageView dragable;

    @FXML
    private ImageView dragable1;


    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;


    public GirlDetailsController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setDragable(dragable);
        setDetectors();
        setDetector(dragable1);
    }

    public void setDetectors(){
        EqGrid.getChildren().stream().forEach(imageview -> setDetector((ImageView)imageview));
        PlayerEqGrid.getChildren().stream().forEach(imageview -> setDetector((ImageView)imageview));
        GirlEqGrid.getChildren().stream().forEach(imageview -> setDetector((ImageView)imageview));
    }
    public void setDetector(ImageView image){
        image.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
            public void handle(MouseDragEvent event) {
                ((ImageView) (event.getPickResult().getIntersectedNode())).setImage(((ImageView)event.getGestureSource()).getImage());
            }
        });
    }
    public void setDragable(ImageView image) {
        image.setOnDragDetected(new EventHandler <MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
                image.startFullDrag();
            }
        });
        image.setOnMousePressed(onMousePressedEventHandler);
        image.setOnMouseDragged(onMouseDraggedEventHandler);
        image.setOnMouseReleased(onMouseReleasedEventHanlder);
    }


    EventHandler<MouseEvent> onMouseReleasedEventHanlder =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    ((ImageView) (t.getSource())).setMouseTransparent(false);
                }
            };
    EventHandler<MouseEvent> onMousePressedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((ImageView) (t.getSource())).getTranslateX();
                    orgTranslateY = ((ImageView) (t.getSource())).getTranslateY();
                    ((ImageView) (t.getSource())).setMouseTransparent(true);
                    t.setDragDetect(true);
                }
            };

    EventHandler<MouseEvent> onMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    ((ImageView) (t.getSource())).setTranslateX(newTranslateX);
                    ((ImageView) (t.getSource())).setTranslateY(newTranslateY);
                }
            };

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }




}
