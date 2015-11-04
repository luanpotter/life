package xyz.ll.life.commands;

import xyz.ll.life.Game;
import xyz.ll.life.model.Food;
import xyz.ll.life.model.Specie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IdentifySpeciesMessage implements Message {

    @Override
    public boolean matches(String command) {
        return command.equals("identify species");
    }

    @Override
    public String exec(Game game) {
        String buffer = "";
        List<Specie> species = new ArrayList<>();
        int n = game.numberOfSpecies(species);
        buffer += "Number of species: " + n + " Species: ";
        buffer += species.stream().map(s -> s.toString()).collect(Collectors.joining(", "));
        return buffer;
    }
}
