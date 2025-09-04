package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuAutoOpenBox;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.recipes.ScrapboxRecipeManager;
import com.denfop.screen.ScreenAutoOpenBox;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityAutoOpenBox extends BlockEntityInventory implements IUpgradableBlock {

    public final Inventory slot;
    public final Inventory slot1;
    public final Energy energy;
    public final InventoryUpgrade upgradeSlot;
    public int timer = 20;
    private boolean doublescrap;

    public BlockEntityAutoOpenBox(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.auto_open_box, pos, state);
        this.slot = new InventoryOutput(this, 15);
        this.slot1 = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.is(IUItem.scrapBox.getItem()) || stack.getItem() == (IUItem.doublescrapBox).getItem();
            }

            @Override
            public ItemStack set(int i, final ItemStack content) {
                super.set(i, content);
                if (!content.isEmpty()) {
                    doublescrap = !content.is(IUItem.scrapBox.getItem());
                } else {
                    doublescrap = false;
                }
                return content;
            }
        };
        this.energy = this.addComponent(Energy.asBasicSink(this, 100, 1));
        this.upgradeSlot = new InventoryUpgrade(this, 2);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));

    }

    @Override
    public void addInformation(final ItemStack itemStack, final List<String> info) {
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                info.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                info.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }

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
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenAutoOpenBox((ContainerMenuAutoOpenBox) menu);
    }

    @Override
    public ContainerMenuAutoOpenBox getGuiContainer(final Player var1) {
        return new ContainerMenuAutoOpenBox(this, var1);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.auto_open_box;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
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
            this.slot1.get(0).shrink(1);
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
        return EnumSet.of(UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput, UpgradableProperty.EnergyStorage,
                UpgradableProperty.Transformer
        );
    }

}
