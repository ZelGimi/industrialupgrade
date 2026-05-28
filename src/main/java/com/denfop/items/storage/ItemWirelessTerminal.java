package com.denfop.items.storage;


import com.denfop.config.ModConfig;
import com.denfop.api.storage.IMonitor;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.items.BaseEnergyItem;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;

import javax.annotation.Nonnull;

public class ItemWirelessTerminal extends BaseEnergyItem implements IItemTab {
    private String nameItem;

    public ItemWirelessTerminal() {
        super(ModConfig.itemDouble("wireless_terminal_energy_capacity", 1000000.0D), ModConfig.itemDouble("wireless_terminal_transfer_limit", 4096.0D), 4);


    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "iu." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = ModUtils.get(player, hand);

        if (ElectricItem.manager.canUse(player.getItemInHand(hand), 500)) {

            CompoundTag nbt = ModUtils.nbt(stack);
            if (!world.isClientSide && !player.isShiftKeyDown() && nbt.contains("pos") && nbt.contains("level")) {
                BlockPos pos = BlockPos.of(nbt.getLong("pos"));
                String idLevel = nbt.getString("level");
                if (world.dimension().toString().trim().equals(idLevel)) {
                    BlockEntity blockEntity = world.getBlockEntity(pos);
                    if (blockEntity instanceof IMonitor && blockEntity instanceof BlockEntityInventory inventory && CommonHooks.canEntityDestroy(world, pos, player)) {
                        ElectricItem.manager.use(player.getItemInHand(hand), 500, player);
                        inventory.onActivated(player, hand, Direction.NORTH, new Vec3(0, 0, 0));
                    }
                }
                return InteractionResultHolder.success(player.getItemInHand(hand));

            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public InteractionResult useOn(
            UseOnContext context
    ) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = context.getItemInHand();
        if (CommonHooks.canEntityDestroy(world, pos, player)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof IMonitor) {
                ModUtils.nbt(stack).putLong("pos", BlockPos.asLong(blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ()));
                ModUtils.nbt(stack).putString("level", world.dimension().toString());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
