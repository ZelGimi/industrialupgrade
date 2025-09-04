package com.denfop.inventory;


import com.denfop.IUItem;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import com.denfop.blockentity.panels.entity.EnumSolarPanels;
import com.denfop.blockentity.panels.entity.EnumType;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.WirelessConnection;
import com.denfop.items.modules.*;
import com.denfop.utils.ElectricItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public class InventoryPanel extends Inventory {

    public final int tier;

    public InventoryPanel(final BlockEntitySolarPanel base, final int tier, final int slotNumbers, final TypeItemSlot typeItemSlot) {
        super(base, typeItemSlot, slotNumbers);
        this.tier = tier;
        this.setStackSizeLimit(1);
    }


    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        this.checkmodule();
        BlockEntitySolarPanel tile = (BlockEntitySolarPanel) base;
        tile.solarType = this.solartype();
        return content;
    }

    public boolean canPlaceItem(final int index, final ItemStack stack) {
        return stack.getItem() instanceof ItemBaseModules
                || stack.getItem() instanceof ItemModuleTypePanel
                || stack.getItem() instanceof ItemAdditionModule
                || stack.getItem() instanceof EnergyItem
                || stack.getItem() instanceof ItemModuleType
                ;
    }


    public int solartype() {
        BlockEntitySolarPanel tile = (BlockEntitySolarPanel) base;
        tile.levelPanel = 0;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemModuleType) {
                tile.levelPanel = IUItem.module5.getMeta((ItemModuleType) get(i).getItem()) + 1;
            }
        }
        EnumType type = EnumType.getFromID(tile.levelPanel);

        return tile.setSolarType(type);
    }

    public int solartypeFast() {
        BlockEntitySolarPanel tile = (BlockEntitySolarPanel) base;

        EnumType type = EnumType.getFromID(tile.levelPanel);

        return tile.setSolarType(type);
    }

    public void checkmodule() {
        BlockEntitySolarPanel tile = (BlockEntitySolarPanel) base;
        tile.charge = false;
        double temp_day = tile.defaultDay;
        double temp_night = tile.defaultNight;
        double temp_storage = tile.defaultMaxStorage;
        double temp_producing = tile.defaultOutoput;
        tile.tier = (int) tile.defaultTier;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemAdditionModule) {
                int kk = IUItem.module7.getMeta((ItemAdditionModule) this.get(i).getItem());
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
                int g = IUItem.module6.getMeta((ItemModuleTypePanel) this.get(i).getItem());

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
            if (!this.get(i).isEmpty() && this
                    .get(i)
                    .getItem() instanceof ItemBaseModules && EnumModule.getFromID(IUItem.basemodules.getMeta((ItemBaseModules) this.get(i).getItem())) != null) {
                EnumModule module = EnumModule.getFromID(IUItem.basemodules.getMeta((ItemBaseModules) this.get(i).getItem()));
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
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemBaseModules && EnumModule.getFromID(IUItem.basemodules.getMeta((ItemBaseModules) this.get(i).getItem())) != null) {
                EnumModule module = EnumModule.getFromID(IUItem.basemodules.getMeta((ItemBaseModules) this.get(i).getItem()));
                EnumBaseType type = module.type;
                if (type == EnumBaseType.PHASE) {
                    tile.coef = module.percent;
                    break;
                }

            }
        }
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemBaseModules && EnumModule.getFromID(IUItem.basemodules.getMeta((ItemBaseModules) this.get(i).getItem())) != null) {
                EnumModule module = EnumModule.getFromID(IUItem.basemodules.getMeta((ItemBaseModules) this.get(i).getItem()));
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
        BlockEntitySolarPanel tile = (BlockEntitySolarPanel) base;
        for (int j = 0; j < this.size(); j++) {

            if (!this.get(j).isEmpty() && this.get(j).getItem() instanceof EnergyItem
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
        BlockEntitySolarPanel tile = (BlockEntitySolarPanel) base;
        tile.wirelessComponent.setUpdate(false);
        tile.wirelessComponent.removeAll();
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty() && this.get(i).getItem() instanceof ItemAdditionModule && IUItem.module7.getMeta((ItemAdditionModule) this.get(i).getItem()) == 10) {
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

}
