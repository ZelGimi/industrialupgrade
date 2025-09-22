package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerImpAlloySmelter;
import com.denfop.gui.GuiImpAlloySmelter;
import com.denfop.invslot.Inventory;
import com.denfop.invslot.InventoryDischarge;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityImpAlloySmelter extends TileElectricMachine implements IHasRecipe, IType, IUpdateTick, IUpgradableBlock {

    public final HeatComponent heat;
    public final Inventory input_slot;
    public final InventoryUpgrade upgradeSlot;
    public final ComponentProcess componentProcess;
    public final InventoryRecipes inputSlotA;
    public final ComponentProgress componentProgress;
    private final ComponentUpgrade componentUpgrades;
    private final ComponentUpgradeSlots componentUpgrade;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe output;

    public TileEntityImpAlloySmelter() {
        super(300, 1, 1);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.inputSlotA = new InventoryRecipes(this, "impalloysmelter", this);
        this.dischargeSlot = new InventoryDischarge(this, Inventory.TypeItemSlot.INPUT, 1, false);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1));
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
        this.heat = this.addComponent(HeatComponent
                .asBasicSink(this, 8000));

        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TileEntityImpAlloySmelter) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileEntityImpAlloySmelter) this.base).inputSlotA.changeAccepts(this.get());
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
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.125));
    }

    public static void addAlloysmelter(
            Object container, Object fill, Object fill1, Object fill2, ItemStack output,
            int temperature
    ) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("impalloysmelter", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill), input.getInput(fill1), input.getInput(fill2)),
                new RecipeOutput(nbt, output)
        ));

    }

    public void addInformation(ItemStack stack, List<String> tooltip) {

        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.heatmachine.info"));
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());

        }


        super.addInformation(stack, tooltip);
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

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("sound", this.sound);
        return nbttagcompound;
    }

    public MachineRecipe getOutput() {


        this.output = this.inputSlotA.process();

        return this.output;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            inputSlotA.load();
            this.getOutput();
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get());
            }
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.imp_alloy_smelter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override

    public void init() {

        addAlloysmelter(new ItemStack(IUItem.iudust, 4, 21), new ItemStack(IUItem.plastic_plate),
                new ItemStack(IUItem.alloysingot, 1, 20),
                new ItemStack(IUItem.alloysingot, 1, 10),
                new ItemStack(IUItem.crafting_elements, 1
                        , 479)
                , 6000
        );

        addAlloysmelter("ingotIron", "ingotChromium", "ingotNickel", "ingotManganese", new ItemStack(IUItem.alloysingot, 1, 13)
                , 7000);

        addAlloysmelter("ingotIron", "ingotChromium", "ingotNickel", "ingotMolybdenum", new ItemStack(IUItem.alloysingot, 1, 14)
                , 7500);

        addAlloysmelter("ingotCobalt", "ingotChromium", "ingotIron", "ingotTungsten", new ItemStack(IUItem.alloysingot, 1,
                        16
                )
                , 6500);

        addAlloysmelter("ingotTin", "ingotLead", "ingotBismuth", "ingotCadmium", new ItemStack(IUItem.alloysingot, 1,
                        18
                )
                , 5000);

        addAlloysmelter(new ItemStack(Items.IRON_INGOT, 2), new ItemStack(Items.COAL, 3), "ingotNickel", "ingotManganese",
                IUItem.advIronIngot
                , 5000
        );

        addAlloysmelter("ingotIron", new ItemStack(IUItem.iudust, 2, 21), "ingotNickel", "ingotMolybdenum",
                new ItemStack(IUItem.alloysingot, 1, 21)
                , 8000
        );
    }

    public ContainerImpAlloySmelter getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerImpAlloySmelter(entityPlayer, this);
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiImpAlloySmelter(new ContainerImpAlloySmelter(entityPlayer, this));
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        sound = !sound;
        new PacketUpdateFieldTile(this, "sound", this.sound);

        if (!sound) {
            if (this.getTypeAudio() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                initiate(2);

            }
        }
    }

    public void updateField(String name, CustomPacketBuffer is) {


        super.updateField(name, is);
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.alloysmelter.getSoundEvent();
    }

    public void initiate(int soundEvent) {
        if (this.getTypeAudio() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }

        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (!getEnable()) {
            return;
        }
        if (getSound() == null) {
            return;
        }
        if (soundEvent == 0) {
            this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 1F, 1);
        } else if (soundEvent == 1) {
            new PacketStopSound(getWorld(), this.pos);
            this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundCategory.BLOCKS, 1F, 1);
        } else {
            new PacketStopSound(getWorld(), this.pos);
        }
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

}
