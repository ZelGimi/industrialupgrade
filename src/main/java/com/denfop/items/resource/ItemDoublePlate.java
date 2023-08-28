package com.denfop.items.resource;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class ItemDoublePlate extends ItemSubTypes<ItemDoublePlate.ItemDoublePlateTypes> implements IModelRegister {

    protected static final String NAME = "itemdoubleplates";

    public ItemDoublePlate() {
        super(null, ItemDoublePlateTypes.class);
        this.setCreativeTab(IUCore.RecourseTab);
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
                new ModelResourceLocation(Constants.MOD_ID + ":itemdoubleplates/" + ItemDoublePlateTypes
                        .getFromID(meta)
                        .getName(), null)
        );
    }

    public enum ItemDoublePlateTypes implements ISubEnum {
        mikhail_doubleplate(0),
        aluminium_doubleplate(1),
        vanady_doubleplate(2),
        wolfram_doubleplate(3),
        invar_doubleplate(4),
        caravky_doubleplate(5),
        cobalt_doubleplate(6),
        magnesium_doubleplate(7),
        nickel_doubleplate(8),
        platium_doubleplate(9),
        titanium_doubleplate(10),
        chromium_doubleplate(11),
        spinel_doubleplate(12),
        electrium_doubleplate(13),
        silver_doubleplate(14),
        zinc_doubleplate(15),
        manganese_doubleplate(16),
        iridium_doubleplate(17),
        germanium_doubleplate(18),
        bronze_doubleplate(19),
        copper_doubleplate(20),
        gold_doubleplate(21),
        iron_doubleplate(22),
        lapis_doubleplate(23),
        lead_doubleplate(24),
        obsidian_doubleplate(25),
        steel_doubleplate(26),
        tin_doubleplate(27);

        private final String name;
        private final int ID;

        ItemDoublePlateTypes(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static ItemDoublePlateTypes getFromID(final int ID) {
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
