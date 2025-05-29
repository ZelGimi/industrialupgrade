package com.denfop.mixin;

import com.denfop.tiles.base.TileEntityBlock;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {
    @Shadow
    static final Logger LOGGER = LogUtils.getLogger();
    @Shadow
    public ServerPlayer player;

    private ServerGamePacketListenerImpl getSelf() {
        return (ServerGamePacketListenerImpl) (Object) this;
    }

    @Shadow
    private Vec3 awaitingPositionFromClient;

    private static boolean wasBlockPlacementAttempt(ServerPlayer pPlayer, ItemStack pStack) {
        if (pStack.isEmpty()) {
            return false;
        } else {
            Item item = pStack.getItem();
            return (item instanceof BlockItem || item instanceof BucketItem) && !pPlayer.getCooldowns().isOnCooldown(item);
        }
    }

    @Inject(method = "handleUseItemOn", at = @At("HEAD"))
    private void onHandleUseItemOn(ServerboundUseItemOnPacket pPacket, CallbackInfo ci) {
        PacketUtils.ensureRunningOnSameThread(pPacket, getSelf(), this.player.getLevel());
        this.player.connection.ackBlockChangesUpTo(pPacket.getSequence());
        ServerLevel serverlevel = this.player.getLevel();
        InteractionHand interactionhand = pPacket.getHand();
        ItemStack itemstack = this.player.getItemInHand(interactionhand);
        BlockHitResult blockhitresult = pPacket.getHitResult();
        Vec3 vec3 = blockhitresult.getLocation();
        BlockPos blockpos = blockhitresult.getBlockPos();
        Vec3 vec31 = Vec3.atCenterOf(blockpos);
        if (this.player.canInteractWith(blockpos, 3)) {
            Vec3 vec32 = vec3.subtract(vec31);
            double d0 = 1.0000001D;
            BlockEntity blockEntity = this.player.getLevel().getBlockEntity(blockpos);
            if (blockEntity instanceof TileEntityBlock base) {
                AABB aabb = base.getAabb(false);
                if ((Math.abs(vec32.x()) > 1 || Math.abs(vec32.y()) > 1 || Math.abs(vec32.z()) > 1) && Math.abs(vec32.x()) <= aabb.maxX && Math.abs(vec32.y()) <= aabb.maxY && Math.abs(vec32.z()) <= aabb.maxZ) {
                    Direction direction = blockhitresult.getDirection();
                    this.player.resetLastActionTime();
                    int i = this.player.level.getMaxBuildHeight();
                    if (blockpos.getY() < i) {
                        if (this.awaitingPositionFromClient == null && serverlevel.mayInteract(this.player, blockpos)) {
                            InteractionResult interactionresult = this.player.gameMode.useItemOn(this.player, serverlevel, itemstack, interactionhand, blockhitresult);
                            if (direction == Direction.UP && !interactionresult.consumesAction() && blockpos.getY() >= i - 1 && wasBlockPlacementAttempt(this.player, itemstack)) {
                                Component component = Component.translatable("build.tooHigh", i - 1).withStyle(ChatFormatting.RED);
                                this.player.sendSystemMessage(component, true);
                            } else if (interactionresult.shouldSwing()) {
                                this.player.swing(interactionhand, true);
                            }
                        }
                    } else {
                        Component component1 = Component.translatable("build.tooHigh", i - 1).withStyle(ChatFormatting.RED);
                        this.player.sendSystemMessage(component1, true);
                    }

                    this.player.connection.send(new ClientboundBlockUpdatePacket(serverlevel, blockpos));
                    this.player.connection.send(new ClientboundBlockUpdatePacket(serverlevel, blockpos.relative(direction)));
                } else {
                    LOGGER.warn("Rejecting UseItemOnPacket from {}: Location {} too far away from hit block {}.", this.player.getGameProfile().getName(), vec3, blockpos);
                }
            }
        }
    }
}

