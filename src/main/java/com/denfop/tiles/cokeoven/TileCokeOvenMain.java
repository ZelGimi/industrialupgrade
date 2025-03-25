package com.denfop.tiles.cokeoven;

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
import com.denfop.blocks.mechanism.BlockCokeOven;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerCokeOven;
import com.denfop.gui.GuiCokeOven;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotDrainTank;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileCokeOvenMain extends TileMultiBlockBase implements IMain,
        IUpdatableTileEvent,
        IAudioFixer {

    public final InvSlotDrainTank fluidSlot1;
    public final InvSlotOutput output2;
    public final InvSlotOutput output1;
    public final InvSlotFluidByList fluidSlot;
    public final HeatComponent heat;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public FluidTank tank = null;
    public boolean load = false;
    public InvSlotCokeOven invSlotBlastFurnace = new InvSlotCokeOven(this);
    public InvSlotOutput output = new InvSlotOutput(this, 1);
    public FluidTank tank1 = null;

    public IHeat blastHeat;
    public IInputFluid blastInputFluid;
    public IOutputFluid blastOutputFluid;
    public List<EntityPlayer> entityPlayerList;
    public double progress = 0;
    public int bar = 1;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();

    private boolean sound = true;

    public TileCokeOvenMain() {
        super(InitMultiBlockSystem.cokeOvenMultiBlock);
        this.full = false;
        this.entityPlayerList = new ArrayList<>();
        this.fluidSlot = new InvSlotFluidByList(this, 1, FluidName.fluidsteam.getInstance());
        this.fluidSlot1 = new InvSlotDrainTank(this, InvSlot.TypeItemSlot.INPUT, 1,
                InvSlotFluid.TypeFluidSlot.OUTPUT, FluidName.fluidcreosote.getInstance()
        );
        ;
        this.output1 = new InvSlotOutput(this, 1);
        this.output2 = new InvSlotOutput(this, 1);
        this.heat = this.addComponent(HeatComponent.asBasicSink(this, 1000));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.2));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.5));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockCokeOven.coke_oven_main;
    }

    public BlockTileEntity getBlock() {
        return IUItem.cokeoven;
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
            EncoderHandler.encode(packet, tank);
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

        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer).getDisplayName());

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
                        IInputFluid.class
                );
        this.setInputFluid((IInputFluid) this.getWorld().getTileEntity(pos1.get(0)));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IOutputFluid.class
                );
        this.setOutputFluid((IOutputFluid) this.getWorld().getTileEntity(pos1.get(0)));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IHeat.class
                );
        this.setHeat((IHeat) this.getWorld().getTileEntity(pos1.get(0)));
    }

    @Override
    public void usingBeforeGUI() {


    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
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
            this.fluidSlot1.transferToTank(this.tank1, output1, false);
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
                    progress += 1 + (0.25 * (bar1 - 1));
                    tank.drain(Math.min(bar1, this.tank.getFluidAmount()), true);
                    if (progress >= 3600) {
                        progress = 0;
                        this.invSlotBlastFurnace.get(0).shrink(1);
                        this.setActive(false);
                        this.tank1.fill(new FluidStack(FluidName.fluidcreosote.getInstance(), 500), true);
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
            this.heat.setParent((TileEntityInventory) blastHeat);
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
        if (this.getWorld().isRemote) {
            return false;
        }
        if (!(!this.full || !this.activate)) {
            if (!this.getWorld().isRemote && player
                    .getHeldItem(hand)
                    .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                return ModUtils.interactWithFluidHandler(player, hand,
                        this.blastInputFluid
                                .getFluid()
                                .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
                );
            }
        }

        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public ContainerCokeOven getGuiContainer(final EntityPlayer entityPlayer) {
        if (!this.entityPlayerList.contains(entityPlayer)) {
            this.entityPlayerList.add(entityPlayer);
        }
        return new ContainerCokeOven(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiCokeOven getGui(final EntityPlayer entityPlayer, final boolean b) {

        return new GuiCokeOven(this.getGuiContainer(entityPlayer));
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
