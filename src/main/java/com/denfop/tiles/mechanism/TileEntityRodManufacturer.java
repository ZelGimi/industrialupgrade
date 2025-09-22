package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerRodManufacturer;
import com.denfop.gui.GuiRodManufacturer;
import com.denfop.invslot.Inventory;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityRodManufacturer extends TileEntityInventory implements IUpgradableBlock, IUpdateTick,
        IHasRecipe {

    public final InventoryRecipes inputSlotA;
    public final Energy energy;
    public final InventoryUpgrade upgradeSlot;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final InventoryOutput outputSlot;
    public final Inventory input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe output;
    public short progress;
    public double guiProgress;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick = 1;

    public TileEntityRodManufacturer() {

        this.defaultEnergyConsume = this.energyConsume = 2;
        this.defaultOperationLength = this.operationLength = 300;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 2 * 300;
        this.output = null;
        this.outputSlot = new InventoryOutput(this, 1);
        this.energy = this.addComponent(Energy.asBasicSink(this, defaultEnergyStorage, defaultTier));
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.inputSlotA = new InventoryRecipes(this, "rod_assembler", this);
        inputSlotA.setInventoryStackLimit(1);
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.05));
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TileEntityRodManufacturer) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileEntityRodManufacturer) this.base).inputSlotA.changeAccepts(this.get());
                }
            }

            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.recipe_schedule;
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
    }

    public static void addRecipe(
            ItemStack input, ItemStack input1, ItemStack input2,
            ItemStack input3,
            ItemStack input4, ItemStack input5, ItemStack output
    ) {
        final IInputHandler recipeInputFactory = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("rod_assembler", new BaseMachineRecipe(
                new Input(
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input2),
                        recipeInputFactory.getInput(input3),
                        recipeInputFactory.getInput(input4),
                        recipeInputFactory.getInput(input5)
                )

                ,
                new RecipeOutput(null, output)
        ));
    }

    public static void addRecipe(String input, String input1, ItemStack output) {
        final IInputHandler recipeInputFactory = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("rod_assembler", new BaseMachineRecipe(
                new Input(
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1)
                )

                ,
                new RecipeOutput(null, output)
        ));
    }

    public static void addRecipe1(String input, String input1, ItemStack output) {
        final IInputHandler recipeInputFactory = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("rod_assembler", new BaseMachineRecipe(
                new Input(
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1)
                )

                ,
                new RecipeOutput(null, output)
        ));
    }

    public static void addRecipe2(String input, String input1, ItemStack output) {
        final IInputHandler recipeInputFactory = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("rod_assembler", new BaseMachineRecipe(
                new Input(
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1)
                )

                ,
                new RecipeOutput(null, output)
        ));
    }

    public static void addRecipe(ItemStack stack1, String input, String input1, ItemStack output) {
        final IInputHandler recipeInputFactory = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("rod_assembler", new BaseMachineRecipe(
                new Input(
                        recipeInputFactory.getInput(stack1),
                        recipeInputFactory.getInput(stack1),
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input)
                )

                ,
                new RecipeOutput(null, output)
        ));
    }

    public static void addRecipe1(ItemStack stack1, String input, String input1, ItemStack output) {
        final IInputHandler recipeInputFactory = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("rod_assembler", new BaseMachineRecipe(
                new Input(
                        recipeInputFactory.getInput(output),
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input)
                )

                ,
                new RecipeOutput(null, stack1)
        ));
    }

    public static void addRecipe(ItemStack stack1, ItemStack input, String input1, ItemStack output) {
        final IInputHandler recipeInputFactory = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("rod_assembler", new BaseMachineRecipe(
                new Input(
                        recipeInputFactory.getInput(stack1),
                        recipeInputFactory.getInput(stack1),
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input)
                )

                ,
                new RecipeOutput(null, output)
        ));
    }

    public static void addRecipe1(ItemStack stack1, ItemStack input, String input1, ItemStack output) {
        final IInputHandler recipeInputFactory = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("rod_assembler", new BaseMachineRecipe(
                new Input(
                        recipeInputFactory.getInput(stack1),
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input1),
                        recipeInputFactory.getInput(input)
                )

                ,
                new RecipeOutput(null, output)
        ));
    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.rods_manufacturer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void init() {
        addRecipe("logWood", "plankWood", new ItemStack(IUItem.windrod, 1, 0));
        addRecipe1("logWood", "plankWood", new ItemStack(IUItem.water_rod, 1, 0));
        addRecipe(new ItemStack(IUItem.windrod, 1, 0), "plateBronze", "casingBronze", new ItemStack(IUItem.windrod, 1, 1));
        addRecipe(new ItemStack(IUItem.windrod, 1, 1), "plateIron", "casingIron", new ItemStack(IUItem.windrod, 1, 2));
        addRecipe(new ItemStack(IUItem.windrod, 1, 2), "plateSteel", "casingSteel", new ItemStack(IUItem.windrod, 1, 3));
        addRecipe(new ItemStack(IUItem.windrod, 1, 3), "plateCarbon", "plateCarbon", new ItemStack(IUItem.windrod, 1, 4));
        addRecipe(new ItemStack(IUItem.windrod, 1, 4), "plateIridium", "casingIridium", new ItemStack(IUItem.windrod, 1, 5));
        addRecipe(
                new ItemStack(IUItem.windrod, 1, 5),
                IUItem.iridiumOre,
                "doubleplateIridium",
                new ItemStack(IUItem.windrod, 1, 6)
        );
        addRecipe(new ItemStack(IUItem.windrod, 1, 6), "plateElectrum", "casingElectrum", new ItemStack(IUItem.windrod, 1, 7));

        addRecipe(new ItemStack(IUItem.windrod, 1, 7), "crystalProton", "crystalPhoton", new ItemStack(IUItem.windrod, 1, 8));
        addRecipe(new ItemStack(IUItem.windrod, 1, 8), "crystalPhoton", "crystalingotPhoton", new ItemStack(IUItem.windrod, 1,
                10
        ));
        addRecipe(new ItemStack(IUItem.windrod, 1, 10), "nuggetNeutron", "casingVitalium", new ItemStack(IUItem.windrod, 1, 9));
        addRecipe(new ItemStack(IUItem.windrod, 1, 9), "plateSpinel", "casingSpinel", new ItemStack(IUItem.windrod, 1, 11));
        addRecipe(new ItemStack(IUItem.windrod, 1, 11), "plateCobalt", "casingCobalt", new ItemStack(IUItem.windrod, 1, 12));
        addRecipe(new ItemStack(IUItem.windrod, 1, 12), "plateMikhail", "casingMikhail", new ItemStack(IUItem.windrod, 1, 13));


        addRecipe(new ItemStack(IUItem.water_rod, 1, 0), "plateBronze", "casingBronze", new ItemStack(IUItem.water_rod, 1, 1));
        addRecipe(new ItemStack(IUItem.water_rod, 1, 1), "plateIron", "casingIron", new ItemStack(IUItem.water_rod, 1, 2));
        addRecipe(new ItemStack(IUItem.water_rod, 1, 2), "plateSteel", "casingSteel", new ItemStack(IUItem.water_rod, 1, 3));
        addRecipe(new ItemStack(IUItem.water_rod, 1, 3), "plateCarbon", "plateCarbon", new ItemStack(IUItem.water_rod, 1, 4));
        addRecipe(new ItemStack(IUItem.water_rod, 1, 4), "plateIridium", "casingIridium", new ItemStack(IUItem.water_rod, 1, 5));
        addRecipe(
                new ItemStack(IUItem.water_rod, 1, 5),
                IUItem.iridiumOre,
                "doubleplateIridium",
                new ItemStack(IUItem.water_rod, 1, 6)
        );
        addRecipe(
                new ItemStack(IUItem.water_rod, 1, 6),
                "plateElectrum",
                "casingElectrum",
                new ItemStack(IUItem.water_rod, 1, 7)
        );

        addRecipe(new ItemStack(IUItem.water_rod, 1, 7), "crystalProton", "crystalPhoton", new ItemStack(IUItem.water_rod, 1, 8));
        addRecipe(new ItemStack(IUItem.water_rod, 1, 8), "crystalPhoton", "crystalingotPhoton", new ItemStack(IUItem.water_rod, 1,
                10
        ));
        addRecipe(
                new ItemStack(IUItem.water_rod, 1, 10),
                "nuggetNeutron",
                "casingVitalium",
                new ItemStack(IUItem.water_rod, 1, 9)
        );
        addRecipe(new ItemStack(IUItem.water_rod, 1, 9), "plateSpinel", "casingSpinel", new ItemStack(IUItem.water_rod, 1, 11));
        addRecipe(new ItemStack(IUItem.water_rod, 1, 11), "plateCobalt", "casingCobalt", new ItemStack(IUItem.water_rod, 1, 12));
        addRecipe(
                new ItemStack(IUItem.water_rod, 1, 12),
                "plateMikhail",
                "casingMikhail",
                new ItemStack(IUItem.water_rod, 1, 13)
        );


        addRecipe2("logWood", "plankWood", new ItemStack(IUItem.wood_steam_blade));
        addRecipe1(
                new ItemStack(IUItem.bronze_steam_blade, 1),
                "plateBronze",
                "casingBronze",
                new ItemStack(IUItem.wood_steam_blade)
        );
        addRecipe1(
                new ItemStack(IUItem.iron_steam_blade, 1),
                "plateIron",
                "casingIron",
                new ItemStack(IUItem.bronze_steam_blade)
        );
        addRecipe1(
                new ItemStack(IUItem.steel_steam_blade, 1),
                "plateSteel",
                "casingSteel",
                new ItemStack(IUItem.iron_steam_blade)
        );
        addRecipe1(
                new ItemStack(IUItem.carbon_steam_blade, 1),
                "plateCarbon",
                "plateCarbon",
                new ItemStack(IUItem.steel_steam_blade)
        );
        addRecipe1(IUItem.iridium_steam_blade, "plateIridium", "casingIridium", new ItemStack(IUItem.carbon_steam_blade));
        addRecipe1(
                IUItem.iridium_steam_blade,
                IUItem.iridiumOre,
                "doubleplateIridium",
                IUItem.compressiridium_steam_blade
        );
        addRecipe1(
                IUItem.spectral_steam_blade,
                "plateElectrum",
                "casingElectrum",
                IUItem.compressiridium_steam_blade
        );

        addRecipe1(IUItem.myphical_steam_blade, "crystalProton", "crystalPhoton", IUItem.spectral_steam_blade);
        addRecipe1(IUItem.photon_steam_blade, "crystalPhoton", "crystalingotPhoton", IUItem.myphical_steam_blade);
        addRecipe1(
                IUItem.neutron_steam_blade,
                "nuggetNeutron",
                "casingVitalium",
                IUItem.photon_steam_blade
        );
        addRecipe1(IUItem.barion_steam_blade, "plateSpinel", "casingSpinel", IUItem.neutron_steam_blade);
        addRecipe1(IUItem.hadron_steam_blade, "plateCobalt", "casingCobalt", IUItem.barion_steam_blade);
        addRecipe1(
                IUItem.ultramarine_steam_blade,
                "plateMikhail",
                "casingMikhail",
                IUItem.hadron_steam_blade
        );
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            guiProgress = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiProgress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public int getInventoryStackLimit() {

        return 1;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

    }

    public double getProgress() {
        return this.guiProgress;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            setOverclockRates();
            inputSlotA.load();
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get());
            }
            this.getOutput();

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

    public void addInformation(ItemStack stack, List<String> tooltip) {

        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);
        }
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }

        super.addInformation(stack, tooltip);
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        MachineRecipe output = this.output;
        if (this.output != null && this.energy.canUseEnergy(energyConsume) && !this.inputSlotA.isEmpty() && this.outputSlot.canAdd(
                this.output.getRecipe().getOutput().items)) {


            this.progress = (short) (this.progress + 1);
            this.energy.useEnergy(energyConsume);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(output);
                this.progress = 0;
            }
        } else {

            if (output == null) {
                this.progress = 0;
            }

        }
        if (this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

    }

    public void operateOnce(List<ItemStack> processResult) {

        this.inputSlotA.consume();

        this.outputSlot.addAll(processResult);
    }

    public void onUnloaded() {
        super.onUnloaded();
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
        return nbttagcompound;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
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
    public ContainerRodManufacturer getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerRodManufacturer(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiRodManufacturer(getGuiContainer(entityPlayer));
    }


}
