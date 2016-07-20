package xyz.ll.life.model.genetics;

import xyz.ll.life.model.EntityShape;
import xyz.luan.geometry.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas-cleto on 10/28/15.
 */
public class MorfologicGene implements Gene<MorfologicGene> {

    private static final int NUMBER_OF_CHARACTERISTICS = 12;
    private static final int MORFOLOGIC_PRECISION = 100;
    private static final double ARC = 2 * Math.PI / (double) MORFOLOGIC_PRECISION;

    private static final double CHARACTERISTIC_WEIGHT = 0.5d, CHARACTERISTIC_VARIANCE = 0.1d;

    private double[] characteristics;

    private void init(double size) {
        this.characteristics = new double[MorfologicGene.NUMBER_OF_CHARACTERISTICS];
        for (int i = 0; i < MorfologicGene.NUMBER_OF_CHARACTERISTICS; i++) {
            this.characteristics[i] = 0d;
        }

        this.characteristics[0] = size;
        this.characteristics[1] = 1d;
        this.characteristics[2] = 0d;
        this.characteristics[3] = 0d;
        this.characteristics[4] = size;
        this.characteristics[5] = 1d;
        this.characteristics[6] = 0d;
        this.characteristics[7] = 1d;
        this.characteristics[8] = 0d;
        this.characteristics[9] = 0d;
        this.characteristics[10] = 3d;
        this.characteristics[11] = 2d;
    }

    private MorfologicGene(double[] characteristics) {
        if (MorfologicGene.NUMBER_OF_CHARACTERISTICS == characteristics.length) {
            this.characteristics = characteristics;
        } else {
            init(4d);
        }
    }

    public MorfologicGene(double size) {
        init(size);
    }

    private Point getPoint(double t) {
        double a = characteristics[0] * 3 * Math.pow(Math.sin(t), 2);
        double b = characteristics[1] * Math.pow(Math.sin(characteristics[2] * t), 2) * Math.pow(Math.cos(characteristics[3] * t), 2);
        double c = characteristics[4] * 3 * Math.pow(Math.cos(t), 2);
        double d = characteristics[5] * Math.sin(characteristics[6] * 10 * t);
        double e = characteristics[7] * Math.sin(characteristics[8] * t) * Math.cos(characteristics[9] * t);
        double f = characteristics[10] * Math.cos(characteristics[11] * 10 * t);
        double radius = a + b + c + d + e + f;
        return new Point(radius * Math.cos(t), radius * Math.sin(t));
    }

    public EntityShape generateShape(Point center) {
        List<Point> vertices = new ArrayList<>();
        for (int i = 0; i < MorfologicGene.MORFOLOGIC_PRECISION; i++) {
            vertices.add(getPoint(i * MorfologicGene.ARC));
        }
        return new EntityShape(center, vertices);
    }

    @Override
    public void mutation() {
        for (int i = 0; i < MorfologicGene.NUMBER_OF_CHARACTERISTICS; i++) {
            if (Math.random() < MUTATION_PROBABILITY) {
                this.characteristics[i] += Math.random() * MorfologicGene.CHARACTERISTIC_VARIANCE * (Math.random() > 5 ? 1 : -1);
            }
        }
    }

    @Override
    public MorfologicGene meiosis(MorfologicGene gene) {
        double[] characteristics = new double[MorfologicGene.NUMBER_OF_CHARACTERISTICS];
        for (int i = 0; i < MorfologicGene.NUMBER_OF_CHARACTERISTICS; i++) {
            characteristics[i] = Util.random(this.characteristics[i], gene.characteristics[i]);
        }
        MorfologicGene childGene = new MorfologicGene(characteristics);
        childGene.mutation();

        return childGene;
    }

    @Override
    public double distance(MorfologicGene gene) {
        double distance = 0;
        for (int i = 0; i < MorfologicGene.NUMBER_OF_CHARACTERISTICS; i++) {
            double fc = 1 / MorfologicGene.CHARACTERISTIC_WEIGHT;
            distance += Math.abs(this.characteristics[i] - gene.characteristics[i]) / fc;
        }
        return distance;
    }

    @Override
    public List<Double> getValues() {
        List<Double> values = new ArrayList<>();
        for (double c : characteristics) {
            c /= 10;
            values.add(c < 0 ? 0 : c > 1 ? 1 : c);
            // /\ /\ TODO normalize! /\ /\
        }
        return values;
    }
}
