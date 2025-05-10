package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.tiles.base.TileAnalyzer;
import com.denfop.tiles.mechanism.quarry.TileBaseQuantumQuarry;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class InvSlotAnalyzer extends InvSlot implements ITypeSlot {

    private final int type;
    private final TileAnalyzer tile;
    private int stackSizeLimit;

    public InvSlotAnalyzer(TileAnalyzer base1, String name, int count, int type) {
        super(base1, TypeItemSlot.INPUT, count);
        this.type = type;
        this.stackSizeLimit = 1;
        this.tile = base1;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        if (this.type == 0) {
            return EnumTypeSlot.QUARRY1;
        }

        return EnumTypeSlot.BLOCKS;
    }

    public void update() {
        if (this.type == 0) {
            this.tile.blacklist = this.getblacklist();
            this.tile.whitelist = this.getwhitelist();
            this.tile.size = this.getChunksize();
            this.tile.lucky = this.lucky();
            this.tile.macerator = this.macerator();
            this.tile.comb_macerator = this.comb_macerator();
            this.tile.polisher = this.polisher();
            this.tile.consume = this.getenergycost();
            this.tile.update_chunk();
            this.tile.furnace = this.getFurnaceModule();
        }
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        if (this.type == 0) {
            this.tile.blacklist = this.getblacklist();
            this.tile.whitelist = this.getwhitelist();
            this.tile.size = this.getChunksize();
            this.tile.lucky = this.lucky();
            this.tile.consume = this.getenergycost();
            this.tile.macerator = this.macerator();
            this.tile.polisher = this.polisher();
            this.tile.comb_macerator = this.comb_macerator();
            this.tile.furnace = this.getFurnaceModule();
        }
        return content;
    }

    public boolean accepts(ItemStack itemStack, final int index) {
        if (this.type == 0) {
            for (int i = 0; i < this.size(); i++) {
                if (!this.get(i).isEmpty()) {

                    if ((itemStack.getItem() instanceof ItemQuarryModule) && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) == IUItem.module9.getMeta((ItemQuarryModule) itemStack.getItem()) && this
                            .get(i)
                            .getItem() == itemStack.getItem()) {
                        return false;

                    }

                }
            }


            return (itemStack.getItem() instanceof ItemQuarryModule)

                    || itemStack.getItem().equals(IUItem.quarrymodule.getItem())
                    || (itemStack.getItem() instanceof ItemAdditionModule && IUItem.module7.getMeta((ItemAdditionModule) itemStack.getItem()) == 10);
        } else if (this.type == 1) {
            if (itemStack.getItemHolder().tags().collect(Collectors.toList()).size() > 0) {
                List<TagKey<Item>> id = itemStack.getItemHolder().tags().collect(Collectors.toList());
                String name = id.get(0).location().getPath();
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
                if (this.get(i).getItem().equals(IUItem.quarrymodule.getItem())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean macerator() {

        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                if (this.get(i).getItem() instanceof ItemQuarryModule && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) == 14) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean comb_macerator() {

        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                if (this.get(i).getItem() instanceof ItemQuarryModule && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) == 15) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean polisher() {

        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                if (this.get(i).getItem() instanceof ItemQuarryModule && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) == 16) {
                    return true;
                }
            }
        }
        return false;
    }

    public int lucky() {

        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                if (this.get(i).getItem() instanceof ItemQuarryModule && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) >= 6 && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) < 9) {
                    return IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) - 5;
                }
            }
        }
        return 0;
    }

    public boolean getFurnaceModule() {
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemQuarryModule && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) == 0) {
                return true;
            }
        }
        return false;
    }

    public List<String> getblacklist() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {

            if (!this.get(i).isEmpty()&& this.get(i).getItem() instanceof ItemQuarryModule && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) == 12) {
                final CompoundTag nbt = ModUtils.nbt(this.get(i));
                int size = nbt.getInt("size");
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
            if (!this.get(i).isEmpty()&& this.get(i).getItem() instanceof ItemQuarryModule  && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) == 13) {
                final CompoundTag nbt = ModUtils.nbt(this.get(i));
                int size = nbt.getInt("size");
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
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemQuarryModule && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) > 8 && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) <= 11) {
                size = IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) - 8;
                return size;
            }
        }

        return size;
    }

    public boolean getwirelessmodule() {
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemAdditionModule && IUItem.module7.getMeta((ItemAdditionModule) this.get(i).getItem()) == 10) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> wirelessmodule() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemAdditionModule && IUItem.module7.getMeta((ItemAdditionModule) this.get(i).getItem()) == 10) {
                int x;
                int y;
                int z;
                CompoundTag nbttagcompound = ModUtils.nbt(this.get(i));

                x = nbttagcompound.getInt("Xcoord");
                y = nbttagcompound.getInt("Ycoord");
                z = nbttagcompound.getInt("Zcoord");

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
                if (this.get(i).getItem() instanceof ItemQuarryModule && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) > 0 && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) < 6) {
                    proccent = IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem());
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
                if (this.get(i).getItem() instanceof ItemQuarryModule && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) > 0 && IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem()) < 6) {
                    proccent = IUItem.module9.getMeta((ItemQuarryModule) this.get(i).getItem());
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
