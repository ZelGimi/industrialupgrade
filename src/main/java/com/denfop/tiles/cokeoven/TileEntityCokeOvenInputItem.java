package com.denfop.tiles.cokeoven;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCokeOven;
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

public class TileEntityCokeOvenInputItem extends TileEntityMultiBlockElement implements IInputItem {


    public TileEntityCokeOvenInputItem(BlockPos pos, BlockState state) {
        super(BlockCokeOven.coke_oven_input, pos, state);
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {

        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer.getItem()).getDisplayName().getString());

    }

    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (this.getMain() != null) {
            return ((TileMultiBlockBase) this.getMain()).getCapability(cap, side);
        }
        return super.getCapability(cap, side);
    }


    public IMultiTileBlock getTeBlock() {
        return BlockCokeOven.coke_oven_input;
    }

    public BlockTileEntity getBlock() {
        return IUItem.cokeoven.getBlock(getTeBlock());
    }

}
