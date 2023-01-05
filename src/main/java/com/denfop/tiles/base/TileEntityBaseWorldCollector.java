package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.container.ContainerWorldCollector;
import com.denfop.gui.GuiWolrdCollector;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.invslot.InvSlotWorldCollector;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityBaseWorldCollector extends TileEntityElectricMachine implements IUpdateTick, IUpgradableBlock,
        IHasRecipe {

    public final EnumTypeCollector enumTypeCollector;
    public final InvSlotRecipes inputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotWorldCollector MatterSlot;
    public final double max_matter_energy;
    private final int defaultEnergyConsume;
    private final int defaultOperationLength;
    public double matter_energy;
    public double need_matter;
    public double guiProgress;
    protected MachineRecipe machineRecipe;
    private int energyConsume;
    private int operationLength;
    private int operationsPerTick;
    private boolean canWork = false;
    private int progress;


    public TileEntityBaseWorldCollector(EnumTypeCollector enumTypeCollector1) {
        super(0, 1, 1);
        enumTypeCollector = enumTypeCollector1;
        this.MatterSlot = new InvSlotWorldCollector(this, "matterslot");
        this.inputSlot = new InvSlotRecipes(this, enumTypeCollector1.name().toLowerCase() + "collector", this);
        this.machineRecipe = null;
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.defaultEnergyConsume = this.energyConsume = 40;
        this.defaultOperationLength = this.operationLength = 800;
        this.operationsPerTick = 1;
        this.matter_energy = 0;
        this.max_matter_energy = 1000;
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        tooltip.add(Localization.translate("iu.need_info") + new ItemStack(
                IUItem.matter,
                1,
                enumTypeCollector.getMeta()
        ).getDisplayName());
        super.addInformation(stack, tooltip, advanced);
    }

    protected void addRecipe(ItemStack input, double need, ItemStack output) {
        final IRecipeInputFactory input_recipe = ic2.api.recipe.Recipes.inputFactory;
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setDouble("need", need);
        Recipes.recipes.addRecipe(
                enumTypeCollector.name().toLowerCase() + "collector",
                new BaseMachineRecipe(new Input(input_recipe.forStack(input)), new RecipeOutput(nbt, output))
        );
    }

    protected void addRecipe(String input, ItemStack output, double need) {
        final IRecipeInputFactory input_recipe = ic2.api.recipe.Recipes.inputFactory;
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setDouble("need", getMatterFromEnergy(need));
        Recipes.recipes.addRecipe(
                enumTypeCollector.name().toLowerCase() + "collector",
                new BaseMachineRecipe(new Input(input_recipe.forOreDict(input)), new RecipeOutput(nbt, output))
        );
    }

    public MachineRecipe getOutput() {
        this.machineRecipe = this.inputSlot.process();

        return this.machineRecipe;
    }

    protected double getMatterFromEnergy(double energy) {
        return energy / 250000D;
    }

    protected void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            inputSlot.load();
            this.setOverclockRates();
            this.getOutput();
            if (this.machineRecipe != null) {
                this.getrequiredmatter(machineRecipe.getRecipe().getOutput());
            } else {
                this.getrequiredmatter(null);
            }
            this.MatterSlot.getmatter();
        }

    }

    public void operate(MachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick; i++) {

            operateOnce(output.getRecipe().output.items);

            if (this.inputSlot.get(0).isEmpty() || this.inputSlot
                    .get(0)
                    .getCount() < this.machineRecipe.getRecipe().input
                    .getInputs()
                    .get(0)
                    .getInputs()
                    .get(0)
                    .getCount() || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                this.getOutput();
                break;
            } else {
                if (this.inputSlot.get(0).isEmpty() || this.inputSlot
                        .get(0)
                        .getCount() < 1 || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                    this.getOutput();
                    break;
                }
            }


        }


    }

    public void operateOnce(List<ItemStack> processResult) {

        this.matter_energy -= this.need_matter;
        this.inputSlot.consume();
        this.outputSlot.add(processResult);
        this.getrequiredmatter(this.machineRecipe.getRecipe().getOutput());


    }

    public void setOverclockRates() {
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength(this.defaultOperationLength);
        this.energyConsume = this.upgradeSlot.getEnergyDemand(this.defaultEnergyConsume);


    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount, boolean consume) {
        if (this.energy.canUseEnergy(amount)) {
            if (consume) {
                this.energy.useEnergy(amount);
            }
            return true;
        }
        return false;
    }

    public boolean useEnergy(double amount) {
        if (this.energy.canUseEnergy(amount)) {
            this.energy.useEnergy(amount);
            return true;
        }
        return false;
    }

    public double getGuiProgress() {
        return guiProgress;
    }

    public double getMatter_energy() {
        return matter_energy;
    }

    public double getMax_matter_energy() {
        return max_matter_energy;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.matter_energy = nbttagcompound.getDouble("matter_energy");
        this.progress = nbttagcompound.getInteger("progress");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        final NBTTagCompound nbt = super.writeToNBT(nbttagcompound);
        nbt.setDouble("matter_energy", this.matter_energy);
        nbt.setInteger("progress", this.progress);
        return nbt;
    }

    public void updateEntityServer() {

        super.updateEntityServer();


        MachineRecipe output = this.machineRecipe;
        if (output != null && this.outputSlot.canAdd(this.machineRecipe.getRecipe().output.items) && !this.inputSlot.isEmpty() && this.canWork && this.need_matter <= this.matter_energy) {
            if (!this.getActive()) {
                setActive(true);
            }
            if (this.world.provider.getWorldTime() % 20 == 0) {
                this.MatterSlot.getmatter();
            }
            this.progress++;

            double p = (this.progress * 1D / operationLength);


            if (p <= 1) {
                this.guiProgress = p;
            }
            if (p > 1) {
                this.guiProgress = 1;
            }
            if (progress >= operationLength) {

                operate(output);
                this.progress = 0;


            }
        } else {
            if (progress != 0) {
                progress = 0;
            }
            this.guiProgress = 0;
            if (this.getActive()) {
                setActive(false);
            }
        }
        if ((!this.inputSlot.isEmpty() || !this.outputSlot.isEmpty()) && this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

    }

    @Override
    public ContainerWorldCollector getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerWorldCollector(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiWolrdCollector getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiWolrdCollector(getGuiContainer(entityPlayer));
    }

    @Override
    public void onUpdate() {

        if (this.machineRecipe == null) {
            getrequiredmatter(null);
        } else {
            getrequiredmatter(this.machineRecipe.getRecipe().getOutput());
        }
        this.MatterSlot.getmatter();
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing
        );
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.machineRecipe;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.machineRecipe = output;
    }

    public void getrequiredmatter(RecipeOutput output) {
        if (output == null) {
            this.need_matter = 0;
            this.canWork = false;
            return;
        }
        this.canWork = true;
        this.need_matter = output.metadata.getDouble("need");
    }

    @Override
    public void init() {

    }

}
