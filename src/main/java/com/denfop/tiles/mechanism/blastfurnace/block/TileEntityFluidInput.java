package com.denfop.tiles.mechanism.blastfurnace.block;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.componets.Fluids;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastInputFluid;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityFluidInput extends TileEntityMultiBlockElement implements IBlastInputFluid {

    private final Fluids fluids;
    FluidTank tank;

    public TileEntityFluidInput() {
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = fluids.addTank("tank", 10000, InvSlot.Access.I, InvSlot.InvSide.ANY,
                Fluids.fluidPredicate(FluidRegistry.WATER)
        );

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.add(Localization.translate("iu.blastfurnace.info1"));
        tooltip.add(Localization.translate("iu.blastfurnace.info3") + Localization.translate(new ItemStack(
                IUItem.blastfurnace,
                1,
                0
        ).getUnlocalizedName()));
        tooltip.add(Localization.translate("iu.blastfurnace.info4"));
        tooltip.add(Localization.translate("iu.blastfurnace.info5") + Localization.translate(Ic2Items.ForgeHammer.getUnlocalizedName()));
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
