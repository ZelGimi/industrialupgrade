package com.denfop.blockentity.mechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.item.upgrade.UpgradeItem;
import com.denfop.api.item.upgrade.UpgradeModificator;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.item.upgrade.UpgradeWithBlackList;
import com.denfop.api.item.upgrade.event.EventItemBlackListLoad;
import com.denfop.api.item.upgrade.event.EventItemLoad;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityDoubleElectricMachine;
import com.denfop.blockentity.base.EnumDoubleElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockUpgradeBlockEntity;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuDoubleElectricMachine;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.items.modules.ItemUpgradeModule;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenUpgradeBlock;
import com.denfop.sound.EnumSound;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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


                    CompoundTag nbt1 = ModUtils.nbt(stack1);
                    module = module.copy();
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
                        double newCharge = ElectricItem.manager.getCharge(stack1);
                        final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);
                        this.invSlotRecipes.consume();
                        this.outputSlot.add(processResult);
                        ItemStack stack = this.outputSlot.get(0);
                        stack.setTag(nbt1);
                        CompoundTag nbt = ModUtils.nbt(stack);
                        final List<UpgradeModificator> list = UpgradeSystem.system.getListModifications(stack);
                        ListTag modesTagList = nbt.getList("modes", 10);
                        CompoundTag upgrade = new CompoundTag();
                        upgrade.putInt("index", type.ordinal());
                        modesTagList.add(upgrade);
                        nbt.put("modes", modesTagList);
                        ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                        ElectricItem.manager.use(stack, 1, null);
                        EnchantmentHelper.setEnchantments(enchantmentMap, stack);
                        MinecraftForge.EVENT_BUS.post(new EventItemLoad(level, (UpgradeItem) stack.getItem(), stack));
                    } else if (module.getItem() instanceof ItemQuarryModule && IUItem.module9.getMeta((ItemQuarryModule) module.getItem()) == 12) {
                        CompoundTag nbt2 = ModUtils.nbt(module);
                        double newCharge = ElectricItem.manager.getCharge(stack1);
                        final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);
                        this.invSlotRecipes.consume();
                        this.outputSlot.add(processResult);
                        ItemStack stack = this.outputSlot.get(0);
                        stack.setTag(nbt1);
                        CompoundTag nbt = ModUtils.nbt(stack);
                        ListTag tagList = nbt.getList("blacklist", 8);
                        int size = nbt2.getInt("size");
                        for (int j = 0; j < size; j++) {
                            String l = "number_" + j;
                            String temp = nbt2.getString(l);
                            StringTag nbtTagString = StringTag.valueOf(temp);
                            tagList.add(nbtTagString);
                        }
                        nbt.put("blacklist", tagList);
                        ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                        ElectricItem.manager.use(stack, 1, null);
                        EnchantmentHelper.setEnchantments(enchantmentMap, stack);

                        MinecraftForge.EVENT_BUS.post(new EventItemBlackListLoad(
                                level,
                                (UpgradeWithBlackList) stack.getItem(),
                                stack,
                                nbt2
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
                        CompoundTag nbt1 = ModUtils.nbt(stack1);
                        double newCharge = ElectricItem.manager.getCharge(stack1);
                        final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);

                        this.invSlotRecipes.consume();
                        this.outputSlot.add(processResult);
                        ItemStack stack = this.outputSlot.get(0);
                        stack.setTag(nbt1);
                        UpgradeSystem.system.addModificate(
                                stack,
                                this.updateTick.getRecipeOutput().getRecipe().output.metadata.getString("type")
                        );
                        ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                        ElectricItem.manager.use(stack, 1, null);
                        EnchantmentHelper.setEnchantments(enchantmentMap, stack);

                        MinecraftForge.EVENT_BUS.post(new EventItemLoad(level, (UpgradeItem) stack.getItem(), stack));

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

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Processing, EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage, EnumBlockEntityUpgrade.ItemExtract, EnumBlockEntityUpgrade.ItemInput
        );
    }

}
