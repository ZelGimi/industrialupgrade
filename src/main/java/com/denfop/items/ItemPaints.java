package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemPaints<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemPaints(T element) {
        super(new Item.Properties().tab(IUCore.ItemTab), element);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(ModUtils.mode(getElement().getId())));
    }

    public enum Types implements ISubEnum {
        emptybox(0),
        bluepaint(1),
        yellowpaint(2),
        greenpaint(3),
        redpaint(4),
        orangepaint(5),
        darkbluepaint(6),
        purplepaint(7),
        turquoisepaint(8),
        whitepaint(9),
        lightyellowpaint(10),
        limepaint(11),
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
            return "paints";
        }

        public int getId() {
            return this.ID;
        }
    }
}
