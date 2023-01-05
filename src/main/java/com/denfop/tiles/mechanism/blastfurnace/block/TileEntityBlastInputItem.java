package com.denfop.tiles.mechanism.blastfurnace.block;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.blastfurnace.api.BlastSystem;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastInputItem;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastMain;
import com.denfop.tiles.mechanism.blastfurnace.api.InvSlotBlastFurnace;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityBlastInputItem extends TileEntityInventory implements IBlastInputItem {

    InvSlotBlastFurnace invSlotBlastFurnace = new InvSlotBlastFurnace(this);
    IBlastMain blastMain;

    @Override
    public InvSlotBlastFurnace getInput() {
        return invSlotBlastFurnace;
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
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);

        BlastSystem.instance.update(this.pos, world, this);
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
    protected void onUnloaded() {
        if (this.getMain() != null) {
            if (this.getMain().getFull()) {
                this.getMain().setFull(false);
                this.getMain().setInputItem(null);
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
