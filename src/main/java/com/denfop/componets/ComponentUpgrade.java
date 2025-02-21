package com.denfop.componets;

import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComponentUpgrade extends AbstractComponent {

    private final List<TypeUpgrade> listUpgrade;
    private final List<TypeUpgrade> listActiveUpgrade = new ArrayList<>();
    private boolean change = true;

    public ComponentUpgrade(final TileEntityInventory parent, TypeUpgrade... typeUpgrades) {
        super(parent);
        this.listUpgrade = Arrays.asList(typeUpgrades);
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(final boolean change) {
        this.change = change;
    }


    public boolean hasUpgrade(TypeUpgrade typeUpgrade) {
        return listActiveUpgrade.contains(typeUpgrade);
    }

    @Override
    public boolean canUsePurifier(final EntityPlayer player) {
        return !this.listActiveUpgrade.isEmpty();
    }

    @Override
    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.ItemStack;
    }

    @Override
    public List<ItemStack> getDrops() {
        final List<ItemStack> list = super.getDrops();
        for (TypeUpgrade upgrade : this.listActiveUpgrade) {
            list.add(upgrade.getStack().copy());
        }
        return list;
    }

    public ItemStack getItemStackUpgrade() {
        final TypeUpgrade type = this.listActiveUpgrade.remove(0);
        this.change = true;
        if (type != null) {
            return type.getStack().copy();
        }
        return super.getItemStackUpgrade();
    }

    @Override
    public boolean onBlockActivated(final EntityPlayer player, final EnumHand hand) {
        final ItemStack stack = player.getHeldItem(hand);
        for (TypeUpgrade upgrade : this.listUpgrade) {
            if (upgrade.getStack().isItemEqual(stack)) {
                if (!this.listActiveUpgrade.contains(upgrade)) {
                    this.listActiveUpgrade.add(upgrade);
                    stack.shrink(1);
                    this.change = true;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public NBTTagCompound writeToNbt() {
        final NBTTagCompound nbt = super.writeToNbt();
        nbt.setInteger("max", this.listActiveUpgrade.size());
        int i = 0;
        for (TypeUpgrade upgrade : this.listActiveUpgrade) {
            nbt.setInteger(String.valueOf(i), upgrade.ordinal());
            i++;
        }
        return nbt;
    }

    public CustomPacketBuffer updateComponent() {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeInt(this.listActiveUpgrade.size());
        for (TypeUpgrade upgrade : this.listActiveUpgrade) {
            buffer.writeInt(upgrade.ordinal());

        }
        return buffer;
    }

    @Override
    public void onNetworkUpdate(final CustomPacketBuffer is) throws IOException {
        super.onNetworkUpdate(is);
        int i = is.readInt();
        for (int j = 0; j < i; j++) {
            this.listActiveUpgrade.add(TypeUpgrade.values()[is.readInt()]);
        }

    }

    @Override
    public void readFromNbt(final NBTTagCompound nbt) {
        super.readFromNbt(nbt);
        int size = nbt.getInteger("max");
        final TypeUpgrade[] values = TypeUpgrade.values();
        for (int i = 0; i < size; i++) {
            this.listActiveUpgrade.add(values[nbt.getInteger(String.valueOf(i))]);
        }
    }

}
