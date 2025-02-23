package com.denfop.tiles.smeltery;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerCyclotronCryogen;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySmelteryFluidTank extends TileEntityMultiBlockElement implements ITank {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public TileEntitySmelteryFluidTank() {
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTankExtract("fluids", 10000);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.fluidTank.setCanFill(this.getMain() != null);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerCyclotronCryogen getGuiContainer(final EntityPlayer var1) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return null;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSmeltery.smeltery_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery;
    }

    @Override
    public FluidTank getTank() {
        return fluidTank;
    }

}
