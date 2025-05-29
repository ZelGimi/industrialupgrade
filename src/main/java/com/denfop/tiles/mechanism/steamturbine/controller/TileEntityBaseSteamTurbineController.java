package com.denfop.tiles.mechanism.steamturbine.controller;

import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.steam.EnumSteamPhase;
import com.denfop.api.steam.ISteamBlade;
import com.denfop.api.steam.Steam;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerBaseSteamTurbineController;
import com.denfop.gui.GuiBaseSteamTurbineController;
import com.denfop.gui.GuiCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.steam.SteamRod;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.tiles.mechanism.steamturbine.*;
import com.denfop.world.WorldBaseGen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.denfop.register.InitMultiBlockSystem.SteamTurbineMultiBlock;
import static com.denfop.render.windgenerator.WindGeneratorRenderer.rotorModels;

public class TileEntityBaseSteamTurbineController extends TileMultiBlockBase implements IController, IUpdatableTileEvent {

    private final int blocLevel;
    public List<IExchanger> listExchanger = new ArrayList<>();
    public List<ICoolant> listCoolant = new ArrayList<>();
    public ITank tankWater;
    public ITank tankSteam;
    public IPressure pressure;
    public List<IRod> iRodListMap = new ArrayList<>();
    public EnumSteamPhase enumSteamPhase = EnumSteamPhase.ONE;
    public EnumSteamPhase stableenumSteamPhase = EnumSteamPhase.ONE;
    public int phase = 0;
    public double generation;
    public double heat;
    public boolean work;
    public Steam steam;
    public ISocket energy;
    private long lastcheck;
    private float angle;

    public TileEntityBaseSteamTurbineController(int blocLevel, IMultiTileBlock tileBlock, BlockPos pos, BlockState state) {
        super(SteamTurbineMultiBlock, tileBlock, pos, state);
        this.blocLevel = blocLevel;
    }

    @Override
    public int getBlockLevel() {
        return blocLevel;
    }

    @Override
    public ISocket getEnergy() {
        return energy;
    }

    @Override
    public List<ICoolant> getCoolant() {
        return listCoolant;
    }

    @Override
    public List<IExchanger> getExchanger() {
        return listExchanger;
    }


    public float getAngle() {
        if (this.getWorld().dimension() != Level.OVERWORLD) {
            return 0;
        }
        if (this.work && this.phase > 0) {
            final long k = (System.currentTimeMillis() - this.lastcheck);
            this.angle += Math.max(0.025, (float) ((float) k * this.phase * this.enumSteamPhase.ordinal() / 500D));
            this.angle = this.angle % 360;
        }

        this.lastcheck = System.currentTimeMillis();
        return this.angle;
    }

    @Override
    public FluidTank getSteamFluid() {
        return tankSteam.getTank();
    }

