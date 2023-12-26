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

public class ItemPlate extends ItemSubTypes<ItemPlate.ItemPlateTypes> implements IModelRegister {

    protected static final String NAME = "itemplates";

    public ItemPlate() {
        super(ItemPlateTypes.class);
        this.setCreativeTab(IUCore.RecourseTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item stack, final int meta, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":itemplates/" + ItemPlateTypes.getFromID(meta).getName(), null)
        );
    }

    public enum ItemPlateTypes implements ISubEnum {
        mikhail_plate(0),
        aluminium_plate(1),
        vanady_plate(2),
        wolfram_plate(3),
        invar_plate(4),
        caravky_plate(5),
        cobalt_plate(6),
        magnesium_plate(7),
        nickel_plate(8),
        platium_plate(9),
        titanium_plate(10),
        chromium_plate(11),
        spinel_plate(12),
        electrium_plate(13),
        silver_plate(14),
        zinc_plate(15),
        manganese_plate(16),
        iridium_plate(17),
        germanium_plate(18),
        bronze_plate(19),
        copper_plate(20),
        gold_plate(21),
        iron_plate(22),
        lapis_plate(23),
        lead_plate(24),
        obsidian_plate(25),
        steel_plate(26),
        tin_plate(27),

        osmium(28),
        tantalum(29),
        cadmium(30)
        ;

        private final String name;
        private final int ID;

        ItemPlateTypes(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static ItemPlateTypes getFromID(final int ID) {
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
