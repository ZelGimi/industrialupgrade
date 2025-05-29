package com.denfop.invslot;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.tiles.base.TileEntityAutoDigger;
import net.minecraft.world.item.ItemStack;

public class InvSlotDigger extends InvSlot implements ITypeSlot {

    public final TileEntityAutoDigger tile;
    public int stackSizeLimit;

    public InvSlotDigger(TileEntityAutoDigger base1) {
        super(base1, TypeItemSlot.INPUT, 3);
        this.tile = base1;
        this.stackSizeLimit = 1;
    }

    public void update() {


        this.tile.consume = this.tile.energyconsume;
        this.tile.col = 1;
        this.tile.chance = 0;
        this.tile.furnace = false;
        this.tile.comb_mac_enabled = false;
        this.tile.mac_enabled = false;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                ItemStack type1 = this.get(i);
                EnumQuarryModules module = EnumQuarryModules.getFromID(((ItemQuarryModule<?>) type1.getItem()).getElement().getId());
                EnumQuarryType type = module.type;

                switch (type) {
                    case SPEED:
                        break;
                    case DEPTH:
                        this.tile.col = module.efficiency * module.efficiency;
                        break;
                    case LUCKY:
                        this.tile.chance = module.efficiency;
                        break;
                    case FURNACE:
                        this.tile.furnace = true;

                        break;
                    case COMB_MAC:
                        this.tile.comb_mac_enabled = true;

                        break;
                    case MACERATOR:
                        this.tile.mac_enabled = true;
                        break;
                }
                this.tile.consume += this.tile.energyconsume * (module.cost);

            }
        }


    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        this.tile.consume = this.tile.energyconsume;
        this.tile.col = 1;
        this.tile.chance = 0;
        this.tile.furnace = false;
        this.tile.comb_mac_enabled = false;
        this.tile.mac_enabled = false;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                ItemStack type1 = this.get(i);
                EnumQuarryModules module = EnumQuarryModules.getFromID(((ItemQuarryModule<?>) type1.getItem()).getElement().getId());
                EnumQuarryType type = module.type;

                switch (type) {
                    case SPEED:
                        break;
                    case DEPTH:
                        this.tile.col = module.efficiency * module.efficiency;
                        break;
                    case LUCKY:
                        this.tile.chance = module.efficiency;
                        break;
                    case FURNACE:
                        this.tile.furnace = true;

                        break;
                    case COMB_MAC:
                        this.tile.comb_mac_enabled = true;

                        break;
                    case MACERATOR:
                        this.tile.mac_enabled = true;
                        break;
                }
                this.tile.consume += this.tile.energyconsume * (module.cost);
            }
        }


        this.tile.inputslot.update();
        return content;
    }

    public boolean accepts(ItemStack itemStack, final int index) {


        return itemStack.getItem() instanceof ItemQuarryModule && (EnumQuarryModules.getFromID(((ItemQuarryModule<?>) itemStack.getItem()).getElement().getId()).type != EnumQuarryType.WHITELIST && EnumQuarryModules.getFromID(
                ((ItemQuarryModule<?>) itemStack.getItem()).getElement().getId()).type != EnumQuarryType.BLACKLIST);

    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    @Override
    public EnumTypeSlot getTypeSlot(int slotid) {


        return EnumTypeSlot.QUARRY1;

    }

}
