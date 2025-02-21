package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerAdvSteamQuarry;
import com.denfop.gui.GuiAdvSteamQuarry;
import com.denfop.invslot.InvSlot;
import com.denfop.items.block.ItemBlockTileEntity;
import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.tiles.base.FakePlayerSpawner;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    public TileAdvSteamQuarry() {
        fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank2", 4000, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance()
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
                return stack.getItem() instanceof ItemCraftingElements && stack.getItemDamage() == 517;
            }

            @Override
            public int getStackSizeLimit() {
                return 1;
            }
        };
        this.stackPipe = new ItemStack(IUItem.basemachine2, 1, 197);
        this.invSlot1 = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.basemachine2.item && stack.getItemDamage() == 197;
            }
        };
        work = true;
    }

    public static int onBlockBreakEvent(World world, GameType gameType, EntityPlayerMP entityPlayer, BlockPos pos) {
        // Logic from tryHarvestBlock for pre-canceling the event
        boolean preCancelEvent = false;
        ItemStack itemstack = entityPlayer.getHeldItemMainhand();
        if (gameType.isCreative() && !itemstack.isEmpty()
                && !itemstack.getItem().canDestroyBlockInCreative(world, pos, itemstack, entityPlayer)) {
            preCancelEvent = true;
        }

        if (gameType.hasLimitedInteractions()) {
            if (gameType == GameType.SPECTATOR) {
                preCancelEvent = true;
            }

            if (!entityPlayer.isAllowEdit()) {
                if (itemstack.isEmpty() || !itemstack.canDestroy(world.getBlockState(pos).getBlock())) {
                    preCancelEvent = true;
                }
            }
        }

        // Tell client the block is gone immediately then process events
        if (world.getTileEntity(pos) == null) {
            SPacketBlockChange packet = new SPacketBlockChange(world, pos);
            packet.blockState = Blocks.AIR.getDefaultState();
        }

        // Post the block break event
        IBlockState state = world.getBlockState(pos);
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, entityPlayer);
        event.setCanceled(preCancelEvent);
        MinecraftForge.EVENT_BUS.post(event);

        // Handle if the event is canceled
        if (event.isCanceled()) {
            // Let the client know the block still exists


            // Update any tile entity data for this block
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity != null) {
                Packet<?> pkt = tileentity.getUpdatePacket();
                if (pkt != null) {

                }
            }
        }
        return event.isCanceled() ? -1 : event.getExpToDrop();
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setInteger("x1", x);
        nbtTagCompound.setInteger("y1", y);
        nbtTagCompound.setInteger("z1", z);
        nbtTagCompound.setBoolean("work", work);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        x = nbtTagCompound.getInteger("x1");
        y = nbtTagCompound.getInteger("y1");
        z = nbtTagCompound.getInteger("z1");
        work = nbtTagCompound.getBoolean("work");
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.y = this.getPos().getY() - 1;
        this.x = this.getPos().getX() - 3;
        this.z = this.getPos().getZ() - 3;
    }

    @SideOnly(Side.CLIENT)
    public GuiAdvSteamQuarry getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAdvSteamQuarry(getGuiContainer(entityPlayer));
    }

    public ContainerAdvSteamQuarry getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerAdvSteamQuarry(entityPlayer, this);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            entity = new FakePlayerSpawner(this.world);
            entity.setActiveHand(EnumHand.MAIN_HAND);
            entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));
        }
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.adv_steam_quarry;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.invSlot.isEmpty() && steam.canUseEnergy(1) && work && !invSlot1.isEmpty() && y > 4) {
            BlockPos pos1 = new BlockPos(x, y, z);
            final IBlockState state = world.getBlockState(pos1);
            Block block1 = state.getBlock();
            int meta = block1.getMetaFromState(state);
            steam.useEnergy(1);
            if (state.getMaterial() == Material.AIR && onBlockBreakEvent(world, world.getWorldInfo().getGameType(),
                    entity, pos1
            ) != -1) {
                if (x == pos.getX() && z == pos.getZ()) {
                    invSlot1.get().shrink(1);
                    ItemBlockTileEntity blockTileEntity = (ItemBlockTileEntity) stackPipe.getItem();
                    IBlockState iblockstate1 = blockTileEntity.getBlock().getStateForPlacement(world, pos1,
                            EnumFacing.NORTH, 0,
                            0,
                            0, stackPipe.getItemDamage(), entity, EnumHand.MAIN_HAND
                    );
                    blockTileEntity.placeBlockAt(stackPipe, entity, world, pos1, EnumFacing.NORTH, 0,
                            0, 0, iblockstate1
                    );

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
            if (onBlockBreakEvent(world, world.getWorldInfo().getGameType(), entity, pos1) == -1) {
                if (x == pos1.getX() && z == pos1.getZ()) {
                    work = false;
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


            if (!(block1 instanceof BlockTileEntity) && block1.removedByPlayer(state, world, pos1, entity, true)) {
                block1.onBlockDestroyedByPlayer(world, pos1, state);
                block1.harvestBlock(world, entity, pos1, state, null, entity.getHeldItem(EnumHand.MAIN_HAND));
                List<EntityItem> items = this.getWorld().getEntitiesWithinAABB(
                        EntityItem.class,
                        new AxisAlignedBB(pos1.getX() - 1, pos1.getY() - 1, pos1.getZ() - 1, pos1.getX() + 1,
                                pos1.getY() + 1,
                                pos1.getZ() + 1
                        )
                );
                for (EntityItem item : items) {
                    if (!entity.getEntityWorld().isRemote && !item.isDead) {
                        if (this.output.addWithoutIgnoring(Collections.singletonList(item.getItem()), false)) {
                            item.setDead();
                        }

                    }
                }
                if (x == this.getPos().getX() && z == this.getPos().getZ()) {
                    ItemBlockTileEntity blockTileEntity = (ItemBlockTileEntity) stackPipe.getItem();
                    IBlockState iblockstate1 = blockTileEntity.getBlock().getStateForPlacement(world, pos1,
                            EnumFacing.NORTH, 0,
                            0,
                            0, stackPipe.getItemDamage(), entity, EnumHand.MAIN_HAND
                    );
                    blockTileEntity.placeBlockAt(stackPipe, entity, world, pos1, EnumFacing.NORTH, 0,
                            0, 0, iblockstate1
                    );
                    invSlot1.get().shrink(1);
                }
            } else {
                if (x == this.getPos().getX() && z == this
                        .getPos()
                        .getZ() && (block1 instanceof BlockTileEntity && meta == 197)) {
                    this.work = false;
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
