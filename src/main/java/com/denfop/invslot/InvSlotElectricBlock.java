package com.denfop.invslot;

import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergySink;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.panels.entity.WirelessTransfer;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class InvSlotElectricBlock extends InvSlot {

    private final int type;

    public InvSlotElectricBlock(TileEntityInventory base, int oldStartIndex1, int count) {
        super(base, TypeItemSlot.INPUT_OUTPUT, count);
        this.type = oldStartIndex1;
        this.setStackSizeLimit(1);
    }


    public boolean accepts(ItemStack itemStack, final int index) {
        if (type == 3) {
            return ((itemStack.getItemDamage() > 4 && itemStack.getItem() instanceof ItemAdditionModule)
                    || (EnumModule.getFromID(itemStack.getItemDamage()) != null && EnumModule.getFromID(itemStack.getItemDamage()) ==
                    EnumModule.OUTPUT && itemStack.getItem() instanceof ItemBaseModules));
        }
        return false;
    }

    public boolean checkignore() {
        TileElectricBlock tile = (TileElectricBlock) base;
        return tile.movementcharge || tile.movementchargeitem;
    }


    public List<Boolean> getstats() {
        List<Boolean> list = new ArrayList<>();
        List<Boolean> list1 = new ArrayList<>();
        if (this.isEmpty()) {
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


        }
        list1.add(gettrue(list.get(0), list.get(2)));
        list1.add(gettrue(list.get(1), list.get(3)));

        return list1;
    }

    public boolean gettrue(boolean o, boolean j) {
        return (o || j);
    }


    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        TileElectricBlock tile = (TileElectricBlock) base;
        if (this.type == 3) {
            tile.output_plus = this.output_plus(tile.l);
            tile.output = tile.l + tile.output_plus;
            tile.movementcharge = this.getstats().get(0);
            tile.movementchargeitem = this.getstats().get(1);
            tile.wireless = false;
            tile.wirelessTransferList.clear();
            for (int i = 0; i < this.size(); i++) {
                if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemAdditionModule && this
                        .get(i)
                        .getItemDamage() == 10) {
                    tile.wireless = true;
                    this.wirelessmodule();
                    break;
                }
            }
        }
    }

    public void wirelessmodule() {
        TileElectricBlock tile = (TileElectricBlock) base;

        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getItem() instanceof ItemAdditionModule && this.get(i).getItemDamage() == 10) {

                int x;
                int y;
                int z;
                NBTTagCompound nbttagcompound = ModUtils.nbt(this.get(i));

                x = nbttagcompound.getInteger("Xcoord");
                y = nbttagcompound.getInteger("Ycoord");
                z = nbttagcompound.getInteger("Zcoord");
                BlockPos pos = new BlockPos(x, y, z);
                final IEnergyTile energy = EnergyNetGlobal.instance.getTile(this.base
                        .getParent()
                        .getWorld(), pos);
                if (energy instanceof IEnergySink) {
                    tile.wirelessTransferList.add(new WirelessTransfer(
                            tile.getWorld().getTileEntity(pos),
                            (IEnergySink) energy
                    ));
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
