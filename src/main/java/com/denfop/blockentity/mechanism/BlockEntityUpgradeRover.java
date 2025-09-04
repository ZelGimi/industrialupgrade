package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import com.denfop.api.space.upgrades.event.EventItemLoad;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityDoubleElectricMachine;
import com.denfop.blockentity.base.EnumDoubleElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuDoubleElectricMachine;
import com.denfop.items.space.ItemSpaceUpgradeModule;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenRoverUpgradeBlock;
import com.denfop.sound.EnumSound;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityUpgradeRover extends BlockEntityDoubleElectricMachine implements IHasRecipe {

    public BlockEntityUpgradeRover(BlockPos pos, BlockState state) {
        super(1, 300, 1, EnumDoubleElectricMachine.UPGRADE_ROVER, false, BlockBaseMachine3Entity.upgrade_rover, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((BlockEntityUpgradeRover) this.getParent()).componentProcess;
            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1) {

            @Override
            public void operateWithMax(final MachineRecipe output, final int size) {
                this.operateWithMax(output);
            }

            @Override
            public void operateWithMax(final MachineRecipe output) {
                final List<ItemStack> processResult = this.updateTick.getRecipeOutput().getRecipe().output.items;


                ItemStack stack1 = getUpgradeItem(this.invSlotRecipes.get(0))
                        ? this.invSlotRecipes.get(0)
                        : this.invSlotRecipes.get(1);
                ItemStack module = getUpgradeItem(this.invSlotRecipes.get(0))
                        ? this.invSlotRecipes.get(1)
                        : this.invSlotRecipes.get(0);
                if (module.isEmpty()) {
                    return;
                }

                module = module.copy();
                CompoundTag nbt1 = ModUtils.nbt(stack1);
                if (module.getItem() instanceof ItemSpaceUpgradeModule<?>) {
                    if (SpaceUpgradeSystem.system.getRemaining(stack1) == 0) {
                        this.updateTick.setRecipeOutput(null);
                        return;
                    }
                    EnumTypeUpgrade type = ItemSpaceUpgradeModule.getType(((ItemSpaceUpgradeModule<?>) module.getItem()).getElement().getId());
                    boolean should = SpaceUpgradeSystem.system.shouldUpdate(type, stack1);
                    if (!should) {
                        this.updateTick.setRecipeOutput(null);
                        return;
                    }
                    this.invSlotRecipes.consume();
                    this.outputSlot.addAll(processResult);
                    ItemStack stack = this.outputSlot.get(0);
                    stack.setTag(nbt1);
                    CompoundTag nbt = ModUtils.nbt(stack);
                    ListTag modesTagList = nbt.getList("modes", 10);
                    CompoundTag upgrade = new CompoundTag();
                    upgrade.putInt("index", ((ItemSpaceUpgradeModule<?>) module.getItem()).getElement().getId());
                    modesTagList.add(upgrade);
                    nbt.put("modes", modesTagList);
                    ElectricItem.manager.charge(stack, 1, Integer.MAX_VALUE, true, false);
                    ElectricItem.manager.use(stack, 1, null);
                    MinecraftForge.EVENT_BUS.post(new EventItemLoad(level, (IRoversItem) stack.getItem(), stack));
                }

            }


        });
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
    }

    public static boolean getUpgradeItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof IRoversItem;

    }

    public void init() {

    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.upgrade_rover;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.upgrade_block.getSoundEvent();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenRoverUpgradeBlock((ContainerMenuDoubleElectricMachine) menu);
    }

    @Override
    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        if (this.output == null) {
            return null;
        }
        ItemStack stack1 = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(0) : this.inputSlotA.get(1);
        ItemStack module = getUpgradeItem(this.inputSlotA.get(0)) ? this.inputSlotA.get(1) : this.inputSlotA.get(0);
        if (module.getItem() instanceof ItemSpaceUpgradeModule) {
            if (SpaceUpgradeSystem.system.getRemaining(stack1) == 0) {
                this.output = null;
                this.energy.addEnergy(this.componentProcess.getDefaultEnergyConsume() * this.componentProcess.getDefaultOperationLength());
                return null;
            }
            EnumTypeUpgrade type = ItemSpaceUpgradeModule.getType(((ItemSpaceUpgradeModule<?>) module.getItem()).getElement().getId());
            boolean should = SpaceUpgradeSystem.system.shouldUpdate(type, stack1);
            if (!should) {
                this.energy.addEnergy(this.componentProcess.getDefaultEnergyConsume() * this.componentProcess.getDefaultOperationLength());
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
