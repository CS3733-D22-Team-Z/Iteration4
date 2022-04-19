package edu.wpi.cs3733.D22.teamZ.entity;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class ScrollListener {

  private BooleanProperty scrolling;

  public ScrollListener(Node observableNode) {
    scrolling = new ReadOnlyBooleanWrapper(false);

    observableNode.addEventHandler(MouseEvent.DRAG_DETECTED, e -> scrolling.set(true));

    observableNode.addEventFilter(
        MouseEvent.MOUSE_CLICKED,
        evt -> {
          if (scrolling.get()) {
            scrolling.set(false);
            evt.consume();
          }
        });

    observableNode.addEventHandler(MouseEvent.MOUSE_EXITED, e -> scrolling.set(false));
  }

  public ReadOnlyBooleanProperty scrollingProperty() {
    return scrolling;
  }

  public boolean isScrolling() {
    return scrolling.get();
  }
}
