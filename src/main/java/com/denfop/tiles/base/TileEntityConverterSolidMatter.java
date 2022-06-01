package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.container.ContainerConverterSolidMatter;
import com.denfop.gui.GuiConverterSolidMatter;
import com.denfop.invslot.InvSlotConverterSolidMatter;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityConverterSolidMatter extends TileEntityElectricMachine
        implements IHasGui, INetworkTileEntityEventListener, IUpgradableBlock, IUpdateTick {

    public final double[] quantitysolid = new double[8];
    public final InvSlotConverterSolidMatter MatterSlot;
    public final InvSlotRecipes inputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final int defaultOperationLength;
    public final int defaultEnergyConsume;
    public int energyConsume;
    public double defaultEnergyStorage;
    public AudioSource audioSource;
    public double progress;
    public double guiProgress = 0;
    public int operationLength;
    public int operationsPerTick;
    public MachineRecipe output;
    public boolean required;

    public TileEntityConverterSolidMatter() {
        super(50000, 14, 1);
        this.MatterSlot = new InvSlotConverterSolidMatter(this, "input");
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 3);
        this.inputSlot = new InvSlotRecipes(this, "converter", this);
        this.progress = 0;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultEnergyStorage = 50000;
        this.defaultEnergyConsume = this.energyConsume = 2;
    }

    public static void init() {

        addrecipe(new ItemStack(Blocks.STONE), 0.5, 0, 0, 0, 0, 0.25, 0, 0);
        addrecipe(new ItemStack(Blocks.GRASS), 0.5, 0, 0, 0, 0, 0.25, 0, 0);
        addrecipe(new ItemStack(Blocks.GRAVEL), 0.5, 0, 0, 0, 0, 0.25, 0, 0);
        addrecipe(new ItemStack(Blocks.GOLD_ORE), 1, 0, 0, 0, 0, 4, 0, 0);
        addrecipe(new ItemStack(Blocks.COAL_ORE), 1, 0, 0, 0, 0, 2, 0, 0);
        addrecipe(new ItemStack(Blocks.GOLD_BLOCK), 45, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.GLOWSTONE), 1, 0, 0, 3, 0, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.GLASS), 2, 0, 0.5, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.FURNACE), 2, 0, 0, 0, 0, 1, 0, 0);
        addrecipe(new ItemStack(Blocks.END_STONE), 0.5, 0, 0, 0, 0, 0, 0.25, 0);
        for (int i = 0; i < IUItem.name_mineral.size(); i++) {
            addrecipe(new ItemStack(IUItem.iuingot, 1, i), 2, 0, 0, 0, 0, 6, 0, 0);
        }

    }

    public static void addrecipe(
            ItemStack stack,
            double matter,
            double sunmatter,
            double aquamatter,
            double nethermatter,
            double nightmatter,
            double earthmatter,
            double endmatter,
            double aermatter
    ) {
        NBTTagCompound nbt = new NBTTagCompound();
        double[] quantitysolid = {matter, sunmatter, aquamatter, nethermatter, nightmatter, earthmatter, endmatter, aermatter};
        for (int i = 0; i < quantitysolid.length; i++) {
            ModUtils.SetDoubleWithoutItem(nbt, ("quantitysolid_" + i), quantitysolid[i]);
        }
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        final int[] i = OreDictionary.getOreIDs(stack);
        if (i.length < 1) {
            Recipes.recipes.addRecipe("converter", new BaseMachineRecipe(new Input(input.forStack(stack)), new RecipeOutput(
                    nbt,
                    stack
            )));
        } else {
            Recipes.recipes.addRecipe(
                    "converter",
                    new BaseMachineRecipe(new Input(input.forOreDict(OreDictionary.getOreName(i[0]))), new RecipeOutput(
                            nbt,
                            stack
                    ))
            );
        }

    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.convertersolidmatter);
    }

    public void setOverclockRates() {
        this.upgradeSlot.onChanged();
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength(this.defaultOperationLength);


    }

    @Override
    public void onNetworkUpdate(String field) {

    }


    public double getProgress() {
        return this.guiProgress;
    }

    public void updateEntityServer() {

        super.updateEntityServer();


        MachineRecipe output = this.output;
        boolean needsInvUpdate = false;
        if (output != null && this.outputSlot.canAdd(this.output.getRecipe().output.items) && !this.inputSlot.isEmpty() && this.required) {
            setActive(true);


            if (this.useEnergy(this.energyConsume, false) && this.required) {
                this.progress++;
                this.useEnergy(this.energyConsume, true);
                needsInvUpdate = true;
            }

            double p = (this.progress / operationLength);


            if (p <= 1) {
                this.guiProgress = p;
            }
            if (p > 1) {
                this.guiProgress = 1;
            }
            if (progress >= operationLength && this.required) {

                operate(output);
                this.progress = 0;


            }
        } else {


            setActive(false);
        }
        needsInvUpdate |= this.upgradeSlot.tickNoMark();
        if (needsInvUpdate) {
            super.markDirty();
        }
    }

    private void useMatter(RecipeOutput output) {
        NBTTagCompound nbt = output.metadata;
        double[] outputmatter = new double[9];
        for (int i = 0; i < this.quantitysolid.length; i++) {
            outputmatter[i] = nbt.getDouble(("quantitysolid_" + i));
        }
        for (int i = 0; i < this.quantitysolid.length; i++) {
            this.quantitysolid[i] -= outputmatter[i];
        }
    }

    public void getrequiredmatter(RecipeOutput output) {
        if (output == null) {
            return;
        }


        NBTTagCompound nbt = output.metadata;
        double[] outputmatter = new double[9];

        for (int i = 0; i < this.quantitysolid.length; i++) {
            outputmatter[i] = nbt.getDouble(("quantitysolid_" + i));
        }
        for (int i = 0; i < this.quantitysolid.length; i++) {
            if (!(this.quantitysolid[i] >= outputmatter[i])) {
                this.required = false;
                return;
            }
        }
        this.required = true;

    }


    public void operate(MachineRecipe output) {
        List<ItemStack> processResult = output.getRecipe().output.items;
        operateOnce(processResult, output);
    }

    public void operateOnce(List<ItemStack> processResult, MachineRecipe output) {
        useMatter(output.getRecipe().getOutput());
        this.outputSlot.add(processResult);
        this.getrequiredmatter(this.output.getRecipe().getOutput());
        this.MatterSlot.getmatter();
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        inputSlot.load();
        this.getOutput();
        if (this.output != null) {
            this.getrequiredmatter(this.output.getRecipe().getOutput());
        }
        this.MatterSlot.getmatter();

    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlot.process();

        return this.output;
    }


    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
        }
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getDouble("progress");
        for (int i = 0; i < this.quantitysolid.length; i++) {
            this.quantitysolid[i] = nbttagcompound.getDouble("quantitysolid" + i);
        }
    }


    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("progress", this.progress);
        for (int i = 0; i < this.quantitysolid.length; i++) {
            nbttagcompound.setDouble("quantitysolid" + i, this.quantitysolid[i]);
        }

        return nbttagcompound;

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


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiConverterSolidMatter(new ContainerConverterSolidMatter(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityConverterSolidMatter> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerConverterSolidMatter(entityPlayer, this);
    }


    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    @Override
    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IC2.audioManager.createSource(this, getStartSoundFile());
        }
        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (getInterruptSoundFile() != null) {
                        IC2.audioManager.playOnce(this, getInterruptSoundFile());
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
                break;
        }
    }

    @Override
    public void onGuiClosed(EntityPlayer arg0) {
    }

    @Override
    public String getInventoryName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing
        );
    }

    @Override
    public void onUpdate() {
        this.MatterSlot.getmatter();
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
