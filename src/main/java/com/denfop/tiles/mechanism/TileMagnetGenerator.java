package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerMagnetGenerator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiMagnetGenerator;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.List;

public class TileMagnetGenerator extends TileElectricMachine {

    public int timer;

    public TileMagnetGenerator(BlockPos pos, BlockState state) {
        super(0, 14, 1, BlockBaseMachine1.magnet_generator, pos, state);
        this.timer = 86400;
        this.energy = this.addComponent(Energy.asBasicSource(
                this,
                3456000,
                14
        ));
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.magnet_generator.getSoundEvent();
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.magnet_generator.info"));
        super.addInformation(stack, tooltip);
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.timer = nbttagcompound.getInt("timer");

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            timer = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, timer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("timer", this.timer);
        return nbttagcompound;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.magnet_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine.getBlock(getTeBlock().getId());
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (!(getWorld()).isClientSide) {
            CompoundTag nbt = ModUtils.nbt(stack);
            if (nbt.getBoolean("work")) {
                this.timer = nbt.getInt("timer");
            }
        }
    }

    @Override
    public List<ItemStack> getWrenchDrops(final Player player, final int fortune) {
        List<ItemStack> list = super.getWrenchDrops(player, fortune);
        CompoundTag nbt = ModUtils.nbt(list.get(0));
        nbt.putInt("timer", this.timer);
        nbt.putBoolean("work", true);
        return list;
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (drop.is(this.getPickBlock(
                null,
                null
        ).getItem()) && (wrench || this.teBlock.getDefaultDrop() == DefaultDrop.Self)) {
            CompoundTag nbt = ModUtils.nbt(drop);
            nbt.putInt("timer", this.timer);
            nbt.putBoolean("work", true);

        }
        return drop;
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        if (timer == 0) {
            if (this.getActive()) {
                initiate(2);
                setActive(false);
            }
            return;
        }
        if (this.getWorld().getGameTime() % 20 == 0) {
            timer--;
        }
        this.energy.addEnergy(2);
        if (!getActive()) {
            setActive(true);
            initiate(0);
        }
        if (this.getWorld().getGameTime() % 200 == 0) {
            initiate(2);
            initiate(0);
        }

    }

    public String getStartSoundFile() {
        return "Machines/magnet_generator.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    @Override
    public ContainerMagnetGenerator getGuiContainer(final Player entityPlayer) {
        return new ContainerMagnetGenerator(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(final Player entityPlayer, ContainerBase<? extends IAdvInventory> b) {
        return new GuiMagnetGenerator(new ContainerMagnetGenerator(entityPlayer, this));
    }

}
