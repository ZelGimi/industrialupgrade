package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.Fluids;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerCanner;
import com.denfop.gui.GuiCanner;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileElectricLiquidTankInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileCanner extends TileElectricLiquidTankInventory
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

    public TileCanner() {
        super(300, 1, 8);
        this.inputSlotA = new InvSlotRecipes(this, "cannerenrich", this, this.fluidTank);
        Recipes.recipes.addInitRecipes(this);
        this.outputTank = this.fluids.addTankExtract("outputTank", 8000);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
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


    public static void addEnrichRecipe(FluidStack input, ItemStack additive, Fluid output) {
       int count = input.amount / 1000;
        Recipes.recipes.addRecipe("cannerenrich", new BaseMachineRecipe(
                new Input(
                        input,
                        com.denfop.api.Recipes.inputFactory.getInput(IUItem.FluidCell),
                        com.denfop.api.Recipes.inputFactory.getInput(additive)
                ),
                new RecipeOutput(null, ModUtils.getCellFromFluid(output))
        ));
        ItemStack stack = ModUtils.getCellFromFluid(input.getFluid());
        stack.setCount(count);
        Recipes.recipes.addRecipe("cannerenrich", new BaseMachineRecipe(
                new Input(
                        com.denfop.api.Recipes.inputFactory.getInput(stack),
                        com.denfop.api.Recipes.inputFactory.getInput(additive, 1)
                ),
                new RecipeOutput(null, ModUtils.getCellFromFluid(output))
        ));
    }

    public static void addEnrichRecipe(FluidStack input, int i, String additive, Fluid output) {
        Recipes.recipes.addRecipe("cannerenrich", new BaseMachineRecipe(
                new Input(
                        input,
                        com.denfop.api.Recipes.inputFactory.getInput(IUItem.FluidCell),
                        com.denfop.api.Recipes.inputFactory.getInput(additive, i)
                ),
                new RecipeOutput(null, ModUtils.getCellFromFluid(output))
        ));
        Recipes.recipes.addRecipe("cannerenrich", new BaseMachineRecipe(
                new Input(
                        com.denfop.api.Recipes.inputFactory.getInput(ModUtils.getCellFromFluid(input.getFluid())),
                        com.denfop.api.Recipes.inputFactory.getInput(additive, i)
                ),
                new RecipeOutput(null, ModUtils.getCellFromFluid(output))
        ));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.canner_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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

    public void updateTileServer(EntityPlayer player, double event) {
        this.switchTanks();
        this.getOutput();

    }


    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
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
    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

            return FluidUtil.interactWithFluidHandler(player, hand,
                    this.getComp(Fluids.class).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
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
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing,
                UpgradableProperty.FluidConsuming, UpgradableProperty.FluidProducing
        );
    }

    @Override
    public void init() {


        addEnrichRecipe(new FluidStack(FluidRegistry.WATER, 1000), 8, "dustLapis", FluidName.fluidcoolant.getInstance());
        addEnrichRecipe(new FluidStack(FluidName.fluiddistilled_water.getInstance(), 1000), 1, "dustLapis",
                FluidName.fluidcoolant.getInstance()
        );
        addEnrichRecipe(new FluidStack(FluidRegistry.WATER, 1000), IUItem.cfPowder,
                FluidName.fluidconstruction_foam.getInstance()
        );
        addEnrichRecipe(new FluidStack(FluidRegistry.WATER, 6000), new ItemStack(
                Items.STICK), FluidName.fluidhot_water.getInstance());
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
