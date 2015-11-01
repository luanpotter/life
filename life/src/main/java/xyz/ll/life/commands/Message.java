package xyz.ll.life.commands;

import xyz.ll.life.Game;

public interface Message {

    public boolean matches(String command);

    public String exec(Game game);
}
