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

public class ItemDust extends ItemSubTypes<ItemDust.ItemDustTypes> implements IModelRegister {

    protected static final String NAME = "itemdust";

    public ItemDust() {
        super(null, ItemDustTypes.class);
        this.setCreativeTab(IUCore.RecourseTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item stack, final int meta, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(
                        Constants.MOD_ID + ":itemdust/" + ItemDustTypes.getFromID(meta).getName() + "_dust",
                        null
                )
        );
    }

    public enum ItemDustTypes implements ISubEnum {
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
        clay(20),
        coal(21),
        copper(22),
        diamond(23),
        energium(24),
        gold(25),
        iron(26),
        lapis(27),
        lead(28),
        obsidian(29),
        stone(30),
        sulfur(31),
        tin(32);

        private final String name;
        private final int ID;

        ItemDustTypes(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static ItemDustTypes getFromID(final int ID) {
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
