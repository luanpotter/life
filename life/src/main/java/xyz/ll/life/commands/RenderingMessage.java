package xyz.ll.life.commands;

import xyz.ll.life.Game;

public class RenderingMessage implements Message {

    @Override
    public boolean matches(String command) {
        return command.startsWith("rendering ");
    }

    @Override
    public String exec(Game game) {
        game.setRendering(!game.isRendering());
        return "Toggled rendering.";
    }

}
