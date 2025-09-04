package com.denfop.api.energy.forgeenergy;

import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.otherenergies.common.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.*;

public class EnergyForge implements EnergyTile {

    protected final Map<Direction, IEnergyStorage> storages;
    private final boolean isClientSide;
    BlockPos pos;
    List<InfoTile<EnergyTile>> validReceivers = new ArrayList<>();
    Map<Direction, EnergyTile> energyConductorMap = new HashMap<>();
    private long id;

    public EnergyForge(BlockEntity blockEntity) {
        this.isClientSide = blockEntity.getLevel().isClientSide;
        pos = blockEntity.getBlockPos();
        this.storages = new HashMap<>();
        for (Direction direction : Direction.values()) {
            storages.put(direction, blockEntity.getCapability(ForgeCapabilities.ENERGY, direction).orElse(new EnergyStorage(0, 0, 0, 0)));
        }
    }

    public Map<Direction, IEnergyStorage> getStorages() {
        return storages;
    }

    @Override
    public List<InfoTile<EnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public long getIdNetwork() {
        return this.id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public void RemoveTile(EnergyTile tile, final Direction facing1) {
        if (!isClientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<EnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<EnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public void AddTile(EnergyTile tile, final Direction facing1) {
        if (!isClientSide) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
        }
    }

    @Override
    public Map<Direction, EnergyTile> getTiles() {
        return energyConductorMap;
    }

    @Override
    public int getHashCodeSource() {
        return 0;
    }

    @Override
    public void setHashCodeSource(int hashCode) {

    }
}