    @Override
    public ContainerBaseSteamTurbineController getGuiContainer(final Player var1) {
        return new ContainerBaseSteamTurbineController(this, var1);
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.work);
        try {
            EncoderHandler.encode(customPacketBuffer, getSteamFluid());
            EncoderHandler.encode(customPacketBuffer, getWaterFluid());
            customPacketBuffer.writeBytes(energy.getEnergy().updateComponent());
            customPacketBuffer.writeInt(this.phase);
            customPacketBuffer.writeInt(this.enumSteamPhase.ordinal());
            customPacketBuffer.writeInt(this.stableenumSteamPhase.ordinal());
            customPacketBuffer.writeDouble(this.heat);
            customPacketBuffer.writeDouble(this.generation);
            customPacketBuffer.writeByte(this.listCoolant.size());
            for (ICoolant coolant : this.listCoolant) {
                EncoderHandler.encode(customPacketBuffer, coolant.getCoolant());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        work = customPacketBuffer.readBoolean();
        this.phase = customPacketBuffer.readInt();
        this.enumSteamPhase = EnumSteamPhase.values()[customPacketBuffer.readInt()];
        this.stableenumSteamPhase = EnumSteamPhase.values()[customPacketBuffer.readInt()];
        this.heat = customPacketBuffer.readDouble();
        this.generation = customPacketBuffer.readDouble();
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        customPacketBuffer.writeBoolean(work);
        customPacketBuffer.writeInt(this.phase);
        customPacketBuffer.writeInt(this.enumSteamPhase.ordinal());
        customPacketBuffer.writeInt(this.stableenumSteamPhase.ordinal());
        customPacketBuffer.writeDouble(this.heat);
        customPacketBuffer.writeDouble(this.generation);
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        work = customPacketBuffer.readBoolean();
        if (!this.isFull()) {
            this.updateFull();
        }
        try {
            FluidTank fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank2 != null) {
                getSteamFluid().readFromNBT(fluidTank2.writeToNBT(new CompoundTag()));
            }
            fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank2 != null) {
                getWaterFluid().readFromNBT(fluidTank2.writeToNBT(new CompoundTag()));
            }
            this.energy.getEnergy().onNetworkUpdate(customPacketBuffer);
            this.phase = customPacketBuffer.readInt();
            this.enumSteamPhase = EnumSteamPhase.values()[customPacketBuffer.readInt()];
            this.stableenumSteamPhase = EnumSteamPhase.values()[customPacketBuffer.readInt()];
            this.heat = customPacketBuffer.readDouble();
            this.generation = customPacketBuffer.readDouble();
            int col = customPacketBuffer.readByte();
            for (int i = 0; i < col; i++) {
                ICoolant coolant = listCoolant.get(i);
                fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
                if (fluidTank2 != null) {
                    coolant.getCoolant().readFromNBT(fluidTank2.writeToNBT(new CompoundTag()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiBaseSteamTurbineController((ContainerBaseSteamTurbineController) menu);
    }

    @Override
    public FluidTank getWaterFluid() {
        return tankWater.getTank();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(final TileMultiBlockBase tileEntityMultiBlockBase, RenderLevelStageEvent event) {
        super.render(tileEntityMultiBlockBase, event);
        if (this.isFull()) {
            int diameter = 2;
            BlockPos prev = pos;
            EntityModel model = rotorModels.get(diameter);
            if (model == null) {
                model = new SteamRod(diameter);
                rotorModels.put(diameter, model);
            }
            if (!(model instanceof SteamRod)) {
                model = new SteamRod(diameter);
                rotorModels.replace(diameter, model);
            }
            if (this.iRodListMap.isEmpty()) {
                final List<BlockPos> pos1 = this
                        .getMultiBlockStucture()
                        .getPosFromClass(this.getFacing(), this.getBlockPos(),
                                IRod.class
                        );
                for (BlockPos pos2 : pos1) {
                    iRodListMap.add((IRod) this.getWorld().getBlockEntity(pos2));
                }
            }
            PoseStack poseStack = event.getPoseStack();
            for (IRod rod : this.iRodListMap) {
                BlockPos pos = rod.getBlockPos();
                poseStack.pushPose();
                float angle = this.getAngle();
                poseStack.translate(pos.getX() - prev.getX(), pos.getY() - prev.getY(), pos.getZ() - prev.getZ());
                Direction facing = this.getFacing();

                switch (facing) {
                    case NORTH -> {
                        poseStack.translate(0F, 0.5F, 0F);
                        if (facing.getAxis() == Direction.Axis.X)
                            poseStack.mulPose(Axis.XP.rotationDegrees(angle));
                        else if (facing.getAxis() == Direction.Axis.Z)
                            poseStack.mulPose(Axis.ZP.rotationDegrees(angle));

                        poseStack.mulPose(Axis.YP.rotationDegrees(-90f));
                    }
                    case EAST -> {
                        poseStack.translate(0F, 0.5F, 0F);
                        if (facing.getAxis() == Direction.Axis.X)
                            poseStack.mulPose(Axis.XP.rotationDegrees(angle));
                    }
                    case SOUTH -> {
                        poseStack.translate(0F, 0.5F, 0F);
                        if (facing.getAxis() == Direction.Axis.X)
                            poseStack.mulPose(Axis.XP.rotationDegrees(angle));
                        else if (facing.getAxis() == Direction.Axis.Z)
                            poseStack.mulPose(Axis.ZP.rotationDegrees(angle));

                        poseStack.mulPose(Axis.YP.rotationDegrees(-90f));
                    }
                    case WEST -> {
                        poseStack.translate(0.25F, 0.5F, 0F);
                        if (facing.getAxis() == Direction.Axis.X)
                            poseStack.mulPose(Axis.XP.rotationDegrees(angle));
                        else if (facing.getAxis() == Direction.Axis.Z)
                            poseStack.mulPose(Axis.ZP.rotationDegrees(angle));
                    }
                }


                for (ISteamBlade steamBlade : rod.getRods()) {
                    ResourceLocation rotorRL = steamBlade.getTexture();
                    GuiCore.bindTexture(rotorRL);
                    VertexConsumer consumer = Minecraft.getInstance()
                            .renderBuffers()
                            .bufferSource()
                            .getBuffer(RenderType.entityCutout(rotorRL));
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    int packedLight = event.getLevelRenderer().getLightColor(getLevel(), pos);
                    if (facing == Direction.EAST || facing == Direction.WEST) {
                        if (facing.getAxis() == Direction.Axis.X) {
                            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                        } else if (facing.getAxis() == Direction.Axis.Z) {
                            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                        }
                    } else {
                        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    }

                    model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                }
                poseStack.popPose();
            }
        }
    }

    @Override
    public EnumSteamPhase getSteamPhase() {
        return enumSteamPhase;
    }

    @Override
    public int getPhase() {
        return phase;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.full && this.work) {
            if (this.getWorld().getGameTime() % 20 == 0) {
                if (this.steam.getCoef() > 0) {
                    this.steam.onTick();

                    for (IExchanger exchanger : this.listExchanger) {
                        if (exchanger.getExchanger() == null) {
                            continue;
                        }
                        this.removeHeat(exchanger.getPower());
                        boolean update = exchanger.getExchanger().damageItem(exchanger.getSlot().get(0), -1);
                        if (update) {
                            exchanger.getSlot().set(0, ItemStack.EMPTY);
                        }
                    }
                }
                if (this.getWorld().getGameTime() % 120 == 0) {
                    this.removePhase(WorldBaseGen.random.nextInt(this.blocLevel + 2));
                }

                this.steam.updateData();
            }

            this.generation = steam.getGeneration();
            this.energy.getEnergy().addEnergy(generation);
            energy.getEnergy().setSourceTier(EnergyNetGlobal.instance.getTierFromPower(generation));
        } else {
            generation = 0;
            if (this.getWorld().getGameTime() % 60 == 0) {
                this.removePhase(WorldBaseGen.random.nextInt(this.blocLevel + 2));
            }
        }
    }

    @Override
    public void addPhase(final int phase) {
        final int oldphase = this.phase;
        this.phase += phase;

        if (oldphase != this.phase) {
            new PacketUpdateFieldTile(this, "phase", this.phase);
        }
        if (this.enumSteamPhase == this.stableenumSteamPhase && this.phase > this.enumSteamPhase.getMax()) {
            this.phase = this.enumSteamPhase.getMax();
        }
        if (this.phase < this.enumSteamPhase.getMin() || this.phase > this.enumSteamPhase.getMax()) {
            final EnumSteamPhase old = enumSteamPhase;
            recalculatePhase();
            if (old != enumSteamPhase) {
                new PacketUpdateFieldTile(this, "enumSteamPhase", enumSteamPhase.ordinal());
            }
        }
        if (this.phase > this.enumSteamPhase.getMax()) {
            this.phase = this.enumSteamPhase.getMax();
        }

    }

    private void recalculatePhase() {
        for (EnumSteamPhase enumSteamPhase1 : EnumSteamPhase.values()) {
            if (this.phase >= enumSteamPhase1.getMin() && this.phase <= enumSteamPhase1.getMax()) {
                this.enumSteamPhase = enumSteamPhase1;
                break;
            }
        }
    }

    @Override
    public void removePhase(final int phase) {
        final int oldphase = this.phase;
        this.phase -= phase;
        if (this.phase < 0) {
            this.phase = 0;
        }
        if (oldphase != this.phase) {
            new PacketUpdateFieldTile(this, "phase", this.phase);
        }
        if (this.phase < this.enumSteamPhase.getMin() || this.phase > this.enumSteamPhase.getMax()) {
            final EnumSteamPhase old = enumSteamPhase;
            recalculatePhase();
            if (old != enumSteamPhase) {
                new PacketUpdateFieldTile(this, "enumSteamPhase", enumSteamPhase.ordinal());
            }
        }

    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.trim().equals("phase")) {
            try {
                this.phase = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.trim().equals("enumSteamPhase")) {
            try {
                this.enumSteamPhase = EnumSteamPhase.values()[(int) DecoderHandler.decode(is)];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        nbttagcompound = super.writeToNBT(nbttagcompound);
        nbttagcompound.putBoolean("work", work);
        nbttagcompound.putByte("enumSteamPhase", (byte) enumSteamPhase.ordinal());
        nbttagcompound.putByte("stableenumSteamPhase", (byte) stableenumSteamPhase.ordinal());
        nbttagcompound.putShort("phase", (short) phase);
        return nbttagcompound;
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        work = nbttagcompound.getBoolean("work");
        enumSteamPhase = EnumSteamPhase.values()[nbttagcompound.getByte("enumSteamPhase")];
        stableenumSteamPhase = EnumSteamPhase.values()[nbttagcompound.getByte("stableenumSteamPhase")];
        phase = nbttagcompound.getShort("phase");
    }

    @Override
    public void updateInfo() {
        iRodListMap.forEach(IRod::updateBlades);


    }

    @Override
    public int getPressure() {
        return pressure.getPressure();
    }

    @Override
    public List<com.denfop.tiles.mechanism.steamturbine.IRod> getInfo() {
        return iRodListMap;
    }

    @Override
    public boolean isWork() {
        return this.work;
    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            this.iRodListMap.clear();
            this.listCoolant.clear();
            this.listExchanger.clear();
            this.pressure = null;
            this.tankWater = null;
            this.tankSteam = null;
            this.energy = null;
        }
    }

    @Override
    public void setGeneration(final double generation) {
        this.generation = generation;
    }

    @Override
    public double getHeat() {
        return this.heat;
    }

    @Override
    public void removeHeat(final double heat) {
        this.heat -= heat;
        if (this.heat < 0) {
            this.heat = 0;
        }
    }

    @Override
    public void addHeat(final double heat) {
        this.heat += heat;
        if (this.heat > this.getMaxHeat()) {
            this.heat = this.getMaxHeat();
        }
    }

    @Override
    public double getMaxHeat() {
        return 500;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            work = !work;
        } else if (var2 == 1) {

            final EnumSteamPhase[] values = EnumSteamPhase.values();
            this.stableenumSteamPhase = values[(this.stableenumSteamPhase.ordinal() + 1) % values.length];
        } else {
            final EnumSteamPhase[] values = EnumSteamPhase.values();
            this.stableenumSteamPhase = values[Math.max(0, (this.stableenumSteamPhase.ordinal() - 1))];
        }
    }

    public EnumSteamPhase getStableSteamPhase() {
        return stableenumSteamPhase;
    }

    @Override
    public void updateAfterAssembly() {


        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IPressure.class
                );
        this.pressure = (IPressure) this.getWorld().getBlockEntity(pos1.get(0));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISocket.class
                );
        this.energy = (ISocket) this.getWorld().getBlockEntity(pos1.get(0));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ITank.class
                );
        this.tankWater = (ITank) this.getWorld().getBlockEntity(pos1.get(0));
        tankWater.setWaterTank();
        tankWater.clear(false);
        this.tankSteam = (ITank) this.getWorld().getBlockEntity(pos1.get(1));
        tankSteam.setSteamTank();
        tankSteam.clear(true);
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ICoolant.class
                );
        for (BlockPos pos2 : pos1) {
            listCoolant.add((ICoolant) this.getWorld().getBlockEntity(pos2));
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IExchanger.class
                );
        for (BlockPos pos2 : pos1) {
            listExchanger.add((IExchanger) this.getWorld().getBlockEntity(pos2));
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IRod.class
                );
        for (BlockPos pos2 : pos1) {
            iRodListMap.add((IRod) this.getWorld().getBlockEntity(pos2));
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IControllerRod.class
                );
        final IControllerRod controllerRod = (IControllerRod) this.getWorld().getBlockEntity(pos1.get(0));
        controllerRod.setList(iRodListMap);
        this.steam = new Steam(this);
    }

    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        CustomPacketBuffer customPacketBuffer = super.writeUpdatePacket();
        customPacketBuffer.writeBoolean(isFull());
        if (isFull()) {
            for (IRod rod : iRodListMap) {
                customPacketBuffer.writeBlockPos(rod.getBlockPos());
                customPacketBuffer.writeBytes(((TileEntityMultiBlockElement)rod).writePacket());
            }
        }
        return customPacketBuffer;
    }

    @Override
    public void readUpdatePacket(CustomPacketBuffer packetBuffer) {
        super.readUpdatePacket(packetBuffer);
        boolean isFull = packetBuffer.readBoolean();
        if (isFull){
            iRodListMap.clear();
            for (int i = 0;i < 9;i++){
                TileEntityMultiBlockElement multiBlockElement = (TileEntityMultiBlockElement) this.getWorld().getBlockEntity(packetBuffer.readBlockPos());
                packetBuffer.readShort();
                multiBlockElement.readPacket(packetBuffer);
                iRodListMap.add((IRod)multiBlockElement);
            }
            List<BlockPos> pos1 = this
                    .getMultiBlockStucture()
                    .getPosFromClass(this.getFacing(), this.getBlockPos(),
                            IControllerRod.class
                    );
            final IControllerRod controllerRod = (IControllerRod) this.getWorld().getBlockEntity(pos1.get(0));
            controllerRod.setList(iRodListMap);
        }
    }

    @Override
    public void usingBeforeGUI() {

    }

}
