package com.denfop.tiles.adv_cokeoven;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAdvCokeOven;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileEntityCokeOvenInputItem extends TileEntityMultiBlockElement implements IInputItem {
    public TileEntityCokeOvenInputItem(BlockPos pos, BlockState state) {
        super(BlockAdvCokeOven.adv_coke_oven_input, pos, state);
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {

        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer.getItem()).getDisplayName().getString());

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (this.getMain() != null){
            return ((TileMultiBlockBase) this.getMain()).getCapability(cap, facing);
        }
        return super.getCapability(cap, facing);
    }



    public IMultiTileBlock getTeBlock() {
        return BlockAdvCokeOven.adv_coke_oven_input;
    }

    public BlockTileEntity getBlock() {
        return IUItem.adv_cokeoven.getBlock(getTeBlock());
    }

}
