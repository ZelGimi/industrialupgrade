package com.denfop.utils;


import com.mojang.math.Vector3f;
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
        return player.getLevel().isClientSide ? getBlockReachDistance_client() : (player instanceof ServerPlayer
                ? getBlockReachDistance_server((ServerPlayer) player) : 5.0D);
    }

    public static BlockHitResult retrace(Player player, double reach) {
        return retrace(player, reach, true);
    }

    @OnlyIn(Dist.CLIENT)
    private static double getBlockReachDistance_client() {
        return Minecraft.getInstance().player.getReachDistance();
    }

    private static double getBlockReachDistance_server(ServerPlayer player) {
        return player.getReachDistance();
    }

    public static BlockHitResult retrace(Player player, double reach, boolean stopOnFluids) {
        Vec3 startVec = getStartVec(player);
        Vec3 endVec = getEndVec(player, reach);
        return player.getLevel().clip(new ClipContext(startVec, endVec, ClipContext.Block.OUTLINE, ClipContext.Fluid.WATER, player));
    }

    public static Vec3 getStartVec(Player player) {
        return getCorrectedHeadVec(player);
    }

    public static Vec3 getCorrectedHeadVec(Player player) {
        Vector3f v = new Vector3f(player.position());
        v.add(0.0f, player.getEyeHeight(), 0.0f);
        return new Vec3(v);
    }

    public static Vec3 getEndVec(Player player, double reach) {
        Vector3f headVec = new Vector3f(getCorrectedHeadVec(player));
        Vec3 lookVec = player.getLookAngle();
        headVec.add((float) (lookVec.x * reach), (float) (lookVec.y * reach), (float) (lookVec.z * reach));
        return new Vec3(headVec);
    }

}
