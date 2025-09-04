package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.IType;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuPerAlloySmelter;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryDischarge;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenPerAlloySmelter;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.denfop.utils.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityPerAlloySmelter extends BlockEntityElectricMachine implements IHasRecipe, IType, IUpdateTick, IUpgradableBlock {

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

    public BlockEntityPerAlloySmelter(BlockPos pos, BlockState state) {
        super(300, 1, 1, BlockBaseMachine3Entity.per_alloy_smelter, pos, state);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.inputSlotA = new InventoryRecipes(this, "peralloysmelter", this);
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
                .asBasicSink(this, 10000));

        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((BlockEntityPerAlloySmelter) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((BlockEntityPerAlloySmelter) this.base).inputSlotA.changeAccepts(this.get(0));
                }
                return content;
            }

            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.025));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public static void addAlloysmelter(
            Object container, Object fill, Object fill1, Object fill2, Object fill3, ItemStack output,
            int temperature
    ) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        final CompoundTag nbt = ModUtils.nbt();
        nbt.putShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("peralloysmelter", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill), input.getInput(fill1), input.getInput(fill2),
                        input.getInput(fill3)
                ),
                new RecipeOutput(nbt, output)
        ));

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getActive() && this.level.getGameTime() % 5 == 0) {
            ParticleUtils.spawnAlloySmelterParticles(level, pos, level.random);
        }
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

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putBoolean("sound", this.sound);
        return nbttagcompound;
    }

    public MachineRecipe getOutput() {


        this.output = this.inputSlotA.process();

        return this.output;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            inputSlotA.load();
            this.getOutput();
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get(0));
            }
        }
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.per_alloy_smelter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override

    public void init() {
        addAlloysmelter("forge:ingots/Cobalt", "forge:ingots/Chromium", "forge:ingots/Nickel", "forge:ingots/Molybdenum", "forge:ingots/Aluminium",
                new ItemStack(IUItem.alloysingot.getStack(19), 1)
                , 7000
        );


        addAlloysmelter("forge:ingots/StainlessSteel", "forge:ingots/Redbrass", "forge:ingots/Vanadoalumite", "forge:ingots/Barium",
                "forge:ingots/TantalumTungstenHafnium",
                new ItemStack(IUItem.alloysingot.getStack(28), 1)
                , 8000
        );

        addAlloysmelter("forge:ingots/SuperalloyHaynes", "forge:ingots/Gadolinium", "forge:ingots/Polonium", "forge:ingots/Zirconium",
                "forge:ingots/Woods",
                new ItemStack(IUItem.alloysingot.getStack(29), 1)
                , 10000
        );
    }

    public ContainerMenuPerAlloySmelter getGuiContainer(Player entityPlayer) {
        return new ContainerMenuPerAlloySmelter(entityPlayer, this);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenPerAlloySmelter((ContainerMenuPerAlloySmelter) menu);
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
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

        if (name.equals("sound")) {
            try {
                this.sound = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
            this.getWorld().playSound(null, this.pos, getSound(), SoundSource.BLOCKS, 1F, 1);
        } else if (soundEvent == 1) {
            new PacketStopSound(getWorld(), this.pos);
            this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundSource.BLOCKS, 1F, 1);
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
