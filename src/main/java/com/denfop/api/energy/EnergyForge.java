package com.denfop.api.energy;

import com.denfop.api.sytem.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.*;

public class EnergyForge implements IEnergyTile {
    protected final Map<Direction, IEnergyStorage> storages;
    private final BlockEntity blockEntity;
    List<InfoTile<IEnergyTile>> validReceivers = new ArrayList<>();
    Map<Direction, IEnergyTile> energyConductorMap = new HashMap<>();
    private long id;

    public EnergyForge(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        this.storages = new HashMap<>();
        for (Direction direction : Direction.values()) {
            storages.put(direction, blockEntity.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, blockEntity.getBlockPos(), direction));
        }
    }

    public Map<Direction, IEnergyStorage> getStorages() {
        return storages;
    }

    @Override
    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    @Override
    public BlockPos getPos() {
        return blockEntity.getBlockPos();
    }

    @Override
    public long getIdNetwork() {
        return this.id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public void RemoveTile(IEnergyTile tile, final Direction facing1) {
        if (!this.blockEntity.getLevel().isClientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<IEnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<IEnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public void AddTile(IEnergyTile tile, final Direction facing1) {
        if (!this.blockEntity.getLevel().isClientSide) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
        }
    }

    @Override
    public Map<Direction, IEnergyTile> getTiles() {
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
