package com.denfop.componets;

import com.denfop.api.sytem.EnergyType;
import com.denfop.blocks.FluidName;
import com.denfop.effects.BiomassParticle;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ComponentBioFuelEnergy extends ComponentBaseEnergy {

    FluidTank fluidTank;

    public ComponentBioFuelEnergy(EnergyType type, TileEntityInventory parent, double capacity) {
        this(type, parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public ComponentBioFuelEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int tier
    ) {
        this(type, parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public ComponentBioFuelEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(type, parent, capacity, sinkDirections, sourceDirections, sinkTier, sourceTier, fullEnergy);
    }

    public ComponentBioFuelEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            List<EnumFacing> sinkDirections,
            List<EnumFacing> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(type, parent, capacity, sinkDirections, sourceDirections, sinkTier, sourceTier, fullEnergy);
    }

    public static ComponentBioFuelEnergy asBasicSink(TileEntityInventory parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static ComponentBioFuelEnergy asBasicSink(TileEntityInventory parent, double capacity, int tier) {
        return new ComponentBioFuelEnergy(
                EnergyType.BIOFUEL,
                parent,
                capacity,
                ModUtils.allFacings,
                Collections.emptySet(),
                tier
        );
    }

    public static ComponentBioFuelEnergy asBasicSource(TileEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static ComponentBioFuelEnergy asBasicSource(TileEntityInventory parent, double capacity, int tier) {
        return new ComponentBioFuelEnergy(
                EnergyType.BIOFUEL,
                parent,
                capacity,
                Collections.emptySet(),
                ModUtils.allFacings,
                tier
        );
    }

    public void setFluidTank(final FluidTank fluidTank) {
        this.fluidTank = fluidTank;
    }

    @Override
    public double addEnergy(final double amount) {
        super.addEnergy(amount);
        if (fluidTank.getFluid() == null && amount >= 1) {
            fluidTank.fill(new FluidStack(FluidName.fluidbiomass.getInstance(), (int) this.storage), true);
        } else if (fluidTank.getFluid() != null) {
            fluidTank.getFluid().amount = (int) this.storage;
        }
        return amount;
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.parent.getActive() && this.parent.getWorld().getWorldTime() % 4 == 0) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new BiomassParticle(this.parent.getWorld(),
                    this.parent.getPos().getX() + 0.5,
                    this.parent.getPos().getY() + 1, this.parent.getPos().getZ() + 0.5
            ));
        }
    }

    @Override
    public boolean useEnergy(final double amount) {
        super.useEnergy(amount);
        if (fluidTank.getFluid() != null) {
            fluidTank.getFluid().amount = (int) this.storage;
            if (fluidTank.getFluid().amount == 0) {
                fluidTank.setFluid(null);
            }
        }
        return true;
    }

}
