package com.denfop.integration.oc;

import com.denfop.tiles.reactors.IChamber;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;

public class ReactorChamberAdapter extends AbstractManagedEnvironment implements ManagedPeripheral, NamedBlock {

    private final IChamber chamber;

    public ReactorChamberAdapter(IChamber chamber) {
        this.chamber = chamber;
        this.setNode(Network.newNode(this, Visibility.Network).withComponent("eu_chamber", Visibility.Network).create());
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
        try {
            switch (method) {
                case "getHeat":
                    return new Object[]{chamber.getReactor().getHeat()};
                case "getMaxHeat":
                    return new Object[]{chamber.getReactor().getMaxHeat()};
                case "getHeatEffectModifier":
                    return new Object[]{chamber.getReactor().getHeatEffectModifier()};
                case "getReactorEUEnergyOutput":
                    return new Object[]{chamber.getReactor().getReactorEUEnergyOutput()};
                case "isWork":
                    return new Object[]{chamber.getReactor().isWork()};
                case "setWork":
                    if (args.isBoolean(0)) {
                        chamber.getReactor().setWork(args.checkBoolean(0));
                    }
                    return new Object[]{chamber.getReactor().isWork()};
                case "isFull":
                    return new Object[]{chamber.getReactor().isFull()};
                default:
                    return new Object[]{};
            }
        } catch (Exception e) {
            return new Object[]{};
        }
    }

    public String preferredName() {
        return "eu_chamber";
    }

    public int priority() {
        return 5;
    }

}
