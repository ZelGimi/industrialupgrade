package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerCyclotronCryogen;
import com.denfop.gui.GuiCyclotronCryogen;
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

public class TileEntityCyclotronCryogen extends TileEntityMultiBlockElement implements ICryogen {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public TileEntityCyclotronCryogen() {
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidcryogen.getInstance()));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerCyclotronCryogen getGuiContainer(final EntityPlayer var1) {
        return new ContainerCyclotronCryogen(this, var1);
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
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiCyclotronCryogen(getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCyclotron.cyclotron_cryogen;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron;
    }

    @Override
    public FluidTank getCryogenTank() {
        return fluidTank;
    }

}
