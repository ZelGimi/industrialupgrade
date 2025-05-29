package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerAdvSteamQuarry;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiAdvSteamQuarry;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemCraftingElements;
import com.denfop.tiles.base.FakePlayerSpawner;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;

import java.util.Collections;
import java.util.List;

public class TileAdvSteamQuarry extends TileEntityInventory {

    public final Fluids fluids;
    public final Fluids.InternalFluidTank fluidTank1;
    public final ComponentSteamEnergy steam;
    public final InvSlot invSlot;
    public final InvSlot invSlot1;
    public final InvSlotOutput output;
    public final ItemStack stackPipe;
    public int y;
    public int x;
    public int z;
    public FakePlayerSpawner entity;
    public boolean work;

    public TileAdvSteamQuarry(BlockPos pos,BlockState state) {
        super(BlockBaseMachine3.adv_steam_quarry,pos,state);
        fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank2", 4000, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance().get()
        ), InvSlot.TypeItemSlot.NONE);
        this.output = new InvSlotOutput(this, 24);
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.steam.setFluidTank(fluidTank1);
        this.y = 0;
        this.x = 0;
        this.z = 0;
        this.invSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemCraftingElements && ((ItemCraftingElements<?>) stack.getItem()).getElement().getId() == 517;
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.BIT;
            }

            @Override
            public int getStackSizeLimit() {
                return 1;
            }
        };
        this.stackPipe = new ItemStack(IUItem.basemachine2.getItem(197), 1);
        this.invSlot1 = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.basemachine2.getItem(197);
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.TUBE;
            }
        };
        work = true;
    }

    public static int onBlockBreakEvent(Level level,GameType gameType, ServerPlayer entityPlayer, BlockPos pos) {
        Boolean preCancelEvent = false;
        ItemStack itemstack = entityPlayer.getMainHandItem();
        if (!itemstack.isEmpty() && !itemstack.getItem().canAttackBlock(level.getBlockState(pos), level, pos, entityPlayer))
        {
            preCancelEvent = true;
        }
        if (gameType.isBlockPlacingRestricted())
        {
            if (gameType == GameType.SPECTATOR)
                preCancelEvent = true;

            if (!entityPlayer.mayBuild())
            {
                if (itemstack.isEmpty() || !itemstack.hasAdventureModeBreakTagForBlock(level.registryAccess().registryOrThrow(Registries.BLOCK), new BlockInWorld(level, pos, false)))
                    preCancelEvent = true;
            }
        }



        // Post the block break event
        BlockState state = level.getBlockState(pos);
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, entityPlayer);
        event.setCanceled(preCancelEvent);
        MinecraftForge.EVENT_BUS.post(event);

        // Handle if the event is canceled
        if (event.isCanceled())
        {
            // Let the client know the block still exists
            entityPlayer.connection.send(new ClientboundBlockUpdatePacket(level, pos));

            // Update any tile entity data for this block
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null)
            {

            }
        }
        return event.isCanceled() ? -1 : event.getExpToDrop();
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putInt("x1", x);
        nbtTagCompound.putInt("y1", y);
        nbtTagCompound.putInt("z1", z);
        nbtTagCompound.putBoolean("work", work);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        x = nbtTagCompound.getInt("x1");
        y = nbtTagCompound.getInt("y1");
        z = nbtTagCompound.getInt("z1");
        work = nbtTagCompound.getBoolean("work");
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        this.y = this.getPos().getY() - 1;
        this.x = this.getPos().getX() - 1;
        this.z = this.getPos().getZ() - 1;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiAdvSteamQuarry((ContainerAdvSteamQuarry) menu);
    }

    public ContainerAdvSteamQuarry getGuiContainer(Player entityPlayer) {
        return new ContainerAdvSteamQuarry(entityPlayer, this);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            entity = new FakePlayerSpawner(this.level);
            entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_PICKAXE));
        }
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.adv_steam_quarry;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.invSlot.isEmpty() && steam.canUseEnergy(2) && work && !invSlot1.isEmpty() && y > -30) {
            BlockPos pos1 = new BlockPos(x, y, z);
            final BlockState state = level.getBlockState(pos1);
            Block block1 = state.getBlock();
            steam.useEnergy(2);
            if ((state.isAir()  || state.getDestroySpeed(level,pos1) < 0)&& onBlockBreakEvent(level,
                    GameType.SURVIVAL,
                    entity, pos1
            ) != -1) {
                if (state.getDestroySpeed(level,pos1) < 0){
                    return;
                }
                if (x == pos.getX() && z == pos.getZ()) {
                    invSlot1.get(0).shrink(1);
                    ItemBlockTileEntity blockTileEntity = (ItemBlockTileEntity) stackPipe.getItem();
                    blockTileEntity.placeTeBlock(stackPipe, entity, level, pos1);

                }
                if (x == this.pos.getX() + 3) {
                    x = this.pos.getX() - 3;
                    z++;
                    if (z == this.pos.getZ() + 4) {
                        z = this.pos.getZ() - 3;
                        y--;
                    }
                } else {
                    x++;
                }
                return;
            }
            if (onBlockBreakEvent(level, GameType.SURVIVAL, entity, pos1) == -1) {

                if (x == this.pos.getX() + 3) {
                    x = this.pos.getX() - 3;
                    z++;
                    if (z == this.pos.getZ() + 4) {
                        z = this.pos.getZ() - 3;
                        y--;
                    }
                } else {
                    x++;
                }
                return;
            }


            if (!(block1 instanceof BlockTileEntity) && block1.onDestroyedByPlayer(state, level, pos1, entity, true, level.getFluidState(pos1))) {
                block1.destroy(level, pos1, state);
                block1.playerDestroy(level, entity, pos1, state, null, entity.getMainHandItem());
                List<ItemEntity> items = entity.level().getEntitiesOfClass(
                        ItemEntity.class,
                        new AABB(pos1.getX() - 1, pos1.getY() - 1, pos1.getZ() - 1, pos1.getX() + 1,
                                pos1.getY() + 1,
                                pos1.getZ() + 1
                        )
                );
                for (ItemEntity item : items) {
                    if (!entity.level().isClientSide && !item.isRemoved()) {
                        if (this.output.addWithoutIgnoring(Collections.singletonList(item.getItem()), false)) {
                            item.setRemoved(Entity.RemovalReason.KILLED);
                        }

                    }
                }
                if (x == this.getPos().getX() && z == this.getPos().getZ()) {
                    ItemBlockTileEntity blockTileEntity = (ItemBlockTileEntity) stackPipe.getItem();
                    blockTileEntity.placeTeBlock(stackPipe, entity, level, pos1);
                    invSlot1.get(0).shrink(1);
                }
            } else {
                if (x == this.getPos().getX() && z == this
                        .getPos()
                        .getZ() && (block1 instanceof BlockTileEntity && ((BlockTileEntity)block1).item == stackPipe.getItem())) {

                }
                if (x == this.pos.getX() + 3) {
                    x = this.pos.getX() - 3;
                    z++;
                    if (z == this.pos.getZ() + 4) {
                        z = this.pos.getZ() - 3;
                        y--;
                    }
                } else {
                    x++;
                }
            }

        }
    }

}
