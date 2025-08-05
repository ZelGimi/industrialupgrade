package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerBlockLimiter;
import com.denfop.gui.GuiBlockLimiter;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlotLimiter;
import com.denfop.items.ItemCraftingElements;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class TileLimiter extends TileEntityInventory implements IUpdatableTileEvent, IAudioFixer {

    private final Energy energy;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio;
    public boolean sound = true;
    public InvSlotLimiter slot;
    public double max_value;

    public TileLimiter(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.limiter, pos, state);
        this.energy = this.addComponent(new Energy(
                this,
                Double.MAX_VALUE,
                Arrays
                        .asList(Direction.values())
                        .stream()
                        .filter(facing -> facing != this.getFacing())
                        .collect(Collectors.toList()), Collections.singletonList(this.getFacing()),
                14,
                0,
                false
        ));
        this.energy.setLimit(true);
        this.slot = new InvSlotLimiter(this);
        valuesAudio = EnumTypeAudio.values();
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            max_value = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, max_value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.limiter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public EnumTypeAudio getTypeAudio() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
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
    public void onLoaded() {
        super.onLoaded();
        if (this.slot.isEmpty()) {
            setTier(0);
        } else {
            setTier(((ItemCraftingElements<?>) this.slot.get(0).getItem()).getElement().getId() - 205);
        }
        this.energy.setDirections(Arrays
                .asList(Direction.values())
                .stream()
                .filter(facing -> facing != this.getFacing())
                .collect(Collectors.toList()), Collections.singletonList(this.getFacing()));
        this.energy.setCapacity(this.energy.limit_amount);
    }

    public void setTier(int tier) {
        this.energy.setSourceTier(tier);
        if (tier != 0) {
            this.energy.setCapacity(EnergyNetGlobal.instance.getPowerFromTier(tier));
        } else {
            this.energy.setCapacity(0);
            this.energy.limit_amount = 0;
        }
        if (tier != 0) {
            this.max_value = EnergyNetGlobal.instance.getPowerFromTier(tier);
        } else {
            this.max_value = 0;
        }

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();

    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putDouble("max_value", max_value);
        return nbt;
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.max_value = nbtTagCompound.getDouble("max_value");
    }

    public Energy getEnergy() {
        return energy;
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {

        this.energy.limit_amount += i;
        this.energy.limit_amount = (int) Math.min(this.max_value, this.energy.limit_amount);
        this.energy.limit_amount = (int) Math.max(0, this.energy.limit_amount);
        this.energy.setDirections(Arrays
                .asList(Direction.values())
                .stream()
                .filter(facing -> facing != this.getFacing())
                .collect(Collectors.toList()), Collections.singletonList(this.getFacing()));
        this.energy.setCapacity(this.energy.limit_amount);
        initiate(2);
        initiate(0);

    }

    @Override
    public ContainerBlockLimiter getGuiContainer(final Player entityPlayer) {
        return new ContainerBlockLimiter(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiBlockLimiter((ContainerBlockLimiter) menu);
    }


}
