package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.space.EnumLevels;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.colonies.Colony;
import com.denfop.api.space.colonies.InfoSends;
import com.denfop.api.space.colonies.Sends;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.fakebody.Data;
import com.denfop.api.space.fakebody.EnumOperation;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.api.space.fakebody.SpaceOperation;
import com.denfop.api.space.research.api.IResearchTable;
import com.denfop.api.space.research.event.ResearchTableLoadEvent;
import com.denfop.api.space.research.event.ResearchTableReLoadEvent;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuResearchTableSpace;
import com.denfop.inventory.Inventory;
import com.denfop.items.space.ItemResearchLens;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFakeBody;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenResearchTableSpace;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BlockEntityResearchTableSpace extends BlockEntityInventory implements IResearchTable {

    private static final List<AABB> aabbs = Collections.singletonList(new AABB(0, 0.0D, 0, 2, 2.0D,
            1.5
    ));
    private static final List<AABB> aabbs_east = Collections.singletonList(new AABB(0, 0.0D, -1, 1, 2.0D,
            1
    ));
    private static final List<AABB> aabbs_south = Collections.singletonList(new AABB(0, 0.0D, 0, 2, 2.0D,
            1
    ));
    private static final List<AABB> aabbs_west = Collections.singletonList(new AABB(0, 0.0D, 0, 1, 2.0D,
            2
    ));
    private static final List<AABB> aabbs_north = Collections.singletonList(new AABB(-1, 0.0D, 0, 1, 2.0D,
            1
    ));
    public final Inventory slotLens;
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
    private UUID player = new UUID(WorldBaseGen.random.nextLong(), WorldBaseGen.random.nextLong());
    private InfoSends sends;

    public BlockEntityResearchTableSpace(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.research_table_space, pos, state);
        this.map = new HashMap<>();
        this.player = new UUID(WorldBaseGen.random.nextLong(), WorldBaseGen.random.nextLong());
        this.fakeBodySpaceOperationMap = new LinkedList<>();
        this.slotLens = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemResearchLens;
            }

            @Override
            public void update() {
                super.update();
                if (this.get(0).isEmpty()) {
                    level = EnumLevels.NONE;
                } else {
                    level = EnumLevels.values()[((ItemResearchLens<?>) this.get(0).getItem()).getElement().getId()];
                }
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                update();
                return content;
            }
        };
    }

    public InfoSends getSends() {
        return sends;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.research_table_space;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!added) {
            MinecraftForge.EVENT_BUS.post(new ResearchTableLoadEvent(this.getWorld(), this));
            added = true;
        }
        if (!this.getWorld().isClientSide) {
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
            CompoundTag nbt = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
            ListTag tagList = nbt.getList("list", 10);
            for (ListIterator<Tag> it = tagList.listIterator(); it.hasNext(); ) {
                Tag nbtbase = it.next();
                CompoundTag tagCompound = (CompoundTag) nbtbase;
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
        int sizeSpaceBodyInformation = customPacketBuffer.readInt();
        Map<IBody, SpaceOperation> information = getSpaceBody();
        information.clear();
        for (int i = 0; i < sizeSpaceBodyInformation; i++) {
            IBody body1 = SpaceNet.instance.getBodyFromName(customPacketBuffer.readString());
            boolean auto = customPacketBuffer.readBoolean();
            EnumOperation operation = EnumOperation.getID(customPacketBuffer.readInt());
            information.put(body1, new SpaceOperation(body1, operation, auto));
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        ListTag tagList = new ListTag();
        for (Map.Entry<IBody, Data> entry : dataMap.entrySet()) {
            final CompoundTag nbt = entry.getValue().writeNBT();
            tagList.add(nbt);
        }
        CompoundTag nbtTagCompound = new CompoundTag();
        nbtTagCompound.put("list", tagList);
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
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenResearchTableSpace((ContainerMenuResearchTableSpace) menu);
    }

    @Override
    public ContainerMenuResearchTableSpace getGuiContainer(final Player var1) {
        return new ContainerMenuResearchTableSpace(this, var1);
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 80 == 0)
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
        if (this.getWorld().getGameTime() % 20 == 0) {
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
                                sends = null;
                            } else {
                                colony = list.get(0);

                            }
                        }
                    } else {
                        List<Sends> sends =
                                SpaceNet.instance.getColonieNet().getSendsFromUUID(this.player).stream().filter(sends1 -> sends1.getBody() == colony.getBody()).collect(
                                        Collectors.toList());
                        this.sends = new InfoSends();
                        for (Sends send : sends) {
                            this.sends.addTimer(send.getTimerToPlanet());
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
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof Player) {
            this.player = placer.getUUID();
        }
    }

    public List<AABB> getAabbs(boolean forCollision) {
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


    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.player = nbtTagCompound.getUUID("player");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putUUID("player", player);
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
    public EnumLevels getLevelTable() {
        return this.level;
    }

    @Override
    public void setLevel(final EnumLevels level) {
        this.level = level;
    }

}
