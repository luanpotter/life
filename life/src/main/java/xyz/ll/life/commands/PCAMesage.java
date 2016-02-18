package xyz.ll.life.commands;

import xyz.ll.life.Game;

/**
 * Created by lucas-cleto on 2/18/16.
 */
public class PCAMesage implements Message {
    @Override
    public boolean matches(String command) {
        return command.equals("pca");
    }

    @Override
    public String exec(Game game) {
        game.getPca().showPhylogeneticsTreeAnalyser();
        return "success";
    }
}
