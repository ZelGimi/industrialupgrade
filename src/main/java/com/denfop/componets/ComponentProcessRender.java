package com.denfop.componets;

import com.denfop.tiles.mechanism.EnumTypeMachines;

public class ComponentProcessRender {

    private final ProcessMultiComponent process;
    private final EnumTypeMachines typeMachines;

    public ComponentProcessRender(ProcessMultiComponent process, EnumTypeMachines typeMachines) {
        this.process = process;
        this.typeMachines = typeMachines;


    }

    public EnumTypeMachines getTypeMachines() {
        return typeMachines;
    }

    public ProcessMultiComponent getProcess() {
        return process;
    }

}
