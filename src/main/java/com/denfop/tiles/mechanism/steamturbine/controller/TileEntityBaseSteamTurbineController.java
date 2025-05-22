package com.denfop.tiles.mechanism.steamturbine.controller;

import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.steam.EnumSteamPhase;
import com.denfop.api.steam.ISteamBlade;
import com.denfop.api.steam.Steam;
import com.denfop.container.ContainerBaseSteamTurbineController;
import com.denfop.gui.GuiBaseSteamTurbineController;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.steam.SteamRod;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.tiles.mechanism.steamturbine.IController;
import com.denfop.tiles.mechanism.steamturbine.IControllerRod;
import com.denfop.tiles.mechanism.steamturbine.ICoolant;
import com.denfop.tiles.mechanism.steamturbine.IExchanger;
import com.denfop.tiles.mechanism.steamturbine.IPressure;
import com.denfop.tiles.mechanism.steamturbine.IRod;
import com.denfop.tiles.mechanism.steamturbine.ISocket;
import com.denfop.tiles.mechanism.steamturbine.ITank;
import com.denfop.world.WorldBaseGen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.denfop.register.InitMultiBlockSystem.SteamTurbineMultiBlock;
import static com.denfop.render.windgenerator.WindGeneratorRenderer.rotorModels;

public class TileEntityBaseSteamTurbineController extends TileMultiBlockBase implements IController, IUpdatableTileEvent {

    private final int level;
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

    public TileEntityBaseSteamTurbineController(int level) {
        super(SteamTurbineMultiBlock);
        this.level = level;
    }

