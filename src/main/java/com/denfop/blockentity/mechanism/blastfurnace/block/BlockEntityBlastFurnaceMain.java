package com.denfop.blockentity.mechanism.blastfurnace.block;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.sound.AudioFixer;
import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.mechanism.blastfurnace.api.IBlastHeat;
import com.denfop.blockentity.mechanism.blastfurnace.api.IBlastInputFluid;
import com.denfop.blockentity.mechanism.blastfurnace.api.IBlastMain;
import com.denfop.blockentity.mechanism.blastfurnace.api.InventoryBlastFurnace;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockBase;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBlastFurnaceEntity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuBlastFurnace;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.items.resource.ItemIngots;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.screen.ScreenBlastFurnace;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.apache.commons.lang3.mutable.MutableObject;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockEntityBlastFurnaceMain extends BlockEntityMultiBlockBase implements IBlastMain,
        IUpdatableTileEvent,
        AudioFixer {

    public final FluidTank tank;
    public final InventoryOutput output1;
    public final InventoryFluidByList fluidSlot;
    public final HeatComponent heat;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public boolean load = false;
    public ItemStack outputStack = ItemStack.EMPTY;
    public InventoryBlastFurnace invSlotBlastFurnace = new InventoryBlastFurnace(this);
    public InventoryOutput output = new InventoryOutput(this, 1);
    public FluidTank tank1 = new FluidTank(12000);

    public IBlastHeat blastHeat;
    public IBlastInputFluid blastInputFluid;
    public List<Player> entityPlayerList;
    public double progress = 0;
    public int bar = 1;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();

    private boolean sound = true;


    public BlockEntityBlastFurnaceMain(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.blastFurnaceMultiBlock, BlockBlastFurnaceEntity.blast_furnace_main, pos, state);
        this.full = false;
        final Fluids fluids = this.addComponent(new Fluids(this));

        this.tank = fluids.addTank("tank", 10000, Inventory.TypeItemSlot.INPUT,
                Fluids.fluidPredicate(FluidName.fluidsteam.getInstance().get())
        );
        this.entityPlayerList = new ArrayList<>();
        this.fluidSlot = new InventoryFluidByList(this, 1, net.minecraft.world.level.material.Fluids.WATER);
        this.output1 = new InventoryOutput(this, 1);
        this.heat = this.addComponent(HeatComponent.asBasicSink(this, 1000));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.2));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.5));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();

        if (this.getActive() && level.getGameTime() % 5 == 0) {
            RandomSource rand = RandomSource.create();
            Direction facing = this.getFacing();

            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    double offsetX = 0;
                    double offsetZ = 0;

                    switch (facing) {
                        case NORTH -> offsetZ = -0.3;
                        case SOUTH -> offsetZ = 0.3;
                        case WEST -> offsetX = -0.3;
                        case EAST -> offsetX = 0.3;
                    }

                    Level world = getLevel();
                    if (world == null) return;

                    Vec3 particlePos = new Vec3(
                            pos.getX() + 0.5 + dx + offsetX * -1 + rand.nextDouble() * 0.2 - 0.1,
                            pos.getY() + 1.2 + rand.nextDouble() * 0.2,
                            pos.getZ() + 0.5 + dz + offsetZ * -1 + rand.nextDouble() * 0.2 - 0.1
                    );


                    for (int i = 0; i < 1 + rand.nextInt(2); i++) {
                        world.addParticle(ParticleTypes.FLAME, particlePos.x, particlePos.y, particlePos.z, 0, 0.05, 0);
                    }

                    for (int j = 0; j < 1 + rand.nextInt(2); j++) {
                        world.addParticle(ParticleTypes.SMOKE, particlePos.x, particlePos.y + 0.3, particlePos.z, 0, 0.02, 0);
                    }

                    if (rand.nextInt(3) == 0) {
                        world.addParticle(new DustParticleOptions(new Vector3f(1, 0, 0), 1),
                                pos.getX() + 0.5 + dx + offsetX * -1 + rand.nextDouble() * 0.4 - 0.2,
                                pos.getY() + 1.4 + rand.nextDouble() * 0.3,
                                pos.getZ() + 0.5 + dz + offsetZ * -1 + rand.nextDouble() * 0.4 - 0.2,
                                1.0f, 0.3f, 0.1f);
                    }

                    if (rand.nextInt(5) == 0) {
                        world.addParticle(ParticleTypes.LAVA,
                                pos.getX() + 0.5 + dx + offsetX * -4 + rand.nextDouble() * 0.4 - 0.2,
                                pos.getY() + 1.6,
                                pos.getZ() + 0.5 + dz + offsetZ * -4 + rand.nextDouble() * 0.4 - 0.2,
                                0, 0.1, 0);
                    }
                }
            }
        }
    }


    /*  @SideOnly(Side.CLIENT)
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
      */
    public MultiBlockEntity getTeBlock() {
        return BlockBlastFurnaceEntity.blast_furnace_main;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blastfurnace.getBlock(getTeBlock().getId());
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
            heat.buffer.storage = (double) DecoderHandler.decode(customPacketBuffer);

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
            EncoderHandler.encode(packet, heat.buffer.storage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.blastfurnace.info1"));
        tooltip.add(Localization.translate("iu.blastfurnace.info3") + Localization.translate(new ItemStack(
                IUItem.blastfurnace.getItem(0)
        ).getDescriptionId()));
        tooltip.add(Localization.translate("iu.blastfurnace.info4"));
        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer.getItem()).getDisplayName().getString());
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
            this.getWorld().playSound(null, this.pos, getSound(), SoundSource.BLOCKS, 1F, 1);
        } else if (soundEvent == 1) {
            new PacketStopSound(getWorld(), this.pos);
            this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundSource.BLOCKS, 1F, 1);
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
        this.setInputFluid((IBlastInputFluid) this.getWorld().getBlockEntity(pos1.get(0)));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IBlastHeat.class
                );
        this.setHeat((IBlastHeat) this.getWorld().getBlockEntity(pos1.get(0)));
    }

    @Override
    public void usingBeforeGUI() {


    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            new PacketUpdateFieldTile(this, "sound", this.sound);
            if (this.invSlotBlastFurnace.isEmpty()) {
                outputStack = ItemStack.EMPTY;
            } else {
                final ItemStack content = this.invSlotBlastFurnace.get(0);
                if (content.getItem().equals(Items.IRON_INGOT)) {
                    this.outputStack = IUItem.advIronIngot;
                } else {
                    if (content.getItem() instanceof ItemIngots && IUItem.iuingot.getMeta((ItemIngots) content.getItem()) == 3) {
                        this.outputStack = new ItemStack(IUItem.crafting_elements.getStack(480));
                    } else {
                        this.outputStack = new ItemStack(IUItem.crafting_elements.getStack(479), 1);

                    }
                }
            }
        } else {

        /*    IBlockState blockState1 = this.block
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

         */
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
                    tank.drain(Math.min(bar1 * 2, this.tank.getFluidAmount()), IFluidHandler.FluidAction.EXECUTE);
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
                        this.tank.fill(new FluidStack(FluidName.fluidsteam.getInstance().get(), add * 2), IFluidHandler.FluidAction.EXECUTE);
                        this.getInputFluid().getFluidTank().drain(add * 5, IFluidHandler.FluidAction.EXECUTE);

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
            this.heat.setParent((BlockEntityInventory) blastHeat);
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
            new PacketUpdateFieldTile(this, "fluidtank1", false);
        } else {
            this.tank1 = this.blastInputFluid.getFluidTank();
            new PacketUpdateFieldTile(this, "fluidtank2", tank1);
        }
    }


    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");
        this.bar = nbttagcompound.getInt("bar");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putBoolean("sound", this.sound);
        nbttagcompound.putInt("bar", this.bar);
        return nbttagcompound;
    }

    @Override
    public double getProgress() {
        return this.progress;
    }


    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
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
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if ((this.full && this.activate)) {
            if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player.getItemInHand(hand)) != null) {
                return ModUtils.interactWithFluidHandler(player, hand,
                        this.blastInputFluid
                                .getFluid()
                                .getCapability(ForgeCapabilities.FLUID_HANDLER, side)
                );
            } else {
                return super.onActivated(player, hand, side, vec3);
            }
        }

        return super.onActivated(player, hand, side, vec3);
    }


    @Override
    public ContainerMenuBlastFurnace getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuBlastFurnace(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(final Player entityPlayer, final ContainerMenuBase<? extends CustomWorldContainer> b) {

        return new ScreenBlastFurnace((ContainerMenuBlastFurnace) b);
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


    @Override
    public int getBlockLevel() {
        return 0;
    }
}
