package xyz.ll.life.model.genetics;

import javafx.scene.paint.Color;
import xyz.ll.life.model.EntityShape;

/**
 * Created by lucas-cleto on 10/27/15.
 */
public class ColorGene implements Gene<ColorGene> {

    private static final Mutation HUE = Mutation.helper().min(0d).max(2 * Math.PI).variance(Math.PI / 20d).build();
    private static final Mutation SATURATION = Mutation.helper().min(0d).max(1d).variance(0.00001d).build();
    private static final Mutation BRIGHTNESS = Mutation.helper().min(0d).max(1d).variance(0.00001d).build();

    private double hue;
    private double saturation;
    private double brightness;

    private ColorGene(double hue, double saturation, double brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public ColorGene() {
        this.hue = Math.PI;
        this.saturation = 1d;
        this.brightness = 1d;
    }

    private Color getColor() {
        return Color.hsb(Math.toDegrees(hue), saturation, brightness);
    }

    public void dye(EntityShape body) {
        body.setColor(getColor());
    }

    @Override
    public void mutation() {
        this.hue = HUE.mutate(this.hue);
        this.saturation = SATURATION.mutate(this.saturation);
        this.brightness = BRIGHTNESS.mutate(this.brightness);
    }

    @Override
    public ColorGene meiosis(ColorGene gene) {
        double hue = Util.random(this.hue, gene.hue);
        double saturation = Util.random(this.saturation, gene.saturation);
        double brightness = Util.random(this.brightness, gene.brightness);
        ColorGene childGene = new ColorGene(hue, saturation, brightness);
        childGene.mutation();

        return childGene;
    }

    @Override
    public double distance(ColorGene gene) {
        double hueDistance = Math.abs(this.hue - gene.hue) / HUE.range();
        double saturationDistance = Math.abs(this.saturation - gene.saturation) / SATURATION.range();
        double briightnessDistance = Math.abs(this.brightness - gene.brightness) / BRIGHTNESS.range();
        return hueDistance + saturationDistance + briightnessDistance;
    }
}
