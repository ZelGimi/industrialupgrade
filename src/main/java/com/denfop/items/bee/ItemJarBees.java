package com.denfop.items.bee;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.bee.BeeNetwork;
import com.denfop.api.bee.IBee;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.IProperties;
import com.denfop.items.ItemMain;
import com.denfop.utils.ModUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemJarBees<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IProperties {
    public ItemJarBees(T element) {
        super(new Item.Properties(), element);
        IUCore.proxy.addProperties(this);
    }

    public ItemStack getStackFromId(int id) {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("bee_id", id);
        return stack;
    }
    public static IBee getBee(final ItemStack stack) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        IBee bee = BeeNetwork.instance.getBee(nbt.getInt("bee_id"));
        if (bee == null) {
            return null;
        }
        return bee.copy();
    }
    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (allowedIn(tab)) {
            BeeNetwork.instance.getBeeMap().forEach((id, crop) -> {
                ItemStack stack = new ItemStack(this);
                CompoundTag tag = stack.getOrCreateTag();
                tag.putInt("bee_id", id);
                items.add(stack);
            });
        }
    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.BeesTab;
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level world,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        tooltip.add(Component.translatable("iu.use_bee_analyzer").append(Component.translatable(IUItem.bee_analyzer.getItem().getDescriptionId())));

        tooltip.add(Component.translatable("iu.bee_negative"));

        IBee bee = getBee(stack);
        if (bee != null) {
            List<IBee> unCompatibleBees = bee.getUnCompatibleBees();
            for (IBee bee1 : unCompatibleBees) {
                tooltip.add(Component.translatable("bee_" + bee1.getName()));
            }
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("bee_id")) {
            IBee crop = BeeNetwork.instance.getBee(tag.getInt("bee_id"));
            return Component.translatable(super.getDescriptionId(stack))
                    .append(": ")
                    .append(Component.translatable("bee_" + crop.getName()));
        } else {
            return Component.translatable(super.getDescriptionId(stack));
        }
    }





    @Override
    public String[] properties() {
        return new String[]{"mode"};
    }



    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack itemStack, ClientLevel level, LivingEntity entity, int p174679, String property) {
        final CompoundTag nbt = ModUtils.nbt(itemStack);

        return nbt.getInt("bee_id");
    }


    public enum Types implements ISubEnum {
        bees;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        Types() {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = this.ordinal();
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "jar_bee";
        }

        public int getId() {
            return this.ID;
        }
    }
}
