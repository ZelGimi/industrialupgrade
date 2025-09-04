package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentProgress;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuNightConverter;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenNightConverter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.Set;

public class BlockEntityNightConverter extends BlockEntityElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent {


    public final InventoryRecipes inputSlotA;
    public final ComponentBaseEnergy ne;
    public final ComponentProgress progress;
    public MachineRecipe output;

    public BlockEntityNightConverter(BlockPos pos, BlockState state) {
        super(0, 14, 1, BlockBaseMachine3Entity.night_converter, pos, state);
        inputSlotA = new InventoryRecipes(this, "solar_glass_recipe", this);
        inputSlotA.setStackSizeLimit(1);
        this.ne = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.NIGHT, this, 10000));
        this.progress = this.addComponent(new ComponentProgress(this, 1, 80));
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
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.night_converter;
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
        if (this.ne.getEnergy() >= 62.5 && this.output != null) {
            this.progress.addProgress();
            this.setActive(true);
            this.ne.useEnergy(62.5);
            if (this.progress.getBar() >= 1) {
                this.progress.setProgress((short) 0);
                this.inputSlotA.consume();
                this.outputSlot.add(this.output.getRecipe().output.items.get(0));
                getOutput();
            }
        } else {
            this.setActive(false);
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
        return this.output;
    }

    @Override
    public ContainerMenuNightConverter getGuiContainer(final Player var1) {
        return new ContainerMenuNightConverter(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenNightConverter((ContainerMenuNightConverter) menu);
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }


}
