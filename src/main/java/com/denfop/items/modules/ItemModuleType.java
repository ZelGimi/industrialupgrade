package com.denfop.items.modules;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import com.denfop.utils.Localization;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemModuleType<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemModuleType(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ModuleTab;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable TooltipContext p_41422_, List<Component> info, TooltipFlag p_41424_) {
        super.appendHoverText(itemStack, p_41422_, info, p_41424_);
        int meta = this.getElement().getId();
        switch (meta) {
            case 0:
                info.add(Component.literal(Localization.translate("aerpanel")));
                info.add(Component.literal(Localization.translate("aerpanel1")));
                break;
            case 1:
                info.add(Component.literal(Localization.translate("earthpanel")));
                info.add(Component.literal(Localization.translate("earthpanel1")));
                break;
            case 2:
                info.add(Component.literal(Localization.translate("netherpanel")));
                break;
            case 3:
                info.add(Component.literal(Localization.translate("endpanel")));
                break;
            case 4:
                info.add(Component.literal(Localization.translate("nightpanel")));
                break;
            case 5:
                info.add(Component.literal(Localization.translate("sunpanel")));
                break;
            case 6:
                info.add(Component.literal(Localization.translate("rainpanel")));
                info.add(Component.literal(Localization.translate("rainpanel1")));
                break;
        }
    }

    public enum Types implements ISubEnum {
        module51(0),
        module52(1),
        module53(2),
        module54(3),
        module55(4),
        module56(5),
        module57(6);;

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
            return "modultype";
        }

        public int getId() {
            return this.ID;
        }
    }
}
