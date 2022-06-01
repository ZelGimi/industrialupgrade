package com.simplequarries;

import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.utils.ModUtils;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotBaseQuarry extends InvSlot {


    public final TileEntityBaseQuarry tile;
    public int stackSizeLimit;

    public InvSlotBaseQuarry(TileEntityBaseQuarry base1, int oldStartIndex1) {
        super(base1, "input", Access.I, oldStartIndex1, InvSlot.InvSide.TOP);
        this.tile = base1;
        this.stackSizeLimit = 1;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.tile.comb_mac_enabled = false;
        this.tile.mac_enabled = false;
        this.tile.col = 1;
        this.tile.chance = 0;
        this.tile.furnace = false;
        if (!this.isEmpty()) {
            for (int i = 0; i < this.size(); i++) {
                ItemStack type1 = this.get(i);
                if (type1.isEmpty()) {
                    continue;
                }
                EnumQuarryModules module = EnumQuarryModules.getFromID(type1.getItemDamage());
                EnumQuarryType type = module.type;

                switch (type) {
                    case SPEED:
                        tile.energyconsume += tile.consume * (module.meta * -0.03);
                        break;
                    case DEPTH:
                        if (tile.col == 1) {
                            tile.col = module.efficiency * module.efficiency;
                            tile.energyconsume += tile.consume * (module.cost);
                        }
                        break;
                    case LUCKY:
                        if (tile.chance == 0) {
                            tile.chance = module.efficiency;
                            tile.energyconsume += tile.consume * (module.cost);
                        }
                        break;
                    case FURNACE:
                        if (!tile.furnace) {
                            tile.furnace = true;
                            tile.energyconsume += tile.consume * (module.cost);
                        }
                        break;
                    case MACERATOR:
                        if (!tile.mac_enabled && !tile.comb_mac_enabled) {
                            tile.mac_enabled = true;
                            tile.energyconsume += tile.consume * (module.cost);
                        }
                        break;
                    case COMB_MAC:
                        if (!tile.mac_enabled && !tile.comb_mac_enabled) {
                            tile.comb_mac_enabled = true;
                            tile.energyconsume += tile.consume * (module.cost);
                        }
                        break;
                    case WHITELIST:
                    case BLACKLIST:
                        ((TileEntityBaseQuarry) this.base).list = ModUtils.getListFromModule(type1);
                        tile.list_modules = module;
                        break;

                }
            }
        }
    }

    public void update() {
        this.tile.comb_mac_enabled = false;
        this.tile.mac_enabled = false;
        this.tile.col = 1;
        this.tile.chance = 0;
        this.tile.furnace = false;
        if (!this.isEmpty()) {
            for (int i = 0; i < this.size(); i++) {
                ItemStack type1 = this.get(i);
                if (type1.isEmpty()) {
                    continue;
                }
                EnumQuarryModules module = EnumQuarryModules.getFromID(type1.getItemDamage());
                EnumQuarryType type = module.type;

                switch (type) {
                    case SPEED:
                        tile.energyconsume += tile.consume * (module.meta * -0.03);
                        break;
                    case DEPTH:
                        if (tile.col == 1) {
                            tile.col = module.efficiency * module.efficiency;
                            tile.energyconsume += tile.consume * (module.cost);
                        }
                        break;
                    case LUCKY:
                        if (tile.chance == 0) {
                            tile.chance = module.efficiency;
                            tile.energyconsume += tile.consume * (module.cost);
                        }
                        break;
                    case FURNACE:
                        if (!tile.furnace) {
                            tile.furnace = true;
                            tile.energyconsume += tile.consume * (module.cost);
                        }
                        break;
                    case MACERATOR:
                        if (!tile.mac_enabled && !tile.comb_mac_enabled) {
                            tile.mac_enabled = true;
                            tile.energyconsume += tile.consume * (module.cost);
                        }
                        break;
                    case COMB_MAC:
                        if (!tile.mac_enabled && !tile.comb_mac_enabled) {
                            tile.comb_mac_enabled = true;
                            tile.energyconsume += tile.consume * (module.cost);
                        }
                        break;
                    case WHITELIST:
                    case BLACKLIST:
                        tile.list_modules = module;
                        ((TileEntityBaseQuarry) this.base).list = ModUtils.getListFromModule(type1);
                        break;

                }
            }
        }
    }

    public boolean accepts(ItemStack itemStack) {


        if (itemStack.getItem() instanceof ItemQuarryModule) {
            if (EnumQuarryModules.getFromID(
                    itemStack.getItemDamage()).type == EnumQuarryType.DEPTH) {
                tile.blockpos = null;
            }
            return true;
        }
        return false;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
