package com.denfop.items.modules;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemSpawnerModules<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemSpawnerModules(T element) {
        super(new Item.Properties().tab(IUCore.ModuleTab), element);
    }

    public enum Types implements ISubEnum {
        spawner_module(0),
        spawner_module1(1),
        spawner_module2(2),
        spawner_module3(3),
        spawner_module4(4),
        spawner_module5(5),
        spawner_module6(6),
        spawner_module7(7),
        spawner_module8(8),
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
            return "spawnermodules";
        }

        public int getId() {
            return this.ID;
        }
    }
}
