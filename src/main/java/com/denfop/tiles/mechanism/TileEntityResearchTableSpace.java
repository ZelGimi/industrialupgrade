package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.space.EnumLevels;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.colonies.Colony;
import com.denfop.api.space.colonies.InfoSends;
import com.denfop.api.space.colonies.Sends;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.fakebody.Data;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.api.space.fakebody.SpaceOperation;
import com.denfop.api.space.research.api.IResearchTable;
import com.denfop.api.space.research.event.ResearchTableLoadEvent;
import com.denfop.api.space.research.event.ResearchTableReLoadEvent;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerResearchTableSpace;
import com.denfop.gui.GuiResearchTableSpace;
import com.denfop.invslot.InvSlot;
import com.denfop.items.space.ItemResearchLens;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFakeBody;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TileEntityResearchTableSpace extends TileEntityInventory implements IResearchTable {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(0, 0.0D, 0, 2, 2.0D,
            1.5
    ));
    private static final List<AxisAlignedBB> aabbs_east = Collections.singletonList(new AxisAlignedBB(0, 0.0D, -1, 1, 2.0D,
            1
    ));
    private static final List<AxisAlignedBB> aabbs_south = Collections.singletonList(new AxisAlignedBB(0, 0.0D, 0, 2, 2.0D,
            1
    ));
    private static final List<AxisAlignedBB> aabbs_west = Collections.singletonList(new AxisAlignedBB(0, 0.0D, 0, 1, 2.0D,
            2
    ));
    private static final List<AxisAlignedBB> aabbs_north = Collections.singletonList(new AxisAlignedBB(-1, 0.0D, 0, 1, 2.0D,
            1
    ));
    public final InvSlot slotLens;
    public Map<IBody, SpaceOperation> map;
    public List<SpaceOperation> fakeBodySpaceOperationMap;
    public EnumLevels level = EnumLevels.NONE;
    public int timer;
    public Map<IBody, Data> dataMap = new HashMap<>();
    public Map<IBody, SpaceOperation> operationMap;
    public IBody body;
    public IFakeBody fakeBody;
    public IColony colony;
    boolean added = false;
    private UUID player;
    private InfoSends sends;

    public TileEntityResearchTableSpace() {
        this.map = new HashMap<>();
        this.player = null;
        this.fakeBodySpaceOperationMap = new LinkedList<>();
        this.slotLens = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemResearchLens;
            }

            @Override
            public void update() {
                super.update();
                if (this.get().isEmpty()) {
                    level = EnumLevels.NONE;
                } else {
                    level = EnumLevels.values()[this.get().getItemDamage()];
                }
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                update();
            }
        };
    }

    public InfoSends getSends() {
        return sends;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.research_table_space;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!added) {
            MinecraftForge.EVENT_BUS.post(new ResearchTableLoadEvent(this.getWorld(), this));
            added = true;
        }
        if (!this.getWorld().isRemote) {
            this.getSpaceBody();
            dataMap.clear();
            dataMap = SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(this.player);
            for (IBody body : SpaceNet.instance.getBodyMap().values()) {
                if (!dataMap.containsKey(body)) {
                    dataMap.put(body, new Data(player, body));
                }
            }
        }
        this.slotLens.update();
    }


    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (added) {
            MinecraftForge.EVENT_BUS.post(new ResearchTableLoadEvent(this.getWorld(), this));
            added = false;
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        dataMap.clear();
        try {
            NBTTagCompound nbt = (NBTTagCompound) DecoderHandler.decode(customPacketBuffer);
            NBTTagList tagList = nbt.getTagList("list", 10);
            for (NBTBase nbtbase : tagList.tagList) {
                NBTTagCompound tagCompound = (NBTTagCompound) nbtbase;
                Data data = new Data(tagCompound);
                dataMap.put(data.getBody(), data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean hasColony = customPacketBuffer.readBoolean();
        if (hasColony) {
            this.colony = new Colony(customPacketBuffer);
            this.sends = new InfoSends(customPacketBuffer);
        } else {
            colony = null;
            sends = null;
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        NBTTagList tagList = new NBTTagList();
        for (Map.Entry<IBody, Data> entry : dataMap.entrySet()) {
            final NBTTagCompound nbt = entry.getValue().writeNBT();
            tagList.appendTag(nbt);
        }
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setTag("list", tagList);
        try {
            EncoderHandler.encode(customPacketBuffer, nbtTagCompound);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        customPacketBuffer.writeBoolean(colony != null);
        if (colony != null) {
            customPacketBuffer.writeBytes(colony.writePacket());
            List<Sends> sends =
                    SpaceNet.instance.getColonieNet().getSendsFromUUID(this.player).stream().filter(sends1 -> sends1.getBody() == colony.getBody()).collect(
                            Collectors.toList());
            InfoSends infoSends = new InfoSends();
            for (Sends send : sends) {
                infoSends.addTimer(send.getTimerToPlanet());
            }
            customPacketBuffer.writeBytes(infoSends.writeBuffer());
        }

        return customPacketBuffer;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiResearchTableSpace(getGuiContainer(var1));
    }

    @Override
    public ContainerResearchTableSpace getGuiContainer(final EntityPlayer var1) {
        return new ContainerResearchTableSpace(this, var1);
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getWorldTime() % 80 == 0)
            MinecraftForge.EVENT_BUS.post(new ResearchTableReLoadEvent(this.getWorld(), this));
        if (timer > 0) {
            timer--;
        }
        if (timer == 0) {
            if (body != null || fakeBody != null) {
                new PacketUpdateFakeBody(this, null);
            }
            body = null;
            fakeBody = null;
        }
        if (this.getWorld().getWorldTime() % 20 == 0) {
            boolean find = false;
            if (body != null) {
                if (colony != null) {
                    if (!colony.matched(body)) {
                        List<IColony> list = SpaceNet.instance.getColonieNet().getMap().get(
                                this.player
                        );
                        if (list == null || list.isEmpty()) {
                            colony = null;
                        } else {
                            list = list.stream().filter(colony -> colony.matched(body)).collect(Collectors.toList());
                            if (list.isEmpty()) {
                                colony = null;
                            } else {
                                colony = list.get(0);
                            }
                        }
                    }
                } else {
                    List<IColony> list = SpaceNet.instance.getColonieNet().getMap().get(
                            this.player
                    );
                    if (list != null && !list.isEmpty()) {
                        list = list.stream().filter(colony -> colony.matched(body)).collect(Collectors.toList());
                        if (!list.isEmpty()) {
                            colony = list.get(0);
                        }
                    }
                }
                final List<IFakeBody> list = SpaceNet.instance
                        .getFakeSpaceSystem()
                        .getBodyMap()
                        .computeIfAbsent(player, k -> new LinkedList<>());
                for (IFakeBody fakeBody : list) {
                    if (fakeBody.matched(body)) {
                        this.fakeBody = fakeBody;
                        new PacketUpdateFakeBody(this, this.fakeBody);
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    this.fakeBody = null;
                    new PacketUpdateFakeBody(this, null);
                }
            } else {
                colony = null;
            }
        }
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof EntityPlayer) {
            this.player = placer.getUniqueID();
        }
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        switch (this.getFacing()) {
            case EAST:
                return aabbs_east;
            case SOUTH:
                return aabbs_south;
            case WEST:
                return aabbs_west;
            case NORTH:
                return aabbs_north;
            default:
                return aabbs;
        }

    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.player = nbtTagCompound.getUniqueId("player");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setUniqueId("player", player);
        return nbtTagCompound;
    }

    @Override
    public Map<IBody, SpaceOperation> getSpaceBody() {
        if (operationMap == null) {
            operationMap = SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(this.player);
        }
        return operationMap;
    }

    @Override
    public UUID getPlayer() {
        return this.player;
    }


    @Override
    public EnumLevels getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final EnumLevels level) {
        this.level = level;
    }

}
