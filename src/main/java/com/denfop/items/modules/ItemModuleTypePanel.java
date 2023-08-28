package com.denfop.items.modules;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemModuleTypePanel extends ItemSubTypes<ItemModuleTypePanel.CraftingTypes> implements IModelRegister {

    protected static final String NAME = "modulestype";

    int number = 0;

    public ItemModuleTypePanel() {
        super(null, CraftingTypes.class);
        this.setCreativeTab(IUCore.ModuleTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public static EnumSolarPanels getSolarType(int meta) {
        return EnumSolarPanels.getFromID(meta);
    }


    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {

        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":modulestype/" + CraftingTypes.getFromID(meta).getName(), null)
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
        EnumSolarPanels solar = getSolarType(itemStack.getItemDamage());

        info.add(Localization.translate("supsolpans.iu.GenerationDay.tooltip") + " " + ModUtils.getString(solar.genday) + " EF/t ");
        info.add(Localization.translate("supsolpans.iu.GenerationNight.tooltip") + " " + ModUtils.getString(solar.gennight)
                + " EF/t ");

        info.add(Localization.translate("iu.item.tooltip.Output") + " " + ModUtils.getString(solar.producing) + " EF/t ");
        info.add(Localization.translate("iu.item.tooltip.Capacity") + " " + ModUtils.getString(solar.maxstorage) + " EF ");
        info.add(Localization.translate("iu.tier") + ModUtils.getString(solar.tier));
        info.add(Localization.translate("iu.modules1"));
        info.add(Localization.translate("iu.modules2"));
        info.add(Localization.translate("using_kit"));

        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            final List<ItemStack> list = IUItem.upgrades_panels;
            info.add(Localization.translate(list
                    .get(number % list.size())
                    .getUnlocalizedName()));
        } else {
            for (ItemStack name : IUItem.upgrades_panels) {
                info.add(Localization.translate(name
                        .getUnlocalizedName()));
            }
        }
        if (worldIn != null) {
            if (worldIn.provider.getWorldTime() % 40 == 0) {
                number++;
            }
        }
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    public enum CraftingTypes implements ISubEnum {
        module61(0),
        module62(1),
        module63(2),
        module64(3),
        module65(4),
        module66(5),
        module67(6),
        module68(7),
        module69(8),
        module70(9),
        module91(10),
        module92(11),
        module93(12),
        module94(13);

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
