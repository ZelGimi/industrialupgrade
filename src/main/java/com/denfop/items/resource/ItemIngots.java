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

public class ItemIngots extends ItemMulti<ItemIngots.ItemIngotsTypes> implements IModelRegister {

    protected static final String NAME = "itemingots";

    public ItemIngots() {
        super(null, ItemIngotsTypes.class);
        this.setCreativeTab(IUCore.ItemTab);
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
                new ModelResourceLocation(Constants.MOD_ID + ":itemingots/" + ItemIngotsTypes.getFromID(meta).getName(), null)
        );
    }

    public enum ItemIngotsTypes implements IIdProvider {
        mikhail_ingot(0),
        aluminium_ingot(1),
        vanady_ingot(2),
        wolfram_ingot(3),
        invar_ingot(4),
        caravky_ingot(5),
        cobalt_ingot(6),
        magnesium_ingot(7),
        nickel_ingot(8),
        platium_ingot(9),
        titanium_ingot(10),
        chromium_ingot(11),
        spinel_ingot(12),
        electrium_ingot(13),
        silver_ingot(14),
        zinc_ingot(15),
        manganese_ingot(16),
        iridium_ingot(17),
        germanium_ingot(18),
        ;

        private final String name;
        private final int ID;

        ItemIngotsTypes(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static ItemIngotsTypes getFromID(final int ID) {
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
