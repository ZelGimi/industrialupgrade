package com.denfop.tiles.mechanism.blastfurnace.block;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.blastfurnace.api.BlastSystem;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastInputFluid;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastMain;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquidByList;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;

public class TileEntityFluidInput extends TileEntityInventory implements IBlastInputFluid {

    private final InvSlotConsumableLiquidByList fluidSlot;
    private final Fluids fluids;
    IBlastMain blastMain;
    FluidTank tank;
    InvSlotOutput output;

    public TileEntityFluidInput() {
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = fluids.addTank("tank", 10000, InvSlot.Access.I, InvSlot.InvSide.ANY,
                Fluids.fluidPredicate(FluidRegistry.WATER)
        );
        this.fluidSlot = new InvSlotConsumableLiquidByList(this, "fluidSlot", 1, FluidRegistry.WATER);
        output = new InvSlotOutput(this, "output1", 1);
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
    public IBlastMain getMain() {
        return blastMain;
    }

    @Override
    public void setMain(final IBlastMain blastMain) {
        this.blastMain = blastMain;
    }

    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (this.getMain() != null) {
            return ((TileEntityBlastFurnaceMain) this.getMain()).onActivated(player, hand, side, hitX, hitY, hitZ);
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        MutableObject<ItemStack> output1 = new MutableObject<>();
        if (this.fluidSlot.transferToTank(
                this.tank,
                output1,
                true
        ) && (output1.getValue() == null || this.output.canAdd(output1.getValue()))) {
            this.fluidSlot.transferToTank(this.tank, output1, false);
            if (output1.getValue() != null) {
                this.output.add(output1.getValue());
            }
        }
    }

    @Override
    public FluidTank getFluidTank() {
        return tank;
    }

    @Override
    public InvSlotOutput getInvSlotOutput() {
        return this.output;
    }

    @Override
    public InvSlotConsumableLiquidByList getInvSlotConsumableLiquidBy() {
        return this.fluidSlot;
    }

    @Override
    public Fluids getFluid() {
        return fluids;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        BlastSystem.instance.update(this.pos, world, this);
    }

    @Override
    protected void onUnloaded() {
        if (this.getMain() != null) {
            if (this.getMain().getFull()) {
                this.getMain().setFull(false);
                this.getMain().setInputFluid(null);
            }
        }
        super.onUnloaded();

    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        BlastSystem.instance.update(this.pos, world, this);
    }

}
