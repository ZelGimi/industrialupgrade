package com.denfop.invslot;

import cofh.redstoneflux.api.IEnergyContainerItem;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IAdvEnergySink;
import com.denfop.api.energy.IAdvEnergyTile;
import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.items.modules.ItemModuleType;
import com.denfop.items.modules.ItemModuleTypePanel;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.EnumType;
import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import com.denfop.tiles.panels.entity.WirelessTransfer;
import com.denfop.utils.ModUtils;
import ic2.api.energy.tile.IChargingSlot;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class InvSlotPanel extends InvSlot implements IChargingSlot {

    public final int tier;

    public InvSlotPanel(final TileEntitySolarPanel base, final int tier, final int slotNumbers, final InvSlot.Access access) {
        super(base, "charge", access, slotNumbers, InvSlot.InvSide.ANY);
        this.tier = tier;
        this.setStackSizeLimit(1);
    }


    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.checkmodule();
        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;
        tile.solarType = this.solartype();
    }

    public boolean accepts(final ItemStack stack, final int index) {
        return stack.getItem() instanceof ItemBaseModules
                || stack.getItem() instanceof ItemModuleTypePanel
                || stack.getItem() instanceof ItemAdditionModule
                || stack.getItem() instanceof IElectricItem
                || stack.getItem() instanceof IEnergyContainerItem
                || stack.getItem() instanceof ItemModuleType
                ;
    }


    public int solartype() {
        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;
        tile.level = 0;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemModuleType) {
                tile.level = get(i).getItemDamage() + 1;
            }
        }
        EnumType type = EnumType.getFromID(tile.level);

        return tile.setSolarType(type);
    }
    public int solartypeFast() {
        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;

        EnumType type = EnumType.getFromID(tile.level);

        return tile.setSolarType(type);
    }

    public void checkmodule() {
        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;

        double temp_day = tile.k;
        double temp_night = tile.m;
        double temp_storage = tile.p;
        double temp_producing = tile.u;
        tile.tier = (int) tile.o;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemAdditionModule) {
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
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemModuleTypePanel) {
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
            if (!this.get(i).isEmpty() && EnumModule.getFromID(this.get(i).getItemDamage()) != null && this
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
        tile.moonPhase = 1;
        tile.coef = 0;
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

        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && EnumModule.getFromID(this
                    .get(i).getItemDamage()) != null && this.get(i).getItem() instanceof ItemBaseModules) {
                EnumModule module = EnumModule.getFromID(this.get(i).getItemDamage());
                EnumBaseType type = module.type;
                if (type == EnumBaseType.PHASE) {
                    tile.coef = module.percent;
                    break;
                }

            }
        }
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && EnumModule.getFromID(this
                    .get(i).getItemDamage()) != null && this.get(i).getItem() instanceof ItemBaseModules) {
                EnumModule module = EnumModule.getFromID(this.get(i).getItemDamage());
                EnumBaseType type = module.type;
                if (type == EnumBaseType.MOON_LINSE) {
                    tile.moonPhase = module.percent;
                    break;
                }

            }
        }

    }

    public void charge() {
        double sentPacket;
        TileEntitySolarPanel tile = (TileEntitySolarPanel) base;
        for (int j = 0; j < this.size(); j++) {

            if (!this.get(j).isEmpty() && this.get(j).getItem() instanceof ic2.api.item.IElectricItem
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
                BlockPos pos = new BlockPos(x, y, z);
                if (tile.getWorld().getTileEntity(pos) != null
                        && tile.getWorld().getTileEntity(pos) instanceof TileEntityInventory && x != 0
                        && y != 0 && z != 0 && !nbttagcompound.getBoolean("change")) {
                    TileEntityInventory tile1 = (TileEntityInventory) tile.getWorld().getTileEntity(pos);
                    final IAdvEnergyTile energy = EnergyNetGlobal.instance.getTile(this.base
                            .getParent()
                            .getWorld(), pos);
                    if (energy instanceof IAdvEnergySink) {
                        tile.wirelessTransferList.add(new WirelessTransfer(tile1, (IAdvEnergySink) energy));
                    }

                }
            }


        }

    }

}
