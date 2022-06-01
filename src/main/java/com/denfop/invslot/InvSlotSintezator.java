package com.denfop.invslot;

import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.items.modules.ItemModuleType;
import com.denfop.tiles.base.TileEntitySintezator;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.EnumType;
import com.denfop.utils.ModUtils;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class InvSlotSintezator extends InvSlot {


    public final int type;
    public final TileEntitySintezator tile;

    public InvSlotSintezator(TileEntitySintezator base1, String name, int type, int count) {
        super(base1, name, InvSlot.Access.IO, count, InvSlot.InvSide.TOP);
        this.type = type;
        if (type == 0) {
            this.setStackSizeLimit(Config.limit);
        } else {
            this.setStackSizeLimit(1);
        }
        this.tile = base1;
    }

    @Override
    public void onChanged() {
        super.onChanged();
        if (this.type == 0) {
            double[] tire_massive = new double[9];
            double[] myArray1 = new double[4];
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i) != null && IUItem.map3.get(this.get(i).getUnlocalizedName()) != null) {
                    int p = Math.min(this.get(i).getCount(), Config.limit);
                    ItemStack stack = this.get(i);
                    EnumSolarPanels solar;
                    solar = IUItem.map3.get(stack.getUnlocalizedName());

                    if (solar != null) {


                        myArray1[0] += (solar.genday * p);
                        myArray1[1] += (solar.gennight * p);
                        myArray1[2] += (solar.maxstorage * p);
                        myArray1[3] += (solar.producing * p);
                        tire_massive[i] = solar.tier;
                    }
                } else if (this.get(i) != null && IUItem.panel_list.get(this
                        .get(i)
                        .getUnlocalizedName()) != null) {
                    int p = Math.min(this.get(i).getCount(), Config.limit);
                    ItemStack stack = this.get(i);
                    List solar;
                    solar = IUItem.panel_list.get(stack.getUnlocalizedName() + ".name");

                    if (solar != null) {


                        myArray1[0] += ((double) solar.get(0) * p);
                        myArray1[1] += ((double) solar.get(1) * p);
                        myArray1[2] += ((double) solar.get(2) * p);
                        myArray1[3] += ((double) solar.get(3) * p);
                        tire_massive[i] = (double) solar.get(4);
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
            tile.solartype = this.solartype();
            tile.genDay = myArray1[0];
            tile.genNight = myArray1[1];
            tile.maxStorage = myArray1[2];
            tile.maxStorage2 = myArray1[2] * Config.coefficientrf;
            tile.production = myArray1[3];
        } else {
            this.checkmodule();
            this.getrfmodule();
        }

    }

    public void wirelessmodule() {
        TileEntitySintezator tile = (TileEntitySintezator) base;

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
                        && tile.getWorld().getTileEntity(pos) instanceof TileEntityBlock && x != 0
                        && y != 0 && z != 0 && !nbttagcompound.getBoolean("change")) {
                    TileEntityBlock tile1 = (TileEntityBlock) tile.getWorld().getTileEntity(pos);

                    assert tile1 != null;
                    if (tile1.getComponent(Energy.class) != null) {
                        final Energy energy = tile1.getComponent(Energy.class);
                        tile.storage -= energy.addEnergy(tile.storage);
                    }

                }
            }


        }

    }

    public boolean accepts(ItemStack itemStack) {
        if (this.type == 0) {
            return IUItem.map3.containsKey(itemStack.getUnlocalizedName()) || IUItem.panel_list.containsKey(itemStack.getUnlocalizedName());
        } else {
            return itemStack.getItem() instanceof ItemBaseModules
                    || (itemStack.getItem() instanceof ItemAdditionModule && itemStack.getItemDamage() == 4)
                    || (itemStack.getItem() instanceof ItemAdditionModule && itemStack.getItemDamage() == 10)
                    || (itemStack.getItem() instanceof ItemModuleType)
                    ;
        }
    }

    public void getrfmodule() {
        TileEntitySintezator tile = (TileEntitySintezator) base;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != null && this.get(i).getItemDamage() == 4 && this.get(i).getItem() instanceof ItemAdditionModule) {
                tile.getmodulerf = true;
                return;
            }
        }
        tile.getmodulerf = false;
    }

    public int solartype() {
        TileEntitySintezator tile = (TileEntitySintezator) base;

        List<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != null && this.get(i).getItem() instanceof ItemModuleType) {
                list1.add(get(i).getItemDamage() + 1);
            } else {
                list1.add(0);
            }
        }
        EnumType type = EnumType.getFromID(ModUtils.slot(list1));

        return tile.setSolarType(type);
    }

    public void checkmodule() {
        TileEntitySintezator tile = (TileEntitySintezator) base;
        double temp_day = tile.genDay;
        double temp_night = tile.genNight;
        double temp_storage = tile.maxStorage;
        double temp_producing = tile.production;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != null && EnumModule.getFromID(this.get(i).getItemDamage()) != null) {
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
        tile.genDay = temp_day;
        tile.genNight = temp_night;
        tile.maxStorage = temp_storage;
        tile.production = temp_producing;
    }

}
