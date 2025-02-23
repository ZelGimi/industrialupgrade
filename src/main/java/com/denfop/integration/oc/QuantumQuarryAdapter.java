package com.denfop.integration.oc;

import com.denfop.IUItem;
import com.denfop.tiles.mechanism.quarry.TileBaseQuantumQuarry;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.item.ItemStack;

public class QuantumQuarryAdapter extends AbstractManagedEnvironment implements ManagedPeripheral, NamedBlock {

    private final TileBaseQuantumQuarry quantumQuarry;

    public QuantumQuarryAdapter(TileBaseQuantumQuarry quantumQuarry) {
        this.quantumQuarry = quantumQuarry;
        this.setNode(Network.newNode(this, Visibility.Network).withComponent("quantumQuarry", Visibility.Network).create());
    }

    public String[] methods() {
        return new String[]{"getConsume",
                "getBlocksTick",
                "getChance",
                "getQuantumEnergy",
                "getCapacityEnergy",
                "hasVein",
                "getTypeVein",
                "isWork",
                "getMainModuleName",
                "getTypeVeinStack"
        };
    }

    public Object[] invoke(String method, Context context, Arguments args) {
        try {
            switch (method) {
                case "getConsume":
                    return new Object[]{quantumQuarry.consume};
                case "getBlocksTick":
                    return new Object[]{quantumQuarry.col_tick};
                case "getChance":
                    return new Object[]{quantumQuarry.chance};
                case "getQuantumEnergy":
                    return new Object[]{quantumQuarry.energy.getEnergy()};
                case "getCapacityEnergy":
                    return new Object[]{quantumQuarry.energy.getCapacity()};
                case "hasVein":
                    return new Object[]{quantumQuarry.vein != null};
                case "getTypeVein":
                    try {
                        return new Object[]{new ItemStack(IUItem.heavyore, 1, quantumQuarry.vein.getMeta()).getDisplayName()};
                    } catch (Exception e) {
                        return new Object[]{};
                    }
                case "getTypeVeinStack":
                    try {
                        return new Object[]{new ItemStack(IUItem.heavyore, 1, quantumQuarry.vein.getMeta())};
                    } catch (Exception e) {
                        return new Object[]{};
                    }
                case "isWork":
                    return new Object[]{quantumQuarry.analyzer && quantumQuarry.consume < quantumQuarry.energy.getEnergy()};
                case "getMainModuleName":
                    if (!quantumQuarry.inputslotA.get().isEmpty()) {
                        return new Object[]{quantumQuarry.inputslotA.get().getDisplayName()};
                    } else {
                        return new Object[]{};
                    }
                default:
                    return new Object[]{};
            }
        } catch (Exception e) {
            return new Object[]{};
        }
    }

    public String preferredName() {
        return "quantumQuarry";
    }

    public int priority() {
        return 5;
    }

}
