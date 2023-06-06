package com.denfop.tiles.reactors;

import com.denfop.api.energy.IAdvEnergyTile;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.componets.EnumTypeStyle;
import ic2.api.reactor.IReactorComponent;
import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import ic2.core.util.LogCategory;
import ic2.core.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class TileEntityAdvNuclearReactorElectric extends TileEntityBaseNuclearReactorElectric {

    public TileEntityAdvNuclearReactorElectric() {
        super(10, 6, "textures/gui/GUIAdvNuclearReaktor.png", 1.5);
    }


    public void setblock() {

        for (EnumFacing direction : EnumFacing.values()) {
            TileEntity te = world.getTileEntity(this.pos.offset(direction));
            if (te instanceof TileEntityAdvReactorChamberElectric) {
                this.getWorld().setBlockToAir(this.pos.offset(direction));
            }
        }

        this.getWorld().setBlockToAir(this.pos);
    }


    public void explode() {
        float boomPower = 10.0F;
        float boomMod = 1.0F;

        for (int i = 0; i < this.reactorSlot.size(); ++i) {
            ItemStack stack = this.reactorSlot.get(i);
            if (stack != null && stack.getItem() instanceof IReactorComponent) {
                float f = ((IReactorComponent) stack.getItem()).influenceExplosion(stack, this);
                if (f > 0.0F && f < 1.0F) {
                    boomMod *= f;
                } else {
                    boomPower += f;
                }
            }

            this.reactorSlot.put(i, null);
        }

        boomPower *= this.hem * boomMod;
        IC2.log.log(
                LogCategory.PlayerActivity,
                Level.INFO,
                "Nuclear Reactor at %s melted (raw explosion power %f)",
                Util.formatPosition(this),
                boomPower
        );
        boomPower = Math.min(boomPower, ConfigUtil.getFloat(MainConfig.get(), "protection/reactorExplosionPowerLimit"));
        EnumFacing[] var8 = EnumFacing.values();

        for (EnumFacing direction : var8) {
            TileEntity target = this.getWorld().getTileEntity(pos.offset(direction));
            if (target instanceof TileEntityAdvReactorChamberElectric) {
                this.getWorld().setBlockToAir(pos.offset(direction));
            }
        }

        this.getWorld().setBlockToAir(pos);
        ExplosionIC2 explosion = new ExplosionIC2(this.getWorld(), null, pos, boomPower, 0.01F, ExplosionIC2.Type.Nuclear);
        explosion.doExplosion();
    }

    void getSubs() {
        World world = this.getWorld();
        List<IAdvEnergyTile> newSubTiles = new ArrayList<>();
        newSubTiles.add(this);
        EnumFacing[] var3 = EnumFacing.VALUES;
        for (EnumFacing dir : var3) {
            TileEntity te = world.getTileEntity(this.pos.offset(dir));
            if (te instanceof TileEntityAdvReactorChamberElectric && !te.isInvalid()) {
                newSubTiles.add((TileEntityAdvReactorChamberElectric) te);
            }
        }

        if (!newSubTiles.equals(this.subTiles)) {
            this.change = true;
            if (this.addedToEnergyNet) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
            }

            this.subTiles.clear();
            this.subTiles.addAll(newSubTiles);
            if (this.addedToEnergyNet) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this));
            }
        }
        this.getReactorSize();
    }

    public short getReactorSize() {
        if (world == null) {
            return 10;
        }
        if (this.change) {
            short cols = (short) (this.sizeX - 6);

            EnumFacing[] var2 = EnumFacing.values();


            for (EnumFacing direction : var2) {
                TileEntity target = this.getWorld().getTileEntity(pos.offset(direction));
                if (target instanceof TileEntityAdvReactorChamberElectric) {
                    cols++;
                    this.blockPos.add(target.getPos());
                }
            }
            if (cols != 10) {
                this.blockPos.add(this.getPos());
            }
            List<BlockPos> blockPos1 = new ArrayList<>();
            for (BlockPos pos : this.blockPos) {
                for (EnumFacing facing : var2) {
                    final BlockPos pos1 = pos.offset(facing);
                    if (!blockPos1.contains(pos1)) {
                        blockPos1.add(pos1);
                    }

                }
            }
            this.blockPos.clear();
            this.blockPos.addAll(blockPos1);
            this.redstone.setBlockPosList(this.blockPos);

            this.size = cols;
            this.change = false;
            this.reactorSlot.update();
            return cols;
        } else {
            return this.size;
        }
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

}
