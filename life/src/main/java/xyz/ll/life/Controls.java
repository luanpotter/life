package xyz.ll.life;

import java.util.ArrayList;
import java.util.List;

import org.fxmisc.richtext.InlineCssTextArea;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controls extends Stage {

    private static final Background DARK_BG = new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));

    private List<String> lines;
    private InlineCssTextArea history;

    public Controls() {
        setTitle("Game of Life - Controls");
        setScene(new Scene(content(), 300, 400));
        lines = new ArrayList<>();
    }

    private Pane content() {
        BorderPane grid = new BorderPane();

        grid.setTop(title());
        grid.setCenter(history());
        grid.setBottom(input());

        return grid;
    }

    private TextField input() {
        TextField input = new TextField();
        input.setBackground(DARK_BG);
        input.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String command = input.getText().trim();
                if (!command.isEmpty()) {
                    execute(command);
                    input.setText("");
                }
            }
        });
        return input;
    }

    private void execute(String text) {
        appendLine("> " + text);
        history.setStyle(lines.size() - 1, "-fx-fill: gray;");
        String response = "Command '" + text + "' not found. ";
        appendLine("< " + response);
        history.setStyle(lines.size() - 1, "-fx-fill: red;");
    }

    private void appendLine(String line) {
        lines.add(line);
        history.appendText(line + '\n');
    }

    private InlineCssTextArea history() {
        history = new InlineCssTextArea();
        history.setFocusTraversable(false);
        history.setBackground(DARK_BG);
        history.setEditable(false);
        return history;
    }

    private Text title() {
        Text scenetitle = new Text("Control Panel");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        return scenetitle;
    }
}
