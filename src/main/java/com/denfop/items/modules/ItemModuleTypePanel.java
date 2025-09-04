package com.denfop.items.modules;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.blockentity.panels.entity.EnumSolarPanels;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemModuleTypePanel<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    int number = 0;

    public ItemModuleTypePanel(T element) {
        super(new Item.Properties().tab(IUCore.ModuleTab), element);
    }

    public static EnumSolarPanels getSolarType(int meta) {
        return EnumSolarPanels.getFromID(meta + 1);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level worldIn, List<Component> info, TooltipFlag p_41424_) {
        super.appendHoverText(itemStack, worldIn, info, p_41424_);
        EnumSolarPanels solar = getSolarType(this.getElement().getId());

        info.add(Component.literal(Localization.translate("supsolpans.iu.GenerationDay.tooltip") + " " + ModUtils.getString(solar.genday) + " EF/t "));
        info.add(Component.literal(Localization.translate("supsolpans.iu.GenerationNight.tooltip") + " " + ModUtils.getString(solar.gennight)
                + " EF/t "));

        info.add(Component.literal(Localization.translate("iu.item.tooltip.Output") + " " + ModUtils.getString(solar.producing) + " EF/t "));
        info.add(Component.literal(Localization.translate("iu.item.tooltip.Capacity") + " " + ModUtils.getString(solar.maxstorage) + " EF "));
        info.add(Component.literal(Localization.translate("iu.tier") + ModUtils.getString(solar.tier)));
        info.add(Component.literal(Localization.translate("iu.modules1")));
        info.add(Component.literal(Localization.translate("iu.modules2")));
        info.add(Component.literal(Localization.translate("using_kit")));

        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            final List<ItemStack> list = IUItem.upgrades_panels;
            if (list.size() > 0)
                info.add(Component.literal(Localization.translate(list
                        .get(number % list.size())
                        .getDescriptionId())));
        } else {

            for (ItemStack name : IUItem.upgrades_panels) {
                info.add(Component.literal(Localization.translate(name
                        .getDescriptionId())));
            }
        }
        if (worldIn != null) {
            if (worldIn.getGameTime() % 40 == 0) {
                number++;
            }
        }
    }

    public enum Types implements ISubEnum {
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
        module94(13);;

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

        @Override
        public String getMainPath() {
            return "modulestype";
        }

        public int getId() {
            return this.ID;
        }
    }
}
