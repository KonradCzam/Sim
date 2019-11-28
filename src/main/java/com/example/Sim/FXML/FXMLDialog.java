// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.FXML;

import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.util.Callback;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import java.net.URL;
import javafx.stage.Stage;

public class FXMLDialog extends Stage
{
    public FXMLDialog(final DialogController controller, final URL fxml, final Window owner) {
        this(controller, fxml, owner, StageStyle.DECORATED);
    }
    
    public FXMLDialog(final DialogController controller, final URL fxml, final Window owner, final StageStyle style) {
        super(style);
        this.setResizable(false);
        this.initOwner(owner);
        this.initModality(Modality.WINDOW_MODAL);
        final FXMLLoader loader = new FXMLLoader(fxml);
        try {
            loader.setControllerFactory((Callback)new FXMLDialog$1(this, controller));
            controller.setDialog(this);
            final Scene scene = new Scene((Parent)loader.load());
            scene.getStylesheets().add((Object)this.getClass().getClassLoader().getResource("main.css").toExternalForm());
            this.setScene(scene);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
