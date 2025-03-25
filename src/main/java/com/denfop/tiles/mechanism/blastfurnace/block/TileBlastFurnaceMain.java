package com.denfop.tiles.mechanism.blastfurnace.block;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBlastFurnace;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBlastFurnace;
import com.denfop.effects.SmokeParticle;
import com.denfop.effects.SparkParticle;
import com.denfop.gui.GuiBlastFurnace;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.items.resource.ItemIngots;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.render.oilquarry.DataBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastHeat;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastInputFluid;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastMain;
import com.denfop.tiles.mechanism.blastfurnace.api.InvSlotBlastFurnace;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.particle.ParticleLava;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TileBlastFurnaceMain extends TileMultiBlockBase implements IBlastMain,
        IUpdatableTileEvent,
        IAudioFixer {

    public final FluidTank tank;
    public final InvSlotOutput output1;
    public final InvSlotFluidByList fluidSlot;
    public final HeatComponent heat;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public boolean load = false;
    public ItemStack outputStack = ItemStack.EMPTY;
    public InvSlotBlastFurnace invSlotBlastFurnace = new InvSlotBlastFurnace(this);
    public InvSlotOutput output = new InvSlotOutput(this, 1);
    public FluidTank tank1 = new FluidTank(12000);

    public IBlastHeat blastHeat;
    public IBlastInputFluid blastInputFluid;
    public List<EntityPlayer> entityPlayerList;
    public double progress = 0;
    public int bar = 1;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();

    private boolean sound = true;
    @SideOnly(Side.CLIENT)
    private DataBlock dataBlock;
    @SideOnly(Side.CLIENT)
    private DataBlock dataBlock_active;

    public TileBlastFurnaceMain() {
        super(InitMultiBlockSystem.blastFurnaceMultiBlock);
        this.full = false;
        final Fluids fluids = this.addComponent(new Fluids(this));

        this.tank = fluids.addTank("tank", 10000, InvSlot.TypeItemSlot.INPUT,
                Fluids.fluidPredicate(FluidName.fluidsteam.getInstance())
        );
        this.entityPlayerList = new ArrayList<>();
        this.fluidSlot = new InvSlotFluidByList(this, 1, FluidRegistry.WATER);
        this.output1 = new InvSlotOutput(this, 1);
        this.heat = this.addComponent(HeatComponent.asBasicSink(this, 1000));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.2));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.5));
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();

        if (this.getActive() && this.getWorld().getWorldTime() % 5 == 0) {
            final Random rand = WorldBaseGen.random;

            EnumFacing facing = this.getFacing();

            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    double offsetX = 0;
                    double offsetZ = 0;

                    switch (facing) {
                        case NORTH:
                            offsetZ = -0.3;
                            break;
                        case SOUTH:
                            offsetZ = 0.3;
                            break;
                        case WEST:
                            offsetX = -0.3;
                            break;
                        case EAST:
                            offsetX = 0.3;
                            break;
                        default:
                            break;
                    }


                    int sparkCount = 1 + rand.nextInt(2);
                    for (int i = 0; i < sparkCount; i++) {
                        Minecraft.getMinecraft().effectRenderer.addEffect(new SparkParticle(
                                this.getWorld(),
                                this.getPos().getX() + 0.5 + dx + offsetX  * -1 + rand.nextDouble() * 0.2 - 0.1,
                                this.getPos().getY() + 1.2 + rand.nextDouble() * 0.2,
                                this.getPos().getZ() + 0.5 + dz + offsetZ  * -1 + rand.nextDouble() * 0.2 - 0.1
                        ));
                    }


                    int smokeCount = 1 + rand.nextInt(2);
                    for (int j = 0; j < smokeCount; j++) {
                        Minecraft.getMinecraft().effectRenderer.addEffect(new SmokeParticle(
                                this.getWorld(),
                                this.getPos().getX() + 0.5 + dx + offsetX  * -1 + rand.nextDouble() * 0.3 - 0.15,
                                this.getPos().getY() + 1.5 + rand.nextDouble() * 0.2,
                                this.getPos().getZ() + 0.5 + dz + offsetZ  * -1+ rand.nextDouble() * 0.3 - 0.15
                        ));
                    }

                    if (rand.nextInt(3) == 0) {
                        Minecraft.getMinecraft().effectRenderer.addEffect(
                                new ParticleFlame.Factory().createParticle(
                                        0, this.getWorld(),
                                        this.getPos().getX() + 0.5 + dx + offsetX  * -1 + rand.nextDouble() * 0.2 - 0.1,
                                        this.getPos().getY() + 1.3 + rand.nextDouble() * 0.3,
                                        this.getPos().getZ() + 0.5 + dz + offsetZ  * -1 + rand.nextDouble() * 0.2 - 0.1,
                                        0, 0.05, 0
                                )
                        );
                    }

                    if (rand.nextInt(3) == 0) {
                        Minecraft.getMinecraft().effectRenderer.addEffect(
                                new ParticleRedstone.Factory().createParticle(
                                        0, this.getWorld(),
                                        this.getPos().getX() + 0.5 + dx + offsetX * -1 + rand.nextDouble() * 0.4 - 0.2,
                                        this.getPos().getY() + 1.4 + rand.nextDouble() * 0.3,
                                        this.getPos().getZ() + 0.5 + dz + offsetZ * -1 + rand.nextDouble() * 0.4 - 0.2,
                                        1.0f, 0.3f, 0.1f
                                )
                        );
                    }


                    if (rand.nextInt(5) == 0) {
                        Minecraft.getMinecraft().effectRenderer.addEffect(
                                Objects.requireNonNull(new ParticleLava.Factory().createParticle(
                                        0, this.getWorld(),
                                        this.getPos().getX() + 0.5 + dx + offsetX * -4 + rand.nextDouble() * 0.4 - 0.2,
                                        this.getPos().getY() + 1.6,
                                        this.getPos().getZ() + 0.5 + dz + offsetZ * -4 + rand.nextDouble() * 0.4 - 0.2,
                                        0, 0.1, 0
                                ))
                        );
                    }
                }
            }
        }
    }




    @SideOnly(Side.CLIENT)
    public void renderUniqueMultiBlock() {
        if (this.dataBlock.getBlockState().getBlock() == IUItem.invalid)
            return;
        IBakedModel model2 = this.dataBlock.getState();
        Class<?> clazz = model2.getClass();
        Class<?> clazz1 = null;

        while (clazz != null) {
            if (clazz.getName().equals("net.minecraftforge.client.model.FancyMissingModel$BakedModel")) {
                clazz1 = clazz;
                break;
            }
            clazz = clazz.getSuperclass();
        }
        if (clazz1 != null){
            IBlockState blockState1 = this.block
                    .getDefaultState()
                    .withProperty(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, "global"))
                    .withProperty(
                            BlockTileEntity.facingProperty,
                            this.getFacing()
                    );
            this.dataBlock = new DataBlock(blockState1);
            IBakedModel model = Minecraft
                    .getMinecraft()
                    .getBlockRendererDispatcher()
                    .getModelForState(blockState1);
            this.dataBlock.setState(model);

            blockState1 = this.block
                    .getDefaultState()
                    .withProperty(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, "global_active"))
                    .withProperty(
                            BlockTileEntity.facingProperty,
                            this.getFacing()
                    );
            this.dataBlock_active = new DataBlock(blockState1);
            model = Minecraft
                    .getMinecraft()
                    .getBlockRendererDispatcher()
                    .getModelForState(blockState1);
            this.dataBlock_active.setState(model);
        }
        GlStateManager.popMatrix();
        switch (facing) {
            case 2:
                GlStateManager.translate(this.pos.getX() - 1.05, this.pos.getY() - 1, this.pos.getZ() - 0.05);
                break;
            case 3:
                GlStateManager.translate(this.pos.getX() - 1.05, this.pos.getY() - 1.05, this.pos.getZ() - 2.05);
                break;
            case 4:
                GlStateManager.translate(this.pos.getX() - 0.05, this.pos.getY() - 1.05, this.pos.getZ() - 1);
                break;
            case 5:
                GlStateManager.translate(this.pos.getX() - 2.05, this.pos.getY() - 1.05, this.pos.getZ() - 1.05);
                break;
        }

        GlStateManager.scale(3.08, 3.08, 3.08);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);



        if (!this.getActive()) {

            final IBakedModel model = dataBlock.getState();
            final IBlockState state = dataBlock.getBlockState();
            for (EnumFacing enumfacing : EnumFacing.values()) {
                render(model, state, enumfacing);
            }

            render(model, state, null);
        }else{
            final IBakedModel model = dataBlock_active.getState();
            final IBlockState state = dataBlock_active.getBlockState();
            for (EnumFacing enumfacing : EnumFacing.values()) {
                render(model, state, enumfacing);
            }
            render(model, state, null);
        }
        GlStateManager.pushMatrix();
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBlastFurnace.blast_furnace_main;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blastfurnace;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            full = (boolean) DecoderHandler.decode(customPacketBuffer);
            tank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            progress = (double) DecoderHandler.decode(customPacketBuffer);
            bar = (int) DecoderHandler.decode(customPacketBuffer);
            sound = (boolean) DecoderHandler.decode(customPacketBuffer);
            heat.storage = (double) DecoderHandler.decode(customPacketBuffer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, full);
            EncoderHandler.encode(packet, tank1);
            EncoderHandler.encode(packet, progress);
            EncoderHandler.encode(packet, bar);
            EncoderHandler.encode(packet, sound);
            EncoderHandler.encode(packet, heat.storage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.blastfurnace.info1"));
        tooltip.add(Localization.translate("iu.blastfurnace.info3") + Localization.translate(new ItemStack(
                IUItem.blastfurnace,
                1,
                0
        ).getUnlocalizedName()));
        tooltip.add(Localization.translate("iu.blastfurnace.info4"));
        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer).getDisplayName());
        tooltip.add(Localization.translate("iu.blastfurnace.info6"));
    }

    public EnumTypeAudio getTypeAudio() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    @Override
    public boolean getEnable() {
        return this.sound;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.blast_furnace.getSoundEvent();
    }

    public void initiate(int soundEvent) {
        if (this.getTypeAudio() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }

        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (!getEnable()) {
            return;
        }
        if (getSound() == null) {
            return;
        }
        if (soundEvent == 0) {
            this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 1F, 1);
        } else if (soundEvent == 1) {
            new PacketStopSound(getWorld(), this.pos);
            this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundCategory.BLOCKS, 1F, 1);
        } else {
            new PacketStopSound(getWorld(), this.pos);
        }
    }


    @Override
    public void updateAfterAssembly() {
        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IBlastInputFluid.class
                );
        this.setInputFluid((IBlastInputFluid) this.getWorld().getTileEntity(pos1.get(0)));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IBlastHeat.class
                );
        this.setHeat((IBlastHeat) this.getWorld().getTileEntity(pos1.get(0)));
    }

    @Override
    public void usingBeforeGUI() {


    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            new PacketUpdateFieldTile(this, "sound", this.sound);
            if (this.invSlotBlastFurnace.isEmpty()) {
                outputStack = ItemStack.EMPTY;
            } else {
                final ItemStack content = this.invSlotBlastFurnace.get();
                int meta = content.getItemDamage();
                if (content.getItem().equals(Items.IRON_INGOT)) {
                    this.outputStack = IUItem.advIronIngot;
                } else {
                    if (content.getItem() instanceof ItemIngots && meta == 3) {
                        this.outputStack = new ItemStack(IUItem.crafting_elements, 1, 480);
                    } else {
                        this.outputStack = new ItemStack(IUItem.crafting_elements, 1, 479);

                    }
                }
            }
        } else {

            IBlockState blockState1 = this.block
                    .getDefaultState()
                    .withProperty(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, "global"))
                    .withProperty(
                            BlockTileEntity.facingProperty,
                            this.getFacing()
                    );
            this.dataBlock = new DataBlock(blockState1);
            IBakedModel model = Minecraft
                    .getMinecraft()
                    .getBlockRendererDispatcher()
                    .getModelForState(blockState1);
            this.dataBlock.setState(model);
            IBlockState  blockState2 = this.block
                    .getDefaultState()
                    .withProperty(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, "global_active"))
                    .withProperty(
                            BlockTileEntity.facingProperty,
                            this.getFacing()
                    );
            this.dataBlock_active = new DataBlock(blockState2);
            IBakedModel model1 = Minecraft
                    .getMinecraft()
                    .getBlockRendererDispatcher()
                    .getModelForState(blockState2);
            this.dataBlock_active.setState(model1);
        }

    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.full) {
            if (this.getActive()) {
                this.setActive(false);
                initiate(2);
            }
            return;
        }

        MutableObject<ItemStack> output1 = new MutableObject<>();
        if (this.fluidSlot.transferToTank(
                this.tank1,
                output1,
                true
        ) && (output1.getValue() == null || this.output1.canAdd(output1.getValue()))) {
            this.fluidSlot.transferToTank(this.tank1, output1, false);
            if (output1.getValue() != null) {
                this.output1.add(output1.getValue());
            }
        }


        if (!this.invSlotBlastFurnace.isEmpty() && !outputStack.isEmpty() && this.output.canAdd(outputStack)) {
            int amount_stream = tank.getFluidAmount();
            if (this.heat.getEnergy() == this.heat.getCapacity()) {
                int bar1 = bar;
                if (amount_stream < bar1 * 2) {
                    bar1 = amount_stream / 2;
                }
                if (bar1 > 0) {
                    if (progress == 0) {
                        this.setActive(true);
                        initiate(0);
                    }
                    if (!this.getActive()) {
                        this.setActive(true);
                    }
                    progress += 1 + (0.25 * (bar1 - 1));
                    tank.drain(Math.min(bar1 * 2, this.tank.getFluidAmount()), true);
                    if (progress >= 3600 && this.output.add(outputStack)) {
                        progress = 0;
                        this.invSlotBlastFurnace.get(0).shrink(1);
                        this.setActive(false);
                        initiate(2);
                    }
                }
            }
            double heat = this.heat.getEnergy();
            if (heat > 500 && this.tank.getFluidAmount() + 2 < this.tank.getCapacity()) {
                int size = this.tank1.getFluidAmount();
                int size_stream = this.tank.getCapacity() - this.tank.getFluidAmount();
                int size1 = size / 5;
                size1 = Math.min(size1, 10);
                if (size1 > 0) {
                    int add = Math.min(size_stream / 2, size1);
                    if (add > 0) {
                        this.tank.fill(new FluidStack(FluidName.fluidsteam.getInstance(), add * 2), true);
                        this.getInputFluid().getFluidTank().drain(add * 5, true);

                    }
                }
            }

        } else if (this.getActive()) {
            this.setActive(false);
        }
        if (heat.getEnergy() > 0) {
            heat.useEnergy(1);
        }

    }

    @Override
    public IBlastHeat getHeat() {
        return blastHeat;
    }

    @Override
    public void setHeat(final IBlastHeat blastHeat) {
        this.blastHeat = blastHeat;
        try {
            this.heat.onUnloaded();
        } catch (Exception ignored) {
        }
        if (this.blastHeat != null) {
            this.heat.setParent((TileEntityInventory) blastHeat);
            this.heat.onLoaded();
        }
    }

    @Override
    public IBlastInputFluid getInputFluid() {
        return blastInputFluid;
    }

    @Override
    public void setInputFluid(final IBlastInputFluid blastInputFluid) {
        this.blastInputFluid = blastInputFluid;
        if (this.blastInputFluid == null) {
            this.tank1 = new FluidTank(12000);
            new PacketUpdateFieldTile(this,"fluidtank1",false);
        } else {
            this.tank1 = this.blastInputFluid.getFluidTank();
            new PacketUpdateFieldTile(this,"fluidtank2",tank1);
        }
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");
        this.bar = nbttagcompound.getInteger("bar");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("sound", this.sound);
        nbttagcompound.setInteger("bar", this.bar);
        return nbttagcompound;
    }

    @Override
    public double getProgress() {
        return this.progress;
    }


    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        switch ((int) i) {
            case 0:
                this.bar = Math.min(this.bar + 1, 5);
                break;
            case 1:
                this.bar = Math.max(1, this.bar - 1);
                break;
            case 10:

                sound = !sound;
                new PacketUpdateFieldTile(this, "sound", this.sound);

                if (!sound) {
                    if (this.getTypeAudio() == EnumTypeAudio.ON) {
                        setType(EnumTypeAudio.OFF);
                        initiate(2);

                    }
                }
                break;
        }
    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("sound")) {
            try {
                this.sound = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
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

        if ((this.full && this.activate)) {
            if (!this.getWorld().isRemote && player
                    .getHeldItem(hand)
                    .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                return ModUtils.interactWithFluidHandler(player, hand,
                        this.blastInputFluid
                                .getFluid()
                                .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
                );
            }else {
                return super.onActivated(player, hand, side, hitX, hitY, hitZ);
            }
        }

        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public ContainerBlastFurnace getGuiContainer(final EntityPlayer entityPlayer) {
        if (!this.entityPlayerList.contains(entityPlayer)) {
            this.entityPlayerList.add(entityPlayer);
        }
        return new ContainerBlastFurnace(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiBlastFurnace getGui(final EntityPlayer entityPlayer, final boolean b) {

        return new GuiBlastFurnace(this.getGuiContainer(entityPlayer));
    }


    @Override
    public IMainMultiBlock getMain() {
        return this;
    }

    @Override
    public void setMainMultiElement(final IMainMultiBlock main) {

    }

    @Override
    public boolean isMain() {
        return true;
    }


    @Override
    public void onNetworkEvent(final int var1) {

    }

}
