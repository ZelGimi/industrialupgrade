package com.denfop.tiles.smeltery;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerCyclotronCryogen;
import com.denfop.container.ContainerSmelteryCasting;
import com.denfop.container.ContainerSmelteryFuelTank;
import com.denfop.gui.GuiSmelteryFuelTank;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySmelteryFuelTank extends TileEntityMultiBlockElement implements IFuelTank {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;
    private double speed;

    public TileEntitySmelteryFuelTank() {
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidRegistry.LAVA, FluidName.fluidpahoehoe_lava.getInstance()));
    }

    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.fluidTank.getFluid() != null && this.fluidTank
                .getFluid()
                .getFluid()
                .equals(FluidName.fluidpahoehoe_lava.getInstance())) {
            speed = 2;
        } else {
            speed = 1;
        }
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerSmelteryFuelTank getGuiContainer(final EntityPlayer var1) {
        return new ContainerSmelteryFuelTank(this,var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSmelteryFuelTank(getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSmeltery.smeltery_fuel_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery;
    }

    @Override
    public FluidTank getFuelTank() {
        return fluidTank;
    }

    @Override
    public double getSpeed() {
        return this.speed;
    }

}
