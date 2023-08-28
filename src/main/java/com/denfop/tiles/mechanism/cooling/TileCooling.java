package com.denfop.tiles.mechanism.cooling;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.client.ComponentClientEffectRender;
import com.denfop.componets.client.EffectType;
import com.denfop.container.ContainerCoolMachine;
import com.denfop.gui.GuiCoolMachine;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class TileCooling extends TileElectricMachine implements IUpdatableTileEvent {


    public CoolComponent cold;
    public int max;
    public boolean work;
    private int coef;

    public TileCooling() {
        super(10000D, 14, 1);
        this.cold = this.addComponent(CoolComponent.asBasicSource(this, 4, tier));
        this.max = 4;
        this.componentClientEffectRender = new ComponentClientEffectRender(this, EffectType.REFRIGERATOR);

    }

    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        final CustomPacketBuffer packet = super.writeUpdatePacket();
        try {
            EncoderHandler.encode(packet, cold);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void readUpdatePacket(final CustomPacketBuffer customPacketBuffer) {
        super.readUpdatePacket(customPacketBuffer);
        try {
            cold.readFromNbt((NBTTagCompound) DecoderHandler.decode(customPacketBuffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            max = (int) DecoderHandler.decode(customPacketBuffer);
            work = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, max);
            EncoderHandler.encode(packet, work);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, cold);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            cold.readFromNbt((NBTTagCompound) DecoderHandler.decode(customPacketBuffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.cooling;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.max = nbttagcompound.getInteger("max");
        this.work = nbttagcompound.getBoolean("work");
        this.cold.setCapacity(this.max);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("max", this.max);
        nbttagcompound.setBoolean("work", this.work);
        return nbttagcompound;

    }


    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        if (i == 0) {
            this.cold.setCapacity(this.max + 4);
            if (this.cold.getCapacity() > 64) {
                this.cold.setCapacity(64);

            }
            this.max = (int) this.cold.getCapacity();
        }
        if (i == 1) {
            this.cold.setCapacity(this.max - 4);
            if (this.cold.getCapacity() < 4) {
                this.cold.setCapacity(4);

            }
            this.max = (int) this.cold.getCapacity();
        }
        if (i == 2) {
            this.work = !this.work;
        }
        if (i == 10) {
            super.updateTileServer(entityPlayer, i);
        }
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 30 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip, advanced);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.coef = (int) Math.max(Math.ceil(this.cold.storage / 16), 1);
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.cold.allow || work) {
            if (this.energy.getEnergy() >= 30 * this.coef && this.cold.getEnergy() < this.cold.getCapacity()) {
                this.cold.addEnergy(1);
                this.energy.useEnergy(30 * this.coef);
                initiate(0);

            }
            if (this.world.provider.getWorldTime() % 400 == 0) {
                initiate(2);
            }

            if (this.energy.getEnergy() < 30 * this.coef) {
                initiate(2);
            } else {
                initiate(0);
            }

        } else {
            initiate(2);
        }
        if (this.world.provider.getWorldTime() % 20 == 0 && this.cold.getEnergy() >= 1) {
            this.cold.addEnergy(-1);
        }
    }


    @Override
    public ContainerCoolMachine getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerCoolMachine(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiCoolMachine(getGuiContainer(entityPlayer));
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.cooling.getSoundEvent();
    }

}
