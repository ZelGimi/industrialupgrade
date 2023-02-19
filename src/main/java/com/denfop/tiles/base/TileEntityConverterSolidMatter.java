package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.container.ContainerConverterSolidMatter;
import com.denfop.gui.GuiConverterSolidMatter;
import com.denfop.invslot.InvSlotConverterSolidMatter;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Precision;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.audio.AudioSource;
import ic2.core.block.invslot.InvSlot;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityConverterSolidMatter extends TileEntityElectricMachine
        implements IUpgradableBlock, IUpdateTick, IHasRecipe {

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
    public boolean required = false;
    public double[] outputmatter = new double[9];

    public TileEntityConverterSolidMatter() {
        super(50000, 14, 1);
        this.MatterSlot = new InvSlotConverterSolidMatter(this, "input5");
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 3);
        this.inputSlot = new InvSlotRecipes(this, "converter", this);
        this.progress = 0;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultEnergyStorage = 50000;
        this.defaultEnergyConsume = this.energyConsume = 2;
        Recipes.recipes.addInitRecipes(this);
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
        double[] quantitysolid = {Precision.round(matter, 2),
                Precision.round(sunmatter, 2), Precision.round(aquamatter, 2),
                Precision.round(nethermatter, 2), Precision.round(nightmatter, 2),
                Precision.round(earthmatter, 2),
                Precision.round(endmatter, 2), Precision.round(aermatter, 2)};
        for (int i = 0; i < quantitysolid.length; i++) {
            ModUtils.SetDoubleWithoutItem(nbt, ("quantitysolid_" + i), quantitysolid[i]);
        }
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        if (stack.isEmpty()) {
            return;
        }
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
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        } else {
            InvSlot invSlot = this.getInventorySlot(index);
            if(invSlot instanceof InvSlotConverterSolidMatter)
                return ((InvSlotConverterSolidMatter) invSlot).accepts(this.locateInfoInvSlot(index).getIndex(), stack);
            return invSlot != null && invSlot.canInput() && invSlot.accepts(stack);
        }
    }
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
        if (StackUtil.isEmpty(stack)) {
            return false;
        } else {
            InvSlot targetSlot = this.getInventorySlot(index);
            if (targetSlot instanceof InvSlotConverterSolidMatter) {
                return ((InvSlotConverterSolidMatter) targetSlot).accepts(this.locateInfoInvSlot(index).getIndex(), stack);
            } else {
                if (targetSlot == null) {
                    return false;
                } else if (targetSlot.canInput() && targetSlot.accepts(stack)) {
                    if (targetSlot.preferredSide != InvSlot.InvSide.ANY && targetSlot.preferredSide.matches(side)) {
                        return true;
                    } else {
                        return targetSlot.preferredSide == InvSlot.InvSide.ANY;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    public void init() {

        addrecipe(new ItemStack(Blocks.STONE), 0.2, 0, 0, 0, 0, 0.15, 0, 0);
        addrecipe(new ItemStack(Blocks.COBBLESTONE), 0.1, 0, 0, 0, 0, 0.05, 0, 0);
        addrecipe(new ItemStack(Blocks.NETHERRACK), 0.1, 0, 0, 0.05, 0, 0, 0, 0);

        addrecipe(new ItemStack(Blocks.GRASS), 0.5, 0, 0, 0, 0, 4, 0, 1);
        addrecipe(new ItemStack(Blocks.GRAVEL), 0.5, 0, 0, 0, 0, 0.5, 0, 0);
        addrecipe(new ItemStack(Blocks.GOLD_ORE), 1, 0, 0, 0, 0, 4, 0, 0);
        addrecipe(new ItemStack(Blocks.COAL_ORE), 1, 0, 0, 0, 0, 2, 0, 0);
        addrecipe(new ItemStack(Blocks.GOLD_BLOCK), 180, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.IRON_BLOCK), 24, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.GOLD_INGOT), 20, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.IRON_INGOT), 2.67, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.DIAMOND_BLOCK), 500, 0, 100, 0, 0, 400, 0, 0);
        addrecipe(new ItemStack(Blocks.EMERALD_BLOCK), 700, 0, 100, 0, 0, 400, 0, 200);
        addrecipe(new ItemStack(Blocks.COAL_BLOCK), 15, 0, 0, 0, 0, 4, 0, 0);
        addrecipe(new ItemStack(Items.STRING), 150, 0, 0, 0, 0, 0, 0, 200);
        addrecipe(new ItemStack(Items.LEATHER), 150, 0, 0, 0, 0, 0, 0, 50);
        addrecipe(new ItemStack(Items.SLIME_BALL), 100, 0, 200, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.BLAZE_ROD), 100, 0, 0, 1000, 500, 0, 0, 0);
        addrecipe(new ItemStack(Items.BONE), 20, 0, 0, 0, 100, 0, 0, 0);
        addrecipe(new ItemStack(Items.DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.FLINT), 1, 0, 0, 0, 0, 0.25, 0, 0);
        addrecipe(new ItemStack(Blocks.SOUL_SAND), 0, 0, 0, 150, 50, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.NETHER_BRICK), 0, 0, 0, 250, 100, 0, 0, 0);
        addrecipe(new ItemStack(Items.CLAY_BALL), 0, 0, 40, 0, 0, 0, 0, 20);
        addrecipe(new ItemStack(Items.BRICK), 0, 0, 40, 0, 0, 0, 0, 20);
        addrecipe(new ItemStack(Items.NETHERBRICK), 0, 0, 40, 40, 0, 0, 0, 20);
        addrecipe(new ItemStack(Items.GUNPOWDER), 4, 0, 0, 0, 0, 0, 0, 0);

        addrecipe(new ItemStack(Blocks.LAPIS_BLOCK), 50, 0, 0, 0, 10, 60, 0, 0);
        addrecipe(new ItemStack(Blocks.REDSTONE_BLOCK), 5, 5, 0, 0, 0, 15, 0, 0);
        addrecipe(new ItemStack(Blocks.QUARTZ_BLOCK), 30, 0, 0, 300, 20, 0, 0, 0);
        addrecipe(new ItemStack(Items.QUARTZ), 7.5, 0, 0, 75, 5, 0, 0, 0);
        addrecipe(new ItemStack(Items.STICK), 2, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.PLANKS), 10, 0, 0, 0, 0, 0, 0, 2);
        addrecipe(new ItemStack(Blocks.PLANKS, 1, 1), 10, 0, 0, 0, 0, 0, 0, 2);
        addrecipe(new ItemStack(Blocks.PLANKS, 1, 2), 10, 0, 0, 0, 0, 0, 0, 2);
        addrecipe(new ItemStack(Blocks.PLANKS, 1, 3), 10, 0, 0, 0, 0, 0, 0, 2);
        addrecipe(new ItemStack(Blocks.PLANKS, 1, 4), 10, 0, 0, 0, 0, 0, 0, 2);
        addrecipe(new ItemStack(Blocks.PLANKS, 1, 5), 10, 0, 0, 0, 0, 0, 0, 2);
        addrecipe(new ItemStack(Blocks.PUMPKIN), 1000, 1000, 1000, 0, 0, 0, 0, 2000);
        addrecipe(new ItemStack(Blocks.MELON_BLOCK), 1000, 1000, 1000, 0, 0, 0, 0, 2000);
        addrecipe(new ItemStack(Items.WHEAT), 1000, 2000, 500, 0, 0, 0, 0, 2000);
        addrecipe(new ItemStack(Items.REEDS), 300, 200, 500, 0, 0, 0, 0, 500);
        addrecipe(new ItemStack(Items.FEATHER), 300, 200, 500, 0, 0, 0, 0, 500);
        addrecipe(new ItemStack(Blocks.WOOL), 600, 0, 0, 0, 0, 0, 0, 800);
        addrecipe(new ItemStack(Items.NETHER_WART), 1000, 0, 0, 2500, 2000, 0, 0, 0);
        //  addrecipe(Ic2Items.reBattery, 1000,0 , 0, 2500, 2000, 0, 0, 0);


        addrecipe(new ItemStack(Blocks.GLOWSTONE), 30, 20, 0, 300, 20, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.GLASS), 2, 0, 0.5, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.END_STONE), 0.5, 0, 0, 0, 0, 0, 0.25, 0);
        addrecipe(new ItemStack(Items.GLOWSTONE_DUST), 7.5, 5, 0, 75, 5, 0, 0, 0);


        addrecipe(Ic2Items.copperBlock, 8, 0, 0, 0, 0, 4, 0, 0);
        addrecipe(Ic2Items.tinBlock, 10, 0, 0, 0, 0, 4, 0, 0);
        addrecipe(Ic2Items.bronzeBlock, 10, 0, 0, 0, 0, 4, 0, 0);
        addrecipe(Ic2Items.advironblock, 20, 0, 0, 0, 0, 8, 0, 0);
        addrecipe(Ic2Items.uraniumBlock, 15, 0, 0, 0, 0, 8, 0, 0);
        addrecipe(Ic2Items.leadBlock, 80, 0, 0, 0, 0, 40, 0, 0);


        addrecipe(Ic2Items.copperIngot, 8 / 9D, 0, 0, 0, 0, 4 / 9D, 0, 0);
        addrecipe(Ic2Items.tinIngot, 10 / 9D, 0, 0, 0, 0, 4 / 9D, 0, 0);
        addrecipe(Ic2Items.bronzeIngot, 10 / 9D, 0, 0, 0, 0, 4 / 9D, 0, 0);
        addrecipe(Ic2Items.advIronIngot, 20 / 9D, 0, 0, 0, 0, 8 / 9D, 0, 0);
        addrecipe(new ItemStack(IUItem.itemiu, 1, 2), 15 / 9D, 0, 0, 0, 0, 8 / 9D, 0, 0);
        addrecipe(Ic2Items.leadIngot, 80 / 9D, 0, 0, 0, 0, 40 / 9D, 0, 0);

        addrecipe(Ic2Items.platecopper, 8 / 9D, 0, 0, 0, 0, 4 / 9D, 0, 0);
        addrecipe(Ic2Items.platetin, 10 / 9D, 0, 0, 0, 0, 4 / 9D, 0, 0);
        addrecipe(Ic2Items.platebronze, 10 / 9D, 0, 0, 0, 0, 4 / 9D, 0, 0);
        addrecipe(Ic2Items.plateadviron, 20 / 9D, 0, 0, 0, 0, 8 / 9D, 0, 0);
        addrecipe(Ic2Items.platelead, 80 / 9D, 0, 0, 0, 0, 40 / 9D, 0, 0);

        addrecipe(Ic2Items.casingcopper, 8 / 18D, 0, 0, 0, 0, 4 / 18D, 0, 0);
        addrecipe(Ic2Items.casingtin, 10 / 18D, 0, 0, 0, 0, 4 / 18D, 0, 0);
        addrecipe(Ic2Items.casingbronze, 10 / 18D, 0, 0, 0, 0, 4 / 18D, 0, 0);
        addrecipe(Ic2Items.casingadviron, 20 / 18D, 0, 0, 0, 0, 8 / 18D, 0, 0);
        addrecipe(Ic2Items.casinglead, 80 / 18D, 0, 0, 0, 0, 40 / 18D, 0, 0);


        addrecipe(ModUtils.getCable(Ic2Items.copperCableItem, 0), 8 / 18D, 0, 0, 0, 0, 4 / 18D, 0, 0);
        addrecipe(ModUtils.getCable(Ic2Items.tinCableItem, 0), 10 / 27D, 0, 0, 0, 0, 4 / 27D, 0, 0);
        addrecipe(ModUtils.getCable(Ic2Items.goldCableItem, 0), 5, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(ModUtils.getCable(Ic2Items.ironCableItem, 0), 2.67 / 4D, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(Ic2Items.plategold, 20, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(Ic2Items.plateiron, 2.67, 0, 0, 0, 0, 0, 0, 0);


        addrecipe(Ic2Items.rubber, 150, 0, 40, 0, 0, 0, 0, 50);

        addrecipe(new ItemStack(Items.ENDER_PEARL), 0, 0, 0, 0, 250, 0, 2000, 0);
        addrecipe(new ItemStack(Blocks.OBSIDIAN), 0, 0, 0, 0, 30, 10, 10, 0);

        for (int i = 0; i < IUItem.name_mineral.size(); i++) {
            addrecipe(new ItemStack(IUItem.iuingot, 1, i), 12, 0, 0, 0, 0, 36, 0, 0);
        }
        for (int i = 0; i < IUItem.name_mineral.size(); i++) {
            addrecipe(new ItemStack(IUItem.stik, 1, i), 6, 0, 0, 0, 0, 18, 0, 0);
        }
        for (int i = 0; i < IUItem.name_mineral.size(); i++) {
            addrecipe(new ItemStack(IUItem.plate, 1, i), 12, 0, 0, 0, 0, 36, 0, 0);
        }
        for (int i = 0; i < IUItem.name_mineral.size(); i++) {
            addrecipe(new ItemStack(IUItem.doubleplate, 1, i), 108, 0, 0, 0, 0, 288, 0, 0);
        }
        for (int i = 0; i < IUItem.name_mineral.size(); i++) {
            addrecipe(new ItemStack(IUItem.casing, 1, i), 6, 0, 0, 0, 0, 18, 0, 0);
        }
        IUItem.machineRecipe = Recipes.recipes.getRecipeStack("converter");
    }

    public void setOverclockRates() {
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
        if (output != null && this.outputSlot.canAdd(this.output.getRecipe().output.items) && !this.inputSlot.isEmpty() && this.required) {
            if (!this.getActive()) {
                setActive(true);
            }
            if (this.world.provider.getWorldTime() % 20 == 0) {
                this.MatterSlot.getmatter();
            }
            if (this.useEnergy(this.energyConsume, false) && this.required) {
                this.progress++;
                this.useEnergy(this.energyConsume, true);
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

            if (this.getActive()) {
                setActive(false);
            }
        }
        if (this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

    }

    private void useMatter() {

        for (int i = 0; i < this.quantitysolid.length; i++) {
            this.quantitysolid[i] -= outputmatter[i];
        }
    }

    public void getrequiredmatter(RecipeOutput output) {
        if (output == null) {
            this.required = false;
            return;
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
        operateOnce(processResult);
    }

    public void operateOnce(List<ItemStack> processResult) {

        useMatter();
        this.outputSlot.add(processResult);
        this.getrequiredmatter(this.output.getRecipe().getOutput());


    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        inputSlot.load();
        this.getOutput();
        if (this.output != null) {

            for (int i = 0; i < this.quantitysolid.length; i++) {
                outputmatter[i] = this.output.getRecipe().output.metadata.getDouble(("quantitysolid_" + i));
            }

            this.getrequiredmatter(this.output.getRecipe().getOutput());
        } else {
            this.getrequiredmatter(null);
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
        if (this.output != null) {
            for (int i = 0; i < this.quantitysolid.length; i++) {
                outputmatter[i] = this.output.getRecipe().output.metadata.getDouble(("quantitysolid_" + i));
            }
        }
        if (this.output == null) {
            getrequiredmatter(null);
        } else {
            getrequiredmatter(this.output.getRecipe().getOutput());
        }
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
