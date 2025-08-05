package com.denfop.items.upgradekit;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.ItemMain;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemUpgradeMachinesKit<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public static int tick = 0;
    public static int[] inform = new int[5];

    public ItemUpgradeMachinesKit(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);

    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.UpgradeTab;
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


        if (stack.has(DataComponentsInit.UPGRADE_KIT)) {
            ItemStack input = stack.get(DataComponentsInit.UPGRADE_KIT).input();
            ItemStack output = stack.get(DataComponentsInit.UPGRADE_KIT).output();

            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof TileEntityBlock tileEntityBlock) {
                if (tileEntityBlock.getPickBlock(player, null).is(input.getItem())) {
                    Block outputBlock = ((ItemBlockTileEntity) output.getItem()).getBlock();

                    BlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();

                    block.playerWillDestroy(world, pos, state, player);
                    world.removeBlock(pos, false);
                    block.destroy(world, pos, state);

                    List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class,
                            new AABB(Vec3.atCenterOf(pos.offset(-1, -1, -1)), Vec3.atCenterOf(pos.offset(1, 1, 1))));

                    for (ItemEntity item : items) {
                        item.discard();
                    }

                    BlockState newState = outputBlock.defaultBlockState();
                    world.setBlock(pos, newState, 3);

                    stack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        } else {
            boolean hooks = CommonHooks.onRightClickBlock(player, hand, pos, new BlockHitResult(
                    new Vec3(context.getClickLocation().x, context.getClickLocation().y, context.getClickLocation().z), context.getClickedFace(), pos, false)).isCanceled();
            if (hooks) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
    }

    @Override
    public void appendHoverText(
            @Nonnull ItemStack stack,
            @Nullable Level world,
            @Nonnull List<Component> tooltip,
            @Nonnull TooltipFlag flag
    ) {
        tooltip.add(Component.translatable("waring_kit"));


        if (stack.has(DataComponentsInit.UPGRADE_KIT)) {
            ItemStack inputStack = stack.get(DataComponentsInit.UPGRADE_KIT).input();
            tooltip.add(Component.translatable("using_kit").append(inputStack.getHoverName()));
        }

        super.appendHoverText(stack, world, tooltip, flag);
    }

    public enum Types implements ISubEnum {
        upgradepanelkitmachine(0),
        upgradepanelkitmachine1(1),
        upgradepanelkitmachine2(2),
        upgradepanelkitmachine3(3),
        upgradekitmachine4(3),
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
            return "upgradekitmachine";
        }

        public int getId() {
            return this.ID;
        }
    }

}
