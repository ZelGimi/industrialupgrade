package com.denfop.invslot;

import cofh.redstoneflux.api.IEnergyContainerItem;
import com.denfop.IUItem;
import com.denfop.api.energy.IAdvEnergySink;
import com.denfop.componets.AdvEnergy;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.tiles.base.TileEntityElectricBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.panels.entity.WirelessTransfer;
import com.denfop.utils.ModUtils;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class InvSlotElectricBlock extends InvSlot {

    private final int type;

    public InvSlotElectricBlock(TileEntityInventory base, int oldStartIndex1, String name, int count) {
        super(base, name, InvSlot.Access.IO, count, InvSlot.InvSide.TOP);
        this.type = oldStartIndex1;
        this.setStackSizeLimit(1);
    }

    @Override
    public void onChanged() {
        super.onChanged();
        TileEntityElectricBlock tile = (TileEntityElectricBlock) base;
        if (this.type == 3) {
            if (tile.UUID != null) {
                tile.personality = this.personality();
            }
            tile.output_plus = this.output_plus(tile.l);
            tile.output = tile.l + tile.output_plus;
            tile.movementcharge = this.getstats().get(0);
            tile.movementchargeitem = this.getstats().get(1);
            tile.movementchargerf = this.getstats().get(2);
            tile.movementchargeitemrf = this.getstats().get(3);

            tile.rf = this.getstats().get(4);

        }
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemAdditionModule && this
                    .get(i)
                    .getItemDamage() == 10) {
                tile.wireless = true;
            }
        }


    }

    public boolean accepts(ItemStack itemStack) {
        if (type == 3) {
            return ((itemStack.getItemDamage() >= 4 || itemStack.getItemDamage() == 0)
                    && itemStack.getItem() instanceof ItemAdditionModule)
                    || (EnumModule.getFromID(itemStack.getItemDamage()) != null && EnumModule.getFromID(itemStack.getItemDamage()) ==
                    EnumModule.OUTPUT && itemStack.getItem() instanceof ItemBaseModules || itemStack
                    .getItem()
                    .equals(IUItem.module_quickly));
        }
        if (type == 2) {
            return itemStack.getItem() instanceof IElectricItem;
        }
        if (type == 1) {
            return itemStack.getItem() instanceof IElectricItem || itemStack.getItem() instanceof IEnergyContainerItem;
        }
        return false;
    }

    public boolean checkignore() {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getItem().equals(IUItem.module_quickly)) {
                return true;
            }
        }
        return false;
    }

    public double charge(double amount, ItemStack stack, boolean simulate, boolean ignore) {
        if (amount < 0.0) {
            throw new IllegalArgumentException("Amount must be > 0.");
        }
        if (amount == 0.0) {
            return 0;
        }

        return ElectricItem.manager.charge(stack, amount, 2147483647, ignore, simulate);
    }

    public double discharge(double amount, ItemStack stack, boolean simulate) {
        if (amount < 0.0) {
            throw new IllegalArgumentException("Amount must be > 0.");
        }
        double charged = 0.0;
        if (amount == 0.0) {
            return 0;
        }

        final double energyIn = ElectricItem.manager.discharge(stack, amount, 2147483647, false, true, simulate);
        amount -= energyIn;
        charged += energyIn;
        if (amount <= 0.0) {
            return 0;
        }

        return charged;
    }

    public List<Boolean> getstats() {
        List<Boolean> list = new ArrayList<>();
        List<Boolean> list1 = new ArrayList<>();
        if (this.isEmpty()) {
            list1.add(false);
            list1.add(false);
            list1.add(false);
            list1.add(false);
            list1.add(false);
            return list1;
        }
        for (int i = 0; i < this.size(); i++) {


            ItemStack stack = this.get(i);
            if (stack.getItemDamage() == 5) {
                list.add(true);
            } else {
                list.add(false);
            }
            if (stack.getItemDamage() == 6) {
                list.add(true);
            } else {
                list.add(false);
            }
            if (stack.getItemDamage() == 7) {
                list.add(true);
            } else {
                list.add(false);
            }
            if (stack.getItemDamage() == 8) {
                list.add(true);
            } else {
                list.add(false);
            }
            if (stack.getItemDamage() == 4) {
                list.add(true);
            } else {
                list.add(false);
            }

        }
        list1.add(gettrue(list.get(0), list.get(5)));
        list1.add(gettrue(list.get(1), list.get(6)));
        list1.add(gettrue(list.get(2), list.get(7)));
        list1.add(gettrue(list.get(3), list.get(8)));
        list1.add(gettrue(list.get(4), list.get(9)));

        return list1;
    }

    public boolean gettrue(boolean o, boolean j) {
        return (o || j);
    }

    public boolean personality() {

        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) == null) {
                continue;
            }
            ItemStack stack = this.get(i);
            if (stack.getItem() instanceof ItemAdditionModule) {
                if (stack.getItemDamage() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        if (this.type == 3) {
            wirelessmodule();
        }
    }

    public void wirelessmodule() {
        TileEntityElectricBlock tile = (TileEntityElectricBlock) base;

        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != null && this.get(i).getItem() instanceof ItemAdditionModule && this.get(i).getItemDamage() == 10) {

                int x;
                int y;
                int z;
                NBTTagCompound nbttagcompound = ModUtils.nbt(this.get(i));

                x = nbttagcompound.getInteger("Xcoord");
                y = nbttagcompound.getInteger("Ycoord");
                z = nbttagcompound.getInteger("Zcoord");
                BlockPos pos = new BlockPos(x, y, z);
                if (tile.getWorld().getTileEntity(pos) != null
                        && tile.getWorld().getTileEntity(pos) instanceof TileEntityBlock && x != 0 && nbttagcompound.getBoolean(
                        "change")
                        && y != 0 && z != 0) {
                    TileEntityBlock tile1 = (TileEntityBlock) tile.getWorld().getTileEntity(pos);

                    assert tile1 != null;
                    if (tile1.getComponent(Energy.class) != null) {

                        final Energy energy = tile1.getComponent(Energy.class);
                        if (energy.getDelegate() instanceof IEnergySink) {
                            tile.wirelessTransferList.add(new WirelessTransfer(tile1, (IEnergySink) energy.getDelegate()));
                        }
                    } else if (tile1.getComponent(AdvEnergy.class) != null) {
                        final AdvEnergy energy = tile1.getComponent(AdvEnergy.class);
                        if (energy.getDelegate() instanceof IAdvEnergySink) {
                            tile.wirelessTransferList.add(new WirelessTransfer(tile1, (IEnergySink) energy.getDelegate()));
                        }
                    }

                }
            }


        }

    }

    public double output_plus(double l) {
        int output = 0;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) == null) {
                continue;
            }
            ItemStack stack = this.get(i);
            if (EnumModule.getFromID(stack.getItemDamage()) != null && stack.getItem() instanceof ItemBaseModules) {
                output += l * EnumModule.getFromID(stack.getItemDamage()).percent;
            }
        }
        return output;
    }

}
