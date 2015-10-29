package xyz.luan.life.model.genetics;

import javafx.geometry.Point2D;
import xyz.luan.life.model.EntityShape;

/**
 * Created by lucas-cleto on 10/28/15.
 */
public class MorfologicGene implements Gene<MorfologicGene> {

    private static final int NUMBER_OF_CHARACTERISTICS = 12;
    private static final int MORFOLOGIC_PRECISION = 100;
    private static final double ARC = 2 * Math.PI / (double) MORFOLOGIC_PRECISION;
    private static final double CHARACTERISTIC_VARIANCE = 0.1;

    private double[] characteristics;

    private void init() {
        this.characteristics = new double[MorfologicGene.NUMBER_OF_CHARACTERISTICS];
        for (int i = 0; i < MorfologicGene.NUMBER_OF_CHARACTERISTICS; i++) {
            this.characteristics[i] = 0;
        }
        this.characteristics[0] = 12;
        this.characteristics[4] = 10;
    }

    private MorfologicGene(double[] characteristics) {
        if (MorfologicGene.NUMBER_OF_CHARACTERISTICS == characteristics.length) {
            this.characteristics = characteristics;
        } else {
            init();
        }
    }

    public MorfologicGene() {
        init();
    }

    private Point2D getPoint(double t) {
        double a = characteristics[0] * Math.pow(Math.sin(t), 2);
        double b = characteristics[1] * Math.pow(Math.sin(characteristics[2] * t), 2) * Math.pow(Math.cos(characteristics[3] * t), 2);
        double c = characteristics[4] * Math.pow(Math.cos(t), 2);
        double d = characteristics[5] * Math.sin(characteristics[6] * t);
        double e = characteristics[7] * Math.sin(characteristics[8] * t) * Math.cos(characteristics[9] * t);
        double f = characteristics[10] * Math.cos(characteristics[11] * t);
        double radius = a + b + c + d + e + f;
        return new Point2D(radius * Math.cos(t), radius * Math.sin(t));
    }

    public void generateShape(EntityShape body) {
        Point2D[] points = new Point2D[MorfologicGene.MORFOLOGIC_PRECISION];
        for (int i = 0; i < MorfologicGene.MORFOLOGIC_PRECISION; i++) {
            Point2D point = getPoint(i * MorfologicGene.ARC);
            points[i] = point;
        }
        body.setVertices(points);
    }

    @Override
    public void mutation() {
        for (int i = 0; i < MorfologicGene.NUMBER_OF_CHARACTERISTICS; i++) {
            this.characteristics[i] += Math.random() * MorfologicGene.CHARACTERISTIC_VARIANCE * (Math.random() > 5 ? 1 : -1);
        }
    }

    @Override
    public MorfologicGene meiosis(MorfologicGene gene) {
        double[] characteristics = new double[MorfologicGene.NUMBER_OF_CHARACTERISTICS];
        for (int i = 0; i < MorfologicGene.NUMBER_OF_CHARACTERISTICS; i++) {
            characteristics[i] = (this.characteristics[i] + gene.characteristics[i]) / 2;
        }
        MorfologicGene childGene = new MorfologicGene(characteristics);
        if (Math.random() < Gene.MUTATION_PROBABILITY) {
            childGene.mutation();
        }
        return childGene;
    }

    @Override
    public double distance(MorfologicGene gene) {
        double distance = 0;
        for (int i = 0; i < MorfologicGene.NUMBER_OF_CHARACTERISTICS; i++) {
            distance += Math.abs(this.characteristics[i] - gene.characteristics[i]);
        }
        return distance;
    }
}
