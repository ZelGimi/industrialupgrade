package com.denfop.blockentity.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.componets.PressureComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSteamSqueezer;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSteamSqueezer;
import com.denfop.sound.EnumSound;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntitySteamSqueezer extends BlockEntityElectricMachine implements IHasRecipe, IUpdateTick {

    public final FluidHandlerRecipe fluid_handler;
    public final Fluids.InternalFluidTank fluidTank1;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final Fluids.InternalFluidTank fluidTank;
    public final ComponentSteamEnergy steam;
    public final PressureComponent pressure;
    public final InventoryRecipes inputSlotA;
    private final Fluids fluids;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    protected short progress;
    protected double guiProgress;
    private MachineRecipe output;

    public BlockEntitySteamSqueezer(BlockPos pos, BlockState state) {
        super(0, 1, 0, BlockBaseMachine3Entity.steam_squeezer, pos, state);
        this.progress = 0;
        this.inputSlotA = new InventoryRecipes(this, "squeezer", this);
        this.defaultEnergyConsume = this.energyConsume = 2;
        this.defaultOperationLength = this.operationLength = 100;
        operationsPerTick = 1;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 100;
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTankExtract("fluidTank1", 4000);
        this.fluid_handler = new FluidHandlerRecipe("squeezer", fluids);
        this.fluidTank = fluids.addTank("fluidTank2", 4000, Inventory.TypeItemSlot.NONE, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance().get()
        ));
        this.pressure = this.addComponent(PressureComponent.asBasicSink(this, 2));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.steam.setFluidTank(fluidTank);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        Recipes.recipes.getRecipeFluid().addInitRecipes(this);

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public void init() {

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);
        }
        super.addInformation(stack, tooltip);

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putShort("progress", this.progress);
        return nbttagcompound;
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

    public double getProgress() {
        return this.guiProgress;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            inputSlotA.load();
            this.fluid_handler.load(this.inputSlotA.get(0));
            this.getOutput();
        }
    }

    public void onUnloaded() {
        super.onUnloaded();

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
        this.fluid_handler.setOutput(null);
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        this.fluid_handler.setOutput(null);
        return this.output;
    }

    public void updateEntityServer() {
        super.updateEntityServer();

        if ((this.fluid_handler.output() == null && !this.inputSlotA.isEmpty())) {
            this.fluid_handler.getOutput(this.inputSlotA.get(0));
        } else {
            if (this.fluid_handler.output() != null && this.inputSlotA.isEmpty()) {
                this.fluid_handler.setOutput(null);
            }
        }


        if (this.output != null && this.fluid_handler.output() != null && pressure.getEnergy() >= 2 && this.fluid_handler.canFillFluid() && this.steam.canUseEnergy(
                energyConsume)) {

            if (!this.getActive()) {
                setActive(true);
            }
            if (this.progress == 0) {
                initiate(0);
            }
            this.progress = (short) (this.progress + 1);
            this.steam.useEnergy(energyConsume);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate();
                this.progress = 0;
                initiate(2);
            }
        } else {

            if (this.progress != 0 && getActive()) {
                initiate(0);
            }
            if (this.fluid_handler.output() == null) {
                this.progress = 0;
            }
            if (this.getActive()) {
                setActive(false);
            }
        }


    }

    public void setOverclockRates() {
        if (this.operationLength < 1) {
            this.operationLength = 1;
        }
    }

    public void operate() {
        for (int i = 0; i < this.operationsPerTick; i++) {
            operateOnce();

            this.getOutput();
            if (this.fluid_handler.output() == null) {
                break;
            }
        }
    }

    public void operateOnce() {
        this.fluid_handler.fillFluid();
        this.inputSlotA.consume();
    }


    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.steam_squeezer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public ContainerMenuSteamSqueezer getGuiContainer(Player entityPlayer) {
        return new ContainerMenuSteamSqueezer(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSteamSqueezer((ContainerMenuSteamSqueezer) menu);
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.FluidInput, UpgradableProperty.ItemExtract
        );
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.hasFluidHandler(player.getItemInHand(hand))) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }


}
