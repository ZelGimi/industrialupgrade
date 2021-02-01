package com.denfop.ssp.gui;

import com.denfop.ssp.SuperSolarPanels;
import ic2.core.gui.Gauge;

import java.util.Locale;

public enum ProgressBars implements Gauge.IGaugeStyle {
    PROGRESS_MOLECULAR_TRANSFORMER((new Gauge.GaugePropertyBuilder(
            221,
            7,
            10,
            15,
            Gauge.GaugePropertyBuilder.GaugeOrientation.Down
    )).withTexture(SuperSolarPanels.getIdentifier("textures/gui/MolecularTransformer.png"))),
    PROGRESS_JEI_MOLECULAR_TRANSFORMER((new Gauge.GaugePropertyBuilder(
            176,
            2,
            12,
            11,
            Gauge.GaugePropertyBuilder.GaugeOrientation.Down
    )).withTexture(SuperSolarPanels.getIdentifier("textures/gui/MolecularTransformer JEI.png"))),
    ENERGY_ADVANCED_SOLAR((new Gauge.GaugePropertyBuilder(
            195,
            0,
            24,
            14,
            Gauge.GaugePropertyBuilder.GaugeOrientation.Right
    )).withTexture(SuperSolarPanels.getIdentifier("textures/gui/AdvancedSolarPanel.png")));

    private final String name;

    private final Gauge.GaugeProperties properties;

    ProgressBars(Gauge.GaugePropertyBuilder properties) {
        this.name = name().toLowerCase(Locale.ENGLISH);
        this.properties = properties.build();
    }

    public static void addStyles() {
        for (ProgressBars bar : values()) {
            Gauge.GaugeStyle.addStyle(bar.name, bar);
        }
    }

    public Gauge.GaugeProperties getProperties() {
        return this.properties;
    }
}
