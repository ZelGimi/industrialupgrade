package com.denfop.tiles.quarry_earth;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEarthQuarry;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerEarthChest;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiEarthChest;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityChest extends TileEntityMultiBlockElement implements IEarthChest {

    private final InvSlot slot;

    public TileEntityChest(BlockPos pos, BlockState state) {
        super(BlockEarthQuarry.earth_chest, pos, state);
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.OUTPUT, 9);
    }

    @Override
    public InvSlot getSlot() {
        return slot;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.earthQuarry.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockEarthQuarry.earth_chest;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerEarthChest getGuiContainer(final Player var1) {
        return new ContainerEarthChest(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiEarthChest((ContainerEarthChest) menu);
    }

}
