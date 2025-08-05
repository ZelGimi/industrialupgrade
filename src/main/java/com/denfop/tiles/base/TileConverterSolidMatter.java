package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockConverterMatter;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerConverterSolidMatter;
import com.denfop.gui.GuiConverterSolidMatter;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlotConverterSolidMatter;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Precision;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileConverterSolidMatter extends TileElectricMachine
        implements IUpgradableBlock, IUpdateTick, IHasRecipe {

    public final InvSlotConverterSolidMatter MatterSlot;
    public final InvSlotRecipes inputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final int defaultOperationLength;
    public final int defaultEnergyConsume;
    public double[] quantitysolid = new double[8];
    public int energyConsume;
    public double defaultEnergyStorage;
    public double progress;
    public double guiProgress = 0;
    public int operationLength;
    public int operationsPerTick;
    public MachineRecipe output;
    public boolean required = false;
    public double[] outputmatter = new double[9];

    public TileConverterSolidMatter(BlockPos pos, BlockState state) {
        super(50000, 14, 1, BlockConverterMatter.converter_matter, pos, state);
        this.MatterSlot = new InvSlotConverterSolidMatter(this);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 3);
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
        CompoundTag nbt = new CompoundTag();
        double[] quantitysolid = {Precision.round(matter, 2),
                Precision.round(sunmatter, 2), Precision.round(aquamatter, 2),
                Precision.round(nethermatter, 2), Precision.round(nightmatter, 2),
                Precision.round(earthmatter, 2),
                Precision.round(endmatter, 2), Precision.round(aermatter, 2)};
        for (int i = 0; i < quantitysolid.length; i++) {
            ModUtils.SetDoubleWithoutItem(nbt, ("quantitysolid_" + i), quantitysolid[i]);
        }
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        if (stack.isEmpty()) {
            return;
        }
        Recipes.recipes.addRecipe("converter", new BaseMachineRecipe(new Input(input.getInput(stack)), new RecipeOutput(
                nbt,
                stack
        )));

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            progress = (double) DecoderHandler.decode(customPacketBuffer);
            quantitysolid = (double[]) DecoderHandler.decode(customPacketBuffer);
            guiProgress = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, progress);
            EncoderHandler.encode(packet, quantitysolid);
            EncoderHandler.encode(packet, guiProgress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockConverterMatter.converter_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.convertersolidmatter.getBlock();
    }


    public void init() {

        addrecipe(new ItemStack(Blocks.STONE), 0.2, 0, 0, 0, 0, 0.15, 0, 0);
        addrecipe(new ItemStack(Blocks.COBBLESTONE), 0.1, 0, 0, 0, 0, 0.05, 0, 0);
        addrecipe(new ItemStack(Blocks.NETHERRACK), 0.1, 0, 0, 0.05, 0, 0, 0, 0);

        addrecipe(new ItemStack(Blocks.GRASS_BLOCK), 0.5, 0, 0, 0, 0, 4, 0, 1);
        addrecipe(new ItemStack(Blocks.GRAVEL), 0.5, 0, 0, 0, 0, 0.5, 0, 0);
        addrecipe(new ItemStack(Blocks.GOLD_ORE), 1, 0, 0, 0, 0, 4, 0, 0);
        addrecipe(new ItemStack(Blocks.COAL_ORE), 1, 0, 0, 0, 0, 2, 0, 0);
        addrecipe(new ItemStack(Blocks.GOLD_BLOCK), 180, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.IRON_BLOCK), 24, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.GOLD_INGOT), 20, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.IRON_INGOT), 2.67, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.DIAMOND), 50, 0, 10, 0, 0, 40, 0, 0);
        addrecipe(new ItemStack(Items.EMERALD), 70, 0, 10, 0, 0, 40, 0, 200);
        addrecipe(new ItemStack(Blocks.DIAMOND_BLOCK), 500, 0, 100, 0, 0, 400, 0, 0);
        addrecipe(new ItemStack(Blocks.EMERALD_BLOCK), 700, 0, 100, 0, 0, 400, 0, 200);
        addrecipe(new ItemStack(Items.COAL), 1.5, 0, 0, 0, 0, 0.55, 0, 0);
        addrecipe(new ItemStack(Items.LAPIS_LAZULI), 5, 0, 0, 0, 1, 7, 0, 0);
        addrecipe(new ItemStack(Items.REDSTONE, 1), 0.5, 0.5, 0, 0, 0, 1.7, 0, 0);

        addrecipe(new ItemStack(Blocks.COAL_BLOCK), 15, 0, 0, 0, 0, 4, 0, 0);
        addrecipe(new ItemStack(Items.STRING), 150, 0, 0, 0, 0, 0, 0, 200);
        addrecipe(new ItemStack(Items.LEATHER), 150, 0, 0, 0, 0, 0, 0, 50);
        addrecipe(new ItemStack(Items.SLIME_BALL), 100, 0, 200, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.BLAZE_ROD), 100, 0, 0, 1000, 500, 0, 0, 0);
        addrecipe(new ItemStack(Items.BONE), 20, 0, 0, 0, 100, 0, 0, 0);
        addrecipe(new ItemStack(Items.BLACK_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.RED_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.GREEN_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.BROWN_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.BLUE_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.PURPLE_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.CYAN_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.LIGHT_GRAY_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.GRAY_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.PINK_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.LIME_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.YELLOW_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.LIGHT_BLUE_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.MAGENTA_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.ORANGE_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.WHITE_DYE), 20, 40, 40, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Items.FLINT), 1, 0, 0, 0, 0, 0.25, 0, 0);
        addrecipe(new ItemStack(Blocks.SOUL_SAND), 0, 0, 0, 150, 50, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.NETHER_BRICKS), 0, 0, 0, 250, 100, 0, 0, 0);
        addrecipe(new ItemStack(Items.CLAY_BALL), 0, 0, 40, 0, 0, 0, 0, 20);
        addrecipe(new ItemStack(Items.BRICK), 0, 0, 40, 0, 0, 0, 0, 20);
        addrecipe(new ItemStack(Items.NETHERRACK), 0, 0, 40, 40, 0, 0, 0, 20);
        addrecipe(new ItemStack(Items.GUNPOWDER), 4, 0, 0, 0, 0, 0, 0, 0);

        addrecipe(new ItemStack(Blocks.LAPIS_BLOCK), 50, 0, 0, 0, 10, 60, 0, 0);
        addrecipe(new ItemStack(Blocks.REDSTONE_BLOCK), 5, 5, 0, 0, 0, 15, 0, 0);
        addrecipe(new ItemStack(Blocks.QUARTZ_BLOCK), 30, 0, 0, 300, 20, 0, 0, 0);
        addrecipe(new ItemStack(Items.QUARTZ), 7.5, 0, 0, 75, 5, 0, 0, 0);
        addrecipe(new ItemStack(Items.STICK), 2, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.OAK_PLANKS), 10, 0, 0, 0, 0, 0, 0, 2);
        addrecipe(new ItemStack(Blocks.SPRUCE_PLANKS), 10, 0, 0, 0, 0, 0, 0, 2);
        addrecipe(new ItemStack(Blocks.BIRCH_PLANKS), 10, 0, 0, 0, 0, 0, 0, 2);
        addrecipe(new ItemStack(Blocks.JUNGLE_PLANKS), 10, 0, 0, 0, 0, 0, 0, 2);
        addrecipe(new ItemStack(Blocks.ACACIA_PLANKS), 10, 0, 0, 0, 0, 0, 0, 2);
        addrecipe(new ItemStack(Blocks.DARK_OAK_PLANKS), 10, 0, 0, 0, 0, 0, 0, 2);
        addrecipe(new ItemStack(Blocks.PUMPKIN), 1000, 1000, 1000, 0, 0, 0, 0, 2000);
        addrecipe(new ItemStack(Blocks.MELON), 1000, 1000, 1000, 0, 0, 0, 0, 2000);
        addrecipe(new ItemStack(Items.WHEAT), 1000, 2000, 500, 0, 0, 0, 0, 2000);
        addrecipe(new ItemStack(Items.SUGAR_CANE), 300, 200, 500, 0, 0, 0, 0, 500);
        addrecipe(new ItemStack(Items.FEATHER), 300, 200, 500, 0, 0, 0, 0, 500);
        addrecipe(new ItemStack(Blocks.WHITE_WOOL), 600, 0, 0, 0, 0, 0, 0, 800);
        addrecipe(new ItemStack(Items.NETHER_WART), 1000, 0, 0, 2500, 2000, 0, 0, 0);


        addrecipe(new ItemStack(Blocks.GLOWSTONE), 30, 20, 0, 300, 20, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.GLASS), 2, 0, 0.5, 0, 0, 0, 0, 0);
        addrecipe(new ItemStack(Blocks.END_STONE), 0.5, 0, 0, 0, 0, 0, 0.25, 0);
        addrecipe(new ItemStack(Items.GLOWSTONE_DUST), 7.5, 5, 0, 75, 5, 0, 0, 0);


        addrecipe(IUItem.copperBlock, 8, 0, 0, 0, 0, 4, 0, 0);
        addrecipe(IUItem.tinBlock, 10, 0, 0, 0, 0, 4, 0, 0);
        addrecipe(IUItem.bronzeBlock, 10, 0, 0, 0, 0, 4, 0, 0);
        addrecipe(IUItem.advironblock, 20, 0, 0, 0, 0, 8, 0, 0);
        addrecipe(IUItem.uraniumBlock, 15, 0, 0, 0, 0, 8, 0, 0);
        addrecipe(IUItem.leadBlock, 80, 0, 0, 0, 0, 40, 0, 0);


        addrecipe(IUItem.copperIngot, 8 / 9D, 0, 0, 0, 0, 4 / 9D, 0, 0);
        addrecipe(IUItem.tinIngot, 10 / 9D, 0, 0, 0, 0, 4 / 9D, 0, 0);
        addrecipe(IUItem.bronzeIngot, 10 / 9D, 0, 0, 0, 0, 4 / 9D, 0, 0);
        addrecipe(IUItem.advIronIngot, 20 / 9D, 0, 0, 0, 0, 8 / 9D, 0, 0);
        addrecipe(new ItemStack(IUItem.itemiu.getStack(2), 1), 15 / 9D, 0, 0, 0, 0, 8 / 9D, 0, 0);
        addrecipe(IUItem.leadIngot, 80 / 9D, 0, 0, 0, 0, 40 / 9D, 0, 0);

        addrecipe(IUItem.platecopper, 8 / 9D, 0, 0, 0, 0, 4 / 9D, 0, 0);
        addrecipe(IUItem.platetin, 10 / 9D, 0, 0, 0, 0, 4 / 9D, 0, 0);
        addrecipe(IUItem.platebronze, 10 / 9D, 0, 0, 0, 0, 4 / 9D, 0, 0);
        addrecipe(IUItem.plateadviron, 20 / 9D, 0, 0, 0, 0, 8 / 9D, 0, 0);
        addrecipe(IUItem.platelead, 80 / 9D, 0, 0, 0, 0, 40 / 9D, 0, 0);

        addrecipe(IUItem.casingcopper, 8 / 18D, 0, 0, 0, 0, 4 / 18D, 0, 0);
        addrecipe(IUItem.casingtin, 10 / 18D, 0, 0, 0, 0, 4 / 18D, 0, 0);
        addrecipe(IUItem.casingbronze, 10 / 18D, 0, 0, 0, 0, 4 / 18D, 0, 0);
        addrecipe(IUItem.casingadviron, 20 / 18D, 0, 0, 0, 0, 8 / 18D, 0, 0);
        addrecipe(IUItem.casinglead, 80 / 18D, 0, 0, 0, 0, 40 / 18D, 0, 0);


        addrecipe(IUItem.copperCableItem, 8 / 18D, 0, 0, 0, 0, 4 / 18D, 0, 0);
        addrecipe(IUItem.tinCableItem, 10 / 27D, 0, 0, 0, 0, 4 / 27D, 0, 0);
        addrecipe(IUItem.goldCableItem, 5, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(IUItem.ironCableItem, 2.67 / 4D, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(IUItem.plategold, 20, 0, 0, 0, 0, 0, 0, 0);
        addrecipe(IUItem.plateiron, 2.67, 0, 0, 0, 0, 0, 0, 0);


        addrecipe(IUItem.rubber, 150, 0, 40, 0, 0, 0, 0, 50);

        addrecipe(new ItemStack(Items.ENDER_PEARL), 0, 0, 0, 0, 250, 0, 2000, 0);
        addrecipe(new ItemStack(Blocks.OBSIDIAN), 0, 0, 0, 0, 30, 10, 10, 0);

        for (int i = 0; i < IUItem.name_mineral.size(); i++) {
            addrecipe(new ItemStack(IUItem.iuingot.getStack(i), 1), 12, 0, 0, 0, 0, 36, 0, 0);
        }
        addrecipe(new ItemStack(IUItem.iuingot.getStack(25), 1), 12, 0, 0, 0, 0, 36, 0, 0);
        addrecipe(new ItemStack(IUItem.iuingot.getStack(26), 1), 12, 0, 0, 0, 0, 36, 0, 0);
        addrecipe(new ItemStack(IUItem.iuingot.getStack(27), 1), 12, 0, 0, 0, 0, 36, 0, 0);

        for (int i = 0; i < RegisterOreDictionary.list_baseore1.size(); i++) {
            addrecipe(new ItemStack(IUItem.iuingot.getStack(i + 28), 1), 12, 0, 0, 0, 0, 36, 0, 0);
        }
        for (int i = 0; i < RegisterOreDictionary.list_baseore1.size(); i++) {
            addrecipe(new ItemStack(IUItem.stik.getStack(i + 22), 1), 6, 0, 0, 0, 0, 18, 0, 0);
        }
        for (int i = 0; i < IUItem.name_mineral.size(); i++) {
            addrecipe(new ItemStack(IUItem.stik.getStack(i), 1), 6, 0, 0, 0, 0, 18, 0, 0);
        }
        for (int i = 0; i < RegisterOreDictionary.list_baseore1.size(); i++) {
            addrecipe(new ItemStack(IUItem.plate.getStack(i + 31), 1), 12, 0, 0, 0, 0, 36, 0, 0);
        }
        for (int i = 0; i < IUItem.name_mineral.size(); i++) {
            addrecipe(new ItemStack(IUItem.plate.getStack(i), 1), 12, 0, 0, 0, 0, 36, 0, 0);
        }
        for (int i = 0; i < RegisterOreDictionary.list_baseore1.size(); i++) {
            addrecipe(new ItemStack(IUItem.doubleplate.getStack(i + 31), 1), 108, 0, 0, 0, 0, 288, 0, 0);
        }
        for (int i = 0; i < IUItem.name_mineral.size(); i++) {
            addrecipe(new ItemStack(IUItem.doubleplate.getStack(i), 1), 108, 0, 0, 0, 0, 288, 0, 0);
        }
        for (int i = 0; i < IUItem.name_mineral.size(); i++) {
            addrecipe(new ItemStack(IUItem.casing.getStack(i), 1), 6, 0, 0, 0, 0, 18, 0, 0);
        }
        for (int i = 0; i < RegisterOreDictionary.list_baseore1.size(); i++) {
            addrecipe(new ItemStack(IUItem.casing.getStack(i + 29), 1), 6, 0, 0, 0, 0, 18, 0, 0);
        }
        IUItem.machineRecipe = Recipes.recipes.getRecipeStack("converter");
    }

    public void setOverclockRates() {
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength(this.defaultOperationLength);
        this.energyConsume = (int) this.upgradeSlot.getEnergyDemand(2);


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
            if (this.level.getGameTime() % 20 == 0) {
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
    public void onLoaded() {
        super.onLoaded();
        this.inputSlots.remove(inputSlot);
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


    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getDouble("progress");
        for (int i = 0; i < this.quantitysolid.length; i++) {
            this.quantitysolid[i] = nbttagcompound.getDouble("quantitysolid" + i);
        }
    }


    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putDouble("progress", this.progress);
        for (int i = 0; i < this.quantitysolid.length; i++) {
            nbttagcompound.putDouble("quantitysolid" + i, this.quantitysolid[i]);
        }

        return nbttagcompound;

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


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(Player entityPlayer, ContainerBase<?> isAdmin) {
        return new GuiConverterSolidMatter((ContainerConverterSolidMatter) isAdmin);
    }

    public ContainerConverterSolidMatter getGuiContainer(Player entityPlayer) {
        return new ContainerConverterSolidMatter(entityPlayer, this);
    }


    public float getWrenchDropRate() {
        return 0.85F;
    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
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

    @Override
    public SoundEvent getSound() {
        return null;
    }

}
