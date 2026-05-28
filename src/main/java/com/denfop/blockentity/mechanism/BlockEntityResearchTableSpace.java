package com.denfop.blockentity.mechanism;

import com.denfop.IUCore;
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
import com.denfop.blocks.ISubEnum;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuResearchTableSpace;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.Inventory.TypeItemSlot;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BlockEntityResearchTableSpace extends BlockEntityInventory implements IResearchTable {
    private static final List<AABB> aabbs = Collections.singletonList(new AABB(0.0, 0.0, 0.0, 2.0, 2.0, 1.5));
    private static final List<AABB> aabbs_east = Collections.singletonList(new AABB(0.0, 0.0, -1.0, 1.0, 2.0, 1.0));
    private static final List<AABB> aabbs_south = Collections.singletonList(new AABB(0.0, 0.0, 0.0, 2.0, 2.0, 1.0));
    private static final List<AABB> aabbs_west = Collections.singletonList(new AABB(0.0, 0.0, 0.0, 1.0, 2.0, 2.0));
    private static final List<AABB> aabbs_north = Collections.singletonList(new AABB(-1.0, 0.0, 0.0, 1.0, 2.0, 1.0));
    public final Inventory slotLens;
    public Map<IBody, SpaceOperation> map;
    public List<SpaceOperation> fakeBodySpaceOperationMap;
    public EnumLevels level;
    public int timer;
    public Map<IBody, Data> dataMap;
    public Map<IBody, SpaceOperation> operationMap;
    public IBody body;
    public IFakeBody fakeBody;
    public IColony colony;
    boolean added;
    private UUID player;
    private InfoSends sends;

    public BlockEntityResearchTableSpace(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.research_table_space, pos, state);
        this.level = EnumLevels.NONE;
        this.dataMap = new HashMap<>();
        this.added = false;
        this.player = new UUID(WorldBaseGen.random.nextLong(), WorldBaseGen.random.nextLong());
        this.map = new HashMap<>();
        this.player = new UUID(WorldBaseGen.random.nextLong(), WorldBaseGen.random.nextLong());
        this.fakeBodySpaceOperationMap = new LinkedList<>();
        this.slotLens = new Inventory(this, TypeItemSlot.INPUT, 1) {
            public boolean canPlaceItem(int index, ItemStack stack) {
                return stack.getItem() instanceof ItemResearchLens;
            }

            public void update() {
                super.update();
                if (this.get(0).isEmpty()) {
                    BlockEntityResearchTableSpace.this.level = EnumLevels.NONE;
                } else {
                    BlockEntityResearchTableSpace.this.level = EnumLevels.values()[((ISubEnum) ((ItemResearchLens) this.get(0).getItem()).getElement()).getId()];
                }

            }

            public ItemStack set(int index, ItemStack content) {
                super.set(index, content);
                this.update();
                return content;
            }
        };
    }

    public InfoSends getSends() {
        return this.sends;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.research_table_space;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(this.getTeBlock());
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.added) {
            NeoForge.EVENT_BUS.post(new ResearchTableLoadEvent(this.getWorld(), this));
            this.added = true;
        }

        if (!this.getWorld().isClientSide) {
            this.getSpaceBody();
            this.dataMap.clear();
            this.dataMap = SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(this.player);

            for (IBody body : SpaceNet.instance.getBodyMap().values()) {
                if (!this.dataMap.containsKey(body)) {
                    this.dataMap.put(body, new Data(this.player, body));
                }
            }
        }

        this.slotLens.update();
    }

    public void onUnloaded() {
        super.onUnloaded();
        if (this.added) {
            NeoForge.EVENT_BUS.post(new ResearchTableLoadEvent(this.getWorld(), this));
            this.added = false;
        }

    }

    public void readContainerPacket(CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.dataMap.clear();
        this.player = customPacketBuffer.readUUID();
        try {
            CompoundTag nbt = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
            ListTag tagList = nbt.getList("list", 10);

            for (Tag nbtbase : tagList) {
                CompoundTag tagCompound = (CompoundTag) nbtbase;
                Data data = new Data(tagCompound);
                this.dataMap.put(data.getBody(), data);
            }
        } catch (IOException var9) {
            throw new RuntimeException(var9);
        }

        boolean hasColony = customPacketBuffer.readBoolean();
        if (hasColony) {
            this.colony = new Colony(customPacketBuffer);
            this.sends = new InfoSends(customPacketBuffer);
        } else {
            this.colony = null;
            this.sends = null;
        }

        int sizeSpaceBodyInformation = customPacketBuffer.readInt();
        Map<IBody, SpaceOperation> information = this.getSpaceBody();
        information.clear();

        for (int i = 0; i < sizeSpaceBodyInformation; ++i) {
            IBody body1 = SpaceNet.instance.getBodyFromName(customPacketBuffer.readString());
            boolean auto = customPacketBuffer.readBoolean();
            EnumOperation operation = EnumOperation.getID(customPacketBuffer.readInt());
            information.put(body1, new SpaceOperation(body1, operation, auto));
        }

    }

    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        ListTag tagList = new ListTag();
        customPacketBuffer.writeUUID(this.player);
        for (Map.Entry<IBody, Data> iBodyDataEntry : this.dataMap.entrySet()) {
            Map.Entry<IBody, Data> entry = iBodyDataEntry;
            CompoundTag nbt = (entry.getValue()).writeNBT();
            tagList.add(nbt);
        }

        CompoundTag nbtTagCompound = new CompoundTag();
        nbtTagCompound.put("list", tagList);

        try {
            EncoderHandler.encode(customPacketBuffer, nbtTagCompound);
        } catch (IOException var8) {
            throw new RuntimeException(var8);
        }

        customPacketBuffer.writeBoolean(this.colony != null);
        if (this.colony != null) {
            customPacketBuffer.writeBytes(this.colony.writePacket(customPacketBuffer.registryAccess()));
            List<Sends> sends = SpaceNet.instance.getColonieNet().getSendsFromUUID(this.player).stream().filter((sends1) -> sends1.getBody() == this.colony.getBody()).toList();
            InfoSends infoSends = new InfoSends();

            for (Sends send : sends) {
                infoSends.addTimer(send.getTimerToPlanet());
            }

            customPacketBuffer.writeBytes(infoSends.writeBuffer(customPacketBuffer.registryAccess()));
        }

        customPacketBuffer.writeInt(this.getSpaceBody().keySet().size());

        for (Map.Entry<IBody, SpaceOperation> iBodySpaceOperationEntry : this.getSpaceBody().entrySet()) {
            customPacketBuffer.writeString(iBodySpaceOperationEntry.getKey().getName());
            customPacketBuffer.writeBoolean(iBodySpaceOperationEntry.getValue().getAuto());
            customPacketBuffer.writeInt(iBodySpaceOperationEntry.getValue().getOperation().ordinal());
        }

        return customPacketBuffer;
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenResearchTableSpace((ContainerMenuResearchTableSpace) menu);
    }

    public ContainerMenuResearchTableSpace getGuiContainer(Player var1) {
        return new ContainerMenuResearchTableSpace(this, var1);
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 80L == 0L) {
            NeoForge.EVENT_BUS.post(new ResearchTableReLoadEvent(this.getWorld(), this));
        }

        if (this.timer > 0) {
            --this.timer;
        }

        if (this.timer == 0) {
            if (this.body != null || this.fakeBody != null) {
                new PacketUpdateFakeBody(this, (IFakeBody) null);
            }

            this.body = null;
            this.fakeBody = null;
        }

        if (this.getWorld().getGameTime() % 20L == 0L) {
            boolean find = false;
            if (this.body != null) {
                List<IColony> list;
                Iterator<IColony> var3;
                if (this.colony != null) {
                    if (!this.colony.matched(this.body)) {
                        list = SpaceNet.instance.getColonieNet().getMap().get(this.player);
                        if (list != null && !list.isEmpty()) {
                            list = list.stream().filter((colony) -> colony.matched(this.body)).collect(Collectors.toList());
                            if (list.isEmpty()) {
                                this.colony = null;
                                this.sends = null;
                            } else {
                                this.colony = list.get(0);
                            }
                        } else {
                            this.colony = null;
                        }
                    } else {
                        List<Sends> list1 = SpaceNet.instance.getColonieNet().getSendsFromUUID(this.player).stream().filter((sends1) -> sends1.getBody() == this.colony.getBody()).collect(Collectors.toList());
                        this.sends = new InfoSends();

                        for (Sends send : list1) {
                            this.sends.addTimer(send.getTimerToPlanet());
                        }
                    }
                } else {
                    list = SpaceNet.instance.getColonieNet().getMap().get(this.player);
                    if (list != null && !list.isEmpty()) {
                        list = list.stream().filter((colony) -> colony.matched(this.body)).collect(Collectors.toList());
                        if (!list.isEmpty()) {
                            this.colony = list.get(0);
                        }
                    }
                }

                List<IFakeBody> list2 = SpaceNet.instance.getFakeSpaceSystem().getBodyMap().computeIfAbsent(this.player, (k) -> new LinkedList());

                for (IFakeBody fakeBody : list2) {
                    if (fakeBody.matched(this.body)) {
                        this.fakeBody = fakeBody;
                        new PacketUpdateFakeBody(this, this.fakeBody);
                        find = true;
                        break;
                    }
                }

                if (!find) {
                    this.fakeBody = null;
                    new PacketUpdateFakeBody(this, (IFakeBody) null);
                }
            } else {
                this.colony = null;
            }
        }

    }

    public void onPlaced(ItemStack stack, LivingEntity placer, Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof Player) {
            if (IUCore.network.getClient() == null)
                this.player = placer.getUUID();
            else
                this.player = ((Player) placer).getGameProfile().getId();
        }

    }

    public List<AABB> getAabbs(boolean forCollision) {
        return switch (this.getFacing()) {
            case EAST -> aabbs_east;
            case SOUTH -> aabbs_south;
            case WEST -> aabbs_west;
            case NORTH -> aabbs_north;
            default -> aabbs;
        };
    }

    public void readFromNBT(CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.player = nbtTagCompound.getUUID("player");
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putUUID("player", this.player);
        return nbtTagCompound;
    }

    public Map<IBody, SpaceOperation> getSpaceBody() {
        if (this.operationMap == null) {
            this.operationMap = SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(this.player);
        }

        return this.operationMap;
    }

    public UUID getPlayer() {
        return this.player;
    }

    public EnumLevels getLevelTable() {
        return this.level;
    }

    public void setLevel(EnumLevels level) {
        this.level = level;
    }
}
