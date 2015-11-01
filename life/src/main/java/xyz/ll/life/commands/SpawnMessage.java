package xyz.ll.life.commands;

import xyz.ll.life.Game;
import xyz.ll.life.model.Food;

public class SpawnMessage implements Message {

    @Override
    public boolean matches(String command) {
        return command.startsWith("spawn ");
    }

    @Override
    public String exec(Game game) {
        game.add(Food.randomFood(game.getDimension()));
        return "Spawned food.";
    }

}
