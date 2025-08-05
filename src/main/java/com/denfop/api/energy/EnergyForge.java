package com.denfop.api.energy;

import com.denfop.api.sytem.InfoTile;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.*;

public class EnergyForge implements IEnergyTile{
    private final BlockEntity blockEntity;
    protected final Map<Direction, IEnergyStorage> storages;
    private long id;

    public EnergyForge(BlockEntity blockEntity){
        this.blockEntity=blockEntity;
        this.storages= new HashMap<>();
        for (Direction direction : Direction.values()){
            storages.put(direction,blockEntity.getCapability(ForgeCapabilities.ENERGY,direction).orElse(new EnergyStorage(0,0,0,0)));
        }
    }

    public  Map<Direction, IEnergyStorage> getStorages() {
        return storages;
    }

    List<InfoTile<IEnergyTile>> validReceivers= new ArrayList<>();
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
    Map<Direction, IEnergyTile> energyConductorMap = new HashMap<>();
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
