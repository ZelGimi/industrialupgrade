package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blockentity.base.IManufacturerBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentTimer;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuMoonSpotter;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenMoonSpotter;
import com.denfop.utils.Localization;
import com.denfop.utils.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityMoonSpotter extends BlockEntityElectricMachine implements
        BlockEntityUpgrade, IUpdateTick, IUpdatableTileEvent, IHasRecipe, IManufacturerBlock {

    public final ComponentTimer timer;
    public final InventoryRecipes inputSlotA;
    public final InventoryUpgrade upgradeSlot;
    public MachineRecipe output;
    public int levelBlock;

    public BlockEntityMoonSpotter(BlockPos pos, BlockState state) {
        super(0, 14, 1, BlockBaseMachine3Entity.moon_spotter, pos, state);
        Recipes.recipes.addInitRecipes(this);
        inputSlotA = new InventoryRecipes(this, "solar_glass_recipe", this);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        inputSlotA.setStackSizeLimit(1);
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(0, 1, 0)) {
            @Override
            public int getTickFromSecond() {
                return (int) Math.max(1, 20 - ((BlockEntityMoonSpotter) this.parent).getLevelMechanism() * 1.75);
            }
        });
        this.levelBlock = 0;
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));

    }

    public static void addRecipe(int container) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "solar_glass_recipe",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.solar_day_glass.getStack(container), 1))),
                        new RecipeOutput(null, new ItemStack(IUItem.solar_night_glass.getStack(container), 1))
                )
        );
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("moon_spotter.info"));
    }

    @Override
    public int getLevelMechanism() {
        return this.levelBlock;
    }

    @Override
    public void setLevelMech(final int level) {
        this.levelBlock = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.levelBlock -= level;
    }

    @Override
    public void init() {
        for (int i = 0; i < 14; i++) {
            addRecipe(i);
        }
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (levelBlock < 10) {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation.getItem())) {
                return super.onActivated(player, hand, side, vec3);
            } else {
                stack.shrink(1);
                this.levelBlock++;
                return true;
            }
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }


    public List<ItemStack> getWrenchDrops(Player player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.levelBlock != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation.getItem(), this.levelBlock));
            this.levelBlock = 0;
        }
        return ret;
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.levelBlock = nbttagcompound.getInt("level");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("level", this.levelBlock);
        return nbttagcompound;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.moon_spotter;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            inputSlotA.load();
            this.getOutput();
        }


    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().isDay()) {
            this.timer.setCanWorkWithOut(false);
            this.setActive(false);
            return;
        }
        if (this.inputSlotA.get(0).isEmpty() || this.output == null || this.outputSlot.get(0).getCount() >= 64) {
            this.timer.setCanWork(false);
            this.setActive(false);
            return;
        }
        this.setActive(true);
        if (!this.timer.isCanWork()) {
            this.timer.setCanWork(true);
        }
        if (this.timer.getTimers().get(0).getTime() <= 0) {
            this.inputSlotA.consume();
            this.outputSlot.add(this.output.getRecipe().output.items.get(0));
            getOutput();
        }
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        this.timer.resetTime();
        return this.output;
    }

    @Override
    public ContainerMenuMoonSpotter getGuiContainer(final Player var1) {
        return new ContainerMenuMoonSpotter(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenMoonSpotter((ContainerMenuMoonSpotter) menu);
    }

    @Override
    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(
                EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage,
                EnumBlockEntityUpgrade.ItemExtract,
                EnumBlockEntityUpgrade.ItemInput
        );
    }


}
