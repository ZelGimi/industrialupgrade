package appeng.api.config;

public enum PowerMultiplier {
    ONE,
    CONFIG;

    public double multiplier = 1.0;

    PowerMultiplier() {
    }

    public double multiply(double in) {
        return in * this.multiplier;
    }

    public double divide(double in) {
        return in / this.multiplier;
    }
}
