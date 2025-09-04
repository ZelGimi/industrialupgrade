package com.denfop.blockentity.mechanism.blastfurnace.block;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.blastfurnace.api.IBlastOutputItem;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockBase;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBlastFurnaceEntity;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockEntityBlastOutput extends BlockEntityMultiBlockElement implements IBlastOutputItem {


    public BlockEntityBlastOutput(BlockPos pos, BlockState state) {
        super(BlockBlastFurnaceEntity.blast_furnace_output, pos, state);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
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

    public MultiBlockEntity getTeBlock() {
        return BlockBlastFurnaceEntity.blast_furnace_output;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blastfurnace.getBlock(getTeBlock().getId());
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (this.getMain() != null) {
            return ((BlockEntityMultiBlockBase) this.getMain()).getCapability(cap, facing);
        }
        return super.getCapability(cap, facing);
    }


}
