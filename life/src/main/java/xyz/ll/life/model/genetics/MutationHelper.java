package xyz.ll.life.model.genetics;

public class MutationHelper {

    private double min;
    private double max;
    private double variance;

    public static Builder helper() {
        return new MutationHelper().new Builder();
    }

    public class Builder {

        public Builder min(double min) {
            MutationHelper.this.min = min;
            return this;
        }

        public Builder max(double max) {
            MutationHelper.this.max = max;
            return this;
        }

        public Builder variance(double variance) {
            MutationHelper.this.variance = variance;
            return this;
        }

        public MutationHelper build() {
            return MutationHelper.this;
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

    public double range() {
        return max - min;
    }
}
