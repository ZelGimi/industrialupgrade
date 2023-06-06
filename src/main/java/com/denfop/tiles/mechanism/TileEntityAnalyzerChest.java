package com.denfop.tiles.mechanism;

import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.container.ContainerAnalyzerChest;
import com.denfop.gui.GuiAnalyzerChest;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAnalyzerChest extends TileEntityInventory implements IHasGui {

    public InvSlotOutput outputSlot = new InvSlotOutput(this, "output", 36);

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

    @Override
    public void onGuiClosed(final EntityPlayer entityPlayer) {

    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

}
