package com.denfop.tiles.windturbine;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.api.windsystem.EnumRotorSide;
import com.denfop.api.windsystem.EnumTypeWind;
import com.denfop.api.windsystem.EnumWindSide;
import com.denfop.api.windsystem.IWindMechanism;
import com.denfop.api.windsystem.IWindRotor;
import com.denfop.api.windsystem.InvSlotWindTurbineRotor;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.api.windsystem.event.WindGeneratorEvent;
import com.denfop.api.windsystem.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.windsystem.upgrade.IRotorUpgradeItem;
import com.denfop.api.windsystem.upgrade.RotorUpgradeItemInform;
import com.denfop.api.windsystem.upgrade.RotorUpgradeSystem;
import com.denfop.api.windsystem.upgrade.event.EventRotorItemLoad;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWindTurbine;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerWindTurbine;
import com.denfop.gui.GuiWindTurbine;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotTurbineRotorBlades;
import com.denfop.items.ItemWindRod;
import com.denfop.items.reactors.ItemDamage;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.render.windgenerator.RotorModel;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.utils.DamageHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

import static com.denfop.render.windgenerator.WindGeneratorRenderer.rotorModels;

public class TileEntityWindTurbineController extends TileMultiBlockBase implements IWindMechanism, IType,
        IUpdatableTileEvent {


    public ISocket energy;
    public final InvSlotTurbineRotorBlades slot_blades;
    private final EnumLevelGenerators levelGenerators;
    public InvSlotWindTurbineRotor slot;
    public double generation = 0;
    public boolean need_repair;
    public int mind_wind;
    public int coefficient_power = 100;
    public boolean can_repair;
    public int mind_speed;
    public int timers;
    public double wind_speed;
    public EnumWindSide wind_side;
    public EnumTypeWind enumTypeWind;
    boolean space = false;
    private int tick;
    private double addition_power;
    private double addition_efficient;
    private int addition_strength;
    private double coefficient;
    private EnumRotorSide rotorSide;
    private float speed;
    private float angle;
    private long lastcheck;
    private boolean work = true;
    private int time;
    private boolean can_work = true;

    public TileEntityWindTurbineController() {
        super(InitMultiBlockSystem.WindTurbineMultiBlock);
        this.levelGenerators = EnumLevelGenerators.FOUR;
        this.slot = new InvSlotWindTurbineRotor(this);
        this.slot_blades = new InvSlotTurbineRotorBlades(this);

        this.addition_power = 0;
        this.addition_efficient = 0;
        this.addition_strength = 0;
        this.tick = 0;

    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWindTurbine.wind_turbine_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.windTurbine;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            coefficient = (double) DecoderHandler.decode(customPacketBuffer);
            speed = (float) DecoderHandler.decode(customPacketBuffer);
            slot.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new NBTTagCompound()));
            rotorSide = EnumRotorSide.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            generation = (double) DecoderHandler.decode(customPacketBuffer);
            timers = (int) DecoderHandler.decode(customPacketBuffer);
            coefficient_power = (int) DecoderHandler.decode(customPacketBuffer);
            wind_speed = (double) DecoderHandler.decode(customPacketBuffer);
            wind_side = EnumWindSide.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            mind_wind = (int) DecoderHandler.decode(customPacketBuffer);
            mind_speed = (int) DecoderHandler.decode(customPacketBuffer);
            enumTypeWind = EnumTypeWind.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            if (this.energy != null) {
                this.energy.getEnergy().onNetworkUpdate(customPacketBuffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, coefficient);
            EncoderHandler.encode(packet, speed);
            EncoderHandler.encode(packet, slot);
            EncoderHandler.encode(packet, rotorSide);
            EncoderHandler.encode(packet, generation);
            EncoderHandler.encode(packet, timers);
            EncoderHandler.encode(packet, coefficient_power);
            EncoderHandler.encode(packet, wind_speed);
            EncoderHandler.encode(packet, wind_side);
            EncoderHandler.encode(packet, mind_wind);
            EncoderHandler.encode(packet, mind_speed);
            EncoderHandler.encode(packet, enumTypeWind);
            packet.writeBytes(energy.getEnergy().updateComponent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setInteger("coef", this.coefficient_power);
        nbtTagCompound.setBoolean("work", this.work);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.coefficient_power = nbtTagCompound.getInteger("coef");
        this.work = nbtTagCompound.getBoolean("work");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.windturbine.info"));
        tooltip.add(Localization.translate("wind.need_level") + 1 + " " + Localization.translate(
                "wind.need_level1") + 14 + " " + Localization.translate("wind.need_level2"));
    }


    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        ItemStack stack = player.getHeldItem(hand);
        if (this.getRotor() != null && stack.getItem() instanceof ItemWindRod) {
            ItemStack rotor = this.slot.get();
            if (((ItemWindRod) stack.getItem()).getLevel(this.getRotor().getLevel(), stack.getItemDamage())) {

                if (rotor.getItemDamage() >= rotor.getMaxDamage() * 0.25) {
                    this.slot.damage((int) -(rotor.getMaxDamage() * 0.25), 0);
                    stack.shrink(1);
                    return true;
                }
            }
        }

        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    public boolean checkSpace() {
        int box = this.getRotorDiameter() / 2;
        if (box == 0) {
            return false;
        }
        BlockPos pos1 = pos.add(this.getFacing().getDirectionVec());
        switch (this.getFacing().getAxis()) {
            case Y:
                return false;
            case X:
                for (int z = pos1.getZ() - box; z <= pos1.getZ() + box; z++) {
                    for (int y = pos1.getY() - box; y <= pos1.getY() + box; y++) {
                        IBlockState state = this.world.getBlockState(new BlockPos(pos1.getX(), y, z));
                        if (state.getMaterial() != Material.AIR) {
                            return false;
                        }
                    }
                }
                return true;
            case Z:
                for (int x = pos1.getX() - box; x <= pos1.getX() + box; x++) {
                    for (int y = pos1.getY() - box; y <= pos1.getY() + box; y++) {
                        IBlockState state = this.world.getBlockState(new BlockPos(x, y, pos1.getZ()));
                        if (state.getMaterial() != Material.AIR) {
                            return false;
                        }
                    }
                }
                return true;
        }
        return false;
    }

    public boolean setFacingWrench(EnumFacing facing, EntityPlayer player) {
        boolean fac = super.setFacingWrench(facing, player);
        new PacketUpdateFieldTile(this, "facing", (byte) this.facing);
        return fac;
    }


    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            energy = null;
        }


    }

    @Override
    public void updateAfterAssembly() {

        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISocket.class
                );
        this.energy = (ISocket) this.getWorld().getTileEntity(pos1.get(0));
        if (this.getRotor() != null) {
            this.energy.getEnergy().setSourceTier(this.getRotor().getSourceTier());
        }

    }

    @Override
    public EnumRotorSide getRotorSide() {
        return this.rotorSide;
    }

    @Override
    public void setRotorSide(final EnumRotorSide rotorSide) {
        this.rotorSide = rotorSide;
        new PacketUpdateFieldTile(this, "rotorSide", this.rotorSide);
    }

    @Override
    public double getCoefficient() {
        return 1;
    }

    @Override
    public void setCoefficient(final double coefficient) {
        this.coefficient = coefficient;
        new PacketUpdateFieldTile(this, "coefficient", this.coefficient);
    }

    @Override
    public IWindRotor getRotor() {
        return this.slot.isEmpty() ? null : (IWindRotor) this.slot.get().getItem();
    }

    @Override
    public ItemStack getItemStack() {
        return this.slot.get();
    }

    @Override
    public EnumLevelGenerators getLevelGenerator() {
        return this.levelGenerators;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.full) {
            return;
        }
        this.timers = WindSystem.windSystem.getTime();
        this.wind_side = WindSystem.windSystem.getWindSide();
        this.enumTypeWind = WindSystem.windSystem.getEnumTypeWind();
        if (this.need_repair && this.getRotor() != null) {
            this.slot_blades.work();
        }

        if (!this.work || !can_work) {
            if (generation != 0) {
                generation = 0;
            }
            return;
        }

        if (this.can_repair) {
            if (this.time != 0) {
                if (this.world.provider.getWorldTime() % (this.time * 20L) == 0) {
                    this.slot.damage(-1, 0);

                }
            }
        }
        if (this.world.provider.getWorldTime() % 30 == 0) {
            if (this.getRotor() != null) {
                space = checkSpace();
                new PacketUpdateFieldTile(this, "space", this.space);
            } else {
                if (space) {
                    space = false;
                    new PacketUpdateFieldTile(this, "space", this.space);
                } else {
                    generation = 0;
                }
            }
        }
        this.tick++;
        if (this.tick >= 40) {
            this.tick = 40;
        }
        if (space && (this.getRotor() != null && ((ItemDamage) this.slot
                .get(0)
                .getItem()).getCustomDamage(this.slot.get(0)) > 0)) {
            if (!this.getActive()) {
                this.setActive(false);
            }
            if (this.getRotor() == null) {
                if (this.getActive()) {
                    this.setActive(false);
                }
                if (space) {
                    space = false;
                    new PacketUpdateFieldTile(this, "space", this.space);

                }
                return;
            }
            this.wind_speed = WindSystem.windSystem.getWind_Strength();
            if (this.getMinWind() != 0) {
                if (world.getWorldTime() % 40 == 0) {
                    generation = WindSystem.windSystem.getPowerFromWindRotor(
                            this.world,
                            this.pos,
                            this,
                            this.getItemStack()
                    ) * (coefficient_power / 100D);
                }
            } else {
                generation = WindSystem.windSystem.getPowerFromWindRotor(
                        this.world,
                        this.pos,
                        this,
                        this.getItemStack()
                ) * (coefficient_power / 100D);
            }
            this.energy.getEnergy().addEnergy(generation);
            if (this.world.getWorldTime() % getDamageTimeFromWind() == 0) {
                this.slot.damage(this.getDamageRotor(), this.addition_strength);
            }
        } else {
            generation = 0;
        }
    }

    @SideOnly(Side.CLIENT)
    protected void renderBlockRotor(IWindMechanism windGen, World world, BlockPos pos) {
        int diameter = windGen.getRotorDiameter();

        if (diameter != 0) {
            float angle = windGen.getAngle();
            ResourceLocation rotorRL = windGen.getRotorRenderTexture();
            ModelBase model = rotorModels.get(diameter);
            if (model == null) {
                model = new RotorModel(diameter);
                rotorModels.put(diameter, model);
            }

            EnumFacing facing = windGen.getFacing();
            pos = pos.offset(facing);
            int light = world.getCombinedLight(pos, 0);
            int blockLight = light % 65536;
            int skyLight = light / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) blockLight, (float) skyLight);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5F, 0.5F, 0.5F);

            switch (facing) {
                case NORTH:
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                    break;
                case EAST:
                    GL11.glRotatef(-180.0F, 0.0F, 1.0F, 0.0F);
                    break;
                case SOUTH:
                    GL11.glRotatef(-270.0F, 0.0F, 1.0F, 0.0F);
                    break;
                case UP:
                    GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            }
            if (windGen.getSpace()) {
                if (!Minecraft.getMinecraft().isGamePaused()) {
                    GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
                }
            }
            GlStateManager.translate(-0.2F, 0.0F, 0.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(rotorRL);
            model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.popMatrix();
        }
    }

    @SideOnly(Side.CLIENT)
    public void render(
            TileMultiBlockBase tileEntityMultiBlockBase
    ) {
        if (!this.isFull()) {
            super.render(tileEntityMultiBlockBase);
        }
        if (this.isFull()) {
            GL11.glPushMatrix();
            GlStateManager.translate(-0.5, 0, -0.5);
            this.renderBlockRotor(this, this.getWorld(), this.getPos());
            GL11.glPopMatrix();
        }
    }

    private int getDamageTimeFromWind() {
        switch (this.enumTypeWind) {
            case ONE:
                return 60;
            case TWO:
                return 55;
            case THREE:
                return 50;
            case FOUR:
                return 45;
            case FIVE:
                return 40;
            case SIX:
                return 35;
            case SEVEN:
                return 30;
            case EIGHT:
                return 25;
            case NINE:
                return 20;
            case TEN:
                return 10;
        }
        return 20;
    }

    private int getDamageRotor() {
        if (coefficient_power == 100) {
            return 1;
        }
        return (int) ((int) (this.getRotor().getLevel() * this.coefficient_power / 100D) * Math.pow(
                this.coefficient_power / 100D,
                this.getRotor().getLevel() - 1
        )) * getCoefDamage();
    }

    private int getCoefDamage() {
        switch ((int) (coefficient * 10)) {
            case 10:
                return 1;
            case 7:
                return 2;
            case 5:
                return 3;
            default:
                return 1;
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.getWorld().isRemote || facing == 0 || facing == 1) {
            return;
        }
        this.timers = WindSystem.windSystem.getTime();
        this.wind_side = WindSystem.windSystem.getWindSide();
        this.enumTypeWind = WindSystem.windSystem.getEnumTypeWind();
        if (!this.slot.isEmpty()) {
            MinecraftForge.EVENT_BUS.post(new EventRotorItemLoad(this.getWorld(),
                    (IRotorUpgradeItem) this.slot.get().getItem(), this.slot.get()
            ));
        }
        this.change();
        this.setRotorSide(WindSystem.windSystem.getRotorSide(this.getFacing()));
        MinecraftForge.EVENT_BUS.post(new WindGeneratorEvent(this, this.getWorld(), true));
        new PacketUpdateFieldTile(this, "speed", speed);
        new PacketUpdateFieldTile(this, "space", space);
        new PacketUpdateFieldTile(this, "coefficient", coefficient);
        new PacketUpdateFieldTile(this, "wind_side", wind_side);
        new PacketUpdateFieldTile(this, "angle", angle);
        new PacketUpdateFieldTile(this, "mind_speed", mind_speed);
        new PacketUpdateFieldTile(this, "generation", generation);
        new PacketUpdateFieldTile(this, "work", this.work);
        new PacketUpdateFieldTile(this, "slot", slot);
        this.can_work = this.getWorld().provider.hasSkyLight() &&
                !this.getWorld().provider.isNether();
        if (!this.slot.get().isEmpty()) {
            if (DamageHandler.getDamage(this.slot.get()) <= DamageHandler.getMaxDamage(this.slot.get()) * 0.75) {
                this.need_repair = true;
            }
        }
    }

    @Override
    public void onUnloaded() {
        MinecraftForge.EVENT_BUS.post(new WindGeneratorEvent(this, this.getWorld(), false));
        super.onUnloaded();
    }

    @Override
    public boolean canPlace(final TileEntityBlock te, final BlockPos pos, final World world) {
        for (int i = pos.getX() - 8; i <= pos.getX() + 8; i++) {
            for (int j = pos.getY() - 8; j <= pos.getY() + 8; j++) {
                for (int k = pos.getZ() - 8; k <= pos.getZ() + 8; k++) {
                    final TileEntity tile = world.getTileEntity(new BlockPos(i, j, k));
                    if (tile instanceof IWindMechanism) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onBlockBreak(boolean wrench) {

        super.onBlockBreak(wrench);
    }

    public void setActive(boolean active) {
        if (active != this.getActive()) {
            new PacketUpdateFieldTile(this, "slot", this.slot);
        }

        super.setActive(active);
    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, speed);
            EncoderHandler.encode(packet, slot);
            EncoderHandler.encode(packet, space);
            EncoderHandler.encode(packet, coefficient);
            EncoderHandler.encode(packet, wind_side);
            EncoderHandler.encode(packet, angle);
            EncoderHandler.encode(packet, mind_speed);
            EncoderHandler.encode(packet, generation);
            EncoderHandler.encode(packet, work);
            EncoderHandler.encode(packet, coefficient_power);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            speed = (float) DecoderHandler.decode(customPacketBuffer);
            slot.readFromNbt(((InvSlot) DecoderHandler.decode(customPacketBuffer)).writeToNbt(new NBTTagCompound()));
            space = (boolean) DecoderHandler.decode(customPacketBuffer);
            coefficient = (double) DecoderHandler.decode(customPacketBuffer);
            wind_side = EnumWindSide.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            angle = (float) DecoderHandler.decode(customPacketBuffer);
            mind_speed = (int) DecoderHandler.decode(customPacketBuffer);
            generation = (double) DecoderHandler.decode(customPacketBuffer);
            work = (boolean) DecoderHandler.decode(customPacketBuffer);
            coefficient_power = (int) DecoderHandler.decode(customPacketBuffer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void usingBeforeGUI() {

    }

    @Override
    public float getAngle() {
        if (this.getWorld().provider.getDimension() != 0) {
            return 0;
        }
        if (this.speed != 0.0F && this.work && (this.getRotor() != null && ((ItemDamage) this.slot
                .get(0)
                .getItem()).getCustomDamage(this.slot.get(0)) > 0)) {
            final long k = (System.currentTimeMillis() - this.lastcheck);
            if (this.mind_wind != 0) {
                this.angle += (float) ((float) k * WindSystem.windSystem.getSpeed(Math.min(
                        24.7 + this.mind_speed,
                        WindSystem.windSystem.getSpeedFromPower(this.getBlockPos(), this,
                                this.generation
                        )
                ) * this.getCoefficient()));
            } else {
                this.angle += (float) ((float) k * this.speed * this.getCoefficient());
            }
            this.angle %= 360.0F;
        }

        this.lastcheck = System.currentTimeMillis();
        return this.angle;
    }

    @Override
    public void setRotationSpeed(final float speed) {
        if (this.speed != speed) {
            this.speed = speed;
            new PacketUpdateFieldTile(this, "speed", this.speed);
        }
    }

    @Override
    public int getRotorDiameter() {
        return getRotor() != null ? getRotor().getDiameter(this.slot.get()) : 0;
    }

    @Override
    public ResourceLocation getRotorRenderTexture() {
        return getRotor() != null ? getRotor().getRotorRenderTexture(this.slot.get()) : null;
    }

    @Override
    public void change() {
        this.addition_power = 0;
        this.addition_efficient = 0;
        this.addition_strength = 0;
        this.time = 0;
        this.mind_speed = 0;
        this.mind_wind = 0;
        if (this.getRotor() != null) {
            final List<RotorUpgradeItemInform> list = RotorUpgradeSystem.instance.getInformation(
                    this.getItemStack());

            for (int i = 0; i < 3; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.addition_strength += modules == null ? 0 : (int) (modules.number * modules.upgrade.getCoef() * 100);

            }
            for (int i = 3; i < 6; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.addition_efficient += modules == null ? 0 : modules.number * modules.upgrade.getCoef();
            }
            for (int i = 6; i < 9; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.addition_power += modules == null ? 0 : modules.number * modules.upgrade.getCoef();
            }
            for (int i = 17; i < 20; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.time = modules == null ? 0 : (int) modules.upgrade.getCoef();
            }
            for (int i = 11; i < 14; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.mind_wind = modules == null ? 0 : (int) modules.upgrade.getCoef();
            }
            for (int i = 14; i < 17; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.mind_speed = modules == null ? 0 : (int) modules.upgrade.getCoef();
            }
        }
        new PacketUpdateFieldTile(this, "mind_wind", this.mind_wind);
        new PacketUpdateFieldTile(this, "mind_speed", this.mind_speed);
    }


    public void updateField(String name, CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("speed")) {
            try {
                this.speed = (float) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot")) {
            this.slot.readFromNbt(getNBTFromSlot(is));
        }
        if (name.equals("space")) {
            try {
                this.space = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("coefficient")) {
            try {
                this.coefficient = (double) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.trim().equals("work")) {
            try {
                this.work = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("rotorSide")) {
            try {
                this.rotorSide = EnumRotorSide.values()[(int) DecoderHandler.decode(is)];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("mind_wind")) {
            try {
                this.mind_wind = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("wind_side")) {
            try {
                this.wind_side = EnumWindSide.values()[(int) DecoderHandler.decode(is)];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("angle")) {
            try {
                this.angle = (float) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("generation")) {
            try {
                this.generation = (double) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("mind_speed")) {
            try {
                this.mind_speed = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public double getAdditionalCoefficient() {
        return this.addition_efficient;
    }

    @Override
    public double getAdditionalPower() {
        return this.addition_power;
    }

    @Override
    public boolean getAuto() {
        return false;
    }

    @Override
    public boolean getMin() {
        return true;
    }

    @Override
    public boolean getSpace() {
        return this.space;
    }

    @Override
    public int getTime() {
        return this.time;
    }

    @Override
    public boolean need_repair() {
        return this.need_repair;
    }

    @Override
    public boolean can_repair() {
        return this.can_repair;
    }

    @Override
    public int getMinWind() {
        return this.mind_wind;
    }

    @Override
    public int getMinWindSpeed() {
        return this.mind_speed;
    }

    @Override
    public void setWork(final boolean work) {
        this.work = work;
    }

    @Override
    public ContainerWindTurbine getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerWindTurbine(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiWindTurbine(getGuiContainer(entityPlayer));
    }


    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        if (i == 0) {
        } else {
            this.coefficient_power = (int) i;
            if (this.coefficient_power < 100) {
                coefficient_power = 100;
            }
            if (this.coefficient_power > 150) {
                coefficient_power = 150;
            }
        }
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
