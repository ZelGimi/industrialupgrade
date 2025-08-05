package com.denfop.items.space;

import com.denfop.IUCore;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemResearchLens<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemResearchLens(T element) {
        super(new Item.Properties(), element);
    }

    public static EnumTypeUpgrade getType(int meta) {
        return EnumTypeUpgrade.getFromID(meta);

    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    public enum Types implements ISubEnum {
        lens_1(0),
        lens_2(1),
        lens_3(2),
        lens_4(3),
        lens_5(4),
        lens_6(5),
        lens_7(6);

        private final String name;
        private final int ID;

        Types(int id) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = id;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getMainPath() {
            return "research_lens";
        }

        public int getId() {
            return this.ID;
        }
    }

}
