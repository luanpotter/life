package xyz.luan.life.model;

public enum Gene2 {
    A, B, C, D, E, F, G, H, I, J, K, L, M, N,
    COLOR, TRANSLATION_SPEED(1), TRANSLATION_CONSTANCY(50), ROTATION_SPEED(180.0), ROTATION_CONSTANCY(10.0), LIBIDO, CHARITY;

    private double coefficient;

    private Gene2() {
        this(1.0);
    }

    private Gene2(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getCoefficient() {
        return this.coefficient;
    }
}