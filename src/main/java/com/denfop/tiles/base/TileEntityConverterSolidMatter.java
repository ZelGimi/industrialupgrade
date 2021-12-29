package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.container.ContainerConverterSolidMatter;
import com.denfop.gui.GUIConverterSolidMatter;
import com.denfop.invslot.InvSlotConverterSolidMatter;
import com.denfop.invslot.InvSlotProcessableConverterSolidMatter;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.RecipeOutput;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityConverterSolidMatter extends TileEntityElectricMachine
        implements IHasGui, INetworkTileEntityEventListener, IUpgradableBlock {

    public final double[] quantitysolid = new double[8];
    public final InvSlotConverterSolidMatter MatterSlot;
    public final InvSlotProcessableConverterSolidMatter inputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final int defaultOperationLength;
    public int energyConsume;
    public final int defaultEnergyConsume;
    public double defaultEnergyStorage;
    public AudioSource audioSource;
    public double progress;
    public double guiProgress = 0;
    public int operationLength;
    public int operationsPerTick;

    public TileEntityConverterSolidMatter() {
        super("", 50000, 14, 1);
        this.MatterSlot = new InvSlotConverterSolidMatter(this, "input");
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 3);
        this.inputSlot = new InvSlotProcessableConverterSolidMatter(this, "inputA", 1, Recipes.matterrecipe);
        this.progress = 0;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultEnergyStorage = 50000;
        this.defaultEnergyConsume = this.energyConsume = 2;
    }

    public void setOverclockRates() {
        this.upgradeSlot.onChanged();
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength(this.defaultOperationLength);
        this.energyConsume = this.upgradeSlot.getEnergyDemand(this.defaultEnergyConsume);

        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                (int) this.defaultEnergyStorage,
                this.defaultOperationLength,
                this.defaultEnergyConsume
        ));

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
        Recipes.matterrecipe.addRecipe(input.forStack(stack), nbt, false, stack);
    }


    @Override
    public void onNetworkUpdate(String field) {

    }


    public double getProgress() {
        return this.guiProgress;
    }

    public void updateEntityServer() {

        super.updateEntityServer();

        this.MatterSlot.getmatter();
        RecipeOutput output = getOutput();
        boolean needsInvUpdate = false;
        if (output != null) {
            setActive(true);

            if (this.energy.getEnergy() == 0) {
                IC2.network.get(true).initiateTileEntityEvent(this, 0, true);
            }
            if (this.useEnergy(this.energyConsume, false) && this.getrequiredmatter(output)) {
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
            if (progress >= operationLength && this.getrequiredmatter(output)) {

                operate(output);
                this.progress = 0;


                IC2.network.get(true).initiateTileEntityEvent(this, 2, true);
            }
        } else {
            if (getActive()) {
                IC2.network.get(true).initiateTileEntityEvent(this, 1, true);
            }

            setActive(false);
        }
        needsInvUpdate |= this.upgradeSlot.tickNoMark();
        if (needsInvUpdate) {
            super.markDirty();
        }
    }

    private void useMatter(RecipeOutput output) {
        if (inputSlot.isEmpty()) {
            return;
        }
        ItemStack stack = this.inputSlot.get(0);


        NBTTagCompound nbt = output.metadata;
        double[] outputmatter = new double[9];
        for (int i = 0; i < this.quantitysolid.length; i++) {
            outputmatter[i] = nbt.getDouble(("quantitysolid_" + i));
        }
        for (int i = 0; i < this.quantitysolid.length; i++) {
            this.quantitysolid[i] -= outputmatter[i];
        }
    }

    public boolean getrequiredmatter(RecipeOutput output) {
        NBTTagCompound nbt = output.metadata;
        double[] outputmatter = new double[9];

        for (int i = 0; i < this.quantitysolid.length; i++) {
            outputmatter[i] = nbt.getDouble(("quantitysolid_" + i));
        }

        for (int i = 0; i < this.quantitysolid.length; i++) {
            if (!(this.quantitysolid[i] >= outputmatter[i])) {
                return false;
            }
        }

        return true;
    }


    public void operate(RecipeOutput output) {

        List<ItemStack> processResult = output.items;

        operateOnce(processResult, output);


    }

    public void operateOnce(List<ItemStack> processResult, RecipeOutput output) {
        useMatter(output);
        this.outputSlot.add(processResult);
    }


    public RecipeOutput getOutput() {
        if (this.inputSlot.isEmpty()) {
            return null;
        }
        RecipeOutput output = this.inputSlot.process();
        if (output == null) {
            return null;
        }
        if (this.outputSlot.canAdd(output.items)) {
            return output;
        }
        return null;
    }


    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
        }
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        for (int i = 0; i < this.quantitysolid.length; i++) {
            this.quantitysolid[i] = nbttagcompound.getDouble(("quantitysolid_" + i));
        }
        this.progress = nbttagcompound.getDouble("progress");

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
        for (int i = 0; i < this.quantitysolid.length; i++) {
            nbttagcompound.setDouble(("quantitysolid_" + i), this.quantitysolid[i]);
        }
        nbttagcompound.setDouble("progress", this.progress);
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

    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIConverterSolidMatter(new ContainerConverterSolidMatter(entityPlayer, this));
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

}
