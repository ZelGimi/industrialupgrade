package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.tiles.solidmatter.EnumSolidMatter;
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

public class ItemSolidMatter extends ItemMulti<ItemSolidMatter.Types> implements IModelRegister {

    protected static final String NAME = "solidmatter";

    public ItemSolidMatter() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ItemTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public static EnumSolidMatter getsolidmatter(int meta) {
        switch (meta) {
            case 0:
                return EnumSolidMatter.AER;
            case 1:
                return EnumSolidMatter.AQUA;
            case 2:
                return EnumSolidMatter.EARTH;
            case 3:
                return EnumSolidMatter.END;
            case 5:
                return EnumSolidMatter.NETHER;
            case 6:
                return EnumSolidMatter.NIGHT;
            case 7:
                return EnumSolidMatter.SUN;
            default:
                return EnumSolidMatter.MATTER;

        }

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
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements IIdProvider {
        matter(0),
        sun_matter(1),
        aqua_matter(2),
        nether_matter(3),
        night_matter(4),
        earth_matter(5),
        end_matter(6),
        aer_matter(7);

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
