package com.denfop.componets;

import com.denfop.blockentity.mechanism.EnumTypeMachines;

public class ComponentBioProcessRender {

    private final BioProcessMultiComponent process;
    private final EnumTypeMachines typeMachines;

    public ComponentBioProcessRender(BioProcessMultiComponent process, EnumTypeMachines typeMachines) {
        this.process = process;
        this.typeMachines = typeMachines;


    }

    public EnumTypeMachines getTypeMachines() {
        return typeMachines;
    }

    public BioProcessMultiComponent getProcess() {
        return process;
    }

}
