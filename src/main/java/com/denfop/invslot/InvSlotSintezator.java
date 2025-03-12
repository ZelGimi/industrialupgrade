package com.denfop.invslot;

import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.items.modules.ItemModuleType;
import com.denfop.tiles.base.TileSintezator;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.EnumType;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvSlotSintezator extends InvSlot {


    public final int type;
    public final TileSintezator tile;

    public InvSlotSintezator(TileSintezator base1, String name, int type, int count) {
        super(base1, TypeItemSlot.INPUT_OUTPUT, count);
        this.type = type;
        if (type == 0) {
            this.setStackSizeLimit(Config.limit);
        } else {
            this.setStackSizeLimit(1);
        }
        this.tile = base1;
    }


    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        if (this.type == 0) {
            this.tile.update();
        } else {
            this.update();
        }
    }

    private EnumSolarPanels getType(ItemStack itemStack) {
        for (Map.Entry<ItemStack, EnumSolarPanels> entry : IUItem.map3.entrySet()) {
            if (entry.getKey().isItemEqual(itemStack)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void update() {
        if (this.type == 0) {
            double[] tire_massive = new double[9];
            double[] myArray1 = new double[4];
            for (int i = 0; i < this.size(); i++) {
                EnumSolarPanels solar = getType(this.get(i));
                if (!this.get(i).isEmpty() && solar != null) {
                    int p = Math.min(this.get(i).getCount(), Config.limit);
                    myArray1[0] += (solar.genday * p);
                    myArray1[1] += (solar.gennight * p);
                    myArray1[2] += (solar.maxstorage * p);
                    myArray1[3] += (solar.producing * p);
                    tire_massive[i] = solar.tier;
                } else if (!this.get(i).isEmpty() && IUItem.panel_list.get(this
                        .get(i)
                        .getUnlocalizedName()) != null) {
                    int p = Math.min(this.get(i).getCount(), Config.limit);
                    ItemStack stack = this.get(i);
                    List solar1;
                    solar1 = IUItem.panel_list.get(stack.getUnlocalizedName());

                    if (solar1 != null) {


                        myArray1[0] += ((double) solar1.get(0) * p);
                        myArray1[1] += ((double) solar1.get(1) * p);
                        myArray1[2] += ((double) solar1.get(2) * p);
                        myArray1[3] += ((double) solar1.get(3) * p);
                        tire_massive[i] = (int) solar1.get(4);
                    }
                }
            }
            double max = tire_massive[0];
            for (double v : tire_massive) {
                if (v > max) {
                    max = v;
                }
            }

            tile.machineTire = (int) max;
            tile.genDay = myArray1[0];
            tile.genNight = myArray1[1];
            tile.maxStorage = myArray1[2];
            tile.production = myArray1[3];
            this.tile.inputslotA.checkmodule();
            int type = this.tile.solartype;
            this.tile.solartype = this.solartype();
            if (type != tile.solartype) {
                tile.updateTileEntityField();
            }
        } else {
            this.tile.inputslot.update();
        }
    }

    public void wirelessmodule() {
        TileSintezator tile = (TileSintezator) base;
        tile.wirelessComponent.removeAll();
        tile.wirelessComponent.setUpdate(false);
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
                if (!nbttagcompound.getBoolean("change")) {
                    tile.wirelessComponent.setUpdate(true);
                    BlockPos pos = new BlockPos(x, y, z);
                    tile.wirelessComponent.addConnect(pos);
                }
            }


        }

    }

    public boolean accepts(ItemStack itemStack, final int index) {
        if (this.type == 0) {
            return getType(itemStack) != null || IUItem.panel_list.containsKey(itemStack.getUnlocalizedName());
        } else {
            return itemStack.getItem() instanceof ItemBaseModules
                    || (itemStack.getItem() instanceof ItemAdditionModule && itemStack.getItemDamage() == 4)
                    || (itemStack.getItem() instanceof ItemAdditionModule && itemStack.getItemDamage() == 10)
                    || (itemStack.getItem() instanceof ItemAdditionModule && itemStack.getItemDamage() == 1)
                    || (itemStack.getItem() instanceof ItemAdditionModule && itemStack.getItemDamage() == 2)
                    || (itemStack.getItem() instanceof ItemModuleType)
                    ;
        }
    }


    public int solartype() {
        TileSintezator tile = (TileSintezator) base;

        List<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < tile.inputslotA.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemModuleType) {

                list1.add(get(i).getItemDamage() + 1);
            } else {
                list1.add(0);
            }
        }

        EnumType type = EnumType.getFromID(ModUtils.slot(list1));
        return tile.setSolarType(type);
    }

    public void checkmodule() {
        TileSintezator tile = (TileSintezator) base;
        double temp_day = tile.genDay;
        double temp_night = tile.genNight;
        double temp_storage = tile.maxStorage;
        double temp_producing = tile.production;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && EnumModule.getFromID(this.get(i).getItemDamage()) != null && this
                    .get(i)
                    .getItem() instanceof ItemBaseModules) {
                EnumModule module = EnumModule.getFromID(this.get(i).getItemDamage());
                EnumBaseType type = module.type;
                double percent = module.percent;
                switch (type) {
                    case DAY:
                        temp_day += tile.genDay * percent;
                        break;
                    case NIGHT:
                        temp_night += tile.genNight * percent;
                        break;
                    case STORAGE:
                        temp_storage += tile.maxStorage * percent;
                        break;
                    case OUTPUT:
                        temp_producing += tile.production * percent;
                        break;
                }
            }
        }
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemAdditionModule) {
                int damage = this.get(i).getItemDamage();
                if (damage == 1) {
                    tile.machineTire++;
                }
                if (damage == 2) {
                    tile.machineTire--;
                }
            }
        }
        this.wirelessmodule();
        tile.genDay = temp_day;
        tile.genNight = temp_night;
        tile.maxStorage = temp_storage;
        tile.production = temp_producing;
    }

}
