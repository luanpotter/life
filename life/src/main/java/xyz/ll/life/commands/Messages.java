package xyz.ll.life.commands;

import java.util.ArrayList;
import java.util.List;

import org.reflections.Reflections;

import xyz.ll.life.Game;

public class Messages {

    private static List<Message> messages;

    static {
        messages = new ArrayList<>();
        Reflections r = new Reflections(Messages.class.getPackage().getName());
        for (Class<? extends Message> clazz : r.getSubTypesOf(Message.class)) {
            try {
                messages.add(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                String message = "Every Message impl must have a default no-args public exception-less constructor. Check ";
                throw new RuntimeException(message + clazz.getCanonicalName());
            }
        }
    }

    public static String exec(Game game, String command) {
        for (Message message : messages) {
            if (message.matches(command)) {
                return message.exec(game);
            }
        }
        return "Command '" + command + "' not found.";
    }
}
