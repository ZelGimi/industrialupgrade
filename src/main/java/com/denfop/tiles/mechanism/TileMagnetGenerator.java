package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerMagnetGenerator;
import com.denfop.gui.GuiMagnetGenerator;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

public class TileMagnetGenerator extends TileElectricMachine {

    public int timer;

    public TileMagnetGenerator() {
        super(0, 14, 1);
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

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.timer = nbttagcompound.getInteger("timer");

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

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("timer", this.timer);
        return nbttagcompound;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.magnet_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (!(getWorld()).isRemote) {
            NBTTagCompound nbt = ModUtils.nbt(stack);
            if (nbt.getBoolean("work")) {
                this.timer = nbt.getInteger("timer");
            }
        }
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (drop.isItemEqual(this.getPickBlock(
                null,
                null
        )) && (wrench || this.teBlock.getDefaultDrop() == MultiTileBlock.DefaultDrop.Self)) {
            NBTTagCompound nbt = ModUtils.nbt(drop);
            nbt.setInteger("timer", this.timer);
            nbt.setBoolean("work", true);

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
        if (this.world.provider.getWorldTime() % 20 == 0) {
            timer--;
        }
        this.energy.addEnergy(2);
        if (!getActive()) {
            setActive(true);
            initiate(0);
        }
        if (this.getWorld().provider.getWorldTime() % 200 == 0) {
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
    public ContainerMagnetGenerator getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerMagnetGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiMagnetGenerator getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiMagnetGenerator(new ContainerMagnetGenerator(entityPlayer, this));
    }

}
