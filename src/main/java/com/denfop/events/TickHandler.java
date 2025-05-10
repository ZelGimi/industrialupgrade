package com.denfop.events;

import com.denfop.IUItem;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.upgrade.UpgradeItemInform;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.*;
import com.denfop.blocks.blockitem.*;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.energy.instruments.EnumOperations;
import com.denfop.items.energy.instruments.ItemEnergyInstruments;
import com.denfop.mixin.invoker.ParticleInvoker;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.denfop.render.base.RenderType.LEASH;

public class TickHandler {
    final int latitudeSegments = 4;
    final int longitudeSegments = 4;
    double[] sinLat = new double[latitudeSegments + 1];
    double[] cosLat = new double[latitudeSegments + 1];
    double[] sinLng = new double[longitudeSegments + 1];
    double[] cosLng = new double[longitudeSegments + 1];
    boolean write = false;
    double[][] x1 = new double[latitudeSegments][longitudeSegments];
    double[][] x2 = new double[latitudeSegments][longitudeSegments];
    double[][] x3 = new double[latitudeSegments][longitudeSegments];
    double[][] x4 = new double[latitudeSegments][longitudeSegments];
    double[][] y1 = new double[latitudeSegments][longitudeSegments];
    double[][] y2 = new double[latitudeSegments][longitudeSegments];
    double[][] y3 = new double[latitudeSegments][longitudeSegments];
    double[][] y4 = new double[latitudeSegments][longitudeSegments];
    double[][] z1 = new double[latitudeSegments][longitudeSegments];
    double[][] z2 = new double[latitudeSegments][longitudeSegments];
    double[][] z3 = new double[latitudeSegments][longitudeSegments];
    double[][] z4 = new double[latitudeSegments][longitudeSegments];
    Set<UpgradableProperty> set = EnumSet.of(UpgradableProperty.FluidExtract, UpgradableProperty.FluidInput,
            UpgradableProperty.ItemInput, UpgradableProperty.ItemExtract);

