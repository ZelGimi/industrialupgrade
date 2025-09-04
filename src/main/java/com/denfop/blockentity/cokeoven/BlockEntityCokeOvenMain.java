package com.denfop.blockentity.cokeoven;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.sound.AudioFixer;
import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockBase;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockCokeOvenEntity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuCokeOven;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryDrainTank;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.screen.ScreenCokeOven;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockEntityCokeOvenMain extends BlockEntityMultiBlockBase implements IMain,
        IUpdatableTileEvent,
        AudioFixer {

    public final InventoryDrainTank fluidSlot1;
    public final InventoryOutput output2;
    public final InventoryOutput output1;
    public final InventoryFluidByList fluidSlot;
    public final HeatComponent heat;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public FluidTank tank = null;
    public boolean load = false;
    public InventoryCokeOven invSlotBlastFurnace = new InventoryCokeOven(this);
    public InventoryOutput output = new InventoryOutput(this, 1);
    public FluidTank tank1 = null;

    public IHeat blastHeat;
    public IInputFluid blastInputFluid;
    public IOutputFluid blastOutputFluid;
    public List<Player> entityPlayerList;
    public double progress = 0;
    public int bar = 1;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();

    private boolean sound = true;

    public BlockEntityCokeOvenMain(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.cokeOvenMultiBlock, BlockCokeOvenEntity.coke_oven_main, pos, state);
        this.full = false;
        this.entityPlayerList = new ArrayList<>();
        this.fluidSlot = new InventoryFluidByList(this, 1, FluidName.fluidsteam.getInstance().get());
        this.fluidSlot1 = new InventoryDrainTank(this, Inventory.TypeItemSlot.INPUT, 1,
                InventoryFluid.TypeFluidSlot.OUTPUT, FluidName.fluidcreosote.getInstance().get()
        );
        ;
        this.output1 = new InventoryOutput(this, 1);
        this.output2 = new InventoryOutput(this, 1);
        this.heat = this.addComponent(HeatComponent.asBasicSink(this, 1000));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.2));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.5));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockCokeOvenEntity.coke_oven_main;
    }

    public BlockTileEntity getBlock() {
        return IUItem.cokeoven.getBlock(getTeBlock().getId());
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            full = (boolean) DecoderHandler.decode(customPacketBuffer);
            tank = (FluidTank) DecoderHandler.decode(customPacketBuffer);
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
            EncoderHandler.encode(packet, tank);
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

        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer.getItem()).getDisplayName().getString());

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
                        IInputFluid.class
                );
        this.setInputFluid((IInputFluid) this.getWorld().getBlockEntity(pos1.get(0)));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IOutputFluid.class
                );
        this.setOutputFluid((IOutputFluid) this.getWorld().getBlockEntity(pos1.get(0)));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IHeat.class
                );
        this.setHeat((IHeat) this.getWorld().getBlockEntity(pos1.get(0)));
    }

    @Override
    public void usingBeforeGUI() {


    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            new PacketUpdateFieldTile(this, "sound", this.sound);
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
                this.tank,
                output1,
                true
        ) && (output1.getValue() == null || this.output1.canAdd(output1.getValue()))) {
            this.fluidSlot.transferToTank(this.tank, output1, false);
            if (output1.getValue() != null) {
                this.output1.add(output1.getValue());
            }
        }
        output1 = new MutableObject<>();
        if (this.fluidSlot1.transferFromTank(
                this.tank1,
                output1,
                true
        ) && (output1.getValue() == null || this.output2.canAdd(output1.getValue()))) {
            this.fluidSlot1.transferFromTank(this.tank1, output1, false);
            if (output1.getValue() != null) {
                this.output2.add(output1.getValue());
            }
        }

        if (this.heat.getEnergy() > 500 && !this.invSlotBlastFurnace.isEmpty() && this.tank1 != null && this.tank1.getFluidAmount() + 500 <= this.tank1.getCapacity()) {
            int amount_stream = tank.getFluidAmount();
            if (this.heat.getEnergy() == this.heat.getCapacity() && tank.getFluidAmount() > 0) {
                int bar1 = bar;
                if (amount_stream < bar1) {
                    bar1 = amount_stream;
                }
                if (bar1 > 0) {
                    if (progress == 0) {
                        this.setActive(true);
                        initiate(0);
                    }
                    if (!this.getActive()) {
                        this.setActive(true);
                    }
                    progress += 1 + (0.5 * (bar1 - 1));
                    tank.drain(Math.min(bar1, this.tank.getFluidAmount()), IFluidHandler.FluidAction.EXECUTE);
                    if (progress >= 3600) {
                        progress = 0;
                        this.invSlotBlastFurnace.get(0).shrink(1);
                        this.setActive(false);
                        this.tank1.fill(new FluidStack(FluidName.fluidcreosote.getInstance().get(), 500), IFluidHandler.FluidAction.EXECUTE);
                        initiate(2);
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
    public IHeat getHeat() {
        return blastHeat;
    }

    @Override
    public void setHeat(final IHeat blastHeat) {
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
    public IInputFluid getInputFluid() {
        return blastInputFluid;
    }

    @Override
    public void setInputFluid(final IInputFluid blastInputFluid) {
        this.blastInputFluid = blastInputFluid;
        if (this.blastInputFluid == null) {
            this.tank = null;
        } else {
            this.tank = this.blastInputFluid.getFluidTank();
        }
    }

    @Override
    public IOutputFluid getOutputFluid() {
        return blastOutputFluid;
    }

    @Override
    public void setOutputFluid(final IOutputFluid blastInputFluid) {
        this.blastOutputFluid = blastInputFluid;
        if (this.blastOutputFluid == null) {
            this.tank1 = null;
        } else {
            this.tank1 = this.blastOutputFluid.getFluidTank();
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
        if (this.getWorld().isClientSide) {
            return false;
        }
        if (!(!this.full || !this.activate)) {
            if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player.getItemInHand(hand)) != null) {
                return ModUtils.interactWithFluidHandler(player, hand,
                        this.blastInputFluid
                                .getFluid()
                                .getCapability(Capabilities.FluidHandler.BLOCK, side)
                );
            }
        }

        return super.onActivated(player, hand, side, vec3);
    }


    @Override
    public ContainerMenuCokeOven getGuiContainer(final Player entityPlayer) {
        if (!this.entityPlayerList.contains(entityPlayer)) {
            this.entityPlayerList.add(entityPlayer);
        }
        return new ContainerMenuCokeOven(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenCokeOven((ContainerMenuCokeOven) menu);
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
