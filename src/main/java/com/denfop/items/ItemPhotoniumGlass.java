package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemPhotoniumGlass<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemPhotoniumGlass(T element) {
        super(new Item.Properties(), element);
    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }
    public enum Types implements ISubEnum {
        photoniyglass1(0),
        photoniyglass2(1),
        photoniyglass3(2),
        photoniyglass4(3),
        photoniyglass5(4),
        photoniyglass6(5),
        photoniyglass7(6),
        photoniyglass8(7),
        photoniyglass9(8),
        photoniyglass10(9),
        photoniyglass11(10),
        photoniyglass12(11),
        photoniyglass13(12),
        photoniyglass14(13),
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
            return "photoniumglass";
        }

        public int getId() {
            return this.ID;
        }
    }
}
