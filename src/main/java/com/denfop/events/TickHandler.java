package com.denfop.events;

import com.denfop.IUItem;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.upgrade.UpgradeItemInform;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockApatite;
import com.denfop.blocks.BlockClassicOre;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.BlockMineral;
import com.denfop.blocks.BlockOre;
import com.denfop.blocks.BlockOres2;
import com.denfop.blocks.BlockOres3;
import com.denfop.blocks.BlockPreciousOre;
import com.denfop.blocks.BlockThoriumOre;
import com.denfop.blocks.BlocksRadiationOre;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.energy.instruments.EnumOperations;
import com.denfop.items.energy.instruments.ItemEnergyInstruments;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.denfop.invslot.InvSlotUpgrade.getDirection;

public class TickHandler {

    final int latitudeSegments = 8;
    final int longitudeSegments = 8;
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

    public TickHandler() {
        MinecraftForge.EVENT_BUS.register(this);
        new GlobalRenderManager();
    }

    public int getOreColor(IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.IRON_ORE) {
            return ModUtils.convertRGBcolorToInt(156, 156, 156);
        } else if (block == Blocks.GOLD_ORE) {
            return 0xFFFFD700;
        } else if (block == Blocks.DIAMOND_ORE) {
            return 0xFF00FFFF;
        } else if (block == Blocks.LAPIS_ORE) {
            return ModUtils.convertRGBcolorToInt(30, 50, 173);
        } else if (block == Blocks.REDSTONE_ORE || block == Blocks.LIT_REDSTONE_ORE) {
            return ModUtils.convertRGBcolorToInt(173, 30, 30);
        } else if (block == Blocks.COAL_ORE) {
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block == Blocks.EMERALD_ORE) {
            return ModUtils.convertRGBcolorToInt(0, 232, 0);
        } else if (block == Blocks.QUARTZ_ORE) {
            return ModUtils.convertRGBcolorToInt(223, 223, 223);
        } else if (block == IUItem.toriyore) {
            return ModUtils.convertRGBcolorToInt(134, 134, 139);
        } else if (block instanceof BlockClassicOre) {
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(251, 140, 119);
                case 1:
                    return ModUtils.convertRGBcolorToInt(38, 60, 143);
                case 2:
                    return ModUtils.convertRGBcolorToInt(204, 180, 47);
                case 3:
                    return ModUtils.convertRGBcolorToInt(221, 221, 221);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockOre) {
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
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
                    case 15:
                    return ModUtils.convertRGBcolorToInt(194, 189, 56);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockOres2) {
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
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

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerSP) {
            EntityPlayerSP player = (EntityPlayerSP) event.getEntityLiving();
            World world = player.world;
            ChunkPos chunkPos = new ChunkPos(player.getPosition());


            int tier = getRadiationTier(world, chunkPos);


            if (tier > 1) {
                showRadiationEffects(player, world, chunkPos, tier);
            }
        }
    }

    // Метод для получения уровня радиации в чанке
    private int getRadiationTier(World world, ChunkPos chunkPos) {
        final Radiation rad = RadiationSystem.rad_system.getMap().get(chunkPos);
        return rad == null ? 0 : rad.getLevel().ordinal();
    }

    // Метод для отображения анимации радиации
    private void showRadiationEffects(EntityPlayerSP player, World world, ChunkPos chunkPos, int tier) {
        ParticleManager particleManager = Minecraft.getMinecraft().effectRenderer;
        int particleCount = MathHelper.clamp(tier * 30, 30, 120); // Увеличиваем количество частиц

        // Границы чанка
        double minX = chunkPos.getXStart();
        double maxX = chunkPos.getXEnd() + 1;
        double minZ = chunkPos.getZStart();
        double maxZ = chunkPos.getZEnd() + 1;

        // Создаем частицы вдоль границ чанка
        for (int i = 0; i < particleCount; i++) {
            double x, y = player.posY + world.rand.nextDouble() * 16.0, z;

            switch (world.rand.nextInt(4)) {
                case 0: // Левая сторона (по оси X)
                    x = minX;
                    z = minZ + world.rand.nextDouble() * 16.0;
                    break;
                case 1: // Правая сторона (по оси X)
                    x = maxX;
                    z = minZ + world.rand.nextDouble() * 16.0;
                    break;
                case 2: // Верхняя сторона (по оси Z)
                    x = minX + world.rand.nextDouble() * 16.0;
                    z = minZ;
                    break;
                default: // Нижняя сторона (по оси Z)
                    x = minX + world.rand.nextDouble() * 16.0;
                    z = maxZ;
                    break;
            }


            EnumParticleTypes particleType = world.rand.nextBoolean() ? EnumParticleTypes.PORTAL : EnumParticleTypes.SMOKE_NORMAL;
            Particle particle = particleManager.spawnEffectParticle(particleType.getParticleID(), x, y, z, 0, 0, 0);

            if (particle != null) {
                if (particleType == EnumParticleTypes.PORTAL) {
                    particle.setRBGColorF(0.0F, 1.0F - tier * 0.2F, 0.0F);
                    particle.setAlphaF(0.5F + world.rand.nextFloat() * 0.5F);
                } else {
                    particle.setRBGColorF(0.1F, 0.9F - tier * 0.15F, 0.1F);
                    particle.setAlphaF(0.7F + world.rand.nextFloat() * 0.3F);
                    particle.motionX = (world.rand.nextDouble() - 0.5) * 0.05;
                    particle.motionY = (world.rand.nextDouble() - 0.5) * 0.05;
                    particle.motionZ = (world.rand.nextDouble() - 0.5) * 0.05;
                }

                particleManager.addEffect(particle);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderOres(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (player == null || mc.world == null) {
            return;
        }
        if (!(player.getHeldItemMainhand().getItem() instanceof ItemEnergyInstruments)) {
            return;
        }
        if (!write) {
            writeData();
        }
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
        RayTraceResult ray = mc.objectMouseOver;
        if (ray == null || ray.typeOfHit != RayTraceResult.Type.BLOCK) {
            return;
        }
        final ItemStack stack = player.getHeldItemMainhand();
        ItemEnergyInstruments instruments = (ItemEnergyInstruments) player.getHeldItemMainhand().getItem();
        int toolMode = instruments.readToolMode(stack);
        EnumOperations operations = instruments.getOperations().get(toolMode);
        if (operations != EnumOperations.ORE) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(-x, -y, -z);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        x = ray.getBlockPos().getX();
        y = ray.getBlockPos().getY();
        z = ray.getBlockPos().getZ();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        for (int xPos = (int) (x - 5); xPos <= x + 5; xPos++) {
            for (int yPos = (int) (y - 5); yPos <= y + 5; yPos++) {
                for (int zPos = (int) (z - 5); zPos <= z + 5; zPos++) {
                    BlockPos currentPos = new BlockPos(xPos, yPos, zPos);
                    IBlockState state = player.getEntityWorld().getBlockState(currentPos);
                    if (isOre(state)) {
                        int color = getOreColor(state);
                        GlStateManager.color(
                                ((color >> 16) & 0xFF) / 255.0F,
                                ((color >> 8) & 0xFF) / 255.0F,
                                (color & 0xFF) / 255.0F,
                                0.5F
                        );
                        drawCircle(buffer, currentPos, tessellator);
                    }
                }
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
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

    private boolean isOre(IBlockState state) {

        return state.getBlock() == Blocks.IRON_ORE || state.getBlock() instanceof BlocksRadiationOre || state.getBlock() instanceof BlockThoriumOre ||
                state.getBlock() == Blocks.GOLD_ORE || state.getBlock() == Blocks.COAL_ORE || state.getBlock() == Blocks.REDSTONE_ORE || state.getBlock() == Blocks.LIT_REDSTONE_ORE || state.getBlock() == Blocks.EMERALD_ORE || state.getBlock() == Blocks.QUARTZ_ORE ||
                state.getBlock() == Blocks.LAPIS_ORE || state.getBlock() instanceof BlockClassicOre || state.getBlock() instanceof BlockPreciousOre ||
                state.getBlock() == Blocks.DIAMOND_ORE || state.getBlock() instanceof BlockHeavyOre || state.getBlock() instanceof BlockMineral
                || state.getBlock() instanceof BlockOre || state.getBlock() instanceof BlockOres2 || state.getBlock() instanceof BlockOres3 || state.getBlock() instanceof BlockApatite;
    }

    private void drawCircle(BufferBuilder buffer, BlockPos pos, Tessellator tessellator) {
        double radius = 0.25;


        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        buffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);

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

                buffer.pos(x + x1 * radius, y + y1 * radius, z + z1 * radius).endVertex();
                buffer.pos(x + x2 * radius, y + y2 * radius, z + z2 * radius).endVertex();
                buffer.pos(x + x3 * radius, y + y3 * radius, z + z3 * radius).endVertex();

                buffer.pos(x + x3 * radius, y + y3 * radius, z + z3 * radius).endVertex();
                buffer.pos(x + x4 * radius, y + y4 * radius, z + z4 * radius).endVertex();
                buffer.pos(x + x1 * radius, y + y1 * radius, z + z1 * radius).endVertex();
            }

        }

        tessellator.draw();
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (player == null || mc.world == null) {
            return;
        }

        if (!(player.getHeldItemMainhand().getItem() instanceof ItemEnergyInstruments)) {
            return;
        }


        RayTraceResult ray = mc.objectMouseOver;
        if (ray == null || ray.typeOfHit != RayTraceResult.Type.BLOCK) {
            return;
        }

        ItemEnergyInstruments instruments = (ItemEnergyInstruments) player.getHeldItemMainhand().getItem();
        final ItemStack stack = player.getHeldItemMainhand();
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
        BlockPos pos = ray.getBlockPos();
        int xRange = aoe;
        int yRange = aoe;
        int zRange = aoe;
        switch (ray.sideHit.ordinal()) {
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

        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

        GlStateManager.pushMatrix();
        GlStateManager.translate(-x, -y, -z);
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.color(0.0F, 1.0F, 0.0F, 0.5F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        Vec3d lookVec = player.getLook(1.0F);
        for (int xPos = (int) (x - xRange); xPos <= x + xRange; xPos++) {
            for (int yPos = (int) (y - yRange + Yy); yPos <= y + yRange + Yy; yPos++) {
                for (int zPos = (int) (z - zRange); zPos <= z + zRange; zPos++) {
                    BlockPos currentPos = new BlockPos(xPos, yPos, zPos);
                    IBlockState state = player.getEntityWorld().getBlockState(currentPos);
                    if (state.getMaterial() == Material.AIR) {
                        continue;
                    }
                    Vec3d blockVec = new Vec3d(
                            currentPos.getX() + 0.5 - player.posX,
                            currentPos.getY() + 0.5 - player.posY,
                            currentPos.getZ() + 0.5 - player.posZ
                    );

                    if (lookVec.dotProduct(blockVec) <= 0) {
                        continue;
                    }
                    buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ()).endVertex();
                    buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ()).endVertex();
                    buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ()).endVertex();
                    buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ() + 1).endVertex();
                    buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ() + 1).endVertex();
                    buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ() + 1).endVertex();
                    buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ() + 1).endVertex();
                    buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ()).endVertex();


                    buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ()).endVertex();
                    buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ()).endVertex();
                    buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ()).endVertex();
                    buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                    buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                    buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                    buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                    buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ()).endVertex();


                    buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ()).endVertex();
                    buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ()).endVertex();
                    buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ()).endVertex();
                    buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ()).endVertex();
                    buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ() + 1).endVertex();
                    buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                    buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ() + 1).endVertex();
                    buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                }
            }
        }

        tessellator.draw();

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
    Set<UpgradableProperty> set = EnumSet.of(UpgradableProperty.FluidExtract, UpgradableProperty.FluidInput,
            UpgradableProperty.ItemInput, UpgradableProperty.ItemExtract);
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderTileSide(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (player == null || mc.world == null) {
            return;
        }

        if (!(player.getHeldItemMainhand().getItem() instanceof IUpgradeItem)) {
            return;
        }
        IUpgradeItem upgradeItem = (IUpgradeItem) player.getHeldItemMainhand().getItem();
        if (!upgradeItem.isSuitableFor( player.getHeldItemMainhand(),set))
            return;

        RayTraceResult ray = mc.objectMouseOver;
        if (ray == null || ray.typeOfHit != RayTraceResult.Type.BLOCK) {
            return;
        }
        EnumFacing facing = getDirection(player.getHeldItemMainhand());
        TileEntity tile = player.getEntityWorld().getTileEntity(ray.getBlockPos());
        if (!(tile instanceof IUpgradableBlock))
            return;
        if (facing != null) {
            BlockPos pos = ray.getBlockPos().offset(facing);
            int xRange = 0;
            int yRange = 0;
            int zRange = 0;


            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

            GlStateManager.pushMatrix();
            GlStateManager.translate(-x, -y, -z);
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            Vec3d lookVec = player.getLook(1.0F);
            for (int xPos = (int) (x - xRange); xPos <= x + xRange; xPos++) {
                for (int yPos = (int) (y - yRange); yPos <= y + yRange ; yPos++) {
                    for (int zPos = (int) (z - zRange); zPos <= z + zRange; zPos++) {
                        BlockPos currentPos = new BlockPos(xPos, yPos, zPos);
                        Vec3d blockVec = new Vec3d(
                                currentPos.getX() + 0.5 - player.posX,
                                currentPos.getY() + 0.5 - player.posY,
                                currentPos.getZ() + 0.5 - player.posZ
                        );

                        if (lookVec.dotProduct(blockVec) <= 0) {
                            continue;
                        }
                        buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ()).endVertex();
                        buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ()).endVertex();
                        buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ()).endVertex();
                        buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ() + 1).endVertex();
                        buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ() + 1).endVertex();
                        buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ() + 1).endVertex();
                        buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ() + 1).endVertex();
                        buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ()).endVertex();


                        buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ()).endVertex();
                        buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ()).endVertex();
                        buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ()).endVertex();
                        buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                        buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                        buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                        buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                        buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ()).endVertex();


                        buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ()).endVertex();
                        buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ()).endVertex();
                        buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ()).endVertex();
                        buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ()).endVertex();
                        buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ() + 1).endVertex();
                        buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                        buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ() + 1).endVertex();
                        buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                    }
                }
            }

            tessellator.draw();

            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.popMatrix();
        }else {
            for (EnumFacing facing1 : EnumFacing.VALUES) {
                BlockPos pos = ray.getBlockPos().offset(facing1);
                int xRange = 0;
                int yRange = 0;
                int zRange = 0;


                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

                GlStateManager.pushMatrix();
                GlStateManager.translate(-x, -y, -z);
                x = pos.getX();
                y = pos.getY();
                z = pos.getZ();
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();

                buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
                Vec3d lookVec = player.getLook(1.0F);
                for (int xPos = (int) (x - xRange); xPos <= x + xRange; xPos++) {
                    for (int yPos = (int) (y - yRange); yPos <= y + yRange; yPos++) {
                        for (int zPos = (int) (z - zRange); zPos <= z + zRange; zPos++) {
                            BlockPos currentPos = new BlockPos(xPos, yPos, zPos);
                            Vec3d blockVec = new Vec3d(
                                    currentPos.getX() + 0.5 - player.posX,
                                    currentPos.getY() + 0.5 - player.posY,
                                    currentPos.getZ() + 0.5 - player.posZ
                            );

                            if (lookVec.dotProduct(blockVec) <= 0) {
                                continue;
                            }
                            buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ()).endVertex();
                            buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ()).endVertex();
                            buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ()).endVertex();
                            buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ() + 1).endVertex();
                            buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ() + 1).endVertex();
                            buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ() + 1).endVertex();
                            buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ() + 1).endVertex();
                            buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ()).endVertex();


                            buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ()).endVertex();
                            buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ()).endVertex();
                            buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ()).endVertex();
                            buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                            buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                            buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                            buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                            buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ()).endVertex();


                            buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ()).endVertex();
                            buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ()).endVertex();
                            buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ()).endVertex();
                            buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ()).endVertex();
                            buffer.pos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ() + 1).endVertex();
                            buffer.pos(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                            buffer.pos(currentPos.getX(), currentPos.getY(), currentPos.getZ() + 1).endVertex();
                            buffer.pos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ() + 1).endVertex();
                        }
                    }
                }

                tessellator.draw();

                GlStateManager.enableDepth();
                GlStateManager.disableBlend();
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            ClientTickHandler.onTickRender1();
        }

    }

}
