package com.denfop.tiles.cokeoven;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCokeOven;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TileEntityHeatBlock extends TileEntityMultiBlockElement implements IHeat {


    public TileEntityHeatBlock(BlockPos pos, BlockState state) {
        super(BlockCokeOven.coke_oven_heat, pos, state);

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getMain() != null) {
            TileCokeOvenMain cokeOvenMain = (TileCokeOvenMain) this.getMain();
            this.setActive(cokeOvenMain.getActive());
        } else {
            this.setActive(false);
        }
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer.getItem()).getDisplayName().getString());

    }

    public IMultiTileBlock getTeBlock() {
        return BlockCokeOven.coke_oven_heat;
    }

    public BlockTileEntity getBlock() {
        return IUItem.cokeoven.getBlock(getTeBlock());
    }

}
