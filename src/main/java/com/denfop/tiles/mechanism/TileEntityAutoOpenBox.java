package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerAutoOpenBox;
import com.denfop.gui.GuiAutoOpenBox;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.recipes.ScrapboxRecipeManager;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityAutoOpenBox extends TileEntityInventory implements IUpgradableBlock {

    public final InvSlot slot;
    public final InvSlot slot1;
    public final Energy energy;
    public final InvSlotUpgrade upgradeSlot;
    public int timer = 20;
    private boolean doublescrap;

    public TileEntityAutoOpenBox() {
        this.slot = new InvSlotOutput(this, 15);
        this.slot1 = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.isItemEqual(IUItem.scrapBox) || stack.getItem() == (IUItem.doublescrapBox);
            }

            @Override
            public void put(int i,final ItemStack content) {
                super.put(i,content);
                if (!content.isEmpty()) {
                    doublescrap = !content.isItemEqual(IUItem.scrapBox);
                }else{
                    doublescrap = false;
                }
            }
        };
        this.energy = this.addComponent(Energy.asBasicSink(this, 100, 1));
        this.upgradeSlot = new InvSlotUpgrade(this, 2);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));

    }

    @Override
    public void addInformation(final ItemStack itemStack, final List<String> info) {


        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            info.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            info.add(Localization.translate("iu.machines_work_energy") + 4 + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            info.add(Localization.translate("iu.machines_work_length") + 1);
        }
        super.addInformation(itemStack, info);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiAutoOpenBox(this.getGuiContainer(var1));
    }

    @Override
    public ContainerAutoOpenBox getGuiContainer(final EntityPlayer var1) {
        return new ContainerAutoOpenBox(this, var1);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.auto_open_box;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.slot1.isEmpty() && energy.getEnergy() > 4) {
            this.setActive(true);
            timer = 20;
            if (doublescrap) {
                for (int i = 0; i < 9; i++) {
                    slot.add(ScrapboxRecipeManager.instance.getRandomDrop());
                }
            } else {
                slot.add(ScrapboxRecipeManager.instance.getRandomDrop());
            }
            this.slot1.get().shrink(1);
            this.energy.useEnergy(4);
        } else {
            if (timer > 0) {
                timer--;
            }
            if (timer == 0) {
                this.setActive(false);
            }
        }
        this.energy.setSinkTier(this.energy.defaultSinkTier + this.upgradeSlot.extraTier);
        this.upgradeSlot.tickNoMark();
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.ItemExtract, UpgradableProperty.Transformer);
    }

}
