package com.denfop.items.modules;

import com.denfop.IUCore;
import com.denfop.blocks.SubEnum;
import com.denfop.items.ItemMain;
import com.denfop.utils.CapturedMobUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemEntityModule<T extends Enum<T> & SubEnum> extends ItemMain<T> {
    public ItemEntityModule(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (this.getElement().getId() == 1) {
            if (player.isShiftKeyDown()) {
                stack.setTag(new CompoundTag());
                return InteractionResultHolder.success(stack);
            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ModuleTab;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        if (this.getElement().getId() != 1) {
            CompoundTag nbt = stack.getTag();
            if (nbt != null && !nbt.getString("name").isEmpty()) {
                tooltip.add(Component.literal(nbt.getString("name")));
            }
        } else {

        }
    }


    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity entity,
            InteractionHand hand
    ) {
        if (player.level().isClientSide()) {
            return InteractionResult.PASS;
        }

        if (this.getElement().getId() == 1) {
            if (entity instanceof Player) return InteractionResult.FAIL;

            ResourceLocation entityId = EntityType.getKey(entity.getType());
            CompoundTag tag = new CompoundTag();
            entity.save(tag);


            CapturedMobUtils captured = CapturedMobUtils.create(entity);
            if (captured == null) return InteractionResult.FAIL;

            entity.discard();
            stack.shrink(1);
            ItemStack resultStack = captured.toStack(this, 1);
            tag = resultStack.getOrCreateTag();
            tag.putString("id", entityId.toString());
            tag.putString("nameEntity", entity.getName().getString());
            tag.putInt("id_mob", entity.getId());
            double dx = (player.level().random.nextDouble() * 0.7D) + 0.15D;
            double dy = (player.level().random.nextDouble() * 0.7D) + 0.15D;
            double dz = (player.level().random.nextDouble() * 0.7D) + 0.15D;

            ItemEntity itemEntity = new ItemEntity(
                    player.level(),
                    player.getX() + dx,
                    player.getY() + dy,
                    player.getZ() + dz,
                    resultStack
            );
            itemEntity.setPickUpDelay(10);
            player.level().addFreshEntity(itemEntity);

            return InteractionResult.SUCCESS;
        } else if (this.getElement().getId() == 0) {
            if (entity instanceof Player) {
                ItemStack stackCopy = stack.copy();
                CompoundTag tag = new CompoundTag();
                tag.putString("name", entity.getDisplayName().getString());
                stack.shrink(1);

                double dx = (player.level().random.nextDouble() * 0.7D) + 0.15D;
                double dy = (player.level().random.nextDouble() * 0.7D) + 0.15D;
                double dz = (player.level().random.nextDouble() * 0.7D) + 0.15D;

                ItemEntity itemEntity = new ItemEntity(
                        player.level(),
                        player.getX() + dx,
                        player.getY() + dy,
                        player.getZ() + dz,
                        stackCopy
                );
                itemEntity.setPickUpDelay(10);
                player.level().addFreshEntity(itemEntity);

                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }

        return InteractionResult.PASS;
    }


    public enum Types implements SubEnum {
        module_player(0),
        module_mob(1),
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
            return "entitymodules";
        }

        public int getId() {
            return this.ID;
        }
    }
}
