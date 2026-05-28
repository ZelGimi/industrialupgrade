package com.denfop.blockentity.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.base.FakePlayerSpawner;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSteamQuarry;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemCraftingElements;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSteamQuarry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.Collections;
import java.util.List;

public class BlockEntitySteamQuarry extends BlockEntityInventory {

    public final Fluids fluids;
    public final Fluids.InternalFluidTank fluidTank1;
    public final ComponentSteamEnergy steam;
    public final Inventory inventory;
    public final Inventory inventory1;
    public final InventoryOutput output;
    public final ItemStack stackPipe;
    public int y;
    public int x;
    public int z;
    public FakePlayerSpawner entity;
    public boolean work;

    public BlockEntitySteamQuarry(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.steam_quarry, pos, state);
        fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank2", 4000, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance().get()
        ), Inventory.TypeItemSlot.NONE);
        this.output = new InventoryOutput(this, 24);
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.steam.setFluidTank(fluidTank1);
        this.y = 0;
        this.x = 0;
        this.z = 0;
        this.inventory = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemCraftingElements && ((ItemCraftingElements<?>) stack.getItem()).getElement().getId() == 508;
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
        this.inventory1 = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.basemachine2.getItem(197);
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.TUBE;
            }
        };
        work = true;
    }
    private boolean isPipeColumn(BlockPos targetPos) {
        return targetPos.getX() == this.pos.getX()
                && targetPos.getZ() == this.pos.getZ();
    }

    private boolean isQuarryPipe(BlockState state) {
        return state.getBlock() instanceof BlockTileEntity blockTileEntity
                && blockTileEntity.item == this.stackPipe.getItem();
    }

    private boolean hasFluid(BlockPos targetPos) {
        return this.level != null && !this.level.getFluidState(targetPos).isEmpty();
    }

    private void advanceCursor() {
        if (this.x >= this.pos.getX() + 1) {
            this.x = this.pos.getX() - 1;
            this.z++;

            if (this.z >= this.pos.getZ() + 2) {
                this.z = this.pos.getZ() - 1;
                this.y--;
            }
        } else {
            this.x++;
        }
    }

    private boolean placePipe(BlockPos targetPos) {
        if (this.level == null || this.entity == null || this.inventory1.isEmpty()) {
            return false;
        }

        BlockState currentState = this.level.getBlockState(targetPos);


        if (!currentState.isAir() || hasFluid(targetPos)) {
            return false;
        }

        ItemStack pipeStack = this.inventory1.get(0);
        if (pipeStack.isEmpty() || pipeStack.getItem() != this.stackPipe.getItem()) {
            return false;
        }

        if (!(pipeStack.getItem() instanceof ItemBlockTileEntity blockTileEntity)) {
            return false;
        }

        ItemStack onePipe = pipeStack.copy();
        onePipe.setCount(1);

        blockTileEntity.placeTeBlock(onePipe, this.entity, this.level, targetPos);


        if (isQuarryPipe(this.level.getBlockState(targetPos))) {
            pipeStack.shrink(1);
            return true;
        }

        return false;
    }

    private void collectDrops(BlockPos targetPos) {
        List<ItemEntity> items = this.entity.level().getEntitiesOfClass(
                ItemEntity.class,
                new AABB(
                        targetPos.getX() - 1,
                        targetPos.getY() - 1,
                        targetPos.getZ() - 1,
                        targetPos.getX() + 1,
                        targetPos.getY() + 1,
                        targetPos.getZ() + 1
                )
        );

        for (ItemEntity item : items) {
            if (!this.entity.level().isClientSide && !item.isRemoved()) {
                if (this.output.addWithoutIgnoring(Collections.singletonList(item.getItem()), false)) {
                    item.setRemoved(Entity.RemovalReason.KILLED);
                }
            }
        }
    }
    public static int onBlockBreakEvent(Level level, GameType gameType, ServerPlayer entityPlayer, BlockPos pos) {
        Boolean preCancelEvent = false;
        ItemStack itemstack = entityPlayer.getMainHandItem();
        if (!itemstack.isEmpty() && !itemstack.getItem().canAttackBlock(level.getBlockState(pos), level, pos, entityPlayer)) {
            preCancelEvent = true;
        }

        if (gameType.isBlockPlacingRestricted()) {
            if (gameType == GameType.SPECTATOR)
                preCancelEvent = true;

            if (!entityPlayer.mayBuild()) {
                if (itemstack.isEmpty() || !itemstack.canBreakBlockInAdventureMode(new BlockInWorld(level, pos, false)))
                    preCancelEvent = true;
            }
        }


        // Post the block break event
        BlockState state = level.getBlockState(pos);
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, entityPlayer);
        event.setCanceled(preCancelEvent);
        NeoForge.EVENT_BUS.post(event);

        // Handle if the event is canceled
        if (event.isCanceled()) {
            // Let the client know the block still exists
            entityPlayer.connection.send(new ClientboundBlockUpdatePacket(level, pos));

            // Update any tile entity data for this block
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {

            }
        }
        return event.isCanceled() ? -1 : 1;
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
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenSteamQuarry((ContainerMenuSteamQuarry) menu);
    }

    public ContainerMenuSteamQuarry getGuiContainer(Player entityPlayer) {
        return new ContainerMenuSteamQuarry(entityPlayer, this);
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
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.steam_quarry;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();

        if (this.level == null || this.entity == null) {
            return;
        }

        if (this.inventory.isEmpty()
                || !this.work
                || this.inventory1.isEmpty()
                || this.y <= -30) {
            return;
        }

        BlockPos targetPos = new BlockPos(this.x, this.y, this.z);
        BlockState state = this.level.getBlockState(targetPos);
        Block block = state.getBlock();

        boolean pipeColumn = isPipeColumn(targetPos);

        if (pipeColumn && isQuarryPipe(state)) {
            advanceCursor();
            return;
        }


        if (pipeColumn && hasFluid(targetPos)) {
            return;
        }

        if (!this.steam.canUseEnergy(2)) {
            return;
        }

        this.steam.useEnergy(2);

        if (state.isAir()) {
            if (pipeColumn) {
                if (onBlockBreakEvent(this.level, GameType.SURVIVAL, this.entity, targetPos) == -1) {
                    return;
                }

                if (!placePipe(targetPos)) {
                    return;
                }
            }

            advanceCursor();
            return;
        }

        if (state.getDestroySpeed(this.level, targetPos) < 0) {
            advanceCursor();
            return;
        }

       if (onBlockBreakEvent(this.level, GameType.SURVIVAL, this.entity, targetPos) == -1) {
            advanceCursor();
            return;
        }


        BlockEntity brokenBlockEntity = this.level.getBlockEntity(targetPos);

        if (!(block instanceof BlockTileEntity)
                && block.onDestroyedByPlayer(state, this.level, targetPos, this.entity, true, this.level.getFluidState(targetPos))) {

            block.destroy(this.level, targetPos, state);
            block.playerDestroy(this.level, this.entity, targetPos, state, brokenBlockEntity, this.entity.getMainHandItem());

            collectDrops(targetPos);

            if (pipeColumn) {

                if (!placePipe(targetPos)) {
                    return;
                }
            }
        }

        advanceCursor();
    }

}
