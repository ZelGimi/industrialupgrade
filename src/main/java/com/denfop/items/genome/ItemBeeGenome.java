package com.denfop.items.genome;

import com.denfop.IUCore;
import com.denfop.api.bee.genetics.GeneticTraits;
import com.denfop.api.bee.genetics.IGenomeItem;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public class ItemBeeGenome<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IGenomeItem {
    public ItemBeeGenome(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public GeneticTraits getGenomeTraits(final ItemStack stack) {
        return GeneticTraits.values()[getElement().getId()];
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.GenomeTab;
    }


    public enum Types implements ISubEnum {
        soil_i(0),
        soil_ii(1),
        soil_iii(2),
        food_i(3),
        food_ii(4),
        food_iii(5),
        biome(6),
        jelly_i(7),
        jelly_ii(8),
        jelly_iii(9),
        weather_i(10),
        weather_ii(11),
        product_i(12),
        product_ii(13),
        product_iii(14),
        birth_i(15),
        birth_ii(16),
        birth_iii(17),
        hardening_i(18),
        hardening_ii(19),
        hardening_iii(20),
        population_i(21),
        population_ii(22),
        population_iii(23),
        swarm_i(24),
        swarm_ii(25),
        swarm_iii(26),
        mortality_i(27),
        mortality_ii(28),
        mortality_iii(29),
        pest_i(30),
        pest_ii(31),
        pest_iii(32),
        radius_i(33),
        radius_ii(34),
        radius_iii(35),
        sun(36),
        night(37),
        air_i(38),
        air_ii(39),
        air_iii(40),
        radiation_i(41),
        radiation_ii(42),
        radiation_iii(43),
        genome_resistance_i(44),
        genome_resistance_ii(45),
        genome_resistance_iii(46),
        genome_adaptive_i(47),
        genome_adaptive_ii(48),
        genome_adaptive_iii(49),
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
            return "bee_genome_" + name;
        }

        @Override
        public String getMainPath() {
            return "bee_genome";
        }

        public int getId() {
            return this.ID;
        }
    }

}