    @Override
    public int getBlockLevel() {
        return level;
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
        if (this.getWorld().provider.getDimension() != 0) {
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
    public ContainerBaseSteamTurbineController getGuiContainer(final EntityPlayer var1) {
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
                getSteamFluid().readFromNBT(fluidTank2.writeToNBT(new NBTTagCompound()));
            }
            fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank2 != null) {
                getWaterFluid().readFromNBT(fluidTank2.writeToNBT(new NBTTagCompound()));
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
                    coolant.getCoolant().readFromNBT(fluidTank2.writeToNBT(new NBTTagCompound()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiBaseSteamTurbineController getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiBaseSteamTurbineController(getGuiContainer(var1));
    }

    @Override
    public FluidTank getWaterFluid() {
        return tankWater.getTank();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(final TileMultiBlockBase tileEntityMultiBlockBase) {
        super.render(tileEntityMultiBlockBase);
        if (this.isFull()) {
            int diameter = 2;
            BlockPos prev = pos;
            ModelBase model = rotorModels.get(diameter);
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
                    iRodListMap.add((IRod) this.getWorld().getTileEntity(pos2));
                }
            }
            for (IRod rod : this.iRodListMap) {
                BlockPos pos = rod.getBlockPos();
                GlStateManager.pushMatrix();
                float angle = this.getAngle();
                GlStateManager.translate(pos.getX() - prev.getX(), pos.getY() - prev.getY(), pos.getZ() - prev.getZ());
                EnumFacing facing = this.getFacing();
                switch (facing) {
                    case NORTH:
                        GlStateManager.translate(0F, 0.5F, 0F);
                        GL11.glRotatef(angle, facing.getAxis() == EnumFacing.Axis.X ? 1 : 0, 0,
                                facing.getAxis() == EnumFacing.Axis.Z ? 1 : 0
                        );
                        GL11.glRotatef(-90f, 0, 1, 0);
                        break;
                    case EAST:
                        GlStateManager.translate(0F, 0.5F, 0);
                        GL11.glRotatef(angle, facing.getAxis() == EnumFacing.Axis.X ? 1 : 0, 0,
                                0
                        );
                        break;
                    case SOUTH:
                        GlStateManager.translate(0F, 0.5F, 0);
                        GL11.glRotatef(angle, facing.getAxis() == EnumFacing.Axis.X ? 1 : 0, 0,
                                facing.getAxis() == EnumFacing.Axis.Z ? 1 : 0
                        );
                        GL11.glRotatef(-90f, 0, 1, 0);

                        break;
                    case WEST:
                        GlStateManager.translate(0.25F, 0.5F, 0);
                        GL11.glRotatef(angle, facing.getAxis() == EnumFacing.Axis.X ? 1 : 0, 0,
                                facing.getAxis() == EnumFacing.Axis.Z ? 1 : 0
                        );
                        break;
                }


                for (ISteamBlade steamBlade : rod.getRods()) {
                    ResourceLocation rotorRL = steamBlade.getTexture();
                    int light = world.getCombinedLight(pos, 0);
                    int blockLight = light % 65536;
                    int skyLight = light / 65536;
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) blockLight, (float) skyLight);
                    if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
                        GL11.glRotatef((90.0F) % 360, facing.getAxis() == EnumFacing.Axis.X ? 1 : 0, 0,
                                facing.getAxis() == EnumFacing.Axis.Z ? 1 : 0
                        );
                    } else {

                        GL11.glRotatef(90.0F, 1, 0, 0);
                        ;

                    }
                    Minecraft.getMinecraft().getTextureManager().bindTexture(rotorRL);
                    model.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, (float) (0.0625F));
                }
                GlStateManager.popMatrix();
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
            if (this.getWorld().provider.getWorldTime() % 20 == 0) {
                if (this.steam.getCoef() > 0) {
                    this.steam.onTick();

                    for (IExchanger exchanger : this.listExchanger) {
                        if (exchanger.getExchanger() == null) {
                            continue;
                        }
                        this.removeHeat(exchanger.getPower());
                        boolean update = exchanger.getExchanger().damageItem(exchanger.getSlot().get(), -1);
                        if (update) {
                            exchanger.getSlot().put(0, ItemStack.EMPTY);
                        }
                    }
                }
                if (this.getWorld().provider.getWorldTime() % 120 == 0) {
                    this.removePhase(WorldBaseGen.random.nextInt(this.level + 2));
                }

                this.steam.updateData();
            }

            this.generation = steam.getGeneration();
            this.energy.getEnergy().addEnergy(generation);
            energy.getEnergy().setSourceTier(EnergyNetGlobal.instance.getTierFromPower(generation));
        } else {
            generation = 0;
            if (this.getWorld().provider.getWorldTime() % 60 == 0) {
                this.removePhase(WorldBaseGen.random.nextInt(this.level + 2));
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
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound = super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("work", work);
        nbttagcompound.setByte("enumSteamPhase", (byte) enumSteamPhase.ordinal());
        nbttagcompound.setByte("stableenumSteamPhase", (byte) stableenumSteamPhase.ordinal());
        nbttagcompound.setShort("phase", (short) phase);
        return nbttagcompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
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
    public void updateTileServer(final EntityPlayer var1, final double var2) {
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
        this.pressure = (IPressure) this.getWorld().getTileEntity(pos1.get(0));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISocket.class
                );
        this.energy = (ISocket) this.getWorld().getTileEntity(pos1.get(0));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ITank.class
                );
        this.tankWater = (ITank) this.getWorld().getTileEntity(pos1.get(0));
        tankWater.setWaterTank();
        tankWater.clear(false);
        this.tankSteam = (ITank) this.getWorld().getTileEntity(pos1.get(1));
        tankSteam.setSteamTank();
        tankSteam.clear(true);
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ICoolant.class
                );
        for (BlockPos pos2 : pos1) {
            listCoolant.add((ICoolant) this.getWorld().getTileEntity(pos2));
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IExchanger.class
                );
        for (BlockPos pos2 : pos1) {
            listExchanger.add((IExchanger) this.getWorld().getTileEntity(pos2));
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IRod.class
                );
        for (BlockPos pos2 : pos1) {
            iRodListMap.add((IRod) this.getWorld().getTileEntity(pos2));
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IControllerRod.class
                );
        final IControllerRod controllerRod = (IControllerRod) this.getWorld().getTileEntity(pos1.get(0));
        controllerRod.setList(iRodListMap);
        this.steam = new Steam(this);
    }

    @Override
    public void usingBeforeGUI() {

    }

}
