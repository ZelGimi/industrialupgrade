package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.items.block.ItemBlockTileEntity;
import com.denfop.register.Register;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public class ItemPlaner extends Item implements IModelRegister {

    private final String name;

    public ItemPlaner() {
        super();
        this.setMaxStackSize(1);
        this.canRepair = false;
        this.name = "multiblock_planner";
        this.setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "tools" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    @Override
    public EnumActionResult onItemUseFirst(
            final EntityPlayer player,
            final World world,
            final BlockPos pos,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ,
            final EnumHand hand
    ) {
        if (world.isRemote) {
            return EnumActionResult.PASS;
        }
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IMainMultiBlock) {
            IMainMultiBlock mainMultiBlock = (IMainMultiBlock) tile;
            if (!mainMultiBlock.isFull()) {

                for (Map.Entry<BlockPos, ItemStack> entry : mainMultiBlock.getMultiBlockStucture().ItemStackMap.entrySet()) {
                    BlockPos pos1;
                    if (entry.getValue().isEmpty()) {
                        continue;
                    }
                    EnumFacing rotation = mainMultiBlock.getMultiBlockStucture().RotationMap.get(entry.getKey());
                    switch (((TileMultiBlockBase) mainMultiBlock).getFacing()) {
                        case NORTH:
                            pos1 = new BlockPos(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ());
                            break;
                        case EAST:
                            pos1 = new BlockPos(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX());
                            break;
                        case WEST:
                            pos1 = new BlockPos(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX() * -1);
                            break;
                        case SOUTH:
                            pos1 = new BlockPos(entry.getKey().getX() * -1, entry.getKey().getY(), entry.getKey().getZ() * -1);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: ");
                    }

                    final ItemStack item = entry.getValue();
                    if (!player.isCreative()) {
                        for (ItemStack stack1 : player.inventory.mainInventory) {
                            if (stack1.isItemEqual(item)) {
                                BlockPos pos2 = pos.add(pos1);
                                ItemBlockTileEntity item1 = (ItemBlockTileEntity) stack1.getItem();
                                TileEntity tileEntity = world.getTileEntity(pos2);
                                if (tileEntity == null && canPlace(world.getBlockState(pos2))) {
                                    IBlockState iblockstate1 = item1.getBlock().getStateForPlacement(world, pos2,
                                            rotation, hitX,
                                            hitY,
                                            hitZ, item.getItemDamage(), player, hand
                                    );

                                    if (item1.placeBlockAt(stack1, player, world, pos2, rotation, hitX,
                                            hitY, hitZ, iblockstate1
                                    )) {
                                        stack1.shrink(1);
                                        TileEntityMultiBlockElement tileEntity2 = (TileEntityMultiBlockElement) world.getTileEntity(
                                                pos2);
                                        EnumFacing facing = ((TileMultiBlockBase) mainMultiBlock).getFacing();

                                        if (facing == EnumFacing.NORTH) {
                                            if (rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
                                                rotation = rotation.getOpposite();
                                            }
                                        } else if (facing == EnumFacing.SOUTH) {
                                            if (rotation == EnumFacing.SOUTH || rotation == EnumFacing.NORTH) {
                                                rotation = rotation.getOpposite();
                                            }
                                        } else if (facing == EnumFacing.EAST) {
                                            if (rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
                                                if (rotation == EnumFacing.EAST) {
                                                    rotation = EnumFacing.NORTH;
                                                } else {
                                                    rotation = EnumFacing.SOUTH;
                                                }
                                            } else {
                                                if (rotation == EnumFacing.SOUTH) {
                                                    rotation = EnumFacing.WEST;
                                                } else {
                                                    rotation = EnumFacing.EAST;
                                                }
                                            }
                                        } else if (facing == EnumFacing.WEST) {
                                            if (rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
                                                rotation = rotation.getOpposite();
                                            } else {
                                                if (rotation == EnumFacing.NORTH) {
                                                    rotation = rotation.rotateAround(EnumFacing.Axis.X);
                                                } else {
                                                    rotation = EnumFacing.EAST;
                                                }
                                            }
                                        }


                                        tileEntity2.setFacing(rotation);
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        BlockPos pos2 = pos.add(pos1);
                        ItemBlockTileEntity item1 = (ItemBlockTileEntity) item.getItem();
                        TileEntity tileEntity = world.getTileEntity(pos2);
                        if (tileEntity == null && world.getBlockState(pos2).getMaterial() == Material.AIR) {
                            IBlockState iblockstate1 = item1.getBlock().getStateForPlacement(world, pos2,
                                    rotation, hitX,
                                    hitY,
                                    hitZ, item.getItemDamage(), player, hand
                            );

                            if (item1.placeBlockAt(item, player, world, pos2, rotation, hitX,
                                    hitY, hitZ, iblockstate1
                            )) {

                                TileEntityMultiBlockElement tileEntity2 = (TileEntityMultiBlockElement) world.getTileEntity(
                                        pos2);
                                EnumFacing facing = ((TileMultiBlockBase) mainMultiBlock).getFacing();

                                if (facing == EnumFacing.NORTH) {
                                    if (rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
                                        rotation = rotation.getOpposite();
                                    }
                                } else if (facing == EnumFacing.SOUTH) {
                                    if (rotation == EnumFacing.SOUTH || rotation == EnumFacing.NORTH) {
                                        rotation = rotation.getOpposite();
                                    }
                                } else if (facing == EnumFacing.EAST) {
                                    if (rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
                                        if (rotation == EnumFacing.EAST) {
                                            rotation = EnumFacing.NORTH;
                                        } else {
                                            rotation = EnumFacing.SOUTH;
                                        }
                                    } else {
                                        if (rotation == EnumFacing.SOUTH) {
                                            rotation = EnumFacing.WEST;
                                        } else {
                                            rotation = EnumFacing.EAST;
                                        }
                                    }
                                } else if (facing == EnumFacing.WEST) {
                                    if (rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
                                        rotation = rotation.getOpposite();
                                    } else {
                                        if (rotation == EnumFacing.NORTH) {
                                            rotation = rotation.rotateAround(EnumFacing.Axis.X);
                                        } else {
                                            rotation = EnumFacing.EAST;
                                        }
                                    }
                                }
                                if (rotation == null)
                                    rotation = EnumFacing.NORTH;
                                tileEntity2.setFacing(rotation);
                            }
                        }
                    }

                }
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    private boolean canPlace(IBlockState state) {
        return state.getMaterial() == Material.AIR || state.getBlock() == Blocks.TALLGRASS || state.getBlock() == Blocks.DOUBLE_PLANT || state.getBlock() == Blocks.RED_FLOWER || state
                .getMaterial()
                .isLiquid();
    }

    @Override
    public void registerModels() {
        registerModel(this, 0, this.name);
    }

}
