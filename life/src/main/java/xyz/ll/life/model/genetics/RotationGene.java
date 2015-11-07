package xyz.ll.life.model.genetics;

import xyz.ll.life.model.EntityShape;

public class RotationGene implements Gene<RotationGene> {

    private static final Mutation SPEED = Mutation.helper().min(0d).max(.1d).variance(0.05d).build();
    private static final Mutation INCONSTANCY = Mutation.helper().min(0d).max(.1d).variance(0.05d).build();

    private double speed;
    private double inconstancy;

    private RotationGene(double speed, double inconstancy) {
        this.speed = speed;
        this.inconstancy = inconstancy;
    }

    public RotationGene() {
        this.speed = 0.0025;
        this.inconstancy = 0.0075;
    }

    public void initialAngularVelocity(EntityShape body) {
        body.setAngleAcc(this.speed);
    }

    public void rotate(EntityShape body) {
        double speed = body.getAngleAcc() * acceleration(body.getAngleAcc());
        body.setAngleAcc(speed);
        body.setVelocity(Util.rotate(body.getVelocity(), speed));
    }

    private double acceleration(double theta) {
        return Math.abs(speed / theta) + (2 * Math.random() - 1) * inconstancy;
    }

    @Override
    public void mutation() {
        this.speed = SPEED.mutate(this.speed);
        this.inconstancy = INCONSTANCY.mutate(this.inconstancy);
    }

    @Override
    public RotationGene meiosis(RotationGene gene) {
        double speed = Util.random(this.speed, gene.speed);
        double constancy = Util.random(this.inconstancy, gene.inconstancy);
        RotationGene childGene = new RotationGene(speed, constancy);
        childGene.mutation();

        return childGene;
    }

    @Override
    public double distance(RotationGene gene) {
        double speedDistance = Math.abs(this.speed - gene.speed) / SPEED.range();
        double inconstancyDistance = Math.abs(this.inconstancy - gene.inconstancy) / INCONSTANCY.range();
        return speedDistance + inconstancyDistance;
    }
}
