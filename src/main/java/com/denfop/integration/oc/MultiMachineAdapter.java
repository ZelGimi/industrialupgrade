package com.denfop.integration.oc;

import com.denfop.tiles.base.TileEntityMultiMachine;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.item.ItemStack;

public class MultiMachineAdapter extends AbstractManagedEnvironment implements ManagedPeripheral, NamedBlock {

    private final TileEntityMultiMachine multiMachine;

    public MultiMachineAdapter(TileEntityMultiMachine multiMachine) {
        this.multiMachine = multiMachine;
        this.setNode(Network.newNode(this, Visibility.Network).withComponent("multiMachine", Visibility.Network).create());
    }

    public String[] methods() {
        return new String[]{"getProgressFromIndex",
                "getInputFromIndex",
                "getOutputFromIndex",
                "getHeatMachine",
                "getCapacity",
                "getEnergy",
                "getRFCapacity",
                "getRFEnergy",
                "getTier",
                "getEnergyConsume",
                "getOperationLength"
        };
    }

    public Object[] invoke(String method, Context context, Arguments args) {

        switch (method) {
            case "getProgressFromIndex":
                if (args.isInteger(0)) {
                    return new Object[]{multiMachine.multi_process.getProgress(args.checkInteger(0))};
                }
                return new Object[]{0};
            case "getInputFromIndex":
                if (args.isInteger(0)) {
                    return new Object[]{multiMachine.multi_process.inputSlots.get(args.checkInteger(0))};
                }
                return new Object[]{ItemStack.EMPTY};
            case "getOutputFromIndex":
                if (args.isInteger(0)) {
                    if (multiMachine.multi_process.getOutput(args.checkInteger(0)) != null) {
                        return new Object[]{multiMachine.multi_process.getOutput(args.checkInteger(0)).getRecipe().output.items};
                    }
                    return new Object[]{ItemStack.EMPTY};
                }
                return new Object[]{ItemStack.EMPTY};
            case "getHeatMachine":
                return new Object[]{(multiMachine.multi_process.getCold().getEnergy())};
            case "getCapacity":
                return new Object[]{multiMachine.energy.getCapacity()};
            case "getEnergy":
                return new Object[]{multiMachine.energy.getEnergy()};
            case "getRFCapacity":
                return new Object[]{multiMachine.energy2.getCapacity()};
            case "getRFEnergy":
                return new Object[]{multiMachine.energy2.getEnergy()};
            case "getTier":
                return new Object[]{multiMachine.energy.getSinkTier()};
            case "getEnergyConsume":
                return new Object[]{multiMachine.multi_process.energyConsume};
            case "getOperationLength":
                return new Object[]{multiMachine.multi_process.operationLength};
            default:
                return new Object[]{};
        }

    }

    public String preferredName() {
        return "multiMachine";
    }

    public int priority() {
        return 5;
    }

}
