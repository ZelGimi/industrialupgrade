package com.denfop.api.sytem;

public enum EnergyType {
    SOLARIUM(" SE", false, true),
    NIGHT(" NE", false, true),
    QUANTUM(" QE", false, true),
    EXPERIENCE(" EXP", false, true),
    POSITRONS(" e⁺", false, true),
    RADIATION(" ☢", false, true),
    STEAM(" mb", false, true),
    BIOFUEL(" mb", false, true),
    AMPERE(" A", false, true),
    ;

    private final String prefix;
    private final boolean auto_mode;
    private final boolean draw;

    EnergyType(String prefix, boolean auto_mode, boolean draw) {
        this.prefix = prefix;
        this.auto_mode = auto_mode;
        this.draw = draw;
    }



    public String getPrefix() {
        return prefix;
    }

    public boolean isAuto_mode() {
        return auto_mode;
    }

    public boolean isDraw() {
        return draw;
    }
}
