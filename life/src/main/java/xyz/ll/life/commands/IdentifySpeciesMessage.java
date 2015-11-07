package xyz.ll.life.commands;

import java.util.List;
import java.util.stream.Collectors;

import xyz.ll.life.Game;
import xyz.ll.life.model.Species;

public class IdentifySpeciesMessage implements Message {

    @Override
    public boolean matches(String command) {
        return command.equals("identify species");
    }

    @Override
    public String exec(Game game) {
        List<Species> species = game.species();
        String speciesList = species.stream().map(s -> String.valueOf(s.size())).collect(Collectors.joining(", "));
        return "Total: " + species.size() + " | Species: " + speciesList;
    }
}
