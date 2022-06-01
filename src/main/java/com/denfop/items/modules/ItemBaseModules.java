package com.denfop.items.modules;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.utils.ModUtils;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemBaseModules extends ItemMulti<ItemBaseModules.CraftingTypes> implements IModelRegister {

    protected static final String NAME = "modules";

    public ItemBaseModules() {
        super(null, CraftingTypes.class);
        this.setCreativeTab(IUCore.tabssp1);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":modules/" + CraftingTypes.getFromID(meta).getName(), null)
        );
    }

    @Override
    public void addInformation(
            @Nonnull final ItemStack itemStack,
            @Nullable final World worldIn,
            @Nonnull final List<String> info,
            @Nonnull final ITooltipFlag flagIn
    ) {
        super.addInformation(itemStack, worldIn, info, flagIn);
        if (EnumModule.getFromID(itemStack.getItemDamage()) != null) {
            if (EnumModule.getFromID(itemStack.getItemDamage()).type != EnumBaseType.PHASE && EnumModule.getFromID(itemStack.getItemDamage()).type != EnumBaseType.MOON_LINSE) {
                info.add(Localization.translate(EnumModule.getFromID(itemStack.getItemDamage()).description) + " +" + ModUtils.getString(
                        EnumModule.getFromID(itemStack.getItemDamage()).percent_description) + "% "
                        + Localization.translate("iu.module"));
            }
        }
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    public enum CraftingTypes implements IIdProvider {
        genday(0),
        genday1(1),
        genday2(2),
        gennight(3),
        gennight1(4),
        gennight2(5),
        storage(6),
        storage1(7),
        storage2(8),
        output(9),
        output1(10),
        output2(11),
        phase(12),
        phase1(13),
        phase2(14),
        moonlinse(15),
        moonlinse1(16),
        moonlinse2(17),
        ;

        private final String name;
        private final int ID;

        CraftingTypes(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static CraftingTypes getFromID(final int ID) {
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
