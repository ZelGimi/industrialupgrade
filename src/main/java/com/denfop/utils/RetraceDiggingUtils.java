package com.denfop.utils;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RetraceDiggingUtils {

    public static RayTraceResult retrace(EntityPlayer player) {
        return retrace(player, getBlockReachDistance(player));
    }

    public static double getBlockReachDistance(EntityPlayer player) {
        return player.world.isRemote ? getBlockReachDistance_client() : (player instanceof EntityPlayerMP
                ? getBlockReachDistance_server((EntityPlayerMP) player) : 5.0D);
    }

    public static RayTraceResult retrace(EntityPlayer player, double reach) {
        return retrace(player, reach, true);
    }

    @SideOnly(Side.CLIENT)
    private static double getBlockReachDistance_client() {
        return Minecraft.getMinecraft().playerController.getBlockReachDistance();
    }

    private static double getBlockReachDistance_server(EntityPlayerMP player) {
        return player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
    }

    public static RayTraceResult retrace(EntityPlayer player, double reach, boolean stopOnFluids) {
        Vec3d startVec = getStartVec(player);
        Vec3d endVec = getEndVec(player, reach);
        return player.world.rayTraceBlocks(startVec, endVec, stopOnFluids, false, true);
    }

    public static Vec3d getStartVec(EntityPlayer player) {
        return getCorrectedHeadVec(player);
    }

    public static Vec3d getCorrectedHeadVec(EntityPlayer player) {
        Vector3 v = Vector3.fromEntity(player).add(0.0D, player.getEyeHeight(), 0.0D);
        return v.vec3();
    }

    public static Vec3d getEndVec(EntityPlayer player, double reach) {
        Vec3d headVec = getCorrectedHeadVec(player);
        Vec3d lookVec = player.getLook(1.0F);
        return headVec.addVector(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
    }

}
