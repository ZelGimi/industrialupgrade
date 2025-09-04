package com.denfop.blockentity.mechanism.generator.energy.fluid;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.sound.AudioFixer;
import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.blockentity.base.BlockEntityLiquidTankInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuDieselGenerator;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryCharge;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.screen.ScreenDieselGenerator;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;

public class BlockEntityDieselGenerator extends BlockEntityLiquidTankInventory implements AudioFixer,
        IUpdatableTileEvent {

    public final InventoryCharge chargeSlot = new InventoryCharge(this, 1);
    public final InventoryFluid fluidSlot;
    public final InventoryOutput outputSlot;
    public final String name = null;
    public final Energy energy;
    public final int production = 200;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public double coef;
    public boolean addedToEnergyNet = false;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    private boolean sound = true;

    public BlockEntityDieselGenerator(BlockPos pos, BlockState state) {
        super(12, BlockBaseMachine2Entity.gen_disel, pos, state);
        this.coef = 1;
        this.fluidSlot = new InventoryFluidByList(this, Inventory.TypeItemSlot.INPUT, 1, InventoryFluid.TypeFluidSlot.INPUT,
                FluidName.fluiddiesel.getInstance().get(),
                FluidName.fluida_diesel.getInstance().get(), FluidName.fluidaa_diesel.getInstance().get(),
                FluidName.fluidaaa_diesel.getInstance().get(), FluidName.fluidaaaa_diesel.getInstance().get()
        );
        this.outputSlot = new InventoryOutput(this, 1);
        this.energy = this.addComponent(Energy.asBasicSource(
                this,
                (double) 10000000,
                4
        ).addManagedSlot(chargeSlot));
        ((Fluids.InternalFluidTank) this.getFluidTank()).setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluiddiesel.getInstance().get(),
                FluidName.fluida_diesel.getInstance().get(), FluidName.fluidaa_diesel.getInstance().get(),
                FluidName.fluidaaa_diesel.getInstance().get(), FluidName.fluidaaaa_diesel.getInstance().get()
        ));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.45));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.75));
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            sound = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, sound);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public EnumTypeAudio getTypeAudio() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine2Entity.gen_disel;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1.getBlock(this.getTeBlock().getId());
    }

    @Override
    public boolean getEnable() {
        return this.sound;
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
    public SoundEvent getSound() {
        return EnumSound.diesel_generator.getSoundEvent();
    }

    public void onUnloaded() {
        super.onUnloaded();

    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putBoolean("sound", this.sound);
        return nbttagcompound;
    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getActive() && this.level.getGameTime() % 5 == 0) {
            ParticleUtils.spawnDieselGeneratorParticles(level, pos, level.random);
        }
        if (!this.fluidTank.getFluid().isEmpty()) {
            Fluid fluid = this.fluidTank.getFluid().getFluid();
            if (fluid == FluidName.fluiddiesel.getInstance().get()) {
                coef = 1;
            } else if (fluid == FluidName.fluida_diesel.getInstance().get()) {
                coef = 2;

            } else if (fluid == FluidName.fluidaa_diesel.getInstance().get()) {
                coef = 4;

            } else if (fluid == FluidName.fluidaaa_diesel.getInstance().get()) {
                coef = 8;

            } else if (fluid == FluidName.fluidaaaa_diesel.getInstance().get()) {
                coef = 16;

            }
        }
        if (this.needsFluid()) {
            MutableObject<ItemStack> output = new MutableObject<>();
            if (this.fluidSlot.transferToTank(
                    this.fluidTank,
                    output,
                    true
            ) && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
                this.fluidSlot.transferToTank(this.fluidTank, output, false);
                if (output.getValue() != null) {
                    this.outputSlot.add(output.getValue());
                }
            }
        }

        boolean newActive = this.gainEnergy();


        if (this.getActive() != newActive) {
            this.setActive(newActive);
        }
        if (this.getActive() && getWorld().getGameTime() % 60 == 0) {
            initiate(2);
        }
    }

    public float getWrenchDropRate() {
        return 0.9F;
    }

    public boolean gainEnergy() {
        if (this.isConverting()) {
            this.energy.addEnergy(this.production * coef);
            this.getFluidTank().drain(2, IFluidHandler.FluidAction.EXECUTE);
            initiate(0);
            return true;
        } else {
            initiate(2);
            return false;
        }
    }

    public boolean isConverting() {
        return this.getTankAmount() > 1 && this.energy.getEnergy() + (double) this.production * coef <= this.energy.getCapacity();
    }


    public ContainerMenuDieselGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerMenuDieselGenerator(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenDieselGenerator((ContainerMenuDieselGenerator) isAdmin);
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

}
