package com.simplequarries;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.invslot.InvSlot;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;

public class InvSlotBaseQuarry extends InvSlot implements ITypeSlot {


    public final TileBaseQuarry tile;
    public int stackSizeLimit;

    public InvSlotBaseQuarry(TileBaseQuarry base1, int oldStartIndex1) {
        super(base1, TypeItemSlot.INPUT, oldStartIndex1);
        this.tile = base1;
        this.stackSizeLimit = 1;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.QUARRY1;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.tile.energyconsume = this.tile.constenergyconsume;
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
                            tile.col = module.efficiency;
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
                        this.tile.list = ModUtils.getListFromModule(type1);
                        tile.list_modules = module;
                        break;

                }
            }
        }
        tile.chunkx1 = tile.chunkx;
        tile.chunkz1 = tile.chunkz;
        tile.chunkx2 = tile.chunkx + 15;
        tile.chunkz2 = tile.chunkz + 15;
        if (tile.col != 1) {
            tile.chunkx1 = tile.chunkx - 16 * (tile.col - 1);
            tile.chunkz1 = tile.chunkz - 16 * (tile.col - 1);
        }
    }

    public void update() {
        this.tile.comb_mac_enabled = false;
        this.tile.energyconsume = this.tile.constenergyconsume;
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
                            tile.col = module.efficiency;
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
                        this.tile.list = ModUtils.getListFromModule(type1);
                        break;

                }
            }
        }
        tile.chunkx1 = tile.chunkx;
        tile.chunkz1 = tile.chunkz;
        tile.chunkx2 = tile.chunkx + 15;
        tile.chunkz2 = tile.chunkz + 15;
        if (tile.col != 1) {
            tile.chunkx1 = tile.chunkx - 16 * (tile.col - 1);
            tile.chunkz1 = tile.chunkz - 16 * (tile.col - 1);
        }
    }

    public boolean accepts(ItemStack itemStack, final int index) {


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
