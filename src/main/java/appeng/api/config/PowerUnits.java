package appeng.api.config;

public enum PowerUnits {
    AE("gui.appliedenergistics2.units.appliedenergstics"),
    EU("gui.appliedenergistics2.units.ic2"),
    RF("gui.appliedenergistics2.units.rf"),
    GTEU("gui.appliedenergistics2.units.gteu");

    public final String unlocalizedName;
    public double conversionRatio = 1.0;

    PowerUnits(String un) {
        this.unlocalizedName = un;
    }

    public double convertTo(PowerUnits target, double value) {
        return value * this.conversionRatio / target.conversionRatio;
    }
}
