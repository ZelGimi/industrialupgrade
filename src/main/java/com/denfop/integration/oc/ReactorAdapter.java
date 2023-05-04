package com.denfop.integration.oc;

import com.denfop.api.reactors.IAdvReactor;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;

public class ReactorAdapter extends AbstractManagedEnvironment implements ManagedPeripheral, NamedBlock {

    private final IAdvReactor advReactor;

    public ReactorAdapter(IAdvReactor advReactor) {
        this.advReactor = advReactor;
        this.setNode(Network.newNode(this, Visibility.Network).withComponent("eu_reactor", Visibility.Network).create());
    }

    public String[] methods() {
        return new String[]{"getHeat",
                "getMaxHeat",
                "getHeatEffectModifier",
                "getReactorEUEnergyOutput",
                "isWork",
                "setWork",
                "isFull"};
    }

    public Object[] invoke(String method, Context context, Arguments args) {
        switch (method) {
            case "getHeat":
                return new Object[]{advReactor.getHeat()};
            case "getMaxHeat":
                return new Object[]{advReactor.getMaxHeat()};
            case "getHeatEffectModifier":
                return new Object[]{advReactor.getHeatEffectModifier()};
            case "getReactorEUEnergyOutput":
                return new Object[]{advReactor.getReactorEUEnergyOutput()};
            case "isWork":
                return new Object[]{advReactor.isWork()};
            case "setWork":
                if (args.isBoolean(0)) {
                    advReactor.setWork(args.checkBoolean(0));
                }
                return new Object[]{advReactor.isWork()};
            case "isFull":
                return new Object[]{advReactor.isFull()};
            default:
                return new Object[]{};
        }

    }

    public String preferredName() {
        return "eu_reactor";
    }

    public int priority() {
        return 5;
    }

}
