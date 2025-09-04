package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityElectricLiquidTankInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityCanner extends BlockEntityElectricLiquidTankInventory
        implements BlockEntityUpgrade, IUpdateTick, IHasRecipe {

    public final InventoryRecipes inputSlotA;
    public final Fluids.InternalFluidTank outputTank;
    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProcess componentProcess;
    public final ComponentProgress componentProgress;
    private final ComponentUpgrade componentUpgrades;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe output;
    private int fluid_amount;

    public BlockEntityCanner(BlockPos pos, BlockState state) {
        super(300, 1, 10, BlockBaseMachine3Entity.canner_iu, pos, state);
        fluidTank.setTypeItemSlot(Inventory.TypeItemSlot.INPUT);
        this.inputSlotA = new InventoryRecipes(this, "cannerenrich", this, this.fluidTank);
        Recipes.recipes.addInitRecipes(this);
        this.outputTank = this.fluids.addTankExtract("outputTank", 10000);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1));
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setHasTank(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }


    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.canner_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());
        }
        super.addInformation(stack, tooltip);

    }


    private void switchTanks() {
        FluidStack inputStack = this.fluidTank.getFluid();
        FluidStack outputStack = this.outputTank.getFluid();
        this.fluidTank.setFluid(outputStack);
        this.outputTank.setFluid(inputStack);
    }

    public void updateTileServer(Player player, double event) {
        this.switchTanks();
        this.getOutput();

    }


    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            inputSlotA.load();
            this.getOutput();
        }


    }


    public void updateEntityServer() {
        super.updateEntityServer();

        if (this.output == null && this.fluid_amount != this.fluidTank.getFluidAmount()) {
            this.getOutput();
            this.fluid_amount = this.fluidTank.getFluidAmount();
        }

    }


    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }


    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.canUseEnergy(amount)) {
            this.energy.useEnergy(amount);
            return true;
        }
        return false;
    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Processing, EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage, EnumBlockEntityUpgrade.ItemExtract, EnumBlockEntityUpgrade.ItemInput,
                EnumBlockEntityUpgrade.FluidExtract, EnumBlockEntityUpgrade.FluidInput
        );
    }

    @Override
    public void init() {


    }

    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
