package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.tiles.base.TileAnalyzer;
import com.denfop.tiles.mechanism.quarry.TileBaseQuantumQuarry;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class InvSlotAnalyzer extends InvSlot {

    private final int type;
    private final TileAnalyzer tile;
    private int stackSizeLimit;

    public InvSlotAnalyzer(TileAnalyzer base1, String name, int count, int type) {
        super(base1, TypeItemSlot.INPUT, count);
        this.type = type;
        this.stackSizeLimit = 1;
        this.tile = base1;
    }

    public void update() {
        if (this.type == 0) {
            this.tile.blacklist = this.getblacklist();
            this.tile.whitelist = this.getwhitelist();
            this.tile.size = this.getChunksize();
            this.tile.lucky = this.lucky();
            this.tile.consume = this.getenergycost();
            this.tile.update_chunk();
        }
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        if (this.type == 0) {
            this.tile.blacklist = this.getblacklist();
            this.tile.whitelist = this.getwhitelist();
            this.tile.size = this.getChunksize();
            this.tile.lucky = this.lucky();
            this.tile.consume = this.getenergycost();
        }
    }

    public boolean accepts(ItemStack itemStack, final int index) {
        if (this.type == 0) {
            for (int i = 0; i < this.size(); i++) {
                if (!this.get(i).isEmpty()) {
                    if (this.get(i).getItemDamage() == itemStack.getItemDamage() && this
                            .get(i)
                            .getItem() == itemStack.getItem() && (itemStack.getItem() instanceof ItemQuarryModule)) {
                        return false;

                    }

                }
            }


            return (itemStack.getItem() instanceof ItemQuarryModule)

                    || itemStack.getItem().equals(IUItem.quarrymodule)
                    || (itemStack.getItem() instanceof ItemAdditionModule && itemStack.getItemDamage() == 10);
        } else if (this.type == 1) {
            if (OreDictionary.getOreIDs(itemStack).length > 0) {
                int id = OreDictionary.getOreIDs(itemStack)[0];
                String name = OreDictionary.getOreName(id);
                return name.startsWith("ore");
            } else {
                return false;
            }
        }
        return false;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public boolean quarry() {
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                if (this.get(i).getItem().equals(IUItem.quarrymodule)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int lucky() {

        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                if (this.get(i).getItem() instanceof ItemQuarryModule && this.get(i).getItemDamage() >= 6 && this
                        .get(i)
                        .getItemDamage() < 9) {
                    return this.get(i).getItemDamage() - 5;
                }
            }
        }
        return 0;
    }

    public boolean getFurnaceModule() {
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemQuarryModule && this.get(i).getItemDamage() == 0) {
                return true;
            }
        }
        return false;
    }

    public List<String> getblacklist() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {

            if (!this.get(i).isEmpty() && get(i).getItemDamage() == 12) {
                final NBTTagCompound nbt = ModUtils.nbt(this.get(i));
                int size = nbt.getInteger("size");
                for (int j = 0; j < size; j++) {
                    String l = "number_" + j;
                    String temp = ModUtils.NBTGetString(get(i), l);
                    list.add(temp);
                }
            }

        }
        return list;
    }

    public List<String> getwhitelist() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && get(i).getItemDamage() == 13) {
                final NBTTagCompound nbt = ModUtils.nbt(this.get(i));
                int size = nbt.getInteger("size");
                for (int j = 0; j < size; j++) {
                    String l = "number_" + j;
                    String temp = ModUtils.NBTGetString(get(i), l);
                    list.add(temp);

                }
                break;
            }
        }
        return list;
    }

    public boolean CheckBlackList(List<String> list, String name) {
        if (list.isEmpty()) {
            return false;
        }
        return !list.contains(name);
    }

    public boolean CheckWhiteList(List<String> list, String name) {
        if (list.isEmpty()) {
            return true;
        }
        return list.contains(name);
    }

    public int getChunksize() {
        int size = 0;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemQuarryModule && this
                    .get(i)
                    .getItemDamage() > 8 && this
                    .get(i)
                    .getItemDamage() <= 11) {
                size = this.get(i).getItemDamage() - 8;
                return size;
            }
        }

        return size;
    }

    public boolean getwirelessmodule() {
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemAdditionModule && this
                    .get(i)
                    .getItemDamage() == 10) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> wirelessmodule() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemAdditionModule && this
                    .get(i)
                    .getItemDamage() == 10) {
                int x;
                int y;
                int z;
                NBTTagCompound nbttagcompound = ModUtils.nbt(this.get(i));

                x = nbttagcompound.getInteger("Xcoord");
                y = nbttagcompound.getInteger("Ycoord");
                z = nbttagcompound.getInteger("Zcoord");

                if (x != 0 && y != 0 && z != 0) {
                    list.add(x);
                    list.add(y);
                    list.add(z);
                }
                break;
            }
        }
        return list;
    }

    public double getenergycost(TileBaseQuantumQuarry target1) {
        double energy = target1.energyconsume;
        double proccent;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                if (this.get(i).getItem() instanceof ItemQuarryModule && this.get(i).getItemDamage() > 0 && this
                        .get(i)
                        .getItemDamage() < 6) {
                    proccent = this.get(i).getItemDamage();
                    proccent = (proccent * 0.05);
                    proccent *= energy;
                    proccent = (energy - proccent);
                    return proccent;

                }

            }

        }

        return energy;
    }

    public double getenergycost() {
        double energy = 1000;
        double proccent;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                if (this.get(i).getItem() instanceof ItemQuarryModule && this.get(i).getItemDamage() > 0 && this
                        .get(i)
                        .getItemDamage() < 6) {
                    proccent = this.get(i).getItemDamage();
                    proccent = (proccent * 0.05);
                    proccent *= energy;
                    proccent = (energy - proccent);
                    return proccent;

                }

            }

        }

        return energy;
    }

}
