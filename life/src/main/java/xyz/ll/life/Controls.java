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
import xyz.ll.life.commands.Messages;

public class Controls extends Stage {

    private static final Background DARK_BG = new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));

    private int currentIndex = 0;
    private int currentLine = 0;
    private String currentText = "";
    private List<String> inputs;
    private InlineCssTextArea history;

    private Game game;

    public Controls(Game game) {
        setTitle("Game of Life - Controls");
        setScene(new Scene(content(), 300, 400));
        this.inputs = new ArrayList<>();
        this.game = game;
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
            String command = input.getText().trim();
            if (e.getCode() == KeyCode.ENTER) {
                if (!command.isEmpty()) {
                    execute(command);
                    input.setText("");
                }
                currentIndex = 0;
            } else if (e.getCode() == KeyCode.UP) {
                cacheCurrentText(command);
                currentIndex++;
                readHistory(input);
            } else if (e.getCode() == KeyCode.DOWN) {
                cacheCurrentText(command);
                currentIndex--;
                readHistory(input);
            } else {
                currentIndex = 0;
            }
        });
        return input;
    }

    private void cacheCurrentText(String command) {
        if (currentIndex == 0) {
            currentText = command;
        }
    }

    private void readHistory(TextField input) {
        if (currentIndex > 0 && currentIndex <= inputs.size()) {
            input.setText(inputs.get(inputs.size() - currentIndex));
        } else if (currentIndex == 0) {
            currentIndex = 0;
            input.setText(currentText);
        } else {
            currentIndex = currentIndex <= 0 ? 0 : inputs.size();
        }
    }

    private void execute(String text) {
        input(text);
        output(Messages.exec(game, text));
    }

    private void output(String response) {
        appendLine("< " + response);
        history.setStyle(currentLine, "-fx-fill: red;");
        currentLine++;
    }

    private void input(String text) {
        inputs.add(text);
        appendLine("> " + text);
        history.setStyle(currentLine, "-fx-fill: gray;");
        currentLine++;
    }

    private void appendLine(String line) {
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
