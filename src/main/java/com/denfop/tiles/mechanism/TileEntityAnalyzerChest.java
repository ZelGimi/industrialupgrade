package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerAnalyzerChest;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiAnalyzerChest;
import com.denfop.gui.GuiCore;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityAnalyzerChest extends TileEntityInventory {

    public InvSlotOutput outputSlot = new InvSlotOutput(this, 36);

    public TileEntityAnalyzerChest(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.analyzer_chest, pos, state);
    }

    @Override
    public ContainerAnalyzerChest getGuiContainer(final Player entityPlayer) {
        return new ContainerAnalyzerChest(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(final Player entityPlayer, final ContainerBase<? extends IAdvInventory> b) {
        return new GuiAnalyzerChest((ContainerAnalyzerChest) b);
    }

    public IMultiTileBlock getTeBlock() {

             return BlockBaseMachine3.analyzer_chest;
    }

    public BlockTileEntity getBlock() {
          return IUItem.basemachine2.getBlock(getTeBlock());
    }


}
