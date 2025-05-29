package com.denfop.items.modules;

import com.denfop.IUCore;
import com.denfop.api.cool.EnumCoolUpgrade;
import com.denfop.api.cool.ICoolItem;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public class ItemCoolingUpgrade<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements ICoolItem {
    public ItemCoolingUpgrade(T element) {
        super(new Item.Properties(), element);
    }


    @Override
    public EnumCoolUpgrade getTypeUpgrade(ItemStack stack) {
        return switch (this.getElement().getId()) {
            default -> EnumCoolUpgrade.AZOTE;
            case 1 -> EnumCoolUpgrade.HYDROGEN;
            case 2 -> EnumCoolUpgrade.HELIUM;
        };
    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ModuleTab;
    }
    public enum Types implements ISubEnum {
        azote(0),
        hydrogen(1),
        helium(2),
        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "itemcoolupgrade";
        }

        public int getId() {
            return this.ID;
        }
    }
}
