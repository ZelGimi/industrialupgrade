package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.WirelessConnection;
import com.denfop.items.modules.*;
import com.denfop.tiles.base.TileSintezator;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.EnumType;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

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
            this.setStackSizeLimit(64);
        } else {
            this.setStackSizeLimit(1);
        }
        this.tile = base1;
    }


    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        if (this.type == 0) {
            this.tile.inputslotA.update();
        } else {
            this.update();
        }
        return content;
    }

    private EnumSolarPanels getType(ItemStack itemStack) {
        for (Map.Entry<ItemStack, EnumSolarPanels> entry : IUItem.map3.entrySet()) {
            if (entry.getKey().is(itemStack.getItem())) {
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
                    int p = Math.min(this.get(i).getCount(), 64);
                    myArray1[0] += (solar.genday * p);
                    myArray1[1] += (solar.gennight * p);
                    myArray1[2] += (solar.maxstorage * p);
                    myArray1[3] += (solar.producing * p);
                    tire_massive[i] = solar.tier;
                } else if (!this.get(i).isEmpty() && IUItem.panel_list.get(this
                        .get(i)
                        .getDescriptionId()) != null) {
                    int p = Math.min(this.get(i).getCount(), 64);
                    ItemStack stack = this.get(i);
                    List solar1;
                    solar1 = IUItem.panel_list.get(stack.getDescriptionId());

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
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemAdditionModule && IUItem.module7.getMeta(this.get(i)) == 10) {
                WirelessConnection wirelessConnection = this.get(i).getOrDefault(DataComponentsInit.WIRELESS, WirelessConnection.EMPTY);

                if (wirelessConnection == WirelessConnection.EMPTY)
                    continue;
                int x;
                int y;
                int z;
                x = wirelessConnection.x();
                y = wirelessConnection.y();
                z = wirelessConnection.z();
                if (!wirelessConnection.change()) {
                    tile.wirelessComponent.setUpdate(true);
                    BlockPos pos = new BlockPos(x, y, z);
                    tile.wirelessComponent.addConnect(pos);
                }
            }


        }

    }

    public boolean accepts(ItemStack itemStack, final int index) {
        if (this.type == 0) {
            return getType(itemStack) != null || IUItem.panel_list.containsKey(itemStack.getDescriptionId());
        } else {
            return itemStack.getItem() instanceof ItemBaseModules
                    || (itemStack.getItem() instanceof ItemAdditionModule && IUItem.module7.getMeta(itemStack) == 4)
                    || (itemStack.getItem() instanceof ItemAdditionModule && IUItem.module7.getMeta(itemStack) == 10)
                    || (itemStack.getItem() instanceof ItemAdditionModule && IUItem.module7.getMeta(itemStack) == 1)
                    || (itemStack.getItem() instanceof ItemAdditionModule && IUItem.module7.getMeta(itemStack) == 2)
                    || (itemStack.getItem() instanceof ItemModuleType)
                    ;
        }
    }


    public int solartype() {
        TileSintezator tile = (TileSintezator) base;

        List<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < tile.inputslotA.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemModuleType) {

                list1.add(IUItem.module5.getMeta((ItemModuleType) get(i).getItem()) + 1);
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
            if (!this.get(i).isEmpty() && this
                    .get(i)
                    .getItem() instanceof ItemBaseModules && EnumModule.getFromID(IUItem.basemodules.getMeta(this.get(i))) != null) {
                EnumModule module = EnumModule.getFromID(IUItem.basemodules.getMeta(this.get(i)));
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
                int damage = IUItem.module7.getMeta(this.get(i));
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
