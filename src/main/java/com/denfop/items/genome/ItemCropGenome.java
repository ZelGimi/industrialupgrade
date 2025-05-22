package com.denfop.items.genome;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.agriculture.genetics.GeneticTraits;
import com.denfop.api.agriculture.genetics.IGenomeItem;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class ItemCropGenome extends ItemSubTypes<ItemCropGenome.Types> implements IModelRegister, IGenomeItem {

    protected static final String NAME = "crop_genome";

    public ItemCropGenome() {
        super(Types.class);
        this.setCreativeTab(IUCore.GenomeTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(
                        Constants.MOD_ID + ":" + NAME + "/" + "crop_genome" + Types.getFromID(meta).getName(),
                        null
                )
        );
    }

    @Override
    public GeneticTraits getGenomeTraits(final ItemStack stack) {
        return GeneticTraits.values()[stack.getItemDamage()];
    }

    public enum Types implements ISubEnum {
        soil_i(0),
        soil_ii(1),
        soil_iii(2),
        chance_i(3),
        chance_ii(4),
        chance_iii(5),
        beecombine(6),
        yield_i(7),
        yield_ii(8),
        yield_iii(9),
        weather_i(10),
        weather_ii(11),
        water(12),
        light_i(13),
        light_ii(14),
        light_iii(15),
        light_iv(16),
        biome_i(17),
        biome_ii(18),
        biome(19),
        biome_iii(20),
        biome_iv(21),
        grow_speed_i(22),
        grow_speed_ii(23),
        grow_speed_iii(24),
        pest_i(25),
        pest_ii(26),
        pest_iii(27),
        seed_i(28),
        seed_ii(29),
        seed_iii(30),
        weed_i(31),
        weed_ii(32),
        weed_iii(33),
        soil_block(34),
        sun(35),
        night_grow(36),
        air_i(37),
        air_ii(38),
        air_iii(39),
        radiation_i(40),
        radiation_ii(41),
        radiation_iii(42),
        genome_resistance_i(43),
        genome_resistance_ii(44),
        genome_resistance_iii(45),
        genome_adaptive_i(46),
        genome_adaptive_ii(47),
        genome_adaptive_iii(48),

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

        public int getId() {
            return this.ID;
        }
    }

}
