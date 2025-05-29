package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

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
            return ((itemStack.getItem() instanceof ItemAdditionModule && IUItem.module7.getMeta((ItemAdditionModule) itemStack.getItem()) > 4)
                    || (itemStack.getItem() instanceof ItemBaseModules && EnumModule.getFromID(IUItem.basemodules.getMeta((ItemBaseModules) itemStack.getItem())) != null && EnumModule.getFromID(IUItem.basemodules.getMeta((ItemBaseModules) itemStack.getItem())) ==
                    EnumModule.OUTPUT));
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
            if (stack.getItem() instanceof ItemAdditionModule<?>) {
                int meta = IUItem.module7.getMeta((ItemAdditionModule) stack.getItem());
                if (meta == 5) {
                    list.add(true);
                } else {
                    list.add(false);
                }
                if (meta == 6) {
                    list.add(true);
                } else {
                    list.add(false);
                }
            }else {
                list.add(false);
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
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        TileElectricBlock tile = (TileElectricBlock) base;
        if (this.type == 3) {
            tile.output_plus = this.output_plus(tile.l);
            tile.output = tile.l + tile.output_plus;
            tile.movementcharge = this.getstats().get(0);
            tile.movementchargeitem = this.getstats().get(1);
            this.wirelessmodule();
        }
        return content;
    }

    public void wirelessmodule() {
        TileElectricBlock tile = (TileElectricBlock) base;

        tile.wirelessComponent.setUpdate(false);

        tile.wirelessComponent.removeAll();
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemAdditionModule && IUItem.module7.getMeta((ItemAdditionModule) this.get(i).getItem()) == 10) {

                int x;
                int y;
                int z;
                CompoundTag nbttagcompound = ModUtils.nbt(this.get(i));

                x = nbttagcompound.getInt("Xcoord");
                y = nbttagcompound.getInt("Ycoord");
                z = nbttagcompound.getInt("Zcoord");
                if (nbttagcompound.getBoolean("change")) {
                    BlockPos pos = new BlockPos(x, y, z);
                    tile.wirelessComponent.setUpdate(true);
                    tile.wirelessComponent.addConnect(pos);
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
            if (stack.getItem() instanceof ItemBaseModules && EnumModule.getFromID(IUItem.basemodules.getMeta((ItemBaseModules) stack.getItem())) != null) {
                output += l * EnumModule.getFromID(IUItem.basemodules.getMeta((ItemBaseModules) stack.getItem())).percent;
            }
        }
        return output;
    }

}
