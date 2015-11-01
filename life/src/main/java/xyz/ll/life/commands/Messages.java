package xyz.ll.life.commands;

import java.util.Arrays;
import java.util.List;

import xyz.ll.life.Game;

public class Messages {

    private static List<Message> messages = Arrays.asList(new ExitMessage());

    public static String exec(Game game, String command) {
        for (Message message : messages) {
            if (message.matches(command)) {
                return message.exec(game);
            }
        }
        return "Command '" + command + "' not found.";
    }
}
