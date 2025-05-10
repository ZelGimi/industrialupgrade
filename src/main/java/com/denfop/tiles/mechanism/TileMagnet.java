package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerMagnet;
import com.denfop.container.SlotInfo;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiMagnet;
import com.denfop.mixin.invoker.LevelInvoker;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.tiles.base.TileEntityAntiMagnet;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.List;

public class TileMagnet extends TileElectricMachine implements IUpdatableTileEvent {

    public final SlotInfo slot;
    public int energyconsume;
    public boolean work;
    public String player;
    public int x = 11;
    public int y = 11;
    public int z = 11;
    List<ChunkAccess> list = Lists.newArrayList();
    private AABB axisalignedbb;

    public TileMagnet(BlockPos pos, BlockState state) {
        super(100000, 14, 24, BlockBaseMachine1.magnet, pos, state);
        this.energyconsume = 1000;
        this.player = "";
        this.work = true;
        this.slot = new SlotInfo(this, 18, false);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            energyconsume = (int) DecoderHandler.decode(customPacketBuffer);
            x = (int) DecoderHandler.decode(customPacketBuffer);
            y = (int) DecoderHandler.decode(customPacketBuffer);
            z = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, energyconsume);
            EncoderHandler.encode(packet, x);
            EncoderHandler.encode(packet, y);
            EncoderHandler.encode(packet, z);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.magnet.getSoundEvent();
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(Localization.translate("iu.magnet_work_info"));
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }


    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.magnet;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine.getBlock(getTeBlock().getId());
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof Player) {
            Player player = (Player) placer;
            this.player = player.getName().getString();


          for (int x = this.pos.getX() - this.x; x <= this.pos.getX() + this.x; x++) {
                for (int y = this.pos.getY() - this.y; y <= this.pos.getY() + this.y; y++) {
                    for (int z = this.pos.getZ() - this.z; z <= this.pos.getZ() + this.z; z++) {
                        final BlockPos pos1 = new BlockPos(x, y, z);
                        final BlockEntity tileEntity = getWorld().getBlockEntity(pos1);

                        if (tileEntity != null && !(pos1.equals(this.pos))) {
                            if (tileEntity instanceof TileEntityAntiMagnet) {
                                TileEntityAntiMagnet tile = (TileEntityAntiMagnet) tileEntity;
                                if (!tile.player.equals(this.player)) {
                                    this.work = false;
                                }
                            }
                        }
                    }
                }
            }


        }
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.player = nbttagcompound.getString("player");
        this.work = nbttagcompound.getBoolean("work");
        this.x = nbttagcompound.getInt("x1");
        this.y = nbttagcompound.getInt("y1");
        this.z = nbttagcompound.getInt("z1");
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putString("player", this.player);
        nbttagcompound.putBoolean("work", this.work);
        nbttagcompound.putInt("x1", this.x);
        nbttagcompound.putInt("y1", this.y);
        nbttagcompound.putInt("z1", this.z);
        return nbttagcompound;
    }

    public boolean canInsertOrExtract(ItemStack stack) {
        List<ItemStack> BlackItemStacks = slot.getListBlack();
        if (BlackItemStacks.isEmpty()) {
            List<ItemStack> WhiteItemStacks = slot.getListWhite();
            if (!WhiteItemStacks.isEmpty()) {
                for (ItemStack stack1 : WhiteItemStacks) {
                    if (stack1.is(stack.getItem())) {
                        return true;
                    }

                }
                return false;
            }
            return true;
        } else {
            for (ItemStack stack1 : BlackItemStacks) {
                if (stack1.is(stack.getItem())) {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        updateData();
    }

    public void updateData() {
        this.axisalignedbb = new AABB(
                this.getBlockPos().getX() - this.x,
                this.getBlockPos().getY() - this.y,
                this.getBlockPos().getZ() - this.z,
                this.getBlockPos().getX() + this.x,
                this.getBlockPos().getY() + this.y,
                this.getBlockPos().getZ() + this.z
        );
        int j2 = (int) Math.floor((axisalignedbb.minX - 2) / 16.0D);
        int k2 = (int) Math.ceil((axisalignedbb.maxX + 2) / 16.0D);
        int l2 = (int) Math.floor((axisalignedbb.minZ - 2) / 16.0D);
        int i3 = (int) Math.ceil((axisalignedbb.maxZ + 2) / 16.0D);
        list = Lists.newArrayList();

        for (int j3 = j2; j3 < k2; ++j3) {
            for (int k3 = l2; k3 < i3; ++k3) {
                ChunkAccess chunk = getLevel().getChunkAt(new BlockPos(j3, 0, k3));
                if (!list.contains(chunk)) {
                    list.add(chunk);
                }

            }
        }
    }

    public List<ItemEntity> getEntitiesWithinAABB() {
        List<ItemEntity> list = Lists.newArrayList();
        LevelEntityGetter<Entity> list1 = ((LevelInvoker) level).getGetEntities();

        this.list.forEach(chunk -> list1.get(axisalignedbb.move(chunk.getPos().x * 16, 0, chunk.getPos().z), (p_151522_) -> {
            if (p_151522_ instanceof ItemEntity) {
                list.add((ItemEntity) p_151522_);
            }
        }));

        return list;
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        if (!this.work) {
            return;
        }
        boolean ret = false;
        if (this.getWorld().getGameTime() % 4 == 0) {

            List<ItemEntity> list = getEntitiesWithinAABB();
            for (ItemEntity item : list) {

                if (!item.isRemoved()) {
                    if (this.energy.canUseEnergy(energyconsume)) {
                        ItemStack stack = item.getItem();
                        if (this.outputSlot.canAdd(stack) && canInsertOrExtract(item.getItem())) {
                            item.setRemoved(Entity.RemovalReason.KILLED);
                            initiate(0);
                            setActive(true);
                            this.energy.useEnergy(energyconsume);
                            this.outputSlot.add(stack);
                            ret = true;
                        }


                    }
                }

            }
        }
        if (getWorld().getGameTime() % 10 == 0 && !ret && getActive()) {
            setActive(false);
            initiate(2);
        }


    }

    @Override
    public void updateTileServer(final Player player, final double event) {
        if (event == 10) {
            super.updateTileServer(player, event);
        } else {
            if (event == 0) {
                x--;
                x = Math.max(1, x);
            }
            if (event == 1) {
                x++;
                x = Math.min(11, x);
            }
            if (event == 2) {
                y--;
                y = Math.max(1, y);
            }
            if (event == 3) {
                y++;
                y = Math.min(11, y);
            }
            if (event == 4) {
                z--;
                z = Math.max(1, z);
            }
            if (event == 5) {
                z++;
                z = Math.min(11, z);
            }
            updateData();
        }
    }



    public double getEnergy() {
        return this.energy.getEnergy();
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiMagnet((ContainerMagnet) isAdmin);
    }

    public ContainerMagnet getGuiContainer(Player entityPlayer) {
        return new ContainerMagnet(entityPlayer, this);
    }


}
