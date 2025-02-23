package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerSafe;
import com.denfop.gui.GuiSafe;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class TileEntitySafe extends TileEntityInventory {

    public final InvSlot slot;
    public int timer = 10;

    public TileEntitySafe() {
        this.getComponentPrivate().setActivate(true);
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT_OUTPUT, 63);
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

    @Override
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return false;
        }
        return super.hasCapability(capability, facing);
    }

    public List<InvSlot> getInputSlots() {
        return Collections.emptyList();
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (timer > 0) {
            timer--;
            if (timer == 0) {
                this.setActive(false);
            }
        }

    }

    @Override
    public ContainerSafe getGuiContainer(final EntityPlayer var1) {
        return new ContainerSafe(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSafe(getGuiContainer(var1));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.safe;
    }

}
