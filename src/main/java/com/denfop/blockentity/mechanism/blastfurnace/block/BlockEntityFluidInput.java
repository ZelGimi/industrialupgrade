package com.denfop.blockentity.mechanism.blastfurnace.block;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.blastfurnace.api.IBlastInputFluid;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBlastFurnaceEntity;
import com.denfop.componets.Fluids;
import com.denfop.inventory.Inventory;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class BlockEntityFluidInput extends BlockEntityMultiBlockElement implements IBlastInputFluid {

    private final Fluids fluids;
    FluidTank tank;

    public BlockEntityFluidInput(BlockPos pos, BlockState state) {
        super(BlockBlastFurnaceEntity.blast_furnace_input_fluid, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = fluids.addTank("tank", 10000, Inventory.TypeItemSlot.INPUT,
                Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.WATER)
        );

    }

    public MultiBlockEntity getTeBlock() {
        return BlockBlastFurnaceEntity.blast_furnace_input_fluid;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blastfurnace.getBlock(getTeBlock().getId());
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


    @Override
    public FluidTank getFluidTank() {
        return tank;
    }


    @Override
    public Fluids getFluid() {
        return fluids;
    }


}
