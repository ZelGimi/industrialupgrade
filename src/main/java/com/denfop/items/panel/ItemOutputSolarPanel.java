package com.denfop.items.panel;

import com.denfop.IUCore;
import com.denfop.api.solar.IOutputItem;
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

public class ItemOutputSolarPanel<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IOutputItem {
    public ItemOutputSolarPanel(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable TooltipContext p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("iu.minipanel.output") + this.getOutput(this.getElement().getId()) + " EF/t"));
        p_41423_.add(Component.literal(Localization.translate("iu.minipanel.jei")));
        //   p_41423_.addComponent.literal((Localization.translate("iu.minipanel.jei1") + Localization.translate(new ItemStack(IUItem.basemachine2, 1
        //          , 91).getUnlocalizedName())());
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    @Override
    public double getOutput(final int damage) {
        return 0.5625 * Math.pow(2, damage);
    }

    public enum Types implements ISubEnum {
        adv(0),
        hyb(1),
        ult(2),
        qua(3),
        spe(4),
        pro(5),
        sin(6),
        adm(7),
        pho(8),
        neu(9),
        bar(10),
        adr(11),
        gra(12),
        kvr(13),
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

        @Override
        public String getMainPath() {
            return "solar_panel_output";
        }

        public int getId() {
            return this.ID;
        }
    }
}
