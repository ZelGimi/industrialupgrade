package com.denfop.tiles.base;

import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerDigger;
import com.denfop.gui.GuiDigger;
import com.denfop.invslot.InvSlotDigger;
import com.denfop.invslot.InvSlotInput;
import ic2.core.IHasGui;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityAutoDigger extends TileEntityInventory implements IHasGui {

    public final InvSlotOutput outputSlot;
    public final AdvEnergy energy;
    public final InvSlotInput inputslot;
    public final InvSlotDigger slot_upgrade;
    public boolean mac_enabled = false;
    public boolean comb_mac_enabled = false;
    public boolean furnace;
    public int chance;
    public int col;
    public BaseMachineRecipe[] baseMachineRecipe = new BaseMachineRecipe[16];
    public double consume;
    public double energyconsume;

    public TileEntityAutoDigger() {
        this.chance = 0;
        this.col = 1;
        this.furnace = false;
        this.outputSlot = new InvSlotOutput(this, "output", 48);
        this.energy = this.addComponent(AdvEnergy.asBasicSink(this, 500000, 14));
        this.inputslot = new InvSlotInput(this, "input", 16);

        this.energyconsume = 500;
        this.consume = 500;
        this.slot_upgrade = new InvSlotDigger(this);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(Localization.translate("iu.excavator.info"));
        tooltip.add(Localization.translate("iu.excavator.info1"));
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.slot_upgrade.update();
        this.inputslot.update();

    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        for (int k = 0; k < this.col; k++) {
            for (int i = 0; i < this.inputslot.size(); i++) {
                final BaseMachineRecipe baseMachineRecipe1 = baseMachineRecipe[i];
                if (baseMachineRecipe1 == null) {
                    continue;
                }
                if (this.energy.canUseEnergy(this.consume) && this.outputSlot.canAdd(baseMachineRecipe1.getOutput().items)) {
                    this.energy.useEnergy(this.consume);
                    this.outputSlot.add(baseMachineRecipe1.getOutput().items);
                    this.inputslot.get(i).shrink(1);
                    if (this.inputslot.get(i).isEmpty()) {
                        baseMachineRecipe[i] = null;
                    }
                }
            }
        }
    }

    public void setBaseMachineRecipe(int slotid, final BaseMachineRecipe baseMachineRecipe) {
        this.baseMachineRecipe[slotid] = baseMachineRecipe;
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public ContainerDigger getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerDigger(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiDigger getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiDigger(getGuiContainer(entityPlayer));
    }

    @Override
    public void onGuiClosed(final EntityPlayer entityPlayer) {

    }

}
