package com.denfop.api.sytem;

public enum EnergyType {
    SOLARIUM(" SE", false, true, false),
    QUANTUM(" QE", false, true, false),
    EXPERIENCE("EXP", false, true, false),
    RADIATION(" ", false, true, false);

    private final String prefix;
    private final boolean auto_mode;
    private final boolean draw;
    private final boolean break_conductors;

    EnergyType(String prefix, boolean auto_mode, boolean draw, boolean break_conductors) {
        this.prefix = prefix;
        this.auto_mode = auto_mode;
        this.draw = draw;
        this.break_conductors = break_conductors;
    }

    public boolean isBreak_conductors() {
        return break_conductors;
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
