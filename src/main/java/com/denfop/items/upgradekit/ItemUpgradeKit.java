package com.denfop.items.upgradekit;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.wiring.EnumElectricBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemUpgradeKit<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemUpgradeKit(T element) {
        super(new Item.Properties().tab(IUCore.UpgradeTab), element);
    }

    @Override
    public void appendHoverText(
            @Nonnull ItemStack stack,
            @Nullable Level world,
            @Nonnull List<Component> tooltip,
            @Nonnull TooltipFlag flag
    ) {
        tooltip.add(Component.translatable("waring_kit"));
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        InteractionHand hand = context.getHand();

        if (world.isClientSide || player == null) {
            return InteractionResult.PASS;
        }


        int meta = this.getElement().getId();
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof TileElectricBlock tile) {
            EnumElectricBlock enumblock = tile.getElectricBlock();
            if (enumblock != null && enumblock.kit_meta == meta) {
                ItemStack stack1 = new ItemStack(tile.getElectricBlock().chargepad
                        ? IUItem.chargepadelectricblock.getItem(tile.getElectricBlock().meta)
                        : IUItem.electricblock.getItem(tile.getElectricBlock().meta), 1);

                CompoundTag nbt = stack1.getOrCreateTag();
                nbt.putDouble("energy", tile.energy.getEnergy());

                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();

                block.playerWillDestroy(world, pos, state, player);
                world.removeBlock(pos, false);
                block.destroy(world, pos, state);

                List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class,
                        new AABB(pos.offset(-1, -1, -1), pos.offset(1, 1, 1)));

                for (ItemEntity item : items) {
                    item.discard();
                }

                ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), stack1);
                itemEntity.setPickUpDelay(0);
                world.addFreshEntity(itemEntity);

                List<ItemStack> dropList = tile.getDrop();
                for (ItemStack drop : dropList) {
                    ItemEntity dropEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), drop);
                    dropEntity.setPickUpDelay(0);
                    world.addFreshEntity(dropEntity);
                }

                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public enum Types implements ISubEnum {
        upgradekit(0),
        upgradekit1(1),
        upgradekit2(2),
        upgradekit3(3),

        upgradekit4(4),
        upgradekit5(5),
        upgradekit6(6),
        upgradekit7(7),
        upgradekit8(8),
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
            return "upgradekitstorage";
        }

        public int getId() {
            return this.ID;
        }
    }
}
