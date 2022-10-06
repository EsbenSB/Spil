package client.updated.gui.utils;

import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public abstract class ErrorHandler {
  private static final PseudoClass errorClass = PseudoClass.getPseudoClass("error");

  public static void addError(TextField field) {
    field.pseudoClassStateChanged(errorClass, true);
  }

  public static void addError(Label label) {
    label.pseudoClassStateChanged(errorClass, true);
  }

  public static void removeError(TextField field) {
    field.pseudoClassStateChanged(errorClass, false);
  }

  public static boolean hasError(TextField field) {
    return field.getPseudoClassStates().contains(errorClass);
  }
}
