package xyz.luan.life.model;

public enum Gene {
    A, B, C, D, E, F, G, H, I, J, K, L, M, N,
    COLOR, TRANSLATION_SPEED(0.8), TRANSLATION_CONSTANCY(200.0), ROTATION_SPEED(180.0), ROTATION_CONSTANCY(10.0), LIBIDO, CHARITY;

    private double coefficient;

    private Gene() {
        this(1.0);
    }

    private Gene(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getCoefficient() {
        return this.coefficient;
    }
}
