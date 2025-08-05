package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.datacomponent.DataComponentsInit;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Locale;

public class ItemBaseCircuit<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IProperties {
    public ItemBaseCircuit(T element) {
        super(new Item.Properties(), element);
        if (properties().length > 0)
            IUCore.proxy.addProperties(this);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    @Override
    public String[] properties() {
        if (getElement().getId() != 9 && getElement().getId() != 10 && getElement().getId() != 11 && getElement().getId() != 21)
            return new String[]{};
        return new String[]{"level"};
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack stack, ClientLevel world, LivingEntity entity, int p174679, String property) {
        int level = stack.getOrDefault(DataComponentsInit.LEVEL_MICROCHIP, 0);
        if (level == 0)
            return 0;
        level = switch (this.getElement().getId()) {
            case 9 -> level - 5;
            case 10 -> level - 7;
            case 11 -> level - 9;
            case 21 -> level - 11;
            default -> level;
        };
        return level;
    }

    public enum Types implements ISubEnum {
        nanocircuit_part1(0),
        quantumcircuit_part1(1),
        spectralcircuit_part1(2),
        nanocircuit_part2(3),
        quantumcircuit_part2(4),
        spectralcircuit_part2(5),
        nanocircuit_part3(6),
        quantumcircuit_part3(7),
        spectralcircuit_part3(8),
        nanocircuit(9),
        quantumcircuit(10),
        spectralcircuit(11),
        advanced_part1(12),
        advanced_part2(13),
        advanced_part3(14),
        circuit_part1(15),
        circuit_part2(16),
        circuit_part3(17),
        photon_part1(18),
        photon_part2(19),
        photon_part3(20),
        photoncircuit(21),
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


        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getMainPath() {
            return "circuit";
        }

        public int getId() {
            return this.ID;
        }
    }
}
