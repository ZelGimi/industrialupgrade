package com.denfop.invslot;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.mechanism.quarry.TileBaseQuantumQuarry;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class InvSlotQuantumQuarry extends InvSlot implements ITypeSlot {

    public final int type;
    public final TileBaseQuantumQuarry tile;
    public int stackSizeLimit;

    public InvSlotQuantumQuarry(TileBaseQuantumQuarry base1, int oldStartIndex1, int type) {
        super(base1, TypeItemSlot.INPUT, oldStartIndex1);
        this.tile = base1;
        this.stackSizeLimit = 1;
        this.type = type;
    }

    public void update() {

        switch (this.type) {
            case 0:
                this.tile.comb_mac_enabled = false;
                this.tile.mac_enabled = false;
                this.tile.col = 1;
                this.tile.chance = 0;
                this.tile.furnace = false;
                this.tile.main_list = new ArrayList<>(IUCore.list_quarry);
                if (!this.isEmpty()) {
                    ItemStack type1 = this.get();
                    EnumQuarryModules module = EnumQuarryModules.getFromID(type1.getItemDamage());
                    EnumQuarryType type = module.type;

                    switch (type) {
                        case SPEED:
                            this.tile.original = true;
                            break;
                        case DEPTH:
                            this.tile.col = module.efficiency * module.efficiency;
                            this.tile.original = true;
                            break;
                        case LUCKY:
                            this.tile.chance = module.efficiency;
                            this.tile.original = true;
                            break;
                        case FURNACE:
                            this.tile.furnace = true;
                            this.tile.main_list = new ArrayList<>(IUCore.get_ingot_quarry);
                            this.tile.original = false;
                            break;
                        case COMB_MAC:
                            this.tile.comb_mac_enabled = true;
                            this.tile.main_list = new ArrayList<>(IUCore.get_comb_crushed_quarry);
                            this.tile.original = false;
                            break;
                        case MACERATOR:
                            this.tile.mac_enabled = true;
                            this.tile.main_list = new ArrayList<>(IUCore.get_crushed_quarry);
                            this.tile.original = false;
                            break;
                    }
                    this.tile.consume = this.tile.energyconsume * (1 + module.cost);
                    this.tile.main_list.removeIf(stack -> this.tile.list(this.tile.list_modules, stack));

                } else {

                    this.tile.consume = this.tile.energyconsume;
                    this.tile.col = 1;
                    this.tile.chance = 0;
                    this.tile.furnace = false;
                    this.tile.comb_mac_enabled = false;
                    this.tile.mac_enabled = false;
                    this.tile.main_list = new ArrayList<>(IUCore.list_quarry);
                    this.tile.main_list.removeIf(stack -> this.tile.list(this.tile.list_modules, stack));
                    this.tile.original = true;
                }
                break;
            case 1:
                if (this.get().isEmpty()) {
                    this.tile.list_modules = null;
                } else {
                    this.tile.list_modules = EnumQuarryModules.getFromID(this.get().getItemDamage());
                }
                this.tile.list = ModUtils.getQuarryListFromModule(this.get());
                if (this.tile.furnace) {
                    this.tile.main_list = new ArrayList<>(IUCore.get_ingot_quarry);
                } else if (this.tile.comb_mac_enabled) {
                    this.tile.main_list = new ArrayList<>(IUCore.get_comb_crushed_quarry);

                } else if (this.tile.mac_enabled) {
                    this.tile.main_list = new ArrayList<>(IUCore.get_crushed_quarry);

                } else {
                    this.tile.main_list = new ArrayList<>(IUCore.list_quarry);
                }
                this.tile.main_list.removeIf(stack -> this.tile.list(this.tile.list_modules, stack));
                this.tile.inputslot.update();
                break;
            case 2:
                this.tile.analyzer = !this.get().isEmpty();
                new PacketUpdateFieldTile(this.tile, "analyzer", tile.analyzer);
                break;
        }
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        switch (this.type) {
            case 0:
                this.tile.comb_mac_enabled = false;
                this.tile.mac_enabled = false;
                this.tile.col = 1;
                this.tile.chance = 0;
                this.tile.furnace = false;
                this.tile.main_list = new ArrayList<>(IUCore.list_quarry);
                if (!this.isEmpty()) {
                    ItemStack type1 = this.get();
                    EnumQuarryModules module = EnumQuarryModules.getFromID(type1.getItemDamage());
                    EnumQuarryType type = module.type;

                    switch (type) {
                        case SPEED:
                            this.tile.original = true;
                            break;
                        case DEPTH:
                            this.tile.col = module.efficiency * module.efficiency;
                            this.tile.original = true;
                            break;
                        case LUCKY:
                            this.tile.chance = module.efficiency;
                            this.tile.original = true;
                            break;
                        case FURNACE:
                            this.tile.furnace = true;
                            this.tile.main_list = new ArrayList<>(IUCore.get_ingot_quarry);
                            this.tile.original = false;
                            break;
                        case COMB_MAC:
                            this.tile.comb_mac_enabled = true;
                            this.tile.main_list = new ArrayList<>(IUCore.get_comb_crushed_quarry);
                            this.tile.original = false;
                            break;
                        case MACERATOR:
                            this.tile.mac_enabled = true;
                            this.tile.main_list = new ArrayList<>(IUCore.get_crushed_quarry);
                            this.tile.original = false;
                            break;
                    }
                    this.tile.consume = this.tile.energyconsume * (1 + module.cost);
                    this.tile.main_list.removeIf(stack -> this.tile.list(this.tile.list_modules, stack));

                } else {

                    this.tile.consume = this.tile.energyconsume;
                    this.tile.col = 1;
                    this.tile.chance = 0;
                    this.tile.furnace = false;
                    this.tile.comb_mac_enabled = false;
                    this.tile.mac_enabled = false;
                    this.tile.main_list = new ArrayList<>(IUCore.list_quarry);
                    this.tile.main_list.removeIf(stack -> this.tile.list(this.tile.list_modules, stack));
                    this.tile.original = true;
                }
                break;
            case 1:
                if (this.get().isEmpty()) {
                    this.tile.list_modules = null;
                } else {
                    this.tile.list_modules = EnumQuarryModules.getFromID(this.get().getItemDamage());
                }
                this.tile.list = ModUtils.getQuarryListFromModule(this.get());
                this.tile.main_list.removeIf(stack -> this.tile.list(this.tile.list_modules, stack));
                this.tile.inputslot.update();
                break;
            case 2:
                this.tile.analyzer = !this.get().isEmpty();
                new PacketUpdateFieldTile(this.tile, "analyzer", tile.analyzer);
                break;
        }
    }

    public boolean accepts(ItemStack itemStack, final int index) {
        if (type == 0) {

            return itemStack.getItem() instanceof ItemQuarryModule && (EnumQuarryModules.getFromID(itemStack.getItemDamage()).type != EnumQuarryType.WHITELIST && EnumQuarryModules.getFromID(
                    itemStack.getItemDamage()).type != EnumQuarryType.BLACKLIST);
        } else if (type == 1) {
            if (itemStack.getItem() instanceof ItemQuarryModule && (EnumQuarryModules.getFromID(itemStack.getItemDamage()).type == EnumQuarryType.WHITELIST || EnumQuarryModules.getFromID(
                    itemStack.getItemDamage()).type == EnumQuarryType.BLACKLIST)) {
                ((TileBaseQuantumQuarry) this.base).list = ModUtils.getQuarryListFromModule(itemStack);
                return !itemStack.getItem().equals(IUItem.analyzermodule);
            }

        }
        return itemStack.getItem().equals(IUItem.analyzermodule);
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    @Override
    public EnumTypeSlot getTypeSlot(int slotid) {
        switch (this.type) {
            case 0:
                return EnumTypeSlot.QUARRY1;
            case 1:
                return EnumTypeSlot.LIST;
            case 2:
                return EnumTypeSlot.QUARRY;
        }

        return null;
    }

}
