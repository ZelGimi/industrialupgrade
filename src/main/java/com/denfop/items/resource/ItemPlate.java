package com.denfop.items.resource;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class ItemPlate extends ItemMulti<ItemPlate.ItemPlateTypes> implements IModelRegister {

    protected static final String NAME = "itemplates";

    public ItemPlate() {
        super(null, ItemPlateTypes.class);
        this.setCreativeTab(IUCore.RecourseTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":itemplates/" + ItemPlateTypes.getFromID(meta).getName(), null)
        );
    }

    public enum ItemPlateTypes implements IIdProvider {
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
