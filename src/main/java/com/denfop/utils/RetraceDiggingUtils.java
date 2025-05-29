package com.denfop.utils;


import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RetraceDiggingUtils {

    public static BlockHitResult retrace(Player player) {
        return retrace(player, getBlockReachDistance(player));
    }

    public static double getBlockReachDistance(Player player) {
        return player.level().isClientSide ? getBlockReachDistance_client() : (player instanceof ServerPlayer
                ? getBlockReachDistance_server((ServerPlayer) player) : 5.0D);
    }

    public static BlockHitResult retrace(Player player, double reach) {
        return retrace(player, reach, true);
    }

    @OnlyIn(Dist.CLIENT)
    private static double getBlockReachDistance_client() {
        return Minecraft.getInstance().player.getBlockReach();
    }

    private static double getBlockReachDistance_server(ServerPlayer player) {
        return player.getBlockReach();
    }

    public static BlockHitResult retrace(Player player, double reach, boolean stopOnFluids) {
        Vec3 startVec = getStartVec(player);
        Vec3 endVec = getEndVec(player, reach);
        return player.level().clip(new ClipContext(startVec, endVec, ClipContext.Block.OUTLINE, ClipContext.Fluid.WATER, player));
    }

    public static Vec3 getStartVec(Player player) {
        return getCorrectedHeadVec(player);
    }

    public static Vec3 getCorrectedHeadVec(Player player) {
        Vec3 eyePos = player.position().add(0.0, player.getEyeHeight(), 0.0);
        return eyePos;
    }

    public static Vec3 getEndVec(Player player, double reach) {
        Vec3 headVec = getCorrectedHeadVec(player);
        Vec3 lookVec = player.getLookAngle();
        headVec.add((float) (lookVec.x * reach), (float) (lookVec.y * reach), (float) (lookVec.z * reach));
        return headVec;
    }

}
