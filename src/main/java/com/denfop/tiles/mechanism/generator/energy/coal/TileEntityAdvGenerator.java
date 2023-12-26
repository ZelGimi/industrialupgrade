package com.denfop.tiles.mechanism.generator.energy.coal;

import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerGenerator;
import com.denfop.gui.GuiGenerator;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.generator.energy.TileEntityBaseGenerator;
import com.denfop.utils.DamageHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class TileEntityAdvGenerator extends TileEntityBaseGenerator implements IType {

    public final InvSlot fuelSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1){
        @Override
        public boolean accepts(final ItemStack stack, final int index) {
            return ModUtils.getFuelValue(stack, false) > 0;
        }
    };

    private final double coef;


    public int itemFuelTime = 0;

    public TileEntityAdvGenerator(double coef, int maxstorage, int tier) {
        super(
                coef * (double) Math.round(10.0F * 1),
                tier,
                maxstorage
        );
        this.coef = coef;
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

    public int gaugeFuelScaled(int i) {
        if (this.fuel <= 0) {
            return 0;
        } else {
            if (this.itemFuelTime <= 0) {
                this.itemFuelTime = this.fuel;
            }

            return Math.min(this.fuel * i / this.itemFuelTime, i);
        }
    }

    public int consumeFuel() {
        ItemStack fuel = this.consume(1);
        return fuel == null ? 0 : ModUtils.getFuelValue(fuel, false);
    }

    public boolean gainFuel() {

        int fuelValue = this.consumeFuel() / 4;
        if (fuelValue == 0) {
            return false;
        } else {
            this.fuel += fuelValue;
            this.itemFuelTime = fuelValue;
            return true;
        }
    }


    public ItemStack consume(int amount) {
        ItemStack ret = ItemStack.EMPTY;


        ItemStack stack = this.fuelSlot.get(0);
        if (!stack.isEmpty() && (ModUtils.getSize(stack) == 1 || !stack.getItem().hasContainerItem(stack))) {
            int currentAmount = Math.min(amount, ModUtils.getSize(stack));
            amount -= currentAmount;
            if (ModUtils.getSize(stack) == currentAmount) {
                if (stack.getItem().hasContainerItem(stack)) {
                    ItemStack container = stack.getItem().getContainerItem(stack);
                    if (container.isEmpty() && container.isItemStackDamageable() && DamageHandler.getDamage(container) > DamageHandler.getMaxDamage(
                            container)) {
                        container = ItemStack.EMPTY;
                    }

                    this.fuelSlot.put(0, container);
                } else {
                    this.fuelSlot.put(0, ItemStack.EMPTY);
                }
            } else {
                this.fuelSlot.put(0, ModUtils.decSize(stack, currentAmount));
            }

            ret = ModUtils.setSize(stack, currentAmount);
        }


        return ret;
    }

    public int gaugeStorageScaled(int i) {
        return (int) (this.energy.getEnergy() * (double) i / this.energy.getCapacity());
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }


    public ContainerGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiGenerator(new ContainerGenerator(entityPlayer, this));
    }

    public boolean isConverting() {
        return this.fuel > 0;
    }

    public String getOperationSoundFile() {
        return "Generators/GeneratorLoop.ogg";
    }


    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.itemFuelTime = nbt.getInteger("itemFuelTime");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("itemFuelTime", this.itemFuelTime);
        return nbt;
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
