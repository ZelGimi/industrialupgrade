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
import com.denfop.componets.Fluids;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityCanner extends TileEntityElectricLiquidTankInventory
        implements IUpgradableBlock, IUpdateTick, IHasRecipe {

    public final InvSlotRecipes inputSlotA;
    public final Fluids.InternalFluidTank outputTank;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final InvSlotUpgrade upgradeSlot;
    public Mode mode;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public MachineRecipe output;
    public List<EntityPlayer> entityPlayerList = new ArrayList<>();
    protected short progress;
    protected double guiProgress;
    private int fluid_amount;

    public TileEntityCanner() {
        super(300, 1, 8);
        this.inputSlotA = new InvSlotRecipes(this, "cannerenrich", this, this.fluidTank);
        Recipes.recipes.addInitRecipes(this);
        this.outputTank = this.fluids.addTankExtract("outputTank", 8000);
        this.mode = Mode.cannerEnrich;
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 300;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 300;
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 4);


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

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");
        this.mode = Mode.values[nbttagcompound.getShort("mode")];
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
        nbttagcompound.setShort("mode", (short) this.mode.ordinal());
        return nbttagcompound;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);
        }
        super.addInformation(stack, tooltip, advanced);

    }

    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("mode")) {
            this.setMode(this.mode);
        }

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

    public double getProgress() {
        return this.guiProgress;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
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

    public void setOverclockRates() {
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength(this.defaultOperationLength);
        this.energyConsume = this.upgradeSlot.getEnergyDemand(this.defaultEnergyConsume);
        int tier = this.upgradeSlot.getTier(this.defaultTier);
        this.energy.setSinkTier(tier);
        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                this.defaultEnergyStorage
        ));
        if (this.operationLength < 1) {
            this.operationLength = 1;
        }
    }

    public void operate(MachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick; i++) {
            List<ItemStack> processResult = output.getRecipe().output.items;
            operateOnce(processResult);

            if (!this.inputSlotA.continue_process(this.output) || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                getOutput();
                break;
            }
            if (this.output == null) {
                break;
            }
        }
    }

    public void operateOnce(List<ItemStack> processResult) {

        this.inputSlotA.consume();
        this.outputSlot.add(processResult);
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        MachineRecipe output = this.output;
        if (this.output == null && this.fluid_amount != this.fluidTank.getFluidAmount()) {
            this.getOutput();
            this.fluid_amount = this.fluidTank.getFluidAmount();
        }


        if (this.output != null && this.energy.canUseEnergy(energyConsume) && this.outputSlot.canAdd(
                this.output.getRecipe().getOutput().items) && (this.output.getRecipe().input.getFluid() == null || (
                this.fluidTank.getFluid() != null && this.fluidTank
                        .getFluid()
                        .getFluid()
                        .equals(this.output.getRecipe().input
                                .getFluid()
                                .getFluid()) && this.fluidTank.getFluidAmount() >= this.output.getRecipe().input.getFluid().amount))) {
            if (!this.getActive()) {
                setActive(true);
            }
            if (this.progress == 0) {
                initiate(0);
            }
            this.progress = (short) (this.progress + 1);
            this.energy.useEnergy(energyConsume);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(output);
                this.progress = 0;
                initiate(2);
            }
        } else {
            if (this.progress != 0 && getActive()) {
                initiate(1);
            }
            if (output == null) {
                this.progress = 0;
            }
            if (this.getActive()) {
                setActive(false);
            }
        }
        if (this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
            this.getOutput();
        }

    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;


    }

    @Override
    public ContainerCanner getGuiContainer(final EntityPlayer var1) {
        if (!this.entityPlayerList.contains(var1)) {
            this.entityPlayerList.add(var1);
        }
        return new ContainerCanner(this, var1);
    }

    @Override
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

    public enum Mode {
        cannerEnrich;

        public static final Mode[] values = values();

        Mode() {
        }
    }

}
