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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemQuarryModule extends ItemMulti<ItemQuarryModule.CraftingTypes> implements IModelRegister {

    protected static final String NAME = "quarrymodules";

    public ItemQuarryModule() {
        super(null, CraftingTypes.class);
        this.setCreativeTab(IUCore.ModuleTab);
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
                new ModelResourceLocation(Constants.MOD_ID + ":quarrymodules/" + CraftingTypes.getFromID(meta).getName(), null)
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
        int meta = itemStack.getItemDamage();
        switch (meta) {
            case 0:
                info.add(Localization.translate("iu.quarry"));
                info.add(Localization.translate("iu.quarry1"));
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                info.add(Localization.translate("iu.quarry"));
                info.add(Localization.translate("iu.quarry2"));
                break;
            case 12:
                info.add(Localization.translate("iu.blacklist"));
                NBTTagCompound nbt = ModUtils.nbt(itemStack);
                int size = nbt.getInteger("size");
                for (int i = 0; i < size; i++) {
                    String l = "number_" + i;
                    String ore = ModUtils.NBTGetString(itemStack, l);
                    ItemStack stack = OreDictionary.getOres(ore).get(0);
                    info.add(stack.getDisplayName());
                }
                break;
            case 13:
                info.add(Localization.translate("iu.whitelist"));
                nbt = ModUtils.nbt(itemStack);
                size = nbt.getInteger("size");
                for (int i = 0; i < size; i++) {
                    String l = "number_" + i;
                    String ore = ModUtils.NBTGetString(itemStack, l);
                    ItemStack stack = OreDictionary.getOres(ore).get(0);
                    info.add(stack.getDisplayName());
                }

                break;
            case 14:
                info.add(TextFormatting.DARK_PURPLE + Localization.translate("iu.macerator"));
                break;
            case 15:
                info.add(TextFormatting.DARK_PURPLE + Localization.translate("iu.comb_macerator"));
                break;
        }
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    public enum CraftingTypes implements IIdProvider {
        per(0),
        ef(1),
        ef1(2),
        ef2(3),
        ef3(4),
        ef4(5),
        for1(6),
        for2(7),
        for3(8),
        kar1(9),
        kar2(10),
        kar3(11),
        blackmodule(12),
        whitemodule(13),
        macerator(14),
        comb_macerator(15),
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
