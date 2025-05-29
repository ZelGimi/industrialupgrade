package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerShield;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.gui.GuiCore;
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
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static com.denfop.render.base.RenderType.LEASH_TRANSPARENT;

public class TileEntityShield extends TileEntityInventory implements IUpdatableTileEvent {

    final int latitudeSegments = 16;
    final int longitudeSegments = 16;
    private final InvSlot slot;
    public boolean visibleShield = false;
    public boolean visibleLaser = false;
    Energy energy;
    List<Integer> integerList = new LinkedList<>();
    AABB shieldBox;
    AABB shieldDefaultBox = new AABB(-8, -8, -8, 8, 8, 8);
    Vec3i center;
    LinkedList<LevelChunk> chunks = new LinkedList<>();
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
    @OnlyIn(Dist.CLIENT)
    private  Function<RenderLevelStageEvent, Void> render;
    private double laserProgress;
    private byte mode = 0;
    private long lastShotTime = 0;
    private boolean isShooting = false;
    public TileEntityShield(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.shield,pos,state);
        energy = this.addComponent(Energy.asBasicSink(this, 10000, 14));
        slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 9) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                if (!(stack.getItem() instanceof ItemEntityModule)) {
                    return false;
                }
                if (((ItemEntityModule<?>) stack.getItem()).getElement().getId() == 0) {
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
                        LivingEntity entityLiving = (LivingEntity) captured.getEntity(((TileEntityShield) base).getWorld(), true);
                        integerList.add(entityLiving.getId());
                    }
                }
            }
        };

    }



    @Override
    public CompoundTag writeToNBT(CompoundTag compound) {
        super.writeToNBT(compound);
        compound.putBoolean("VisibleShield", this.visibleShield);
        compound.putBoolean("VisibleLaser", this.visibleLaser);
        compound.putByte("Mode", this.mode);

        return compound;
    }

    @Override
    public void readFromNBT(CompoundTag compound) {
        super.readFromNBT(compound);
        this.visibleShield = compound.getBoolean("VisibleShield");
        this.visibleLaser = compound.getBoolean("VisibleLaser");
        this.mode = compound.getByte("Mode");
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.slot.update();
        shieldBox = new AABB(this.getPos().offset(-8, -8, -8), this.getPos().offset(8, 8, 8));
        center = this.getPos().offset(new Vec3i(0, 0.5, 0));
        int j2 = Mth.floor((shieldBox.minX - 2) / 16.0D);
        int k2 = Mth.ceil((shieldBox.maxX + 2) / 16.0D);
        int l2 = Mth.floor((shieldBox.minZ - 2) / 16.0D);
        int i3 = Mth.ceil((shieldBox.maxZ + 2) / 16.0D);
        for (int j3 = j2; j3 < k2; ++j3) {
            for (int k3 = l2; k3 < i3; ++k3) {
                final LevelChunk chunk = level.getChunk(j3, k3);
                if (!chunks.contains(chunk)) {
                    chunks.add(chunk);
                }
            }
        }
        if (this.getWorld().isClientSide) {
            this.render = createFunction();
            GlobalRenderManager.addRender(this.getWorld(), pos, render);
        }
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (this.getWorld().isClientSide) {
            GlobalRenderManager.removeRender(level, pos);
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

    private  Function<RenderLevelStageEvent, Void> createFunction() {
        Function<RenderLevelStageEvent, Void> function = o -> {
            if (shieldBox == null) {
                shieldBox = new AABB(this.getPos().offset(-8, -8, -8), this.getPos().offset(8, 8, 8));
                center = this.getPos().offset(new Vec3i(0, 0.5, 0));
                int j2 = Mth.floor((shieldBox.minX - 2) / 16.0D);
                int k2 = Mth.ceil((shieldBox.maxX + 2) / 16.0D);
                int l2 = Mth.floor((shieldBox.minZ - 2) / 16.0D);
                int i3 = Mth.ceil((shieldBox.maxZ + 2) / 16.0D);
                for (int j3 = j2; j3 < k2; ++j3) {
                    for (int k3 = l2; k3 < i3; ++k3) {
                        final LevelChunk chunk = level.getChunk(j3, k3);
                        if (!chunks.contains(chunk)) {
                            chunks.add(chunk);
                        }
                    }
                }
            }


            List<Entity> mobs = level.getEntitiesOfClass(Entity.class, shieldBox,
                    e -> {
                        boolean hasmob = (e instanceof Mob);
                        if (mode == 0) {
                            return hasmob && !integerList.contains(e.getId());
                        } else {
                            return hasmob && integerList.contains(e.getId());
                        }
                    }
            );

            boolean mobNearby = !mobs.isEmpty();


            int shieldColor = mobNearby ? ModUtils.convertRGBcolorToInt(168, 0, 0) : 0x0000FFFF;
            if (visibleShield) {
                renderShield(o, shieldBox, shieldColor);
            }


           /* if (mobNearby && visibleLaser) {
                mobs.forEach(mob -> renderLaserEffect(o,mob));
            }*/
            uuidList.clear();
            return null;
        };
        return function;
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

            List<Entity> mobs = level.getEntitiesOfClass(Entity.class, shieldBox,
                    e -> {
                        boolean hasmob = (e instanceof Mob);
                        if (mode == 0) {
                            return hasmob && !integerList.contains(e.getId());
                        } else {
                            return hasmob && integerList.contains(e.getId());
                        }
                    }
            );

            boolean mobNearby = !mobs.isEmpty();
            if (mobNearby) {
                for (Entity entity : mobs) {
                    if (this.energy.getEnergy() >= 25) {
                        new PacketUpdateFieldTile(this, "uuid", entity.getUUID());
                        entity.hurt(DamageSource.MAGIC, 4F);
                        this.energy.useEnergy(25);
                    } else {
                        break;
                    }
                }
            }
        }
    }



    @OnlyIn(Dist.CLIENT)
    private void renderShield(RenderLevelStageEvent event, AABB box, int color) {

        double camX = event.getCamera().getPosition().x();
        double camY = event.getCamera().getPosition().y();
        double camZ = event.getCamera().getPosition().z();
        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        VertexConsumer bufferSource = Minecraft.getInstance()
                .renderBuffers()
                .bufferSource()
                .getBuffer(LEASH_TRANSPARENT);
        Color color1 = new Color(color);
        if (!write) {
            writeData();
        }

        drawCircle(poseStack, bufferSource, pos, color1.getRed() / 255F, color1.getGreen()/ 255F, color1.getBlue()/ 255F, 0.5F);



        poseStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
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
    @OnlyIn(Dist.CLIENT)
    private void drawCircle(PoseStack poseStack, VertexConsumer bufferSource, BlockPos pos, float r, float g, float b, float alpha) {

        float x = pos.getX() + 0.5F;
        float y = pos.getY() + 0.5F;
        float z = pos.getZ() + 0.5F;
        float radius =8F;
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

               bufferSource.vertex(matrix, (float) (x + x1 * radius), (float) (y + y1 * radius), (float) (z + z1 * radius)).color(r, g, b, 0.5f).endVertex();
                bufferSource.vertex(matrix, (float) (x + x2 * radius), (float) (y + y2 * radius), (float) (z + z2 * radius)).color(r, g, b, 0.5f).endVertex();
                bufferSource.vertex(matrix, (float) (x + x3 * radius), (float) (y + y3 * radius), (float) (z + z3 * radius)).color(r, g, b, 0.5f).endVertex();

                bufferSource.vertex(matrix, (float) (x + x3 * radius), (float) (y + y3 * radius), (float) (z + z3 * radius)).color(r, g, b, 0.5f).endVertex();
                bufferSource.vertex(matrix, (float) (x + x4 * radius), (float) (y + y4 * radius), (float) (z + z4 * radius)).color(r, g, b, 0.5f).endVertex();
                bufferSource.vertex(matrix, (float) (x + x1 * radius), (float) (y + y1 * radius), (float) (z + z1 * radius)).color(r, g, b, 0.5f).endVertex();
            }

        }
    }




    @Override
    public ContainerShield getGuiContainer(final Player var1) {
        return new ContainerShield(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiShield((ContainerShield) menu);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.shield;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
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
