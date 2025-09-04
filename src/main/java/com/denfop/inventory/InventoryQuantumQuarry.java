package com.denfop.inventory;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.mechanism.quarry.BlockEntityBaseQuantumQuarry;
import com.denfop.items.ItemCraftingElements;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class InventoryQuantumQuarry extends Inventory implements ITypeSlot {

    public final int type;
    public final BlockEntityBaseQuantumQuarry tile;
    public int stackSizeLimit;

    public InventoryQuantumQuarry(BlockEntityBaseQuantumQuarry base1, int oldStartIndex1, int type) {
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
                this.tile.polisher = false;
                this.tile.separator = false;
                this.tile.col = 1;
                this.tile.chance = 0;
                this.tile.furnace = false;
                this.tile.main_list = new ArrayList<>(IUCore.list_quarry);
                if (!this.isEmpty()) {
                    ItemStack type1 = this.get(0);
                    EnumQuarryModules module = EnumQuarryModules.getFromID(IUItem.module9.getMeta(this.get(0)));
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
                        case POLISHER:
                            this.tile.polisher = true;
                            this.tile.main_list = new ArrayList<>(IUCore.get_polisher_quarry);
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
                    this.tile.polisher = false;
                    this.tile.separator = false;
                    this.tile.main_list = new ArrayList<>(IUCore.list_quarry);
                    this.tile.main_list.removeIf(stack -> this.tile.list(this.tile.list_modules, stack));
                    this.tile.original = true;
                }
                break;
            case 1:
                if (this.get(0).isEmpty()) {
                    this.tile.list_modules = null;
                } else {
                    this.tile.list_modules = EnumQuarryModules.getFromID(IUItem.module9.getMeta(this.get(0)));
                }
                this.tile.list = ModUtils.getQuarryListFromModule(this.get(0));
                if (this.tile.furnace) {
                    this.tile.main_list = new ArrayList<>(IUCore.get_ingot_quarry);
                } else if (this.tile.comb_mac_enabled) {
                    this.tile.main_list = new ArrayList<>(IUCore.get_comb_crushed_quarry);

                } else if (this.tile.mac_enabled) {
                    this.tile.main_list = new ArrayList<>(IUCore.get_crushed_quarry);

                } else if (this.tile.polisher) {
                    this.tile.main_list = new ArrayList<>(IUCore.get_polisher_quarry);

                } else if (this.tile.separator) {
                    this.tile.main_list = new ArrayList<>(IUCore.get_separator_quarry);

                } else {
                    this.tile.main_list = new ArrayList<>(IUCore.list_quarry);
                }
                this.tile.main_list.removeIf(stack -> this.tile.list(this.tile.list_modules, stack));
                this.tile.inputslot.update();
                break;
            case 2:
                this.tile.analyzer = !this.get(0).isEmpty();
                new PacketUpdateFieldTile(this.tile, "analyzer", tile.analyzer);
                break;
            case 3:
                this.tile.plasma = !this.get(0).isEmpty();
                break;
        }
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        switch (this.type) {
            case 0:
                this.tile.comb_mac_enabled = false;
                this.tile.mac_enabled = false;
                this.tile.polisher = false;
                this.tile.separator = false;
                this.tile.col = 1;
                this.tile.chance = 0;
                this.tile.furnace = false;
                this.tile.main_list = new ArrayList<>(IUCore.list_quarry);
                if (!this.isEmpty()) {
                    ItemStack type1 = this.get(0);
                    EnumQuarryModules module = EnumQuarryModules.getFromID(IUItem.module9.getMeta(this.get(0)));
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
                        case POLISHER:
                            this.tile.polisher = true;
                            this.tile.main_list = new ArrayList<>(IUCore.get_polisher_quarry);
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
                    this.tile.polisher = false;
                    this.tile.separator = false;
                    this.tile.main_list = new ArrayList<>(IUCore.list_quarry);
                    this.tile.main_list.removeIf(stack -> this.tile.list(this.tile.list_modules, stack));
                    this.tile.original = true;
                }
                break;
            case 1:
                if (this.get(0).isEmpty()) {
                    this.tile.list_modules = null;
                } else {
                    this.tile.list_modules = EnumQuarryModules.getFromID(IUItem.module9.getMeta(this.get(0)));
                }
                this.tile.list = ModUtils.getQuarryListFromModule(this.get(0));
                this.tile.main_list.removeIf(stack -> this.tile.list(this.tile.list_modules, stack));
                this.tile.inputslot.update();
                break;
            case 2:
                this.tile.analyzer = !this.get(0).isEmpty();
                new PacketUpdateFieldTile(this.tile, "analyzer", tile.analyzer);
                break;
            case 3:
                this.tile.plasma = !this.get(0).isEmpty();
                break;
        }
        return content;
    }

    public boolean canPlaceItem(final int index, ItemStack itemStack) {
        if (type == 0) {

            return itemStack.getItem() instanceof ItemQuarryModule && (EnumQuarryModules.getFromID(IUItem.module9.getMeta(itemStack)).type != EnumQuarryType.WHITELIST && EnumQuarryModules.getFromID(
                    IUItem.module9.getMeta(itemStack)).type != EnumQuarryType.BLACKLIST);
        } else if (type == 1) {
            if (itemStack.getItem() instanceof ItemQuarryModule && (EnumQuarryModules.getFromID(IUItem.module9.getMeta(itemStack)).type == EnumQuarryType.WHITELIST || EnumQuarryModules.getFromID(
                    IUItem.module9.getMeta(itemStack)).type == EnumQuarryType.BLACKLIST)) {
                ((BlockEntityBaseQuantumQuarry) this.base).list = ModUtils.getQuarryListFromModule(itemStack);
                return !itemStack.getItem().equals(IUItem.analyzermodule.getItem());
            }
            return false;
        } else if (type == 3) {
            return itemStack.getItem() instanceof ItemCraftingElements && IUItem.crafting_elements.getMeta(itemStack) == 646;

        }
        return itemStack.getItem().equals(IUItem.analyzermodule.getItem());
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
            case 3:
                return EnumTypeSlot.PLASM;
        }

        return null;
    }

}
