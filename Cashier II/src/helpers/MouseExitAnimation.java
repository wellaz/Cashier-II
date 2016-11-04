/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import javafx.animation.TranslateTransitionBuilder;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("deprecation")
public class MouseExitAnimation implements EventHandler<MouseEvent> {
	@Override
	public void handle(MouseEvent event) {
		TranslateTransitionBuilder.create().toX(0).toY(0).duration(new Duration(500)).node((Node) event.getSource())
				.build().play();
	}
}
