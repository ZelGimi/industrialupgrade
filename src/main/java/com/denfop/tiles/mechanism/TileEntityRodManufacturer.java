package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerRodManufacturer;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiRodManufacturer;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.Keyboard;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityRodManufacturer extends TileEntityInventory implements IUpgradableBlock, IUpdateTick,
        IHasRecipe {

    public final InvSlotRecipes inputSlotA;
    public final Energy energy;
    public final InvSlotUpgrade upgradeSlot;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final InvSlotOutput outputSlot;
    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe output;
    public short progress;
    public double guiProgress;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick = 1;

    public TileEntityRodManufacturer(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.rods_manufacturer, pos, state);

        this.defaultEnergyConsume = this.energyConsume = 2;
        this.defaultOperationLength = this.operationLength = 300;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 2 * 300;
        this.output = null;
        this.outputSlot = new InvSlotOutput(this, 1);
        this.energy = this.addComponent(Energy.asBasicSink(this, defaultEnergyStorage, defaultTier));
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.inputSlotA = new InvSlotRecipes(this, "rod_assembler", this);
        inputSlotA.setStackSizeLimit(1);
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.05));
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((TileEntityRodManufacturer) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileEntityRodManufacturer) this.base).inputSlotA.changeAccepts(this.get(0));
                }
                return content;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
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


    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.rods_manufacturer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public void init() {
        addRecipe("logs", "planks", new ItemStack(IUItem.windrod.getStack(0)));
        addRecipe1("logs", "planks", new ItemStack(IUItem.water_rod.getStack(0)));
        addRecipe(new ItemStack(IUItem.windrod.getStack(0)), "c:plates/Bronze", "c:casings/Bronze", new ItemStack(IUItem.windrod.getStack(1)));
        addRecipe(new ItemStack(IUItem.windrod.getStack(1)), "c:plates/Iron", "c:casings/Iron", new ItemStack(IUItem.windrod.getStack(2)));
        addRecipe(new ItemStack(IUItem.windrod.getStack(2)), "c:plates/Steel", "c:casings/Steel", new ItemStack(IUItem.windrod.getStack(3)));
        addRecipe(new ItemStack(IUItem.windrod.getStack(3)), "c:plates/Carbon", "c:plates/Carbon", new ItemStack(IUItem.windrod.getStack(4)));
        addRecipe(new ItemStack(IUItem.windrod.getStack(4)), "c:plates/Iridium", "c:casings/Iridium", new ItemStack(IUItem.windrod.getStack(5)));
        addRecipe(
                new ItemStack(IUItem.windrod.getStack(5)),
                IUItem.iridiumOre,
                "c:doubleplate/Iridium",
                new ItemStack(IUItem.windrod.getStack(6))
        );
        addRecipe(new ItemStack(IUItem.windrod.getStack(6)), "c:plates/Electrum", "c:casings/Electrum", new ItemStack(IUItem.windrod.getStack(7)));

        addRecipe(new ItemStack(IUItem.windrod.getStack(7)), "c:crystal/Proton", "c:crystal/Photon", new ItemStack(IUItem.windrod.getStack(8)));
        addRecipe(new ItemStack(IUItem.windrod.getStack(8)), "c:crystal/Photon", "c:crystalingot/Photon", new ItemStack(IUItem.windrod.getStack(10), 1
        ));
        addRecipe(new ItemStack(IUItem.windrod.getStack(10)), "c:nuggets/Neutron", "c:casings/Vitalium", new ItemStack(IUItem.windrod.getStack(9)));
        addRecipe(new ItemStack(IUItem.windrod.getStack(9)), "c:plates/Spinel", "c:casings/Spinel", new ItemStack(IUItem.windrod.getStack(11)));
        addRecipe(new ItemStack(IUItem.windrod.getStack(11)), "c:plates/Cobalt", "c:casings/Cobalt", new ItemStack(IUItem.windrod.getStack(12)));
        addRecipe(new ItemStack(IUItem.windrod.getStack(12)), "c:plates/Mikhail", "c:casings/Mikhail", new ItemStack(IUItem.windrod.getStack(13)));


        addRecipe(new ItemStack(IUItem.water_rod.getStack(0)), "c:plates/Bronze", "c:casings/Bronze", new ItemStack(IUItem.water_rod.getStack(1)));
        addRecipe(new ItemStack(IUItem.water_rod.getStack(1)), "c:plates/Iron", "c:casings/Iron", new ItemStack(IUItem.water_rod.getStack(2)));
        addRecipe(new ItemStack(IUItem.water_rod.getStack(2)), "c:plates/Steel", "c:casings/Steel", new ItemStack(IUItem.water_rod.getStack(3)));
        addRecipe(new ItemStack(IUItem.water_rod.getStack(3)), "c:plates/Carbon", "c:plates/Carbon", new ItemStack(IUItem.water_rod.getStack(4)));
        addRecipe(new ItemStack(IUItem.water_rod.getStack(4)), "c:plates/Iridium", "c:casings/Iridium", new ItemStack(IUItem.water_rod.getStack(5)));
        addRecipe(
                new ItemStack(IUItem.water_rod.getStack(5)),
                IUItem.iridiumOre,
                "c:doubleplate/Iridium",
                new ItemStack(IUItem.water_rod.getStack(6))
        );
        addRecipe(
                new ItemStack(IUItem.water_rod.getStack(6)),
                "c:plates/Electrum",
                "c:casings/Electrum",
                new ItemStack(IUItem.water_rod.getStack(7))
        );

        addRecipe(new ItemStack(IUItem.water_rod.getStack(7)), "c:crystal/Proton", "c:crystal/Photon", new ItemStack(IUItem.water_rod.getStack(8)));
        addRecipe(new ItemStack(IUItem.water_rod.getStack(8)), "c:crystal/Photon", "c:crystalingot/Photon", new ItemStack(IUItem.water_rod.getStack(10), 1
        ));
        addRecipe(
                new ItemStack(IUItem.water_rod.getStack(10)),
                "c:nuggets/Neutron",
                "c:casings/Vitalium",
                new ItemStack(IUItem.water_rod.getStack(9))
        );
        addRecipe(new ItemStack(IUItem.water_rod.getStack(9)), "c:plates/Spinel", "c:casings/Spinel", new ItemStack(IUItem.water_rod.getStack(11)));
        addRecipe(new ItemStack(IUItem.water_rod.getStack(11)), "c:plates/Cobalt", "c:casings/Cobalt", new ItemStack(IUItem.water_rod.getStack(12)));
        addRecipe(
                new ItemStack(IUItem.water_rod.getStack(12)),
                "c:plates/Mikhail",
                "c:casings/Mikhail",
                new ItemStack(IUItem.water_rod.getStack(13))
        );


        addRecipe2("logs", "planks", new ItemStack(IUItem.wood_steam_blade.getItem()));
        addRecipe1(
                new ItemStack(IUItem.bronze_steam_blade.getItem(), 1),
                "c:plates/Bronze",
                "c:casings/Bronze",
                new ItemStack(IUItem.wood_steam_blade.getItem())
        );
        addRecipe1(
                new ItemStack(IUItem.iron_steam_blade.getItem(), 1),
                "c:plates/Iron",
                "c:casings/Iron",
                new ItemStack(IUItem.bronze_steam_blade.getItem())
        );
        addRecipe1(
                new ItemStack(IUItem.steel_steam_blade.getItem(), 1),
                "c:plates/Steel",
                "c:casings/Steel",
                new ItemStack(IUItem.iron_steam_blade.getItem())
        );
        addRecipe1(
                new ItemStack(IUItem.carbon_steam_blade.getItem(), 1),
                "c:plates/Carbon",
                "c:plates/Carbon",
                new ItemStack(IUItem.steel_steam_blade.getItem())
        );
        addRecipe1(IUItem.iridium_steam_blade.getItemStack(), "c:plates/Iridium", "c:casings/Iridium", new ItemStack(IUItem.carbon_steam_blade.getItem()));
        addRecipe1(
                IUItem.iridium_steam_blade.getItemStack(),
                IUItem.iridiumOre,
                "c:doubleplate/Iridium",
                IUItem.compressiridium_steam_blade.getItemStack()
        );
        addRecipe1(
                IUItem.spectral_steam_blade.getItemStack(),
                "c:plates/Electrum",
                "c:casings/Electrum",
                IUItem.compressiridium_steam_blade.getItemStack()
        );

        addRecipe1(IUItem.myphical_steam_blade.getItemStack(), "c:crystal/Proton", "c:crystal/Photon", IUItem.spectral_steam_blade.getItemStack());
        addRecipe1(IUItem.photon_steam_blade.getItemStack(), "c:crystal/Photon", "c:crystalingot/Photon", IUItem.myphical_steam_blade.getItemStack());
        addRecipe1(
                IUItem.neutron_steam_blade.getItemStack(),
                "c:nuggets/Neutron",
                "c:casings/Vitalium",
                IUItem.photon_steam_blade.getItemStack()
        );
        addRecipe1(IUItem.barion_steam_blade.getItemStack(), "c:plates/Spinel", "c:casings/Spinel", IUItem.neutron_steam_blade.getItemStack());
        addRecipe1(IUItem.hadron_steam_blade.getItemStack(), "c:plates/Cobalt", "c:casings/Cobalt", IUItem.barion_steam_blade.getItemStack());
        addRecipe1(
                IUItem.ultramarine_steam_blade.getItemStack(),
                "c:plates/Mikhail",
                "c:casings/Mikhail",
                IUItem.hadron_steam_blade.getItemStack()
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

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

    }

    public double getProgress() {
        return this.guiProgress;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            setOverclockRates();
            inputSlotA.load();
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get(0));
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

        this.outputSlot.add(processResult);
    }

    public void onUnloaded() {
        super.onUnloaded();
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putShort("progress", this.progress);
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
    public ContainerRodManufacturer getGuiContainer(final Player entityPlayer) {
        return new ContainerRodManufacturer(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiRodManufacturer((ContainerRodManufacturer) menu);
    }


}
