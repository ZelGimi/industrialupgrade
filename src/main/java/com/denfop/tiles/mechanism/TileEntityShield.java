package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerShield;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.gui.GuiShield;
import com.denfop.invslot.InvSlot;
import com.denfop.items.modules.ItemEntityModule;
import com.denfop.network.DecoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.CapturedMobUtils;
import com.denfop.utils.ModUtils;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class TileEntityShield extends TileEntityInventory implements IUpdatableTileEvent {

    final int latitudeSegments = 16;
    final int longitudeSegments = 16;
    private final InvSlot slot;
    public boolean visibleShield = false;
    public boolean visibleLaser = false;
    Energy energy;
    List<Integer> integerList = new LinkedList<>();
    AxisAlignedBB shieldBox;
    AxisAlignedBB shieldDefaultBox = new AxisAlignedBB(-8, -8, -8, 8, 8, 8);
    Vec3d center;
    LinkedList<Chunk> chunks = new LinkedList<>();
    List<UUID> uuidList = new LinkedList<>();
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
    @SideOnly(Side.CLIENT)
    private Function render;
    private double laserProgress;
    private byte mode = 0;
    private long lastShotTime = 0;
    private boolean isShooting = false;
    public TileEntityShield() {
        energy = this.addComponent(Energy.asBasicSink(this, 10000, 14));
        slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 9) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                if (!(stack.getItem() instanceof ItemEntityModule)) {
                    return false;
                }
                if (stack.getItemDamage() == 0) {
                    return false;
                }


                return CapturedMobUtils.containsSoul(stack);
            }

            @Override
            public void update() {
                super.update();
                integerList.clear();
                for (int i = 0; i < this.size(); i++) {
                    if (!this.get(i).isEmpty()) {
                        final CapturedMobUtils captured = CapturedMobUtils.create(this.get(i));
                        assert captured != null;
                        EntityLiving entityLiving = (EntityLiving) captured.getEntity(((TileEntityShield) base).getWorld(), true);
                        integerList.add(entityLiving.getEntityId());
                    }
                }
            }
        };

    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("VisibleShield", this.visibleShield);
        compound.setBoolean("VisibleLaser", this.visibleLaser);
        compound.setByte("Mode", this.mode);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.visibleShield = compound.getBoolean("VisibleShield");
        this.visibleLaser = compound.getBoolean("VisibleLaser");
        this.mode = compound.getByte("Mode");
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.slot.update();
        shieldBox = new AxisAlignedBB(this.getPos().add(-8, -8, -8), this.getPos().add(8, 8, 8));
        center = new Vec3d(this.getPos()).add(new Vec3d(0, 0.5, 0));
        int j2 = MathHelper.floor((shieldBox.minX - 2) / 16.0D);
        int k2 = MathHelper.ceil((shieldBox.maxX + 2) / 16.0D);
        int l2 = MathHelper.floor((shieldBox.minZ - 2) / 16.0D);
        int i3 = MathHelper.ceil((shieldBox.maxZ + 2) / 16.0D);
        for (int j3 = j2; j3 < k2; ++j3) {
            for (int k3 = l2; k3 < i3; ++k3) {
                final Chunk chunk = world.getChunkFromChunkCoords(j3, k3);
                if (!chunks.contains(chunk)) {
                    chunks.add(chunk);
                }
            }
        }
        if (this.getWorld().isRemote) {
            this.render = createFunction();
            GlobalRenderManager.addRender(this.getWorld(), pos, render);
        }
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (this.getWorld().isRemote) {
            GlobalRenderManager.removeRender(world, pos);
        }
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("uuid")) {
            try {
                UUID uuid = (UUID) DecoderHandler.decode(is);
                uuidList.add(uuid);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("visibleShield")) {
            try {
                visibleShield = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("visibleLaser")) {
            try {
                visibleLaser = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("mode")) {
            try {
                mode = (byte) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Function createFunction() {
        Function function = o -> {
            if (shieldBox == null) {
                shieldBox = new AxisAlignedBB(this.getPos().add(-8, -8, -8), this.getPos().add(8, 8, 8));
                center = new Vec3d(this.getPos()).add(new Vec3d(0, 0.5, 0));
                int j2 = MathHelper.floor((shieldBox.minX - 2) / 16.0D);
                int k2 = MathHelper.ceil((shieldBox.maxX + 2) / 16.0D);
                int l2 = MathHelper.floor((shieldBox.minZ - 2) / 16.0D);
                int i3 = MathHelper.ceil((shieldBox.maxZ + 2) / 16.0D);
                for (int j3 = j2; j3 < k2; ++j3) {
                    for (int k3 = l2; k3 < i3; ++k3) {
                        final Chunk chunk = world.getChunkFromChunkCoords(j3, k3);
                        if (!chunks.contains(chunk)) {
                            chunks.add(chunk);
                        }
                    }
                }
            }


            List<Entity> mobs = this.getEntitiesWithinAABB(Entity.class, shieldBox,
                    e -> {
                        boolean hasmob = (e instanceof EntityMob);
                        if (mode == 0) {
                            return hasmob && !integerList.contains(e.getEntityId());
                        } else {
                            return hasmob && integerList.contains(e.getEntityId());
                        }
                    }
            );

            boolean mobNearby = !mobs.isEmpty();


            int shieldColor = mobNearby ? ModUtils.convertRGBcolorToInt(168, 0, 0) : 0x0000FFFF;
            if (visibleShield) {
                renderShield(shieldBox, shieldColor);
            }


            if (mobNearby && visibleLaser) {
                mobs.forEach(this::renderLaserEffect);
            }
            uuidList.clear();
            return 0;
        };
        return function;
    }

    public <T extends Entity> List<T> getEntitiesWithinAABB(
            Class<? extends T> clazz,
            AxisAlignedBB aabb,
            @Nullable Predicate<? super T> filter
    ) {
        List<T> list = Lists.newArrayList();
        this.chunks.forEach(chunk -> chunk.getEntitiesOfTypeWithinAABB(clazz, aabb, list, filter));
        return list;
    }

    public InvSlot getSlot() {
        return slot;
    }

    public byte getMode() {
        return mode;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.energy.getEnergy() >= 25) {

            List<Entity> mobs = this.getEntitiesWithinAABB(Entity.class, shieldBox,
                    e -> {
                        boolean hasmob = (e instanceof EntityMob);
                        if (mode == 0) {
                            return hasmob && !integerList.contains(e.getEntityId());
                        } else {
                            return hasmob && integerList.contains(e.getEntityId());
                        }
                    }
            );

            boolean mobNearby = !mobs.isEmpty();
            if (mobNearby) {
                for (Entity entity : mobs) {
                    if (this.energy.getEnergy() >= 25) {
                        new PacketUpdateFieldTile(this, "uuid", entity.getUniqueID());
                        entity.attackEntityFrom(DamageSource.MAGIC, 4F);
                        this.energy.useEnergy(25);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();


    }

    @SideOnly(Side.CLIENT)
    private void renderShield(AxisAlignedBB box, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        Color color1 = new Color(color);
        GlStateManager.color(color1.getRed(), color1.getGreen(), color1.getBlue(), 0.5f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        if (!write) {
            writeData();
        }
        drawCircle(buffer, this.pos, tessellator);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
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

    @SideOnly(Side.CLIENT)
    private void drawCircle(BufferBuilder buffer, BlockPos pos, Tessellator tessellator) {


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

                buffer
                        .pos(x + x1 * shieldDefaultBox.maxX, y + y1 * shieldDefaultBox.maxY, z + z1 * shieldDefaultBox.maxZ)
                        .endVertex();
                buffer
                        .pos(x + x2 * shieldDefaultBox.maxX, y + y2 * shieldDefaultBox.maxY, z + z2 * shieldDefaultBox.maxZ)
                        .endVertex();
                buffer
                        .pos(x + x3 * shieldDefaultBox.maxX, y + y3 * shieldDefaultBox.maxY, z + z3 * shieldDefaultBox.maxZ)
                        .endVertex();

                buffer
                        .pos(x + x3 * shieldDefaultBox.maxX, y + y3 * shieldDefaultBox.maxY, z + z3 * shieldDefaultBox.maxZ)
                        .endVertex();
                buffer
                        .pos(x + x4 * shieldDefaultBox.maxX, y + y4 * shieldDefaultBox.maxY, z + z4 * shieldDefaultBox.maxZ)
                        .endVertex();
                buffer
                        .pos(x + x1 * shieldDefaultBox.maxX, y + y1 * shieldDefaultBox.maxY, z + z1 * shieldDefaultBox.maxZ)
                        .endVertex();
            }

        }

        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    private void renderLaserEffect(Entity entity) {

        Vec3d start = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        Vec3d end = entity.getPositionVector().addVector(0, entity.height / 2.0, 0);

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 0.0f, 0.0f, 0.8f);
        GlStateManager.glLineWidth(32.0f);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(start.x, start.y, start.z);
        GL11.glVertex3d(end.x, end.y, end.z);
        GL11.glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        if (uuidList.remove(entity.getUniqueID())) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShotTime > 1000) {
                lastShotTime = currentTime;
                isShooting = true;
                laserProgress = 0.0;

                Minecraft.getMinecraft().player.playSound(
                        SoundEvents.ENTITY_GENERIC_EXPLODE,
                        1.0f,
                        1.0f
                );
            }


            if (isShooting) {
                GlStateManager.pushMatrix();
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.color(1.0f, 0.5f, 0.0f, 1.0f);
                GlStateManager.glLineWidth(8.0f);


                double segmentLength = 0.5;
                Vec3d direction = end.subtract(start).normalize();
                double totalDistance = start.distanceTo(end);
                double currentDistance = laserProgress * totalDistance;
                Vec3d segmentStart = start.add(direction.scale(currentDistance));
                Vec3d segmentEnd = start.add(direction.scale(Math.min(currentDistance + segmentLength, totalDistance)));


                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex3d(segmentStart.x, segmentStart.y, segmentStart.z);
                GL11.glVertex3d(segmentEnd.x, segmentEnd.y, segmentEnd.z);
                GL11.glEnd();

                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();


                spawnLaserParticles(segmentStart, segmentEnd);


                laserProgress += 0.1;
                if (laserProgress >= 1.0) {
                    isShooting = false;
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnLaserParticles(Vec3d start, Vec3d end) {


        double step = 0.1;
        double distance = start.distanceTo(end);
        Vec3d direction = end.subtract(start).normalize();

        for (double i = 0; i < distance; i += step) {
            Vec3d particlePos = start.add(direction.scale(i));
            world.spawnParticle(
                    EnumParticleTypes.FLAME,
                    particlePos.x, particlePos.y, particlePos.z,
                    0.0, 0.0, 0.0
            );
            world.spawnParticle(
                    EnumParticleTypes.REDSTONE,
                    particlePos.x, particlePos.y, particlePos.z,
                    0.0, 0.0, 0.0
            );
        }
    }

    @Override
    public ContainerShield getGuiContainer(final EntityPlayer var1) {
        return new ContainerShield(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiShield(getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.shield;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (var2 == 0) {
            visibleLaser = true;
            new PacketUpdateFieldTile(this, "visibleLaser", visibleLaser);
        }
        if (var2 == 1) {
            visibleShield = true;

            new PacketUpdateFieldTile(this, "visibleShield", visibleShield);
        }
        if (var2 == -1) {
            visibleLaser = false;
            new PacketUpdateFieldTile(this, "visibleLaser", visibleLaser);
        }
        if (var2 == -2) {
            visibleShield = false;
            new PacketUpdateFieldTile(this, "visibleShield", visibleShield);
        }
        if (var2 == 2) {
            mode = 0;
            new PacketUpdateFieldTile(this, "mode", mode);
        }
        if (var2 == 3) {
            mode = 1;
            new PacketUpdateFieldTile(this, "mode", mode);
        }

    }

}
