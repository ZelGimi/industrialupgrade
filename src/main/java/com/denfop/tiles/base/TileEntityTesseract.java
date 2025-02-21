package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.tesseract.Channel;
import com.denfop.api.tesseract.ITesseract;
import com.denfop.api.tesseract.TesseractSystem;
import com.denfop.api.tesseract.TypeChannel;
import com.denfop.api.tesseract.TypeMode;
import com.denfop.api.tesseract.event.EventAdderChannel;
import com.denfop.api.tesseract.event.EventLoadTesseract;
import com.denfop.api.tesseract.event.EventRemoverChannel;
import com.denfop.api.tesseract.event.EventUnLoadTesseract;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerTesseract;
import com.denfop.gui.GuiTesseract;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileEntityTesseract extends TileEntityInventory implements IUpdatableTileEvent, ITesseract {

    private final Energy energy;
    private final Fluids fluids;
    private final Fluids.InternalFluidTank tank;
    private final InvSlot slot;

    public Channel channel;
    List<Channel> channelList = new ArrayList<>();

    List<Channel> publicChannel = new ArrayList<>();

    public TileEntityTesseract() {
        this.energy = this.addComponent(new Energy(this, Integer.MAX_VALUE, ModUtils.allFacings, ModUtils.allFacings, 14));
        this.fluids = this.addComponent(new Fluids(this));
        tank = this.fluids.addTank("tank", 64000);
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT_OUTPUT, 18);
        this.getComponentPrivate().setActivate(true);
    }

    @Override
    public World getLevel() {
        return world;
    }

    @Override
    public ContainerTesseract getGuiContainer(final EntityPlayer var1) {
        return new ContainerTesseract(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiTesseract(getGuiContainer(var1));

    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(this.getChannels().size());
        customPacketBuffer.writeInt(publicChannel.size());
        try {
            EncoderHandler.encode(customPacketBuffer, this.getSlotItem());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Channel channel : this.getChannels()) {
            try {
                EncoderHandler.encode(customPacketBuffer, channel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        publicChannel = TesseractSystem.instance.getPublicChannels(this.getWorld());
        for (Channel channel : publicChannel) {
            try {
                EncoderHandler.encode(customPacketBuffer, channel);
                EncoderHandler.encode(customPacketBuffer, channel.getPos());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        customPacketBuffer.writeBoolean(channel != null);
        try {
            EncoderHandler.encode(customPacketBuffer, channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return customPacketBuffer;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.post(new EventLoadTesseract(this, this.getWorld()));
        }
    }

    @Override
    public void onUnloaded() {
        if (!this.getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.post(new EventUnLoadTesseract(this, this.getWorld()));
        }
        super.onUnloaded();
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        final List<Channel> channels = new ArrayList<>(getChannels());
        nbtTagCompound.setInteger("size", channels.size());
        for (int i = 0; i < channels.size(); i++) {
            final Channel channel = channels.get(i);
            nbtTagCompound.setTag("channel_" + i, channel.writeNBT());
        }
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        int size = nbtTagCompound.getInteger("size");
        for (int i = 0; i < size; i++) {
            final Channel channel = new Channel(nbtTagCompound.getCompoundTag("channel_" + i));
            channel.setTesseract(this);
            channelList.add(channel);
        }
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.tesseract;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        channelList.clear();
        int size = customPacketBuffer.readInt();
        int size1 = customPacketBuffer.readInt();
        try {
            InvSlot invSlot = (InvSlot) DecoderHandler.decode(customPacketBuffer);
            for (int i = 0; i < invSlot.size(); i++) {
                this.slot.put(i, invSlot.get(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < size; i++) {
            try {
                Channel channel = (Channel) DecoderHandler.decode(customPacketBuffer);
                channel.setTesseract(this);
                channelList.add(channel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        publicChannel.clear();
        for (int i = 0; i < size1; i++) {
            try {
                Channel channel = (Channel) DecoderHandler.decode(customPacketBuffer);
                BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
                ITesseract tesseract = (ITesseract) world.getTileEntity(pos);
                channel.setTesseract(tesseract);
                publicChannel.add(channel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        boolean hasChannel = customPacketBuffer.readBoolean();
        if (hasChannel) {
            try {
                channel = (Channel) DecoderHandler.decode(customPacketBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (channel != null) {
            channel.setTesseract(this);
        }

    }

    @Override
    public Channel getChannel(final int channel) {
        return getChannels().get(channel);
    }

    @Override
    public List<Channel> getChannels() {
        return channelList;
    }

    @Override
    public String getPlayer() {
        return this.getComponentPrivate().getPlayers().get(0);
    }

    @Override
    public Energy getEnergy() {
        return this.energy;
    }

    @Override
    public Fluids.InternalFluidTank getTank() {
        return tank;
    }

    @Override
    public InvSlot getSlotItem() {
        return slot;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, double var2) {
        switch ((int) var2) {
            case 1:
                int maxValue1 = 0;
                List<Integer> integerList1 = new ArrayList<>();
                for (Channel channel1 : getChannels()) {
                    integerList1.add(channel1.getChannel());
                }
                while (integerList1.contains(maxValue1)) {
                    maxValue1++;
                }
                this.channel = new Channel(maxValue1, this, TypeMode.NONE, TypeChannel.NONE, false);
                channelList.add(channel);
                MinecraftForge.EVENT_BUS.post(new EventAdderChannel(channel, this.getWorld()));
                break;

            case 2:
                int maxValue2 = 0;
                List<Integer> integerList2 = new ArrayList<>();
                for (Channel channel1 : getChannels()) {
                    integerList2.add(channel1.getChannel());
                }
                while (integerList2.contains(maxValue2) || maxValue2 < channel.getChannel()) {
                    maxValue2++;
                }
                MinecraftForge.EVENT_BUS.post(new EventRemoverChannel(channel, this.getWorld()));
                channelList.remove(channel);
                this.channel = new Channel(maxValue2, this, channel.getMode(), channel.getTypeChannel(), channel.isPrivate());
                channelList.add(channel);
                MinecraftForge.EVENT_BUS.post(new EventAdderChannel(channel, this.getWorld()));
                break;

            case 3:
                int maxValue3 = this.channel.getChannel();
                List<Integer> integerList3 = new ArrayList<>();
                for (Channel channel1 : getChannels()) {
                    integerList3.add(channel1.getChannel());
                }
                while (maxValue3 != -1 && integerList3.contains(maxValue3)) {
                    maxValue3--;
                }
                if (maxValue3 != -1) {
                    MinecraftForge.EVENT_BUS.post(new EventRemoverChannel(channel, this.getWorld()));
                    channelList.remove(channel);
                    this.channel = new Channel(maxValue3, this, channel.getMode(), channel.getTypeChannel(), channel.isPrivate());
                    channelList.add(channel);
                    MinecraftForge.EVENT_BUS.post(new EventAdderChannel(channel, this.getWorld()));
                }
                break;

            case 4:
                var2 -= 4;
                var2 *= 10;
                var2 = Math.round(var2);
                int mode4 = ((int) var2) % 2;
                MinecraftForge.EVENT_BUS.post(new EventRemoverChannel(channel, this.getWorld()));
                if (mode4 == 0) {
                    switch (this.channel.getMode()) {
                        case INPUT:
                            this.channel.setMode(TypeMode.NONE);
                            break;
                        case OUTPUT:
                            this.channel.setMode(TypeMode.INOUT);
                            break;
                        case NONE:
                            this.channel.setMode(TypeMode.INPUT);
                            break;
                        case INOUT:
                            this.channel.setMode(TypeMode.OUTPUT);
                            break;
                    }
                } else {
                    switch (this.channel.getMode()) {
                        case OUTPUT:
                            this.channel.setMode(TypeMode.NONE);
                            break;
                        case INPUT:
                            this.channel.setMode(TypeMode.INOUT);
                            break;
                        case NONE:
                            this.channel.setMode(TypeMode.OUTPUT);
                            break;
                        case INOUT:
                            this.channel.setMode(TypeMode.INPUT);
                            break;
                    }
                }
                MinecraftForge.EVENT_BUS.post(new EventAdderChannel(channel, this.getWorld()));
                break;

            case 5:
                var2 -= 5;
                var2 *= 10;
                var2 = Math.round(var2);
                int mode5 = ((int) var2) % 3;
                MinecraftForge.EVENT_BUS.post(new EventRemoverChannel(channel, this.getWorld()));
                switch (mode5) {
                    case 0:
                        this.channel.setTypeChannel((this.channel.getTypeChannel() == TypeChannel.ENERGY)
                                ? TypeChannel.NONE
                                : TypeChannel.ENERGY);
                        break;
                    case 1:
                        this.channel.setTypeChannel((this.channel.getTypeChannel() == TypeChannel.FLUID)
                                ? TypeChannel.NONE
                                : TypeChannel.FLUID);
                        break;
                    case 2:
                        this.channel.setTypeChannel((this.channel.getTypeChannel() == TypeChannel.ITEM)
                                ? TypeChannel.NONE
                                : TypeChannel.ITEM);
                        break;
                }

                MinecraftForge.EVENT_BUS.post(new EventAdderChannel(channel, this.getWorld()));
                break;

            case 6:
                MinecraftForge.EVENT_BUS.post(new EventRemoverChannel(channel, this.getWorld()));
                this.channel.setPrivate(!this.channel.isPrivate());
                MinecraftForge.EVENT_BUS.post(new EventAdderChannel(channel, this.getWorld()));
                break;

            case 7:
                MinecraftForge.EVENT_BUS.post(new EventRemoverChannel(channel, this.getWorld()));
                this.channel.setActive(!this.channel.isActive());
                MinecraftForge.EVENT_BUS.post(new EventAdderChannel(channel, this.getWorld()));
                break;
            case 8:
                this.channel = null;
                break;
            case 9:
                MinecraftForge.EVENT_BUS.post(new EventRemoverChannel(channel, this.getWorld()));
                channelList.remove(channel);
                channel = null;
                break;
            case 10:
                var2 -= 10;
                if (var2 == 0) {
                    this.channel = this.getChannels().get(0);
                } else {
                    int col = (int) (var2 * 10);
                    int i = 0;
                    var2 *= 10;
                    var2 -= col;
                    while (i < col) {
                        var2 *= 10;
                        i++;
                    }
                    var2 = Math.round(var2);
                    List<Channel> channels = new ArrayList<>(this.getChannels());
                    var2 %= channels.size();
                    this.channel = channels.get((int) var2);
                }
                break;
        }


    }

    public List<Channel> getPublicChannel() {
        return publicChannel;
    }

}
