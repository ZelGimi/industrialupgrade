package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerDigger;
import com.denfop.gui.GuiDigger;
import com.denfop.invslot.InvSlotDigger;
import com.denfop.invslot.InvSlotInput;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityAutoDigger extends TileEntityInventory {

    public final InvSlotOutput outputSlot;
    public final Energy energy;
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
        this.outputSlot = new InvSlotOutput(this, 48);
        this.energy = this.addComponent(Energy.asBasicSink(this, 500000, 14));
        this.inputslot = new InvSlotInput(this, 16);

        this.energyconsume = 500;
        this.consume = 500;
        this.slot_upgrade = new InvSlotDigger(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.5));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.5));

    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(Localization.translate("iu.excavator.info"));
        tooltip.add(Localization.translate("iu.excavator.info1"));

        super.addInformation(stack, tooltip);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.auto_digger;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.slot_upgrade.update();
        this.inputslot.update();

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        for (int k = 0; k < this.col; k++) {
            for (int i = 0; i < this.inputslot.size(); i++) {
                final BaseMachineRecipe baseMachineRecipe1 = baseMachineRecipe[i];
                if (baseMachineRecipe1 == null) {
                    continue;
                }
                if (this.energy.canUseEnergy(this.consume) && this.outputSlot.canAdd(baseMachineRecipe1.getOutput().items)) {
                    this.energy.useEnergy(this.consume);
                    for (ItemStack stack : baseMachineRecipe1.getOutput().items) {
                        this.outputSlot.add(stack);
                    }
                    this.inputslot.get(i).shrink(1);
                    if (this.inputslot.get(i).isEmpty()) {
                        baseMachineRecipe[i] = null;
                    }
                }
            }
        }
        if (this.world.getWorldTime() % 20 == 0 && !this.outputSlot.isEmpty()) {
            ModUtils.tick(this.outputSlot, this);
        }
    }

    public void setBaseMachineRecipe(int slotid, final BaseMachineRecipe baseMachineRecipe) {
        this.baseMachineRecipe[slotid] = baseMachineRecipe;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
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


}
