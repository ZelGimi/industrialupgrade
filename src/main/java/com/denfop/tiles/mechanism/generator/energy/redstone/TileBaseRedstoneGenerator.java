package com.denfop.tiles.mechanism.generator.energy.redstone;

import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerRedstoneGenerator;
import com.denfop.gui.GuiRedstoneGenerator;
import com.denfop.invslot.InvSlotRedstoneGenerator;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class TileBaseRedstoneGenerator extends TileElectricMachine implements IType {


    public final double coef;
    public final InvSlotRedstoneGenerator slot;


    public int fuel = 0;
    public int max_fuel = 400;
    public int redstone_coef = 1;

    public TileBaseRedstoneGenerator(double coef, int tier) {
        super(0, tier, 0);
        energy = this.addComponent(AdvEnergy.asBasicSource(this, 150000 * coef, tier));


        this.coef = coef;
        this.slot = new InvSlotRedstoneGenerator(this);
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
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.info_upgrade_energy") + this.coef);
        }
        if (this.getComp(AdvEnergy.class) != null) {
            AdvEnergy energy = this.getComp(AdvEnergy.class);
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
        if (content.isEmpty()) {
            this.redstone_coef = 0;
        }
        if (content.getItem() == Items.REDSTONE) {
            this.redstone_coef = 1;
        } else {
            this.redstone_coef = 9;
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.slot.isEmpty()) {
            if (fuel == 0) {
                fuel = max_fuel;
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
            this.energy.addEnergy(25 * this.coef * redstone_coef);
        }
        fuel = Math.max(0, this.fuel - 1);
        if (fuel == 0) {
            if (this.slot.get().isEmpty()) {
                this.redstone_coef = 0;
            }
        }
    }

    public int gaugeFuelScaled(int i) {
        if (this.fuel <= 0) {
            return 0;
        } else {


            return Math.min(this.fuel * i / this.max_fuel, i);
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

    public ContainerRedstoneGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerRedstoneGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiRedstoneGenerator(new ContainerRedstoneGenerator(entityPlayer, this));
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
