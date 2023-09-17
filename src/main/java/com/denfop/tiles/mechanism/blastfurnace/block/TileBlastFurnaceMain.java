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
import com.denfop.componets.Fluids;
import com.denfop.componets.HeatComponent;
import com.denfop.container.ContainerBlastFurnace;
import com.denfop.gui.GuiBlastFurnace;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastHeat;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastInputFluid;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastMain;
import com.denfop.tiles.mechanism.blastfurnace.api.InvSlotBlastFurnace;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileBlastFurnaceMain extends TileMultiBlockBase implements IBlastMain,
        IUpdatableTileEvent,
        IAudioFixer {

    public final FluidTank tank;
    public final InvSlotOutput output1;
    public final InvSlotFluidByList fluidSlot;
    public final HeatComponent heat;
    public boolean load = false;
    public InvSlotBlastFurnace invSlotBlastFurnace = new InvSlotBlastFurnace(this);
    public InvSlotOutput output = new InvSlotOutput(this, "output", 1);
    public FluidTank tank1 = null;

    public IBlastHeat blastHeat;
    public IBlastInputFluid blastInputFluid;
    public List<EntityPlayer> entityPlayerList;
    public double progress = 0;
    public int bar = 1;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();

    private boolean sound = true;

    public TileBlastFurnaceMain() {
        super(InitMultiBlockSystem.blastFurnaceMultiBlock);
        this.full = false;
        final Fluids fluids = this.addComponent(new Fluids(this));

        this.tank = fluids.addTank("tank", 10000, InvSlot.TypeItemSlot.INPUT,
                Fluids.fluidPredicate(FluidName.fluidsteam.getInstance())
        );
        this.entityPlayerList = new ArrayList<>();
        this.fluidSlot = new InvSlotFluidByList(this, 1, FluidRegistry.WATER);
        this.output1 = new InvSlotOutput(this, "output1", 1);
        this.heat = this.addComponent(HeatComponent.asBasicSink(this, 1000));
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.add(Localization.translate("iu.blastfurnace.info1"));
        tooltip.add(Localization.translate("iu.blastfurnace.info3") + Localization.translate(new ItemStack(
                IUItem.blastfurnace,
                1,
                0
        ).getUnlocalizedName()));
        tooltip.add(Localization.translate("iu.blastfurnace.info4"));
        tooltip.add(Localization.translate("iu.blastfurnace.info5") + Localization.translate(IUItem.ForgeHammer.getUnlocalizedName()));
        tooltip.add(Localization.translate("iu.blastfurnace.info6"));
    }

    public EnumTypeAudio getType() {
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
        if (this.getType() == valuesAudio[soundEvent % valuesAudio.length]) {
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


        if (!this.invSlotBlastFurnace.isEmpty()) {
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
                    if (progress >= 3600 && this.output.add(IUItem.advIronIngot)) {
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
            this.tank1 = null;
        } else {
            this.tank1 = this.blastInputFluid.getFluidTank();
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
                    if (this.getType() == EnumTypeAudio.ON) {
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
                return FluidUtil.interactWithFluidHandler(player, hand,
                        this.blastInputFluid
                                .getFluid()
                                .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
                );
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
