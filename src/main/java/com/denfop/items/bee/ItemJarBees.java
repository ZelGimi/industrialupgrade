package com.denfop.items.bee;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.bee.BeeNetwork;
import com.denfop.api.bee.IBee;
import com.denfop.api.bee.genetics.Genome;
import com.denfop.blocks.ISubEnum;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.IProperties;
import com.denfop.items.ItemMain;
import com.denfop.world.WorldBaseGen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemJarBees<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IProperties {
    public ItemJarBees(T element) {
        super(new Item.Properties(), element);
        IUCore.proxy.addProperties(this);
    }

    public static IBee getBee(final ItemStack stack) {
        IBee bee = BeeNetwork.instance.getBee(stack.getOrDefault(DataComponentsInit.BEE, 0));
        if (bee == null) {
            return null;
        }
        return bee.copy();
    }

    public ItemStack getStackFromId(int id) {
        ItemStack stack = new ItemStack(this);
        stack.set(DataComponentsInit.BEE, id);
        return stack;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (allowedIn(tab)) {
            BeeNetwork.instance.getBeeMap().forEach((id, crop) -> {
                ItemStack stack = new ItemStack(this);
                stack.set(DataComponentsInit.BEE, id);
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
            @Nullable TooltipContext world,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        tooltip.add(Component.translatable("iu.use_bee_analyzer").append(Component.translatable(IUItem.bee_analyzer.getItem().getDescriptionId())));
        IBee bee = getBee(stack);
        tooltip.add(Component.literal(Localization.translate("iu.bee_analyzer.main_crop") + " " + Localization.translate("crop." + bee
                .getCropFlower()
                .getName())));
        tooltip.add(Component.translatable("iu.bee_negative"));


        if (bee != null) {
            List<IBee> unCompatibleBees = bee.getUnCompatibleBees();
            for (IBee bee1 : unCompatibleBees) {
                tooltip.add(Component.translatable("bee_" + bee1.getName()));
            }
        }
        if (stack.has(DataComponentsInit.SWARM)) {
            int swarm = stack.get(DataComponentsInit.SWARM);
            tooltip.add(Component.literal(Localization.translate("iu.bee.swarm.info") + String.valueOf(swarm)));
        }
        Genome genome = new Genome(stack);
        if (!genome.getGeneticTraitsMap().isEmpty()) {
            tooltip.add(Component.literal(Localization.translate("iu.genomes.info")));
            genome.getGeneticTraitsMap().values().forEach(value -> tooltip.add(Component.literal(Localization.translate("iu.info.bee_genome_" + value.name().toLowerCase()))));

        }
    }

    @Override
    public Component getName(ItemStack stack) {
        if (getBee(stack) != null) {
            IBee crop = BeeNetwork.instance.getBee(stack.getOrDefault(DataComponentsInit.BEE, 0));
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

        return itemStack.getOrDefault(DataComponentsInit.BEE, 0);
    }

    public ItemStack getBeeStack(final int meta) {
        ItemStack stack = getStackFromId(meta);
        IBee bee = getBee(stack);
        stack.set(DataComponentsInit.SWARM, WorldBaseGen.random.nextInt(bee.getMaxSwarm() / 2) + 15);
        return stack;
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
