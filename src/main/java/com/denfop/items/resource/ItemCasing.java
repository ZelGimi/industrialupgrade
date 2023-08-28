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

public class ItemCasing extends ItemSubTypes<ItemCasing.Types> implements IModelRegister {

    protected static final String NAME = "casing";

    public ItemCasing() {
        super(null, Types.class);
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
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements ISubEnum {
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
        bronze(19),
        copper(20),
        gold(21),
        iron(22),
        lead(23),
        steel(24),
        tin(25);

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
