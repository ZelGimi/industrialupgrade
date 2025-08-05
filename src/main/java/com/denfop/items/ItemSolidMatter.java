package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.blocks.ISubEnum;
import com.denfop.tiles.solidmatter.EnumSolidMatter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemSolidMatter<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemSolidMatter(T element) {
        super(new Item.Properties(), element);
    }

    public static EnumSolidMatter getsolidmatter(int meta) {
        return switch (meta) {
            case 0 -> EnumSolidMatter.AER;
            case 1 -> EnumSolidMatter.AQUA;
            case 2 -> EnumSolidMatter.EARTH;
            case 3 -> EnumSolidMatter.END;
            case 5 -> EnumSolidMatter.NETHER;
            case 6 -> EnumSolidMatter.NIGHT;
            case 7 -> EnumSolidMatter.SUN;
            default -> EnumSolidMatter.MATTER;
        };

    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.literal(Localization.translate("iu.matter.info")));
    }

    public enum Types implements ISubEnum {
        matter(0),
        sun_matter(1),
        aqua_matter(2),
        nether_matter(3),
        night_matter(4),
        earth_matter(5),
        end_matter(6),
        aer_matter(7);

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
            return "solidmatter";
        }

        public int getId() {
            return this.ID;
        }
    }
}
