package com.denfop.invslot;

import cofh.redstoneflux.api.IEnergyContainerItem;
import com.denfop.ElectricItem;
import com.denfop.api.item.IEnergyItem;
import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.items.modules.ItemModuleType;
import com.denfop.items.modules.ItemModuleTypePanel;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.EnumType;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class InvSlotPanel extends InvSlot {

    public final int tier;

    public InvSlotPanel(final TileSolarPanel base, final int tier, final int slotNumbers, final TypeItemSlot typeItemSlot) {
        super(base, typeItemSlot, slotNumbers);
        this.tier = tier;
        this.setStackSizeLimit(1);
    }


    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.checkmodule();
        TileSolarPanel tile = (TileSolarPanel) base;
        tile.solarType = this.solartype();
    }

    public boolean accepts(final ItemStack stack, final int index) {
        return stack.getItem() instanceof ItemBaseModules
                || stack.getItem() instanceof ItemModuleTypePanel
                || stack.getItem() instanceof ItemAdditionModule
                || stack.getItem() instanceof IEnergyItem
                || stack.getItem() instanceof IEnergyContainerItem
                || stack.getItem() instanceof ItemModuleType
                ;
    }


    public int solartype() {
        TileSolarPanel tile = (TileSolarPanel) base;
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
        TileSolarPanel tile = (TileSolarPanel) base;

        EnumType type = EnumType.getFromID(tile.level);

        return tile.setSolarType(type);
    }

    public void checkmodule() {
        TileSolarPanel tile = (TileSolarPanel) base;
        tile.charge = false;
        double temp_day = tile.defaultDay;
        double temp_night = tile.defaultNight;
        double temp_storage = tile.defaultMaxStorage;
        double temp_producing = tile.defaultOutoput;
        tile.tier = (int) tile.defaultTier;
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
        if (tile.tier < 1)
            tile.tier = 1;
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
                        temp_day += tile.defaultDay * percent;
                        break;
                    case NIGHT:
                        temp_night += tile.defaultNight * percent;
                        break;
                    case STORAGE:
                        temp_storage += tile.defaultMaxStorage * percent;
                        break;
                    case OUTPUT:
                        temp_producing += tile.defaultOutoput * percent;
                        break;
                }
            }
        }

        tile.genDay = temp_day;
        tile.genNight = temp_night;
        tile.maxStorage = temp_storage;
        tile.output = temp_producing;
        tile.moonPhase = 1;
        tile.coef = 0;
        this.wirelessmodule();

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
        TileSolarPanel tile = (TileSolarPanel) base;
        for (int j = 0; j < this.size(); j++) {

            if (!this.get(j).isEmpty() && this.get(j).getItem() instanceof IEnergyItem
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


    public void wirelessmodule() {
        TileSolarPanel tile = (TileSolarPanel) base;
        tile.wirelessComponent.setUpdate(false);
        tile.wirelessComponent.removeAll();
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

}
