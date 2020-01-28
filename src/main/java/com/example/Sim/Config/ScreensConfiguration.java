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
import com.example.Sim.controllers.StartController;
import com.example.Sim.controllers.SummaryController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.context.annotation.*;

@Configuration
@Lazy
@PropertySource("application.properties")
@EnableAspectJAutoProxy
public class ScreensConfiguration {
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showScreen(Parent screen) {
        primaryStage.setScene(new Scene(screen, 1000, 800));
        primaryStage.show();
    }
    @Bean
    StartController startController() {
        return new StartController(this);
    }
    @Bean
    public FXMLDialog startDialog() {
        return new FXMLDialog(startController(), getClass().getClassLoader().getResource("start.fxml"), primaryStage, StageStyle.DECORATED);
    }

    @Bean
    SummaryController summaryController() {
        return new SummaryController(this);
    }
    @Bean
    public FXMLDialog summaryDialog() {
        return new FXMLDialog(summaryController(), getClass().getClassLoader().getResource("summary.fxml"), primaryStage, StageStyle.DECORATED);
    }


}