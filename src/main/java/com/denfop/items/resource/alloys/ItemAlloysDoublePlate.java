package com.denfop.items.resource.alloys;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class ItemAlloysDoublePlate extends ItemSubTypes<ItemAlloysDoublePlate.Types> implements IModelRegister {

    protected static final String NAME = "alloydoubleplate";

    public ItemAlloysDoublePlate() {
        super(Types.class);
        this.setCreativeTab(IUCore.RecourseTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("iu.alloy", "iu.alloys"));
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements ISubEnum {
        aluminum_bronze(0),
        alumel(1),
        red_brass(2),
        muntsa(3),
        nichrome(4),
        alcled(5),
        vanadoalumite(6),
        vitalium(7),
        duralumin(8),
        ferromanganese(9),
        aluminium_silicon(10),
        beryllium_bronze(11),
        zeliber(12),
        stainless_steel(13),
        inconel(14),
        nitenol(15),
        stellite(16),
        hafnium_boride_alloy(17),
        woods(18),
        nimonic(19),
        tantalum_tungsten_hafnium(20),
        permalloy(21),
        aluminium_lithium_alloy(22),
        cobalt_chrome(23),
        hafnium_carbide(24),
        molybdenum_steel(25),
        niobium_titanium(26),
        osmiridium(27),
        superalloy_haynes(28),
        superalloy_rene(29),
        yttrium_aluminium_garnet(30),
        gallium_arsenic(31),

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
