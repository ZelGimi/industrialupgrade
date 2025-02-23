package com.denfop.integration.oc;

import com.denfop.api.windsystem.EnumRotorSide;
import com.denfop.api.windsystem.IWindMechanism;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;

public class WindWaterAdapter extends AbstractManagedEnvironment implements ManagedPeripheral, NamedBlock {

    private final IWindMechanism windMechanism;

    public WindWaterAdapter(IWindMechanism windMechanism) {
        this.windMechanism = windMechanism;
        this.setNode(Network.newNode(this, Visibility.Network).withComponent(
                "eu_wind_water_mechanism",
                Visibility.Network
        ).create());
    }

    public String[] methods() {
        return new String[]{"getRotorSide",
                "setRotorSide",
                "getCoefficient",
                "getLevelGenerator",
                "getAngle",
                "getRotorDiameter",
                "getAuto",
                "getMin",
                "getSpace",
                "need_repair",
                "can_repair",
                "getMinWind",
                "getMinWindSpeed"};
    }

    public Object[] invoke(String method, Context context, Arguments args) {
        switch (method) {
            case "getRotorSide":
                return new Object[]{windMechanism.getRotorSide().toString()};
            case "setRotorSide":
                if (args.isInteger(0)) {
                    windMechanism.setRotorSide(EnumRotorSide.values()[args.checkInteger(0) % 4]);
                }
                return new Object[]{windMechanism.getRotorSide().name().toUpperCase()};
            case "getCoefficient":
                return new Object[]{windMechanism.getCoefficient()};
            case "getLevelGenerator":
                return new Object[]{windMechanism.getLevelGenerator().name()};
            case "getAngle":
                return new Object[]{windMechanism.getAngle()};
            case "getRotorDiameter":
                return new Object[]{windMechanism.getRotorDiameter()};
            case "getAuto":
                return new Object[]{windMechanism.getAuto()};
            case "getMin":
                return new Object[]{windMechanism.getMin()};
            case "getSpace":
                return new Object[]{windMechanism.getSpace()};
            case "need_repair":
                return new Object[]{windMechanism.need_repair()};
            case "can_repair":
                return new Object[]{windMechanism.can_repair()};
            case "getMinWind":
                return new Object[]{windMechanism.getMinWind()};
            case "getMinWindSpeed":
                return new Object[]{windMechanism.getMinWindSpeed()};
            default:
                return new Object[]{};
        }

    }

    public String preferredName() {
        return "eu_wind_water_mechanism";
    }

    public int priority() {
        return 5;
    }

}
