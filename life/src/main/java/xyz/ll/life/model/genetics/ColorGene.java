package xyz.ll.life.model.genetics;

import javafx.scene.paint.Color;
import xyz.ll.life.model.EntityShape;

/**
 * Created by lucas-cleto on 10/27/15.
 */
public class ColorGene implements Gene<ColorGene> {

    private static final double HUE_MAX = 2 * Math.PI, HUE_MIN = 0d, HUE_VARIANCE = Math.PI / 10d;
    private static final double SATURATION_MAX = 1d, SATURATION_MIN = 0d, SATURATION_VARIANCE = 0.001d;
    private static final double BRIGHTNESS_MAX = 1d, BRIGHTNESS_MIN = 0d, BRIGHTNESS_VARIANCE = 0.001d;

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
        this.hue += Math.random() * ColorGene.HUE_VARIANCE * (Math.random() > .5 ? 1 : -1);
        this.saturation += Math.random() * ColorGene.SATURATION_VARIANCE * (Math.random() > .5 ? 1 : -1);
        this.brightness += Math.random() * ColorGene.BRIGHTNESS_VARIANCE * (Math.random() > .5 ? 1 : -1);

        if (this.hue < ColorGene.HUE_MIN) {
            this.hue = 2 * ColorGene.HUE_MIN - this.hue;
        }
        if (this.hue > ColorGene.HUE_MAX) {
            this.hue = 2 * ColorGene.HUE_MAX - this.hue;
        }
        if (this.saturation < ColorGene.SATURATION_MIN) {
            this.saturation = 2 * ColorGene.SATURATION_MIN - this.saturation;
        }
        if (this.saturation > ColorGene.SATURATION_MAX) {
            this.saturation = 2 * ColorGene.SATURATION_MAX - this.saturation;
        }
        if (this.brightness < ColorGene.BRIGHTNESS_MIN) {
            this.brightness = 2 * ColorGene.BRIGHTNESS_MIN - this.brightness;
        }
        if (this.brightness > ColorGene.BRIGHTNESS_MAX) {
            this.brightness = 2 * ColorGene.BRIGHTNESS_MAX - this.brightness;
        }
    }

    @Override
    public ColorGene meiosis(ColorGene gene) {
        double hue = (this.hue + gene.hue) / 2;
        double saturation = (this.saturation + gene.saturation) / 2;
        double brightness = (this.brightness + gene.brightness) / 2;
        ColorGene childGene = new ColorGene(hue, saturation, brightness);
        if (Math.random() < MUTATION_PROBABILITY) {
            childGene.mutation();
        }
        return childGene;
    }

    @Override
    public double distance(ColorGene gene) {
        double fh = ColorGene.HUE_MAX - ColorGene.HUE_MIN;
        double fs = ColorGene.SATURATION_MAX - ColorGene.SATURATION_MIN;
        double fb = ColorGene.BRIGHTNESS_MAX - ColorGene.BRIGHTNESS_MIN;
        return Math.abs(this.hue - gene.hue) / fh + Math.abs(this.saturation - gene.saturation) / fs + Math.abs(this.brightness - gene.brightness) / fb;
    }
}
