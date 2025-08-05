package com.denfop.tiles.mechanism;

import com.denfop.ElectricItem;
import com.denfop.IUItem;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.*;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSteamGenerator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSteamGenerator;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotCharge;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;

public class TileEntitySteamGenerator extends TileEntityInventory implements
        IAudioFixer, IUpdatableTileEvent {

    public final InvSlotCharge chargeSlot = new InvSlotCharge(this, 1);
    public final double coef;
    public final Energy energy;
    public final int production = Math.round(4.0F * 1);
    public final Fluids.InternalFluidTank fluidTank1;
    public final ComponentSteamEnergy steam;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final Fluids fluids;
    public boolean addedToEnergyNet = false;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    private boolean sound = true;

    public TileEntitySteamGenerator(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.steam_generator, pos, state);
        this.coef = 1;
        fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank2", 4000, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance().get()
        ), InvSlot.TypeItemSlot.NONE);
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.steam.setFluidTank(fluidTank1);
        this.energy = this.addComponent(Energy.asBasicSource(this, (double) 25000 * coef, 1));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
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

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.steam_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putBoolean("sound", this.sound);
        return nbttagcompound;
    }

    public EnumTypeAudio getTypeAudio() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
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
            this.getWorld().playSound(null, this.pos, EnumSound.interruptone_steam.getSoundEvent(), SoundSource.BLOCKS, 1F, 1);
        } else {
            new PacketStopSound(getWorld(), this.pos);
        }
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }


    public void updateEntityServer() {
        super.updateEntityServer();


        boolean newActive = this.gainEnergy();

        if (this.energy.getEnergy() >= 1.0D && !this.chargeSlot.get(0).isEmpty()) {
            double used = ElectricItem.manager.charge(this.chargeSlot.get(0), this.energy.getEnergy(), 1, false, false);
            this.energy.useEnergy(used);
        }


        if (this.getActive() != newActive) {
            this.setActive(newActive);
        }
        if (getWorld().getGameTime() % 60 == 0) {
            initiate(2);
        }
    }


    public float getWrenchDropRate() {
        return 0.9F;
    }

    public boolean gainEnergy() {
        if (this.isConverting()) {
            this.energy.addEnergy(8 * coef);
            this.steam.useEnergy(4);
            if (getWorld().getGameTime() % 60 == 0) {
                initiate(0);
            }
            return true;
        } else {

            initiate(2);
            return false;
        }
    }

    public boolean isConverting() {
        return this.steam.canUseEnergy(4) && this.energy.getEnergy() + (double) 8 <= this.energy.getCapacity();
    }


    public ContainerSteamGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerSteamGenerator(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiSteamGenerator((ContainerSteamGenerator) menu);
    }


    @Override
    public void onNetworkEvent(final int var1) {

    }

}
