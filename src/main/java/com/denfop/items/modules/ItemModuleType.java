package com.denfop.items.modules;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
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

public class ItemModuleType extends ItemSubTypes<ItemModuleType.CraftingTypes> implements IModelRegister {

    protected static final String NAME = "modultype";

    public ItemModuleType() {
        super(CraftingTypes.class);
        this.setCreativeTab(IUCore.ModuleTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
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
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    public enum CraftingTypes implements ISubEnum {
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
