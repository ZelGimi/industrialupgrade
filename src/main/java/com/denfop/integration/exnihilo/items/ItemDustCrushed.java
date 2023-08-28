package com.denfop.integration.exnihilo.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
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

public class ItemDustCrushed extends ItemSubTypes<ItemDustCrushed.Types> implements IModelRegister {

    protected static final String NAME = "dustcrushed";

    public ItemDustCrushed() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.RecourseTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        if (meta >= (Types.values()).length) {
            meta = 0;
        }
        return Types.values()[meta].getName() + "_dustcrushed" + ".name";
    }

    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {

            for (final ItemDustCrushed.Types type : this.typeProperty.getAllowedValues()) {
                if (type != Types.nickel && type != Types.silver && type != Types.platium) {
                    subItems.add(this.getItemStackUnchecked(type));
                }
            }

        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, final int meta, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(
                        Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName() + "_dustcrushed",
                        null
                )
        );
    }

    public enum Types implements ISubEnum {
        mikhail(0),
        aluminium(1),
        vanady(2),
        wolfram(3),
        cobalt(4),
        magnesium(5),
        nickel(6),
        platium(7),
        titanium(8),
        chromium(9),
        spinel(10),
        silver(11),
        zinc(12),
        manganese(13),

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