    public TickHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    protected static BlockHitResult getPlayerPOVHitResult(Level p_41436_, Player p_41437_, ClipContext.Fluid p_41438_) {
        float f = p_41437_.getXRot();
        float f1 = p_41437_.getYRot();
        Vec3 vec3 = p_41437_.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = p_41437_.getReachDistance();
        Vec3 vec31 = vec3.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
        return p_41436_.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, p_41438_, p_41437_));
    }

    public int getOreColor(BlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.IRON_ORE) {
            return ModUtils.convertRGBcolorToInt(156, 156, 156);
        } else if (block == Blocks.GOLD_ORE || block == Blocks.NETHER_GOLD_ORE) {
            return 0xFFFFD700;
        } else if (block == Blocks.DIAMOND_ORE) {
            return 0xFF00FFFF;
        } else if (block == Blocks.LAPIS_ORE) {
            return ModUtils.convertRGBcolorToInt(30, 50, 173);
        } else if (block == Blocks.REDSTONE_ORE) {
            return ModUtils.convertRGBcolorToInt(173, 30, 30);
        } else if (block == Blocks.COAL_ORE) {
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block == Blocks.EMERALD_ORE) {
            return ModUtils.convertRGBcolorToInt(0, 232, 0);
        } else if (block == Blocks.NETHER_QUARTZ_ORE) {
            return ModUtils.convertRGBcolorToInt(223, 223, 223);
        } else if (block == IUItem.toriyore.getBlock(0)) {
            return ModUtils.convertRGBcolorToInt(134, 134, 139);
        } else if (block instanceof BlockClassicOre) {
            final int meta = IUItem.classic_ore.getMeta((ItemBlockClassicOre) state.getBlock().asItem());
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(255, 144, 0);
                case 1:
                    return ModUtils.convertRGBcolorToInt(223, 223, 223);
                case 2:
                    return ModUtils.convertRGBcolorToInt(168, 176, 150);
                case 3:
                    return ModUtils.convertRGBcolorToInt(89, 158, 73);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlocksRadiationOre) {
            final int meta = IUItem.radiationore.getMeta((ItemBlockRadiationOre) state.getBlock().asItem());
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(120, 152, 183);
                case 1:
                    return ModUtils.convertRGBcolorToInt(97, 109, 88);
                case 2:
                    return ModUtils.convertRGBcolorToInt(150, 166, 148);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockPreciousOre) {
            final int meta = IUItem.preciousore.getMeta((ItemBlockPreciousOre) state.getBlock().asItem());

            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(251, 140, 119);
                case 1:
                    return ModUtils.convertRGBcolorToInt(38, 60, 143);
                case 2:
                    return ModUtils.convertRGBcolorToInt(204, 180, 47);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockOre) {
            final int meta = IUItem.ore.getMeta((ItemBlockOre) state.getBlock().asItem());
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(119, 210, 202);
                case 1:
                    return ModUtils.convertRGBcolorToInt(108, 74, 108);
                case 2:
                    return ModUtils.convertRGBcolorToInt(142, 240, 216);
                case 3:
                    return ModUtils.convertRGBcolorToInt(199, 199, 199);
                case 4:
                    return ModUtils.convertRGBcolorToInt(0, 166, 226);
                case 5:
                    return ModUtils.convertRGBcolorToInt(170, 145, 160);
                case 6:
                    return ModUtils.convertRGBcolorToInt(145, 143, 88);
                case 7:
                    return ModUtils.convertRGBcolorToInt(104, 152, 237);
                case 8:
                    return ModUtils.convertRGBcolorToInt(71, 71, 71);
                case 9:
                    return ModUtils.convertRGBcolorToInt(83, 174, 85);
                case 10:
                    return ModUtils.convertRGBcolorToInt(184, 87, 145);
                case 11:
                    return ModUtils.convertRGBcolorToInt(211, 211, 211);
                case 12:
                    return ModUtils.convertRGBcolorToInt(186, 186, 186);
                case 13:
                    return ModUtils.convertRGBcolorToInt(235, 193, 207);
                case 14:
                    return ModUtils.convertRGBcolorToInt(234, 234, 234);
                case 15:
                    return ModUtils.convertRGBcolorToInt(138, 85, 34);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockHeavyOre) {
            final int meta = IUItem.heavyore.getMeta((ItemBlockHeavyOre) state.getBlock().asItem());
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(137, 131, 149);
                case 1:
                    return ModUtils.convertRGBcolorToInt(249, 175, 44);
                case 2:
                    return ModUtils.convertRGBcolorToInt(150, 215, 206);
                case 3:
                    return ModUtils.convertRGBcolorToInt(211, 202, 110);
                case 4:
                    return ModUtils.convertRGBcolorToInt(212, 175, 55);
                case 5:
                    return ModUtils.convertRGBcolorToInt(250, 246, 241);
                case 6:
                    return ModUtils.convertRGBcolorToInt(70, 145, 15);
                case 7:
                    return ModUtils.convertRGBcolorToInt(230, 107, 0);
                case 8:
                    return ModUtils.convertRGBcolorToInt(139, 0, 0);
                case 9:
                    return ModUtils.convertRGBcolorToInt(55, 135, 135);
                case 10:
                    return ModUtils.convertRGBcolorToInt(170, 123, 44);
                case 11:
                    return ModUtils.convertRGBcolorToInt(109, 206, 167);
                case 12:
                    return ModUtils.convertRGBcolorToInt(110, 110, 110);
                case 13:
                    return ModUtils.convertRGBcolorToInt(198, 147, 64);
                case 14:
                    return ModUtils.convertRGBcolorToInt(100, 76, 136);
                case 15:
                    return ModUtils.convertRGBcolorToInt(135, 84, 64);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockMineral) {
            final int meta = IUItem.mineral.getMeta((ItemBlockMineral) state.getBlock().asItem());
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(12, 166, 166);
                case 1:
                    return ModUtils.convertRGBcolorToInt(55, 117, 104);
                case 2:
                    return ModUtils.convertRGBcolorToInt(113, 97, 81);
                case 3:
                    return ModUtils.convertRGBcolorToInt(99, 51, 4);
                case 4:
                    return ModUtils.convertRGBcolorToInt(117, 88, 86);
                case 5:
                    return ModUtils.convertRGBcolorToInt(118, 28, 17);
                case 6:
                    return ModUtils.convertRGBcolorToInt(123, 76, 10);
                case 7:
                    return ModUtils.convertRGBcolorToInt(126, 101, 36);
                case 8:
                    return ModUtils.convertRGBcolorToInt(30, 126, 56);
                case 9:
                    return ModUtils.convertRGBcolorToInt(112, 129, 30);
                case 10:
                    return ModUtils.convertRGBcolorToInt(43, 43, 43);
                case 11:
                    return ModUtils.convertRGBcolorToInt(39, 64, 63);
                case 12:
                    return ModUtils.convertRGBcolorToInt(110, 25, 24);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockOres3) {
            final int meta = IUItem.ore3.getMeta((ItemBlockOre3) state.getBlock().asItem());
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(191, 212, 65);
                case 1:
                    return ModUtils.convertRGBcolorToInt(253, 242, 80);
                case 2:
                    return ModUtils.convertRGBcolorToInt(37, 145, 133);
                case 3:
                    return ModUtils.convertRGBcolorToInt(255, 180, 0);
                case 4:
                    return ModUtils.convertRGBcolorToInt(252, 187, 89);
                case 5:
                    return ModUtils.convertRGBcolorToInt(212, 231, 255);
                case 6:
                    return ModUtils.convertRGBcolorToInt(222, 101, 98);
                case 7:
                    return ModUtils.convertRGBcolorToInt(118, 84, 192);
                case 8:
                    return ModUtils.convertRGBcolorToInt(125, 122, 160);
                case 9:
                    return ModUtils.convertRGBcolorToInt(61, 148, 224);
                case 10:
                    return ModUtils.convertRGBcolorToInt(230, 105, 17);
                case 11:
                    return ModUtils.convertRGBcolorToInt(84, 194, 246);
                case 12:
                    return ModUtils.convertRGBcolorToInt(168, 90, 41);
                case 13:
                    return ModUtils.convertRGBcolorToInt(121, 229, 71);
                case 14:
                    return ModUtils.convertRGBcolorToInt(255, 225, 136);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockOres2) {
            final int meta = IUItem.ore2.getMeta((ItemBlockOre2) state.getBlock().asItem());
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(190, 207, 214);
                case 1:
                    return ModUtils.convertRGBcolorToInt(194, 194, 194);
                case 2:
                    return ModUtils.convertRGBcolorToInt(62, 69, 71);
                case 3:
                    return ModUtils.convertRGBcolorToInt(165, 236, 244);
                case 4:
                    return ModUtils.convertRGBcolorToInt(141, 174, 83);
                case 5:
                    return ModUtils.convertRGBcolorToInt(177, 100, 197);
                case 6:
                    return ModUtils.convertRGBcolorToInt(43, 43, 43);
                case 7:
                    return ModUtils.convertRGBcolorToInt(212, 212, 212);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockApatite) {
            final int meta = IUItem.apatite.getMeta((ItemBlockApatite) state.getBlock().asItem());
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(48, 86, 16);
                case 1:
                    return ModUtils.convertRGBcolorToInt(134, 95, 11);
                case 2:
                    return ModUtils.convertRGBcolorToInt(202, 202, 202);
                case 3:
                    return ModUtils.convertRGBcolorToInt(202, 202, 202);
                case 4:
                    return ModUtils.convertRGBcolorToInt(202, 202, 202);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        }

        return 0xFFFFFFFF;
    }

    private int getRadiationTier(Level world, ChunkPos chunkPos) {
        final Radiation rad = RadiationSystem.rad_system.getMap().get(chunkPos);
        return rad == null ? 0 : rad.getLevel().ordinal();
    }

    public int clamp(int a, int min, int max) {
        return a < min ? min : (a > max ? max : a);
    }

    public void showRadiationEffects(LocalPlayer player, Level world, ChunkPos chunkPos, int tier) {
        ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
        int particleCount = clamp(tier * 30, 30, 120);


        double minX = chunkPos.getMinBlockX();
        double maxX = chunkPos.getMaxBlockX() + 1;
        double minZ = chunkPos.getMinBlockZ();
        double maxZ = chunkPos.getMaxBlockZ() + 1;

        RandomSource rand = new XoroshiroRandomSource(world.getGameTime());

        for (int i = 0; i < particleCount; i++) {
            double x, y = player.getY() + rand.nextDouble() * 16.0, z;

            switch (rand.nextInt(4)) {
                case 0 -> {
                    x = minX;
                    z = minZ + rand.nextDouble() * 16.0;
                }
                case 1 -> {
                    x = maxX;
                    z = minZ + rand.nextDouble() * 16.0;
                }
                case 2 -> {
                    x = minX + rand.nextDouble() * 16.0;
                    z = minZ;
                }
                default -> {
                    x = minX + rand.nextDouble() * 16.0;
                    z = maxZ;
                }
            }

            Vec3 position = new Vec3(x, y, z);


            boolean isPortal = rand.nextBoolean();
            Particle particle = isPortal
                    ? particleEngine.createParticle(ParticleTypes.PORTAL, position.x, position.y, position.z, 0, 0, 0)
                    : particleEngine.createParticle(ParticleTypes.SMOKE, position.x, position.y, position.z, 0, 0, 0);

            if (particle != null) {
                if (isPortal) {
                    particle.setColor(0.0F, 1.0F - tier * 0.2F, 0.0F);
                    ((ParticleInvoker) particle).invokeSetAlpha(0.5F + rand.nextFloat() * 0.5F);
                } else {
                    particle.setColor(0.1F, 0.9F - tier * 0.15F, 0.1F);
                    ((ParticleInvoker) particle).invokeSetAlpha(0.7F + rand.nextFloat() * 0.3F);
                    particle.setLifetime(40);
                }
            }
        }
    }

    public Direction getDirection(ItemStack stack) {
        int rawDir = ModUtils.nbt(stack).getByte("dir");
        return rawDir >= 1 && rawDir <= 6 ? Direction.values()[rawDir - 1] : null;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderTileSide(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.level == null) return;

        if (!(player.getMainHandItem().getItem() instanceof IUpgradeItem upgradeItem)) return;
        if (!upgradeItem.isSuitableFor(player.getMainHandItem(), set)) {
            return;
        }
        if (mc.level == null || event.getStage() != RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
            return;
        }
        BlockHitResult ray = getPlayerPOVHitResult(mc.level, mc.player, ClipContext.Fluid.NONE);
        if (ray == null || ray.getType() != HitResult.Type.BLOCK) {
            return;
        }
        Direction facing = getDirection(player.getMainHandItem());
        BlockEntity tile = player.getLevel().getBlockEntity(ray.getBlockPos());
        if (!(tile instanceof IUpgradableBlock)) {
            return;
        }
        PoseStack poseStack = event.getPoseStack();
        Matrix4f matrix = poseStack.last().pose();
        if (facing != null) {
            BlockPos pos = ray.getBlockPos().offset(facing.getNormal());
            int xRange = 0;
            int yRange = 0;
            int zRange = 0;


            Vec3 camera = event.getCamera().getPosition();

            double x = camera.x;
            double y = camera.y;
            double z = camera.z;

            poseStack.pushPose();
            poseStack.translate(-x, -y, -z);
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
            VertexConsumer p_109623_ = Minecraft.getInstance()
                    .renderBuffers()
                    .bufferSource()
                    .getBuffer(RenderType.lines());
            RenderSystem.lineWidth(10F);
            Vec3 lookVec = player.getLookAngle();
            for (int xPos = (int) (x - xRange); xPos <= x + xRange; xPos++) {
                for (int yPos = (int) (y - yRange); yPos <= y + yRange; yPos++) {
                    for (int zPos = (int) (z - zRange); zPos <= z + zRange; zPos++) {
                        BlockPos currentPos = new BlockPos(xPos, yPos, zPos);
                        Vec3 blockVec = new Vec3(
                                currentPos.getX() + 0.5 - player.getX(),
                                currentPos.getY() + 0.5 - player.getY(),
                                currentPos.getZ() + 0.5 - player.getZ()
                        );


                        float p_109630_ = 1.0f;
                        float p_109635_ = 1.0f;
                        float p_109636_ = 1.0f;
                        float p_109633_ = 1.0f;
                        float p_109634_ = 1.0f;
                        float p_109631_ = 1.0f;
                        float p_109632_ = 1.0f;
                        Matrix4f matrix4f = poseStack.last().pose();
                        Matrix3f matrix3f = poseStack.last().normal();
                        int f = currentPos.getX();
                        int f1 = currentPos.getY();
                        int f2 = currentPos.getZ();
                        int f3 = currentPos.getX() + 1;
                        int f4 = currentPos.getY() + 1;
                        int f5 = currentPos.getZ() + 1;
                        p_109623_.vertex(matrix4f, f, f1, f2).color(p_109630_, p_109635_, p_109636_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f1, f2).color(p_109630_, p_109635_, p_109636_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f1, f2).color(p_109634_, p_109631_, p_109636_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f4, f2).color(p_109634_, p_109631_, p_109636_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f1, f2).color(p_109634_, p_109635_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f1, f5).color(p_109634_, p_109635_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f1, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f1, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();

                    }
                }
            }


            poseStack.popPose();
        } else {
            for (Direction facing1 : Direction.values()) {
                BlockPos pos = ray.getBlockPos().offset(facing1.getNormal());
                int xRange = 0;
                int yRange = 0;
                int zRange = 0;

                Vec3 camera = event.getCamera().getPosition();
                double x = camera.x;
                double y = camera.y;
                double z = camera.z;

                poseStack.pushPose();
                poseStack.translate(-x, -y, -z);
                x = pos.getX();
                y = pos.getY();
                z = pos.getZ();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);

                Tesselator tessellator = Tesselator.getInstance();
                VertexConsumer p_109623_ = Minecraft.getInstance()
                        .renderBuffers()
                        .bufferSource()
                        .getBuffer(RenderType.lines());
                RenderSystem.lineWidth(10F);

                Vec3 lookVec = player.getLookAngle();
                for (int xPos = (int) (x - xRange); xPos <= x + xRange; xPos++) {
                    for (int yPos = (int) (y - yRange); yPos <= y + yRange; yPos++) {
                        for (int zPos = (int) (z - zRange); zPos <= z + zRange; zPos++) {
                            BlockPos currentPos = new BlockPos(xPos, yPos, zPos);
                            Vec3 blockVec = new Vec3(
                                    currentPos.getX() + 0.5 - player.getX(),
                                    currentPos.getY() + 0.5 - player.getY(),
                                    currentPos.getZ() + 0.5 - player.getZ()
                            );

                            if (lookVec.dot(blockVec) <= 0) {
                                continue;
                            }
                            float p_109630_ = 1.0f;
                            float p_109635_ = 1.0f;
                            float p_109636_ = 1.0f;
                            float p_109633_ = 1.0f;
                            float p_109634_ = 1.0f;
                            float p_109631_ = 1.0f;
                            float p_109632_ = 1.0f;
                            Matrix4f matrix4f = poseStack.last().pose();
                            Matrix3f matrix3f = poseStack.last().normal();
                            int f = currentPos.getX();
                            int f1 = currentPos.getY();
                            int f2 = currentPos.getZ();
                            int f3 = currentPos.getX() + 1;
                            int f4 = currentPos.getY() + 1;
                            int f5 = currentPos.getZ() + 1;
                            p_109623_.vertex(matrix4f, f, f1, f2).color(p_109630_, p_109635_, p_109636_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f3, f1, f2).color(p_109630_, p_109635_, p_109636_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f, f1, f2).color(p_109634_, p_109631_, p_109636_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f, f4, f2).color(p_109634_, p_109631_, p_109636_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f, f1, f2).color(p_109634_, p_109635_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                            p_109623_.vertex(matrix4f, f, f1, f5).color(p_109634_, p_109635_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                            p_109623_.vertex(matrix4f, f3, f1, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f3, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f3, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                            p_109623_.vertex(matrix4f, f, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                            p_109623_.vertex(matrix4f, f, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f3, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f3, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
                            p_109623_.vertex(matrix4f, f3, f1, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
                            p_109623_.vertex(matrix4f, f, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f3, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f3, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f3, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                            p_109623_.vertex(matrix4f, f3, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                            p_109623_.vertex(matrix4f, f3, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();


                        }
                    }
                }

                poseStack.popPose();
            }
        }
    }


    @SubscribeEvent
    public void onRenderWorld(RenderLevelStageEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null || event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }
        if (player != null && minecraft.hitResult instanceof BlockHitResult) {
            BlockHitResult ray = (BlockHitResult) minecraft.hitResult;
            if (!(player.getMainHandItem().getItem() instanceof ItemEnergyInstruments)) {
                return;
            }

            if (ray.getType() != HitResult.Type.BLOCK) {
                return;
            }
            BlockPos blockPos = ray.getBlockPos();

            PoseStack poseStack = event.getPoseStack();

            float size = 1.0f;
            ItemEnergyInstruments instruments = (ItemEnergyInstruments) player.getMainHandItem().getItem();
            final ItemStack stack = player.getMainHandItem();
            List<UpgradeItemInform> upgradeItemInforms = UpgradeSystem.system.getInformation(stack);

            int toolMode = instruments.readToolMode(stack);
            EnumOperations operations = instruments.getOperations().get(toolMode);
            int aoe = 0;
            int dig_depth = 0;
            List<Integer> list;
            switch (operations) {
                case BIGHOLES:
                    aoe = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0;
                    aoe += 1;
                    dig_depth = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms).number : 0);

                    list = UpgradeSystem.system.getUpgradeFromList(stack);
                    if (list != null && list.size() >= 5) {
                        dig_depth += list.get(4);
                    }
                    break;
                case MEGAHOLES:
                    aoe = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0;
                    aoe += 2;
                    dig_depth = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms).number : 0);
                    list = UpgradeSystem.system.getUpgradeFromList(stack);
                    if (list != null && list.size() >= 5) {
                        dig_depth += list.get(4);
                    }
                    break;
                case ULTRAHOLES:
                    aoe = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0;
                    aoe += 3;
                    dig_depth = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms).number : 0);
                    list = UpgradeSystem.system.getUpgradeFromList(stack);
                    if (list != null && list.size() >= 5) {
                        dig_depth += list.get(4);
                    }
                    break;
                default:
                    break;
            }
            Minecraft mc = Minecraft.getInstance();
            BlockPos pos = ray.getBlockPos();
            int xRange = aoe;
            int yRange = aoe;
            int zRange = aoe;
            switch (ray.getDirection().ordinal()) {
                case 0:
                case 1:
                    yRange = dig_depth;
                    break;
                case 2:
                case 3:
                    zRange = dig_depth;
                    break;
                case 4:
                case 5:
                    xRange = dig_depth;
                    break;
            }
            int Yy;
            Yy = yRange > 0 ? yRange - 1 : 0;


            poseStack.pushPose();
            double camX = mc.gameRenderer.getMainCamera().getPosition().x();
            double camY = mc.gameRenderer.getMainCamera().getPosition().y();
            double camZ = mc.gameRenderer.getMainCamera().getPosition().z();
            poseStack.translate(-camX, -camY, -camZ);
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, 0.5F);

            Tesselator tessellator = Tesselator.getInstance();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
            VertexConsumer p_109623_ = Minecraft.getInstance()
                    .renderBuffers()
                    .bufferSource()
                    .getBuffer(RenderType.lines());
            RenderSystem.lineWidth(10F);
            Vec3 lookVec = player.getLookAngle();
            for (int xPos = (int) (x - xRange); xPos <= x + xRange; xPos++) {
                for (int yPos = (int) (y - yRange + Yy); yPos <= y + yRange + Yy; yPos++) {
                    for (int zPos = (int) (z - zRange); zPos <= z + zRange; zPos++) {
                        BlockPos currentPos = new BlockPos(xPos, yPos, zPos);
                        BlockState state = player.getLevel().getBlockState(currentPos);
                        if (state.getMaterial() == Material.AIR) {
                            continue;
                        }


                        float p_109630_ = 1.0f;
                        float p_109635_ = 1.0f;
                        float p_109636_ = 1.0f;
                        float p_109633_ = 1.0f;
                        float p_109634_ = 1.0f;
                        float p_109631_ = 1.0f;
                        float p_109632_ = 1.0f;
                        Matrix4f matrix4f = poseStack.last().pose();
                        Matrix3f matrix3f = poseStack.last().normal();
                        int f = currentPos.getX();
                        int f1 = currentPos.getY();
                        int f2 = currentPos.getZ();
                        int f3 = currentPos.getX() + 1;
                        int f4 = currentPos.getY() + 1;
                        int f5 = currentPos.getZ() + 1;
                        p_109623_.vertex(matrix4f, f, f1, f2).color(0, p_109635_, 0, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f1, f2).color(0, p_109635_, 0, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f1, f2).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f4, f2).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f1, f2).color(0, p_109635_, 0, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f1, f5).color(0, p_109635_, 0, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f1, f2).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f4, f2).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f4, f2).color(0, p_109631_, 0, p_109633_).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f4, f2).color(0, p_109631_, 0, p_109633_).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f4, f2).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f4, f5).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f4, f5).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f1, f5).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f1, f5).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f1, f5).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f1, f5).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f1, f2).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f, f4, f5).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f4, f5).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f1, f5).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f4, f5).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f4, f2).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
                        p_109623_.vertex(matrix4f, f3, f4, f5).color(0, p_109631_, 0, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();

                    }
                }
            }


            RenderSystem.disableDepthTest();


            poseStack.popPose();
        }

    }


    @SubscribeEvent
    public void onRenderOres(RenderLevelStageEvent event) {


        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null || event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }
        if (!(player.getMainHandItem().getItem() instanceof ItemEnergyInstruments)) {
            return;
        }
        if (!write) {
            writeData();
        }
        HitResult ray = mc.hitResult;
        if (!(ray instanceof BlockHitResult blockRay)) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        ItemEnergyInstruments instruments = (ItemEnergyInstruments) stack.getItem();
        int toolMode = instruments.readToolMode(stack);
        EnumOperations operations = instruments.getOperations().get(toolMode);
        if (operations != EnumOperations.ORE) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();


        BlockPos centerPos = blockRay.getBlockPos();
        double camX = mc.gameRenderer.getMainCamera().getPosition().x();
        double camY = mc.gameRenderer.getMainCamera().getPosition().y();
        double camZ = mc.gameRenderer.getMainCamera().getPosition().z();
        for (int x = centerPos.getX() - 5; x <= centerPos.getX() + 5; x++) {
            for (int y = centerPos.getY() - 5; y <= centerPos.getY() + 5; y++) {
                for (int z = centerPos.getZ() - 5; z <= centerPos.getZ() + 5; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = player.level.getBlockState(pos);
                    if (isOre(state)) {
                        int color = getOreColor(state);
                        float r = ((color >> 16) & 0xFF) / 255.0F;
                        float g = ((color >> 8) & 0xFF) / 255.0F;
                        float b = (color & 0xFF) / 255.0F;

                        poseStack.pushPose();
                        VertexConsumer bufferSource = Minecraft.getInstance()
                                .renderBuffers()
                                .bufferSource()
                                .getBuffer(LEASH);
                        poseStack.translate(-camX, -camY, -camZ);
                        RenderSystem.enableDepthTest();
                        drawCircle(poseStack, bufferSource, pos, r, g, b, 0.5F);
                        RenderSystem.disableDepthTest();

                        poseStack.popPose();
                    }
                }
            }
        }

    }

    private void drawCircle(PoseStack poseStack, VertexConsumer bufferSource, BlockPos pos, float r, float g, float b, float alpha) {

        float x = pos.getX() + 0.5F;
        float y = pos.getY() + 0.5F;
        float z = pos.getZ() + 0.5F;
        float radius = 0.35F;
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        for (int i = 0; i < latitudeSegments; i++) {
            for (int j = 0; j < longitudeSegments; j++) {


                double x1 = this.x1[i][j];
                double y1 = this.y1[i][j];
                double z1 = this.z1[i][j];


                double x2 = this.x2[i][j];
                double y2 = this.y2[i][j];
                double z2 = this.z2[i][j];


                double x3 = this.x3[i][j];
                double y3 = this.y3[i][j];
                double z3 = this.z3[i][j];


                double x4 = this.x4[i][j];
                double y4 = this.y4[i][j];
                double z4 = this.z4[i][j];

                RenderSystem.setShaderColor(1, 1, 1, 1);
                bufferSource.vertex(matrix, (float) (x + x1 * radius), (float) (y + y1 * radius), (float) (z + z1 * radius)).color(r, g, b, 1f).endVertex();
                bufferSource.vertex(matrix, (float) (x + x2 * radius), (float) (y + y2 * radius), (float) (z + z2 * radius)).color(r, g, b, 1f).endVertex();
                bufferSource.vertex(matrix, (float) (x + x3 * radius), (float) (y + y3 * radius), (float) (z + z3 * radius)).color(r, g, b, 1f).endVertex();

                bufferSource.vertex(matrix, (float) (x + x3 * radius), (float) (y + y3 * radius), (float) (z + z3 * radius)).color(r, g, b, 1f).endVertex();
                bufferSource.vertex(matrix, (float) (x + x4 * radius), (float) (y + y4 * radius), (float) (z + z4 * radius)).color(r, g, b, 1f).endVertex();
                bufferSource.vertex(matrix, (float) (x + x1 * radius), (float) (y + y1 * radius), (float) (z + z1 * radius)).color(r, g, b, 1f).endVertex();
            }

        }
    }

    private void writeData() {
        write = true;
        for (int i = 0; i <= latitudeSegments; i++) {
            double lat = Math.PI * (-0.5 + (double) i / latitudeSegments);
            sinLat[i] = Math.sin(lat);
            cosLat[i] = Math.cos(lat);
        }


        for (int j = 0; j <= longitudeSegments; j++) {
            double lng = 2 * Math.PI * j / longitudeSegments;
            sinLng[j] = Math.sin(lng);
            cosLng[j] = Math.cos(lng);
        }
        for (int i = 0; i < latitudeSegments; i++) {
            for (int j = 0; j < longitudeSegments; j++) {

                x1[i][j] = cosLng[j] * cosLat[i];
                y1[i][j] = sinLat[i];
                z1[i][j] = sinLng[j] * cosLat[i];


                x2[i][j] = cosLng[j] * cosLat[i + 1];
                y2[i][j] = sinLat[i + 1];
                z2[i][j] = sinLng[j] * cosLat[i + 1];


                x3[i][j] = cosLng[j + 1] * cosLat[i + 1];
                y3[i][j] = sinLat[i + 1];
                z3[i][j] = sinLng[j + 1] * cosLat[i + 1];


                x4[i][j] = cosLng[j + 1] * cosLat[i];
                y4[i][j] = sinLat[i];
                z4[i][j] = sinLng[j + 1] * cosLat[i];
            }
        }
    }

    private boolean isOre(BlockState state) {

        return state.getBlock() == Blocks.IRON_ORE || state.getBlock() instanceof BlocksRadiationOre || state.getBlock() instanceof BlockThoriumOre ||
                state.getBlock() == Blocks.GOLD_ORE || state.getBlock() == Blocks.COAL_ORE || state.getBlock() == Blocks.REDSTONE_ORE || state.getBlock() == Blocks.NETHER_GOLD_ORE || state.getBlock() == Blocks.EMERALD_ORE || state.getBlock() == Blocks.NETHER_QUARTZ_ORE ||
                state.getBlock() == Blocks.LAPIS_ORE || state.getBlock() instanceof BlockClassicOre || state.getBlock() instanceof BlockPreciousOre ||
                state.getBlock() == Blocks.DIAMOND_ORE || state.getBlock() instanceof BlockHeavyOre || state.getBlock() instanceof BlockMineral
                || state.getBlock() instanceof BlockOre || state.getBlock() instanceof BlockOres2 || state.getBlock() instanceof BlockOres3 || state.getBlock() instanceof BlockApatite;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void renderTick(RenderGuiOverlayEvent.Post event) {
        ClientTickHandler.onTickRender1(event.getPoseStack());


    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof LocalPlayer) {
            LocalPlayer player = (LocalPlayer) event.getEntity();
            Level world = player.level;
            ChunkPos chunkPos = new ChunkPos(player.getOnPos());


            int tier = getRadiationTier(world, chunkPos);


            if (tier > 1) {
                showRadiationEffects(player, world, chunkPos, tier);
            }
        }
    }

}
