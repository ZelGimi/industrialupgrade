package com.denfop.tiles.hydroturbine;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.water.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.water.upgrade.IRotorUpgradeItem;
import com.denfop.api.water.upgrade.RotorUpgradeItemInform;
import com.denfop.api.water.upgrade.RotorUpgradeSystem;
import com.denfop.api.water.upgrade.event.EventRotorItemLoad;
import com.denfop.api.windsystem.*;
import com.denfop.api.windsystem.event.WindGeneratorEvent;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHydroTurbine;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.client.ComponentClientEffectRender;
import com.denfop.componets.client.EffectType;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerHydroTurbineController;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiHydroTurbineController;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotHydroTurbineRotor;
import com.denfop.invslot.InvSlotHydroTurbineRotorBlades;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.List;

import static com.denfop.render.windgenerator.WindGeneratorRenderer.rotorModels;
import static net.minecraft.tags.BiomeTags.IS_OCEAN;
import static net.minecraft.tags.BiomeTags.IS_RIVER;

public class TileEntityHydroTurbineController extends TileMultiBlockBase implements IWindMechanism, IType,
        IUpdatableTileEvent {


    public final InvSlotHydroTurbineRotorBlades slot_blades;
    private final EnumLevelGenerators levelGenerators;
    public ISocket energy;
    public InvSlotHydroTurbineRotor slot;
    public double generation = 0;
    public boolean need_repair;
    public int mind_wind;
    public boolean can_repair;
    public int mind_speed;
    public int timers;
    public double wind_speed;
    public EnumWindSide wind_side;
    public EnumTypeWind enumTypeWind;
    public int coefficient_power = 100;
    boolean space = false;
    private int tick;
    private double addition_power;
    private double addition_efficient;
    private double addition_strength;
    private double coefficient;
    private EnumRotorSide rotorSide;
    private float speed;
    private float angle;
    private long lastcheck;
    private boolean work = true;
    private int time;
    private boolean can_work = true;
    private double biome;

    public TileEntityHydroTurbineController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.HydroTurbineMultiBlock, BlockHydroTurbine.hydro_turbine_controller, pos, state);
        this.levelGenerators = EnumLevelGenerators.FOUR;
        this.slot = new InvSlotHydroTurbineRotor(this);
        this.slot_blades = new InvSlotHydroTurbineRotorBlades(this);
        this.addition_power = 0;
        this.addition_efficient = 0;
        this.addition_strength = 0;
        this.tick = 0;
        this.biome = 1;
        this.componentClientEffectRender = new ComponentClientEffectRender(this, EffectType.WATER_GENERATOR);
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
        return (int) ((int) (1 * this.coefficient_power / 100D) * Math.pow(
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
    public IMultiTileBlock getTeBlock() {
        return BlockHydroTurbine.hydro_turbine_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hydroTurbine.getBlock(getTeBlock());
    }

    @Override
    public boolean needUpdate() {
        return true;
    }


    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        final CustomPacketBuffer packet = super.writeUpdatePacket();
        try {
            EncoderHandler.encode(packet, slot);
            EncoderHandler.encode(packet, facing);
            EncoderHandler.encode(packet, generation);
            EncoderHandler.encode(packet, mind_speed);
            EncoderHandler.encode(packet, coefficient);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void readUpdatePacket(final CustomPacketBuffer customPacketBuffer) {
        super.readUpdatePacket(customPacketBuffer);
        try {
            slot.readFromNbt(customPacketBuffer.registryAccess(), getNBTFromSlot(customPacketBuffer));
            facing = (byte) DecoderHandler.decode(customPacketBuffer);
            generation = (double) DecoderHandler.decode(customPacketBuffer);
            mind_speed = (int) DecoderHandler.decode(customPacketBuffer);
            coefficient = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("wind.need_level") + 1 + " " + Localization.translate(
                "wind.need_level1") + 14 + " " + Localization.translate("wind.need_level2"));
        tooltip.add(Localization.translate("iu.hydroturbine.info"));
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            coefficient = (double) DecoderHandler.decode(customPacketBuffer);
            speed = (float) DecoderHandler.decode(customPacketBuffer);
            slot.readFromNbt(customPacketBuffer.registryAccess(), ((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(customPacketBuffer.registryAccess(), new CompoundTag()));
            rotorSide = EnumRotorSide.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            generation = (double) DecoderHandler.decode(customPacketBuffer);
            timers = (int) DecoderHandler.decode(customPacketBuffer);
            wind_speed = (double) DecoderHandler.decode(customPacketBuffer);
            wind_side = EnumWindSide.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            mind_wind = (int) DecoderHandler.decode(customPacketBuffer);
            mind_speed = (int) DecoderHandler.decode(customPacketBuffer);
            enumTypeWind = EnumTypeWind.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            coefficient_power = (int) DecoderHandler.decode(customPacketBuffer);
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
            EncoderHandler.encode(packet, wind_speed);
            EncoderHandler.encode(packet, wind_side);
            EncoderHandler.encode(packet, mind_wind);
            EncoderHandler.encode(packet, mind_speed);
            EncoderHandler.encode(packet, enumTypeWind);
            EncoderHandler.encode(packet, coefficient_power);
            packet.writeBytes(energy.getEnergy().updateComponent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.getRotor() != null && stack.getItem() instanceof ItemWindRod) {
            ItemStack rotor = this.slot.get(0);
            if (((ItemWindRod) stack.getItem()).getLevel(this.getRotor().getLevel(), ((ItemWindRod<?>) stack.getItem()).getElement().getId())) {

                if (rotor.getDamageValue() >= rotor.getMaxDamage() * 0.25) {
                    this.slot.damage((int) -(rotor.getMaxDamage() * 0.25), 0);
                    stack.shrink(1);
                    return true;
                }
            }
        }
        return super.onActivated(player, hand, side, vec3);
    }


    public boolean checkSpace() {
        int box = this.getRotorDiameter() / 2;
        if (box == 0) {
            return false;
        }
        Vector3f vec = this.getFacing().step();
        BlockPos pos1 = pos.offset(new BlockPos((int) vec.x(), (int) vec.y(), (int) vec.z()));
        switch (this.getFacing().getAxis()) {
            case Y:
                return false;
            case X:
                for (int z = pos1.getZ() - box; z <= pos1.getZ() + box; z++) {
                    for (int y = pos1.getY() - box; y <= pos1.getY() + box; y++) {
                        BlockState state = this.level.getBlockState(new BlockPos(pos1.getX(), y, z));
                        if (!state.liquid()) {
                            return false;
                        }
                    }
                }
                return true;
            case Z:
                for (int x = pos1.getX() - box; x <= pos1.getX() + box; x++) {
                    for (int y = pos1.getY() - box; y <= pos1.getY() + box; y++) {
                        BlockState state = this.level.getBlockState(new BlockPos(x, y, pos1.getZ()));
                        if (!state.liquid()) {
                            return false;
                        }
                    }
                }
                return true;
        }
        return false;
    }

    public boolean setFacingWrench(Direction facing, Player player) {
        boolean fac = super.setFacingWrench(facing, player);
        new PacketUpdateFieldTile(this, "facing", this.facing);
        return fac;
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
        return this.coefficient;
    }

    @Override
    public void setCoefficient(final double coefficient) {
        this.coefficient = coefficient;
        new PacketUpdateFieldTile(this, "coefficient", this.coefficient);
    }

    @Override
    public IWindRotor getRotor() {
        return this.slot.isEmpty() ? null : (IWindRotor) this.slot.get(0).getItem();
    }

    @Override
    public ItemStack getItemStack() {
        return this.slot.get(0);
    }

    @Override
    public EnumLevelGenerators getLevelGenerator() {
        return this.levelGenerators;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.need_repair && this.getRotor() != null) {
            this.slot_blades.work();
        }
        this.timers = WindSystem.windSystem.getTime();
        this.wind_side = WindSystem.windSystem.getWindSide();
        this.enumTypeWind = WindSystem.windSystem.getEnumTypeWind();
        if (!this.work || !can_work) {
            if (generation != 0) {
                generation = 0;
            }
            return;
        }

        if (this.can_repair) {
            if (this.time != 0) {
                if (this.level.getGameTime() % (this.time * 20L) == 0) {
                    this.slot.damage(-1, 0);

                }
            }
        }
        if (this.level.getGameTime() % 30 == 0) {
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
                .getItem()).getCustomDamage(this.slot.get(0)) <= DamageHandler.getDamage(this.slot.get(0)))) {
            if (!this.getActive()) {
                this.setActive(true);
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

            generation =
                    WindSystem.windSystem.getPowerFromWaterRotor(
                            this.level,
                            this,
                            this.getItemStack()
                    ) * this.biome * this.coefficient_power / 100D;
            this.energy.getEnergy().addEnergy(generation);
            this.energy.getEnergy().setSourceTier(EnergyNetGlobal.instance.getTierFromPower(generation));

            if (this.level.getGameTime() % getDamageTimeFromWind() == 0) {
                this.slot.damage(this.getDamageRotor(), this.addition_strength);
            }
        } else {
            generation = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void render(
            TileMultiBlockBase tileEntityMultiBlockBase, RenderLevelStageEvent event
    ) {
        if (!this.isFull()) {
            super.render(tileEntityMultiBlockBase, event);
        }
        if (this.isFull()) {
            event.getPoseStack().pushPose();
            event.getPoseStack().translate(-0.5, 0, -0.5);
            this.renderBlockRotor(this, this.getWorld(), this.getPos(), event);
            event.getPoseStack().popPose();
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderBlockRotor(IWindMechanism windGen, Level world, BlockPos pos, RenderLevelStageEvent event) {
        int diameter = windGen.getRotorDiameter();

        if (diameter != 0) {
            float angle = windGen.getAngle();
            ResourceLocation rotorRL = windGen.getRotorRenderTexture();
            rotorModels.clear();
            EntityModel model = rotorModels.get(diameter);
            if (model == null) {
                model = new RotorModel(diameter);
                rotorModels.put(diameter, model);
            }

            Direction facing = windGen.getFacing();
            pos = pos.offset(facing.getNormal());
            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            poseStack.translate(0.5F + facing.getStepX() * 0.35, 0.5F, 0.5F + facing.getStepZ() * 0.35);
            switch (facing) {
                case NORTH:
                    poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
                    break;
                case EAST:
                    poseStack.mulPose(Axis.YP.rotationDegrees(-180.0F));
                    break;
                case SOUTH:
                    poseStack.mulPose(Axis.YP.rotationDegrees(-270.0F));
                    break;
                case UP:
                    poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
                    break;
            }

            if (windGen.getSpace()) {
                IWindRotor rotor = this.getRotor();
                if (rotor.getMaxCustomDamage(this.slot.get(0)) - rotor.getCustomDamage(this.slot.get(0)) == 0) {
                    angle = 0;
                }
                if (!Minecraft.getInstance().isPaused()) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(angle));
                }
            }
            poseStack.translate(-0.2F, 0.0F, 0.0F);
            GuiCore.bindTexture(rotorRL);
            VertexConsumer consumer = Minecraft.getInstance()
                    .renderBuffers()
                    .bufferSource()
                    .getBuffer(RenderType.entityCutout(rotorRL));
            RenderSystem.setShaderColor(1, 1, 1, 1);
            int packedLight = event.getLevelRenderer().getLightColor(world, pos);
            model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            poseStack.popPose();
        }
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
            this.slot.readFromNbt(is.registryAccess(), getNBTFromSlot(is));
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
        if (name.equals("work")) {
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
    public void onLoaded() {
        super.onLoaded();
        if (this.getWorld().isClientSide || facing == 0 || facing == 1) {
            return;
        }
        this.timers = WindSystem.windSystem.getTime();
        this.wind_side = WindSystem.windSystem.getWindSide();
        this.enumTypeWind = WindSystem.windSystem.getEnumTypeWind();
        if (!this.slot.isEmpty()) {
            NeoForge.EVENT_BUS.post(new EventRotorItemLoad(this.getWorld(),
                    (IRotorUpgradeItem) this.slot.get(0).getItem(), this.slot.get(0)
            ));
        }
        this.biome =
                (this.getWorld().getBiome(this.pos).is(IS_OCEAN) || this
                        .getWorld()
                        .getBiome(this.pos).is(IS_RIVER)) ? 1 : 0.5;
        this.change();
        this.setRotorSide(WindSystem.windSystem.getRotorSide(this.getFacing()));
        NeoForge.EVENT_BUS.post(new WindGeneratorEvent(this, this.getWorld(), true));
        new PacketUpdateFieldTile(this, "speed", speed);
        new PacketUpdateFieldTile(this, "slot", slot);
        new PacketUpdateFieldTile(this, "space", space);
        new PacketUpdateFieldTile(this, "coefficient", coefficient);
        new PacketUpdateFieldTile(this, "wind_side", wind_side);
        new PacketUpdateFieldTile(this, "angle", angle);
        new PacketUpdateFieldTile(this, "mind_speed", mind_speed);
        new PacketUpdateFieldTile(this, "generation", generation);

        this.can_work = this.getWorld().dimensionType().hasSkyLight() &&
                !(this.getWorld().dimension() == Level.NETHER);
        this.timers = WindSystem.windSystem.getTime();
        this.wind_side = WindSystem.windSystem.getWindSide();
        this.enumTypeWind = WindSystem.windSystem.getEnumTypeWind();
        if (!this.slot.get(0).isEmpty()) {
            if (DamageHandler.getDamage(this.slot.get(0)) <= DamageHandler.getMaxDamage(this.slot.get(0)) * 0.75) {
                this.need_repair = true;
            }
        }
    }

    @Override
    public void onUnloaded() {
        NeoForge.EVENT_BUS.post(new WindGeneratorEvent(this, this.getWorld(), false));
        super.onUnloaded();
    }

    @Override
    public boolean canPlace(final TileEntityBlock te, final BlockPos pos, final Level world, Direction direction, LivingEntity entity) {
        for (int i = pos.getX() - 4; i <= pos.getX() + 4; i++) {
            for (int j = pos.getY() - 4; j <= pos.getY() + 4; j++) {
                for (int k = pos.getZ() - 4; k <= pos.getZ() + 4; k++) {
                    final BlockEntity tile = world.getBlockEntity(new BlockPos(i, j, k));
                    if (tile instanceof IWindMechanism) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onBlockBreak(boolean w) {
        super.onBlockBreak(w);
    }

    @Override
    public void setWork(final boolean work) {
        this.work = work;
    }

    public void setActive(boolean active) {
        if (active != this.getActive()) {
            new PacketUpdateFieldTile(this, "slot", this.slot);
        }

        super.setActive(active);
    }

    public double getGeneration() {
        return generation;
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putInt("coef", this.coefficient_power);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.coefficient_power = nbtTagCompound.getInt("coef");
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
            slot.readFromNbt(customPacketBuffer.registryAccess(), ((InvSlot) DecoderHandler.decode(customPacketBuffer)).writeToNbt(customPacketBuffer.registryAccess(), new CompoundTag()));
            space = (boolean) DecoderHandler.decode(customPacketBuffer);
            coefficient = (double) DecoderHandler.decode(customPacketBuffer);
            wind_side = EnumWindSide.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            angle = (float) DecoderHandler.decode(customPacketBuffer);
            mind_speed = (int) DecoderHandler.decode(customPacketBuffer);
            generation = (double) DecoderHandler.decode(customPacketBuffer);
            coefficient_power = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAfterAssembly() {

        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISocket.class
                );
        this.energy = (ISocket) this.getWorld().getBlockEntity(pos1.get(0));

    }

    @Override
    public void usingBeforeGUI() {

    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            energy = null;
        }


    }

    @Override
    public float getAngle() {
        if (this.getWorld().dimension() != Level.OVERWORLD) {
            return 0;
        }
        if (this.speed != 0.0F && this.work && (this.getRotor() != null && ((ItemDamage) this.slot
                .get(0)
                .getItem()).getCustomDamage(this.slot.get(0)) <= DamageHandler.getDamage(this.slot.get(0)))) {
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
            new PacketUpdateFieldTile(this, "speed", speed);
        }
    }

    @Override
    public int getRotorDiameter() {
        return getRotor() != null ? getRotor().getDiameter(this.slot.get(0)) : 0;
    }

    @Override
    public ResourceLocation getRotorRenderTexture() {
        return getRotor() != null ? getRotor().getRotorRenderTexture(this.slot.get(0)) : null;
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
                this.addition_strength += modules == null ? 0 : modules.number * modules.upgrade.getCoef();
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
            for (int i = 13; i < 16; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.time = modules == null ? 0 : (int) modules.upgrade.getCoef();
            }
            for (int i = 10; i < 13; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.mind_wind = modules == null ? 0 : (int) modules.upgrade.getCoef();
            }
            final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                    16), list);
            this.biome = (this.getWorld().getBiome(this.pos).is(IS_OCEAN) || this
                    .getWorld()
                    .getBiome(this.pos).is(IS_RIVER)) ? 1 : 0.5;
            if (modules != null) {
                this.biome = 1;
            }
        }

        new PacketUpdateFieldTile(this, "mind_wind", this.mind_wind);

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
    public ContainerHydroTurbineController getGuiContainer(final Player entityPlayer) {
        return new ContainerHydroTurbineController(this, entityPlayer);

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiHydroTurbineController((ContainerHydroTurbineController) menu);

    }


    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
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
