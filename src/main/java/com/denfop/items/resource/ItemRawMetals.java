package com.denfop.items.resource;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
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

public class ItemRawMetals extends ItemSubTypes<ItemRawMetals.Types> implements IModelRegister {

    protected static final String NAME = "raw_metals";

    public ItemRawMetals() {
        super(Types.class);
        this.setCreativeTab(IUCore.RecourseTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {

            for (final Types type : this.typeProperty.getAllowedValues()) {
                if (type != Types.uranium) {
                    subItems.add(this.getItemStackUnchecked(type));
                }
            }

        }
    }

    protected ItemStack getItemStackUnchecked(Types type) {
        return new ItemStack(this, 1, ((ISubEnum) type).getId());
    }


    @SideOnly(Side.CLIENT)
    public void registerModel(Item stack, final int meta, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(
                        Constants.MOD_ID + ":" + NAME + "/" + "raw_" + Types.getFromID(meta).getName(),
                        null
                )
        );
    }

    public enum Types implements ISubEnum {
        mikhail(0),
        aluminium(1),
        vanadium(2),
        tungsten(3),
        cobalt(4),
        magnesium(5),
        nickel(6),
        platinum(7),
        titanium(8),
        chromium(9),
        spinel(10),
        silver(11),
        zinc(12),
        manganese(13),
        iridium(14),
        germanium(15),
        copper(16),
        gold(17),
        iron(18),
        lead(19),
        tin(20),
        uranium(21),
        osmium(22),
        tantalum(23),
        cadmium(24),
        arsenic(25),
        barium(26),
        bismuth(27),
        gadolinium(28),
        gallium(29),
        hafnium(30),
        yttrium(31),
        molybdenum(32),
        neodymium(33),
        niobium(34),
        palladium(35),
        polonium(36),
        strontium(37),
        thallium(38),
        zirconium(39),
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
