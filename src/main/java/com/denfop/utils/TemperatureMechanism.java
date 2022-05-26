package com.denfop.utils;

import com.denfop.api.ITemperature;
import com.denfop.api.ITemperatureMechanism;
import com.denfop.tiles.base.TileEntityElectricMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TemperatureMechanism implements ITemperatureMechanism {

    @Override
    public boolean process(ITemperature tile) {
        if (tile.getTile().energy.getEnergy() <= 0 && tile.getFluid() == null) {
            return false;
        }
        short temp = tile.getTemperature();
        if (temp >= tile.getMaxTemperature()) {
            return false;
        }
        if (tile.isFluidTemperature()) {
            if (tile.getFluid().amount >= 5) {
                tile.setTemperature((short) Math.min(temp + 5,tile.getMaxTemperature()));
                tile.getFluid().amount -= 5;
                return true;
            }
        } else {
            if (tile.getTile().energy.getEnergy() >= 50) {
                tile.setTemperature((short) Math.min(temp + 5,tile.getMaxTemperature()));
                tile.getTile().energy.useEnergy(50);
                return true;
            }
        }
        return false;
    }

    @Override
    public short transfer(ITemperature receiver, ITemperature extract) {
        if (extract.getTemperature() <= 0 || receiver.getTemperature() >= receiver.getMaxTemperature()) {
            return 0;
        }
        if (receiver.equals(extract)) {
            return 0;
        }
        short temp = (short) Math.min(extract.getTemperature(), receiver.getMaxTemperature() - receiver.getTemperature());
        extract.setTemperature((short) (extract.getTemperature() - temp));
        receiver.setTemperature((short) (receiver.getTemperature() + temp));
        return temp;
    }

    @Override
    public void work(ITemperature tile) {
        if (tile.getTemperature() - 5 > 0) {
            tile.setTemperature((short) (tile.getTemperature() - 5));
        }
    }

    @Override
    public boolean hasHeaters(final ITemperature tile) {
        final TileEntityElectricMachine tile1 = tile.getTile();
        for (EnumFacing facing : EnumFacing.VALUES) {
            TileEntity tile3 = tile1.getWorld().getTileEntity(tile1.getPos().offset(facing));
            if (tile3 instanceof ITemperature) {
                if (!((ITemperature) tile3).reveiver()) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public boolean hasHeaters(final World world, final BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            TileEntity tile3 = world.getTileEntity(pos.offset(facing));
            if (tile3 instanceof ITemperature) {
                if (!((ITemperature) tile3).reveiver()) {
                    return true;
                }
            }

        }
        return false;
    }

}
