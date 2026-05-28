package com.denfop.componets;

import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.componets.system.EnergyNetDelegateSource;
import com.denfop.componets.system.StorageEnergyNetDelegateSink;
import com.denfop.utils.ModUtils;
import net.minecraft.core.Direction;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ComponentStorageEnergy extends ComponentBaseEnergy {


    public ComponentStorageEnergy(EnergyType type, BlockEntityInventory parent, double capacity) {
        this(type, parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public ComponentStorageEnergy(
            EnergyType type, BlockEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int tier
    ) {
        this(type, parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public ComponentStorageEnergy(
            EnergyType type, BlockEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(type, parent, capacity, sinkDirections, sourceDirections, sinkTier, sourceTier);
    }

    public ComponentStorageEnergy(
            EnergyType type, BlockEntityInventory parent,
            double capacity,
            List<Direction> sinkDirections,
            List<Direction> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(type, parent, capacity, sinkDirections, sourceDirections, sinkTier, sourceTier);
    }

    public static ComponentStorageEnergy asBasicSink(EnergyType type, BlockEntityInventory parent, double capacity) {
        return asBasicSink(type, parent, capacity, 1);
    }

    public static ComponentStorageEnergy asBasicSink(EnergyType type, BlockEntityInventory parent, double capacity, int tier) {
        return new ComponentStorageEnergy(type, parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static ComponentStorageEnergy asBasicSink(BlockEntityInventory parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static ComponentStorageEnergy asBasicSink(BlockEntityInventory parent, double capacity, int tier) {
        return new ComponentStorageEnergy(EnergyType.STORAGE, parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static ComponentStorageEnergy asBasicSource(BlockEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static ComponentStorageEnergy asBasicSource(BlockEntityInventory parent, double capacity, int tier) {
        return new ComponentStorageEnergy(EnergyType.STORAGE, parent, capacity, Collections.emptySet(), ModUtils.allFacings, tier);
    }


    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();


    }


    @Override
    public void createDelegate() {

        if (this.delegate != null) {
        } else {

            if (this.sinkDirections.isEmpty()) {
                this.delegate = new EnergyNetDelegateSource(this);
            } else {
                this.delegate = new StorageEnergyNetDelegateSink(this);
            }

        }
    }
}
