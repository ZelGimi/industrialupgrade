package com.denfop.items.modules;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.IIdProvider;
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

public class ItemModuleType extends ItemMulti<ItemModuleType.CraftingTypes> implements IModelRegister {

    protected static final String NAME = "modultype";

    public ItemModuleType() {
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
                new ModelResourceLocation(Constants.MOD_ID + ":modultype/" + CraftingTypes.getFromID(meta).getName(), null)
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
                info.add(Localization.translate("aerpanel"));
                info.add(Localization.translate("aerpanel1"));
                break;
            case 1:
                info.add(Localization.translate("earthpanel"));
                info.add(Localization.translate("earthpanel1"));
                break;
            case 2:
                info.add(Localization.translate("netherpanel"));
                break;
            case 3:
                info.add(Localization.translate("endpanel"));
                break;
            case 4:
                info.add(Localization.translate("nightpanel"));
                break;
            case 5:
                info.add(Localization.translate("sunpanel"));
                break;
            case 6:
                info.add(Localization.translate("rainpanel"));
                info.add(Localization.translate("rainpanel1"));
                break;
        }


    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    public enum CraftingTypes implements IIdProvider {
        module51(0),
        module52(1),
        module53(2),
        module54(3),
        module55(4),
        module56(5),
        module57(6);

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
