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
        if (!this.inventory.isEmpty() && steam.canUseEnergy(2) && work && !inventory1.isEmpty() && y > -30) {
            BlockPos pos1 = new BlockPos(x, y, z);
            final BlockState state = level.getBlockState(pos1);
            Block block1 = state.getBlock();
            steam.useEnergy(2);
            if ((state.isAir() || state.getDestroySpeed(level, pos1) < 0) && onBlockBreakEvent(level,
                    GameType.SURVIVAL,
                    entity, pos1
            ) != -1) {
                if (x == pos.getX() && z == pos.getZ()) {
                    if (state.getDestroySpeed(level, pos1) < 0) {
                        return;
                    }
                    inventory1.get(0).shrink(1);
                    ItemBlockTileEntity blockTileEntity = (ItemBlockTileEntity) stackPipe.getItem();
                    blockTileEntity.placeTeBlock(stackPipe, entity, level, pos1);

                }
                if (x == this.pos.getX() + 1) {
                    x = this.pos.getX() - 1;
                    z++;
                    if (z == this.pos.getZ() + 2) {
                        z = this.pos.getZ() - 1;
                        y--;
                    }
                } else {
                    x++;
                }
                return;
            }
            if (onBlockBreakEvent(level, GameType.SURVIVAL, entity, pos1) == -1) {

                if (x == this.pos.getX() + 2) {
                    x = this.pos.getX() - 1;
                    z++;
                    if (z == this.pos.getZ() + 2) {
                        z = this.pos.getZ() - 1;
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
                    inventory1.get(0).shrink(1);
                }
            } else {
                if (x == this.getPos().getX() && z == this
                        .getPos()
                        .getZ() && (block1 instanceof BlockTileEntity && ((BlockTileEntity) block1).item == stackPipe.getItem())) {

                }
                if (x == this.pos.getX() + 1) {
                    x = this.pos.getX() - 1;
                    z++;
                    if (z == this.pos.getZ() + 1) {
                        z = this.pos.getZ() - 1;
                        y--;
                    }
                } else {
                    x++;
                }
            }

        }
    }

}
