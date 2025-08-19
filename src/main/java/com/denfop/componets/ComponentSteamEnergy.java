package com.denfop.componets;

import com.denfop.api.sytem.EnergyType;
import com.denfop.blocks.FluidName;
import com.denfop.effects.EffectsRegister;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ComponentSteamEnergy extends ComponentBaseEnergy {

    FluidTank fluidTank;

    public ComponentSteamEnergy(EnergyType type, TileEntityInventory parent, double capacity) {
        this(type, parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    public ComponentSteamEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int tier
    ) {
        this(type, parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public ComponentSteamEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(type, parent, capacity, sinkDirections, sourceDirections, sinkTier, sourceTier);
    }

    public ComponentSteamEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            List<Direction> sinkDirections,
            List<Direction> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(type, parent, capacity, sinkDirections, sourceDirections, sinkTier, sourceTier);
    }

    public static ComponentSteamEnergy asBasicSink(TileEntityInventory parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static ComponentSteamEnergy asBasicSink(TileEntityInventory parent, double capacity, int tier) {
        return new ComponentSteamEnergy(EnergyType.STEAM, parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static ComponentSteamEnergy asBasicSource(TileEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static ComponentSteamEnergy asBasicSource(TileEntityInventory parent, double capacity, int tier) {
        return new ComponentSteamEnergy(EnergyType.STEAM, parent, capacity, Collections.emptySet(), ModUtils.allFacings, tier);
    }

    @Override
    public void onPlaced(ItemStack stack, LivingEntity placer, Direction facing) {
        super.onPlaced(stack, placer, facing);
    }

    public void setFluidTank(final FluidTank fluidTank) {
        this.fluidTank = fluidTank;
    }

    @Override
    public double addEnergy(final double amount) {
        super.addEnergy(amount);
        if (fluidTank.getFluid().isEmpty() && amount >= 1) {
            fluidTank.fill(new FluidStack(FluidName.fluidsteam.getInstance().get(), (int) this.buffer.storage), IFluidHandler.FluidAction.EXECUTE);
        } else if (!fluidTank.getFluid().isEmpty()) {
            fluidTank.getFluid().setAmount((int) this.buffer.storage);
        }
        return amount;
    }
    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (fluidTank.getFluid().getAmount() != this.buffer.storage && (this.buffer.storage > fluidTank.getFluid().getAmount())){
            fluidTank.fill(new FluidStack(FluidName.fluidsteam.getInstance().get(),  (int)this.buffer.storage-fluidTank.getFluid().getAmount()), IFluidHandler.FluidAction.EXECUTE);
        }
        if (fluidTank.getFluid().getAmount() != this.buffer.storage && (this.buffer.storage < fluidTank.getFluid().getAmount())){
            fluidTank.drain(fluidTank.getFluid().getAmount()- (int)this.buffer.storage, IFluidHandler.FluidAction.EXECUTE);
        }
    }
    @Override
    public boolean isClient() {
        return true;
    }


    @Override
    public boolean useEnergy(final double amount) {
        super.useEnergy(amount);
        if (!fluidTank.getFluid().isEmpty()) {
            fluidTank.getFluid().setAmount((int) this.buffer.storage);
            if (fluidTank.getFluid().getAmount() == 0) {
                fluidTank.setFluid(FluidStack.EMPTY);
            }
        }
        return true;
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.parent.getActive() && this.parent.getWorld().getGameTime() % 4 == 0) {
            double x = this.parent.getBlockPos().getX();
            double y = this.parent.getBlockPos().getY() + 1.0;
            double z = this.parent.getBlockPos().getZ();

            this.parent.getLevel().addParticle(
                    EffectsRegister.STEAM_ASH.get(),
                    x, y, z,
                    0.0, 0.1, 0.0
            );
        }
    }
}
