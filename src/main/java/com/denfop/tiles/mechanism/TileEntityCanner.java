package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.Ic2Items;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.Fluids;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerCanner;
import com.denfop.gui.GuiCanner;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityElectricLiquidTankInventory;
import com.denfop.utils.ModUtils;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.item.type.CraftingItemType;
import ic2.core.item.type.DustResourceType;
import ic2.core.ref.FluidName;
import ic2.core.ref.ItemName;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityCanner extends TileEntityElectricLiquidTankInventory
        implements IUpgradableBlock, IUpdateTick, IHasRecipe {

    public final InvSlotRecipes inputSlotA;
    public final Fluids.InternalFluidTank outputTank;
    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProcess componentProcess;
    public final ComponentProgress componentProgress;
    private final ComponentUpgrade componentUpgrades;
    public MachineRecipe output;
    private int fluid_amount;

    public TileEntityCanner() {
        super(300, 1, 8);
        this.inputSlotA = new InvSlotRecipes(this, "cannerenrich", this, this.fluidTank);
        Recipes.recipes.addInitRecipes(this);
        this.outputTank = this.fluids.addTankExtract("outputTank", 8000);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1));
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setHasTank(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT,TypeUpgrade.STACK));

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    public static void addEnrichRecipe(Fluid input, ItemStack additive, Fluid output) {
        Recipes.recipes.addRecipe("cannerenrich", new BaseMachineRecipe(
                new Input(
                        new FluidStack(input, 1000),
                        ic2.api.recipe.Recipes.inputFactory.forStack(Ic2Items.FluidCell),
                        ic2.api.recipe.Recipes.inputFactory.forStack(additive)
                ),
                new RecipeOutput(null, ModUtils.getCellFromFluid(output))
        ));
        Recipes.recipes.addRecipe("cannerenrich", new BaseMachineRecipe(
                new Input(
                        ic2.api.recipe.Recipes.inputFactory.forStack(ModUtils.getCellFromFluid(input)),
                        ic2.api.recipe.Recipes.inputFactory.forStack(additive)
                ),
                new RecipeOutput(null, ModUtils.getCellFromFluid(output))
        ));
    }

    public static void addEnrichRecipe(Fluid input, String additive, int i, Fluid output) {
        Recipes.recipes.addRecipe("cannerenrich", new BaseMachineRecipe(
                new Input(
                        ic2.api.recipe.Recipes.inputFactory.forStack(ModUtils.getCellFromFluid(input)),
                        ic2.api.recipe.Recipes.inputFactory.forOreDict(additive, i)
                ),
                new RecipeOutput(null, ModUtils.getCellFromFluid(output))
        ));
        Recipes.recipes.addRecipe("cannerenrich", new BaseMachineRecipe(
                new Input(
                        new FluidStack(input, 1000),
                        ic2.api.recipe.Recipes.inputFactory.forStack(Ic2Items.FluidCell),
                        ic2.api.recipe.Recipes.inputFactory.forOreDict(additive, i)
                ),
                new RecipeOutput(null, ModUtils.getCellFromFluid(output))
        ));
    }

    public static void addEnrichRecipe(FluidStack input, ItemStack additive, Fluid output) {
        Recipes.recipes.addRecipe("cannerenrich", new BaseMachineRecipe(
                new Input(
                        input,
                        ic2.api.recipe.Recipes.inputFactory.forStack(Ic2Items.FluidCell),
                        ic2.api.recipe.Recipes.inputFactory.forStack(additive)
                ),
                new RecipeOutput(null, ModUtils.getCellFromFluid(output))
        ));
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getDefaultEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getDefaultOperationLength());
        }
        super.addInformation(stack, tooltip, advanced);

    }


    private void switchTanks() {
        FluidStack inputStack = this.fluidTank.getFluid();
        FluidStack outputStack = this.outputTank.getFluid();
        this.fluidTank.setFluid(outputStack);
        this.outputTank.setFluid(inputStack);
    }

    public void onNetworkEvent(EntityPlayer player, int event) {
        this.switchTanks();
        this.getOutput();

    }


    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            inputSlotA.load();
            this.getOutput();
        }


    }

    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
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

    @Override
    public ContainerCanner getGuiContainer(final EntityPlayer var1) {

        return new ContainerCanner(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiCanner getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiCanner(getGuiContainer(var1));
    }

    @Override
    public boolean canFill(final Fluid var2) {
        return false;
    }

    @Override
    public boolean canDrain(final Fluid var2) {
        return false;
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

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing
        );
    }

    @Override
    public void init() {
        if (IUCore.isHasVersion("ic2", "220")) {
            addEnrichRecipe(FluidRegistry.WATER, ItemName.dust.getItemStack(DustResourceType.milk), FluidName.milk.getInstance());
        }
        addEnrichRecipe(
                FluidRegistry.WATER,
                ItemName.crafting.getItemStack(CraftingItemType.cf_powder),
                FluidName.construction_foam.getInstance()
        );
        addEnrichRecipe(FluidRegistry.WATER, "dustLapis", 8, FluidName.coolant.getInstance());
        addEnrichRecipe(FluidName.distilled_water.getInstance(), "dustLapis", 1, FluidName.coolant.getInstance());
        addEnrichRecipe(
                FluidRegistry.WATER,
                ItemName.crafting.getItemStack(CraftingItemType.bio_chaff),
                FluidName.biomass.getInstance()
        );
        addEnrichRecipe(new FluidStack(FluidRegistry.WATER, 6000), new ItemStack(
                Items.STICK), FluidName.hot_water.getInstance());

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


}
