package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerCyclotronCoolant;
import com.denfop.gui.GuiCyclotronCoolant;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityCyclotronCoolant extends TileEntityMultiBlockElement implements ICoolant {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public TileEntityCyclotronCoolant() {
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTankExtract("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidcoolant.getInstance()));
    }

    @Override
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
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) && this.getMain() != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerCyclotronCoolant getGuiContainer(final EntityPlayer var1) {
        return new ContainerCyclotronCoolant(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiCyclotronCoolant(getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCyclotron.cyclotron_coolant;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron;
    }

    @Override
    public FluidTank getCoolantTank() {
        return fluidTank;
    }

}
