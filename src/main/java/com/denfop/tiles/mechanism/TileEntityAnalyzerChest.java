package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerAnalyzerChest;
import com.denfop.gui.GuiAnalyzerChest;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAnalyzerChest extends TileEntityInventory {

    public InventoryOutput outputSlot = new InventoryOutput(this, 36);

    public TileEntityAnalyzerChest() {

    }

    @Override
    public ContainerAnalyzerChest getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerAnalyzerChest(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiAnalyzerChest getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiAnalyzerChest(getGuiContainer(entityPlayer));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.analyzer_chest;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }


}
