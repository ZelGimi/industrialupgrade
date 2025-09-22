package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerPeatGenerator;
import com.denfop.gui.GuiPeatGenerator;
import com.denfop.invslot.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class TilePeatGenerator extends TileElectricMachine implements IType {


    public final Inventory slot;


    public int fuel = 0;

    public TilePeatGenerator() {
        super(0, 1, 0);
        energy = this.addComponent(Energy.asBasicSource(this, 150000, 1));
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.cultivated_peat_balls;
            }
        };
        this.addComponent(new SoilPollutionComponent(this, 0.2));
        this.addComponent(new AirPollutionComponent(this, 0.2));

    }

    @SideOnly(Side.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            ModUtils.showFlames(this.getWorld(), this.pos, this.getFacing());
        }

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            fuel = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.peat_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, fuel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }


    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        final ItemStack content = this.slot.get();
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.slot.isEmpty()) {
            if (fuel == 0) {
                fuel = 400;
                this.slot.get().shrink(1);
                if (!this.getActive()) {
                    this.setActive(true);
                }
            }
        }
        if (fuel == 0) {
            if (this.getActive()) {
                this.setActive(false);
            }
        }
        if (this.getActive()) {
            this.energy.addEnergy(25);
            fuel = Math.max(0, this.fuel - 1);
        }


    }

    public int gaugeFuelScaled(int i) {
        if (this.fuel <= 0) {
            return 0;
        } else {


            return (int) Math.min(this.fuel * i / 400D, i);
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.fuel = nbttagcompound.getInteger("fuel");
    }

    public int gaugeStorageScaled(int i) {
        return (int) (this.energy.getEnergy() * (double) i / this.energy.getCapacity());
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("fuel", this.fuel);
        return nbt;
    }

    public ContainerPeatGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerPeatGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiPeatGenerator(new ContainerPeatGenerator(entityPlayer, this));
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

}
