package com.denfop.invslot;

import cofh.redstoneflux.api.IEnergyContainerItem;
import com.denfop.items.modules.AdditionModule;
import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.items.modules.ItemModuleTypePanel;
import com.denfop.items.modules.ModuleType;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.EnumType;
import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import com.denfop.utils.ModUtils;
import ic2.api.energy.tile.IChargingSlot;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class InvSlotPanel extends InvSlot implements IChargingSlot {

    public final int tier;

    public InvSlotPanel(final TileEntityInventory base, final int tier, final int slotNumbers, final InvSlot.Access access) {
        super(base, "charge", access, slotNumbers, InvSlot.InvSide.TOP);
        this.tier = tier;
        this.setStackSizeLimit(1);
    }

    public boolean accepts(final ItemStack stack) {
        return stack.getItem() instanceof ItemBaseModules
                || stack.getItem() instanceof ItemModuleTypePanel
                || stack.getItem() instanceof AdditionModule
                || stack.getItem() instanceof IElectricItem
                || stack.getItem() instanceof IEnergyContainerItem
                || stack.getItem() instanceof ModuleType
                ;
    }

    public void getrfmodule() {
        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != null && this.get(i).getItemDamage() == 4 && this.get(i).getItem() instanceof AdditionModule) {
                tile.getmodulerf = true;
                return;
            }
        }
        tile.getmodulerf = false;
    }

    public int solartype() {
        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;

        List<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != null && this.get(i).getItem() instanceof ModuleType) {
                list1.add(get(i).getItemDamage() + 1);
            } else {
                list1.add(0);
            }
        }
        EnumType type = EnumType.getFromID(ModUtils.slot(list1));

        return tile.setSolarType(type);
    }

    public void rfcharge() {

        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;
        for (int jj = 0; jj < this.size(); jj++) {
            if (this.get(jj) != null && this.get(jj).getItem() instanceof IEnergyContainerItem
                    && tile.storage2 > 0.0D) {
                IEnergyContainerItem item = (IEnergyContainerItem) this.get(jj).getItem();
                double sent = 0;
                double energy_temp = tile.storage2;
                if (item.getEnergyStored(this.get(jj)) < item.getMaxEnergyStored(this.get(jj))
                        && tile.storage2 > 0) {
                    sent = (sent + tile.extractEnergy1(
                            item.receiveEnergy(this.get(jj), (int) Math.min(tile.storage2, 2147000000), false), false));

                }
                energy_temp -= (sent * 2);
                tile.storage2 = energy_temp;

            }

        }
    }

    public void personality() {
        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;
        for (int m = 0; m < this.size(); m++) {
            if (this.get(m) != null && this.get(m).getItem() instanceof AdditionModule) {
                int kk = get(m).getItemDamage();
                if (kk == 0) {
                    tile.personality = true;
                    break;
                } else {
                    tile.personality = false;
                }
            } else {
                tile.personality = false;
            }
        }
    }

    public void time() {
        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != null && this.get(i).getItemDamage() == 9 && this.get(i).getItem() instanceof AdditionModule) {
                tile.time = 14400 * 2;
                tile.time1 = 14400 * 2;
                tile.time2 = 14400 * 2;
                tile.work = true;
                tile.work1 = true;
                tile.work2 = true;

                return;
            }
        }

        if (tile.time > 0) {
            tile.time--;
        }
        if (tile.time <= 0) {
            tile.work = false;
        }
        if (tile.time1 > 0 && !tile.work) {
            tile.time1--;
        }
        if (tile.time1 <= 0) {
            tile.work1 = false;
        }
        if (tile.time2 > 0 && !tile.work && !tile.work1) {
            tile.time2--;
        }
        if (tile.time2 <= 0) {
            tile.work2 = false;
        }
    }

    public void checkmodule() {
        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;

        double temp_day = tile.k;
        double temp_night = tile.m;
        double temp_storage = tile.p;
        double temp_producing = tile.u;
        tile.tier = (int) tile.o;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != null && this.get(i).getItem() instanceof AdditionModule) {
                int kk = get(i).getItemDamage();
                if (kk == 1) {
                    tile.tier++;
                } else if (kk == 2) {
                    tile.tier--;
                } else if (kk == 3) {
                    tile.charge = true;

                }
            }
        }
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != null && this.get(i).getItem() instanceof ItemModuleTypePanel) {
                int g = this.get(i).getItemDamage();

                if (tile.tier >= g + 1) {
                    EnumSolarPanels solar = ItemModuleTypePanel.getSolarType(g);
                    temp_day += solar.genday;
                    temp_night += solar.gennight;
                    temp_storage += solar.maxstorage;
                    temp_producing += solar.producing;

                }

            }
        }


        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != null && EnumModule.getFromID(this.get(i).getItemDamage()) != null && this
                    .get(i)
                    .getItem() instanceof ItemBaseModules) {
                EnumModule module = EnumModule.getFromID(this.get(i).getItemDamage());
                EnumBaseType type = module.type;
                double percent = module.percent;
                switch (type) {
                    case DAY:
                        temp_day += tile.k * percent;
                        break;
                    case NIGHT:
                        temp_night += tile.m * percent;
                        break;
                    case STORAGE:
                        temp_storage += tile.p * percent;
                        break;
                    case OUTPUT:
                        temp_producing += tile.u * percent;
                        break;
                }
            }
        }

        tile.genDay = temp_day;
        tile.genNight = temp_night;
        tile.maxStorage = temp_storage;
        tile.production = temp_producing;


    }

    public void charge() {
        double sentPacket;
        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;
        for (int j = 0; j < this.size(); j++) {

            if (this.get(j) != null && this.get(j).getItem() instanceof ic2.api.item.IElectricItem
                    && tile.storage > 0.0D) {
                sentPacket = ElectricItem.manager.charge(this.get(j), tile.storage, 2147483647, true,
                        false
                );
                if (sentPacket > 0.0D) {
                    tile.storage -= (int) sentPacket;
                }
            }
        }
    }

    @Override
    public double charge(final double v) {
        return 0;
    }

    public void wirelessmodule() {
        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;
        tile.wireless = 0;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != null && this.get(i).getItem() instanceof AdditionModule && this.get(i).getItemDamage() == 10) {

                tile.wireless = 1;
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

                    if (tile1.getComponent(Energy.class) != null) {
                        final Energy energy = tile1.getComponent(Energy.class);
                        tile.storage -= energy.addEnergy(tile.storage);
                    }

                }
            }


        }

    }

}
