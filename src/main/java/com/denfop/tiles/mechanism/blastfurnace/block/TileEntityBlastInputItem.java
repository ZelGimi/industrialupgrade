package com.denfop.tiles.mechanism.blastfurnace.block;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBlastFurnace;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastInputItem;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileEntityBlastInputItem extends TileEntityMultiBlockElement implements IBlastInputItem {


    public TileEntityBlastInputItem(BlockPos pos, BlockState state) {
        super(BlockBlastFurnace.blast_furnace_input, pos, state);
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.blastfurnace.info1"));
        tooltip.add(Localization.translate("iu.blastfurnace.info3") + Localization.translate(new ItemStack(
                IUItem.blastfurnace.getItem(0)
        ).getDescriptionId()));
        tooltip.add(Localization.translate("iu.blastfurnace.info4"));
        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer.getItem()).getDisplayName().getString());
        tooltip.add(Localization.translate("iu.blastfurnace.info6"));
    }


    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (this.getMain() != null) {
            return ((TileMultiBlockBase) this.getMain()).getCapability(cap, side);
        }
        return super.getCapability(cap, side);
    }


    public IMultiTileBlock getTeBlock() {
        return BlockBlastFurnace.blast_furnace_input;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blastfurnace.getBlock(getTeBlock().getId());
    }
}
