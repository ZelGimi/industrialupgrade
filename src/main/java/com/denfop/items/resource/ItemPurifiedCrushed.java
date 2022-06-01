package com.denfop.items.resource;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemPurifiedCrushed extends ItemMulti<ItemPurifiedCrushed.Types> implements IModelRegister {

    protected static final String NAME = "purifiedcrushed";

    public ItemPurifiedCrushed() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.RecourseTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {

            for (final Types type : this.typeProperty.getShownValues()) {
                if (type != Types.invar && type != Types.electrium && type != Types.caravky) {
                    subItems.add(this.getItemStackUnchecked(type));
                }
            }

        }
    }

    protected ItemStack getItemStackUnchecked(Types type) {
        return new ItemStack(this, 1, ((IIdProvider) type).getId());
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
        mikhail(0),
        aluminium(1),
        vanady(2),
        wolfram(3),
        invar(4),
        caravky(5),
        cobalt(6),
        magnesium(7),
        nickel(8),
        platium(9),
        titanium(10),
        chromium(11),
        spinel(12),
        electrium(13),
        silver(14),
        zinc(15),
        manganese(16),
        iridium(17),
        germanium(18),
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
