package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemCoreWind<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemCoreWind(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    public enum Types implements ISubEnum {
        core_wind(0),
        core_wind1(1),
        core_wind2(2),
        core_wind3(3),
        core_wind4(4),
        core_wind5(5),
        core_wind6(6),
        core_wind7(7),
        core_wind8(8),
        core_wind9(9),
        core_wind10(10),
        core_wind11(11),
        core_wind12(12),
        core_wind13(13),
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
            return "corewind";
        }

        public int getId() {
            return this.ID;
        }
    }
}
