package com.denfop.blockentity.mechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.item.upgrade.UpgradeItem;
import com.denfop.api.item.upgrade.UpgradeItemInform;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.item.upgrade.UpgradeWithBlackList;
import com.denfop.api.item.upgrade.event.EventItemBlackListLoad;
import com.denfop.api.item.upgrade.event.EventItemLoad;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityDoubleElectricMachine;
import com.denfop.blockentity.base.EnumDoubleElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockUpgradeBlockEntity;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuDoubleElectricMachine;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.items.modules.ItemUpgradeModule;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenUpgradeBlock;
import com.denfop.sound.EnumSound;
import com.denfop.utils.ElectricItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;

import java.util.*;

import static com.denfop.events.IUEventHandler.getUpgradeItem;

public class BlockEntityUpgradeBlock extends BlockEntityDoubleElectricMachine implements IHasRecipe {

    public BlockEntityUpgradeBlock(BlockPos pos, BlockState state) {
        super(1, 300, 1, EnumDoubleElectricMachine.UPGRADE, false, BlockUpgradeBlockEntity.upgrade_block, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((BlockEntityUpgradeBlock) this.getParent()).componentProcess;
            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1) {

            @Override
            public void operateWithMax(final MachineRecipe output, final int size) {
                this.operateWithMax(output);
            }

            @Override
            public void operateWithMax(final MachineRecipe output) {
                final List<ItemStack> processResult = this.updateTick.getRecipeOutput().getRecipe().output.items;
                if (this.updateTick.getRecipeOutput().getRecipe().output.metadata.getString("type").isEmpty()) {

                    ItemStack stack1 = getUpgradeItem(this.invSlotRecipes.get(0))
                            ? this.invSlotRecipes.get(0)
                            : this.invSlotRecipes.get(1);
                    ItemStack module = getUpgradeItem(this.invSlotRecipes.get(0))
                            ? this.invSlotRecipes.get(1)
                            : this.invSlotRecipes.get(0);
                    if (module.isEmpty()) {
                        return;
                    }


                    DataComponentMap components = stack1.getComponents();
                    if (module.getItem() instanceof ItemUpgradeModule) {
                        if (UpgradeSystem.system.getRemaining(stack1) == 0) {
                            this.updateTick.setRecipeOutput(null);
                            return;
                        }
                        EnumInfoUpgradeModules type = ItemUpgradeModule.getType(IUItem.upgrademodule.getMeta((ItemUpgradeModule) module.getItem()));
                        boolean should = UpgradeSystem.system.shouldUpdate(type, stack1);
                        if (!should) {
                            this.updateTick.setRecipeOutput(null);
                            return;
                        }
                        ItemStack stack = stack1.copy();
                        this.invSlotRecipes.consume();
                        this.outputSlot.add(processResult);
                        this.outputSlot.set(0, stack);
                        com.denfop.datacomponent.UpgradeItem upgradeItem = stack.get(DataComponentsInit.UPGRADE_ITEM);
                        boolean find = false;
                        List<UpgradeItemInform> upgradeItemInformList = upgradeItem.upgradeItemInforms();
                        for (UpgradeItemInform upgradeItemInform : upgradeItemInformList) {
                            if (upgradeItemInform.upgrade == type) {
                                upgradeItemInform.number++;
                                find = true;
                                break;
                            }
                        }
                        if (!find) {
                            upgradeItemInformList.add(new UpgradeItemInform(type, 1));
                        }
                        upgradeItem = upgradeItem.updateUpgrades(stack, upgradeItemInformList);
                        upgradeItem = upgradeItem.updateAmount(stack, upgradeItem.amount() + 1);
                        ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                        ElectricItem.manager.use(stack, 1, null);
                        NeoForge.EVENT_BUS.post(new EventItemLoad(level, (UpgradeItem) stack.getItem(), stack));
                    } else if (module.getItem() instanceof ItemQuarryModule && IUItem.module9.getMeta((ItemQuarryModule) module.getItem()) == 12) {
                        List<String> stringList = module.getOrDefault(DataComponentsInit.LIST_STRING, Collections.emptyList());
                        ItemStack stack = stack1.copy();
                        this.invSlotRecipes.consume();
                        this.outputSlot.add(processResult);
                        this.outputSlot.set(0, stack);
                        com.denfop.datacomponent.UpgradeItem upgradeItem = stack.get(DataComponentsInit.UPGRADE_ITEM);
                        List<String> list = new ArrayList<>(stringList);
                        upgradeItem = upgradeItem.updateBlackList(stack, list);
                        ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                        ElectricItem.manager.use(stack, 1, null);
                        NeoForge.EVENT_BUS.post(new EventItemBlackListLoad(
                                level,
                                (UpgradeWithBlackList) stack.getItem(),
                                stack,
                                new CompoundTag()
                        ));

                    }
                } else {

                    ItemStack stack1 = getUpgradeItem(this.invSlotRecipes.get(0))
                            ? this.invSlotRecipes.get(0)
                            : this.invSlotRecipes.get(1);
                    ItemStack module = getUpgradeItem(this.invSlotRecipes.get(0))
                            ? this.invSlotRecipes.get(1).copy()
                            : this.invSlotRecipes.get(0).copy();
                    boolean need = UpgradeSystem.system.needModificate(stack1, module);

                    if (need) {

                        ItemStack stack = stack1.copy();
                        this.invSlotRecipes.consume();
                        this.outputSlot.add(processResult);
                        this.outputSlot.set(0, stack);

                        UpgradeSystem.system.addModificate(
                                stack,
                                this.updateTick.getRecipeOutput().getRecipe().output.metadata.getString("type")
                        );
                        ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                        ElectricItem.manager.use(stack, 1, null);

                        NeoForge.EVENT_BUS.post(new EventItemLoad(level, (UpgradeItem) stack.getItem(), stack));

                    }
                }
            }


        });
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
    }

    public void init() {

    }

    public MultiBlockEntity getTeBlock() {
        return BlockUpgradeBlockEntity.upgrade_block;
    }

    public BlockTileEntity getBlock() {
        return IUItem.upgradeblock.getBlock();
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.upgrade_block.getSoundEvent();
    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(Player entityPlayer, ContainerMenuBase<?> isAdmin) {
        return new ScreenUpgradeBlock((ContainerMenuDoubleElectricMachine) isAdmin);
    }

    @Override
    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        if (this.output == null) {
            return null;
        }

        if (this.output.getRecipe().output.metadata.getString("type").isEmpty()) {
            ItemStack stack1 = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(0) : this.inputSlotA.get(1);
            ItemStack module = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(1) : this.inputSlotA.get(0);
            if (module.getItem() instanceof ItemUpgradeModule) {
                if (UpgradeSystem.system.getRemaining(stack1) == 0) {
                    this.output = null;
                    this.energy.addEnergy(this.componentProcess.getDefaultEnergyConsume() * this.componentProcess.getDefaultOperationLength());
                    return null;
                }
                EnumInfoUpgradeModules type = ItemUpgradeModule.getType(IUItem.upgrademodule.getMeta((ItemUpgradeModule) module.getItem()));
                boolean should = UpgradeSystem.system.shouldUpdate(type, stack1);
                if (!should) {
                    this.energy.addEnergy(this.componentProcess.getDefaultEnergyConsume() * this.componentProcess.getDefaultOperationLength());
                    this.output = null;
                    return null;
                }
            }
        } else {
            ItemStack stack1 = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(0) : this.inputSlotA.get(1);
            ItemStack module = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(1) : this.inputSlotA.get(0);
            boolean need = UpgradeSystem.system.needModificate(stack1, module);
            if (need) {
                return output;
            } else {
                this.output = null;
                return null;
            }
        }
        return output;

    }

    public String getStartSoundFile() {
        return "Machines/upgrade_block.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
    }

}
