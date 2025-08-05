package com.denfop.utils;


import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
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
        float f = player.getXRot();
        float f1 = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getReachDistance();
        Vec3 vec31 = vec3.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
        return player.getLevel().clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
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
