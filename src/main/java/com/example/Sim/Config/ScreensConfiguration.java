package com.example.Sim.Config;
/*
 * Copyright (c) 2012, Stephen Chin
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL STEPHEN CHIN OR ORACLE CORPORATION BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Utilities.FileUtility;
import com.example.Sim.Gallery.GalleryController;
import com.example.Sim.Utilities.ImageHandler;
import com.example.Sim.Girls.GirlService;
import com.example.Sim.Girls.GirlCreator;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.context.annotation.*;

@Configuration
@Lazy
@PropertySource("application.properties")
public class ScreensConfiguration {
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }
    public void showScreen(Parent screen) {
        primaryStage.setScene(new Scene(screen, 777, 500));
        primaryStage.show();
    }


    @Bean
    @Scope("prototype")
    public FXMLDialog loginDialog() {
        return new FXMLDialog(controller(), getClass().getClassLoader().getResource("gallery.fxml"), primaryStage, StageStyle.DECORATED);
    }

    @Bean
    @Scope("prototype")
    GalleryController controller() {
        return new GalleryController(this);
    }
    @Bean
    @Scope("prototype")
    public ImageHandler imageHandler(){
        return new ImageHandler();
    }
    @Bean
    @Scope("prototype")
    public FileUtility girlOpener(){
        return new FileUtility();
    }
    @Bean
    @Scope("prototype")
    public GirlService girlService(){
        return new GirlService();
    }
    @Bean
    @Scope("prototype")
    public GirlCreator girlCreator(){
        return new GirlCreator();
    }
}