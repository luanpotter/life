package xyz.ll.life.commands;

import java.util.Arrays;

import javafx.application.Platform;
import xyz.ll.life.Game;

public class ExitMessage implements Message {

    @Override
    public boolean matches(String command) {
        return Arrays.asList("quit", "exit").contains(command);
    }

    @Override
    public String exec(Game game) {
        Platform.exit();
        return "Exiting...";
    }

}
