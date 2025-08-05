package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemExcitedNucleus<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemExcitedNucleus(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    public enum Types implements ISubEnum {
        advcore(0),
        hybcore(1),
        ultcore(2),
        quacore(3),
        specore(4),
        procore(5),
        sincore(6),
        admcore(7),
        phocore(8),
        neucore(9),
        barcore(10),
        adrcore(11),
        gracore(12),
        kvrcore(13),
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
            return "excitednucleus";
        }

        public int getId() {
            return this.ID;
        }
    }
}
