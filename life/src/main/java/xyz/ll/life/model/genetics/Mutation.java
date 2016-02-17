package xyz.ll.life.model.genetics;

public class Mutation {

    private double min;
    private double max;
    private double variance;

    public static Builder helper() {
        return new Mutation().new Builder();
    }

    public class Builder {

        public Builder min(double min) {
            Mutation.this.min = min;
            return this;
        }

        public Builder max(double max) {
            Mutation.this.max = max;
            return this;
        }

        public Builder variance(double variance) {
            Mutation.this.variance = variance;
            return this;
        }

        public Mutation build() {
            return Mutation.this;
        }
    }

    public double mutate(double value) {
        if (Math.random() < Gene.MUTATION_PROBABILITY) {
            value += Math.random() * variance * (Math.random() > 0.5 ? 1 : -1);
            if (value < min) {
                value = 2 * min - value;
            }
            if (value > max) {
                value = 2 * max - value;
            }
        }
        return value;
    }

    public double normalize(double value) {
        return (value - min) / (max - min);
    }

    public double range() {
        return max - min;
    }
}
