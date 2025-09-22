package com.denfop.componets;

import com.denfop.tiles.mechanism.EnumTypeMachines;

public class ComponentSteamProcessRender {

    private final SteamProcessMultiComponent process;
    private final EnumTypeMachines typeMachines;

    public ComponentSteamProcessRender(SteamProcessMultiComponent process, EnumTypeMachines typeMachines) {
        this.process = process;
        this.typeMachines = typeMachines;


    }

    public EnumTypeMachines getTypeMachines() {
        return typeMachines;
    }

    public SteamProcessMultiComponent getProcess() {
        return process;
    }

}
