package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ItemCell extends ItemSubTypes<CellType> implements IModelRegister {

    protected static final String NAME = "itemcell";

    public ItemCell() {
        super(CellType.class);
        this.setCreativeTab(IUCore.ItemTab);
        this.setMaxStackSize(64);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
        IUItem.uuMatterCell = new ItemStack(this, 1, 1);
        IUItem.HeliumCell = new ItemStack(this, 1, 2);
        IUItem.NeftCell = new ItemStack(this, 1, 3);
        IUItem.BenzCell = new ItemStack(this, 1, 4);
        IUItem.DizelCell = new ItemStack(this, 1, 5);
        IUItem.PolyethCell = new ItemStack(this, 1, 6);
        IUItem.PolypropCell = new ItemStack(this, 1, 7);
        IUItem.OxyCell = new ItemStack(this, 1, 8);
        IUItem.HybCell = new ItemStack(this, 1, 9);


    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("iu.iucell", "iu.itemcell"));
    }

    public boolean tryPlaceContainedLiquid(FluidStack fs, @Nullable EntityPlayer player, World worldIn, BlockPos posIn) {

        Block containedBlock;
        if (fs.getFluid() == FluidRegistry.WATER) {
            containedBlock = Blocks.FLOWING_WATER;
        } else if (fs.getFluid() == FluidRegistry.LAVA) {
            containedBlock = Blocks.FLOWING_LAVA;
        } else {
            containedBlock = fs.getFluid().getBlock();
        }
        if (containedBlock == Blocks.AIR) {
            return false;
        } else {
            IBlockState iblockstate = worldIn.getBlockState(posIn);
            Material material = iblockstate.getMaterial();
            boolean flag = !material.isSolid();
            boolean flag1 = iblockstate.getBlock().isReplaceable(worldIn, posIn);

            if (!worldIn.isAirBlock(posIn) && !flag && !flag1) {
                return false;
            } else {
                if (worldIn.provider.doesWaterVaporize() && containedBlock == Blocks.FLOWING_WATER) {
                    int l = posIn.getX();
                    int i = posIn.getY();
                    int j = posIn.getZ();
                    worldIn.playSound(
                            player,
                            posIn,
                            SoundEvents.BLOCK_FIRE_EXTINGUISH,
                            SoundCategory.BLOCKS,
                            0.5F,
                            2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F
                    );

                    for (int k = 0; k < 8; ++k) {
                        worldIn.spawnParticle(
                                EnumParticleTypes.SMOKE_LARGE,
                                (double) l + Math.random(),
                                (double) i + Math.random(),
                                (double) j + Math.random(),
                                0.0D,
                                0.0D,
                                0.0D
                        );
                    }
                } else {
                    if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid()) {
                        worldIn.destroyBlock(posIn, true);
                    }

                    SoundEvent soundevent = containedBlock == Blocks.FLOWING_LAVA
                            ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA
                            : SoundEvents.ITEM_BUCKET_EMPTY;
                    worldIn.playSound(player, posIn, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlockState(posIn, containedBlock.getDefaultState(), 11);
                }
                fs.amount -= 1000;
                return true;
            }
        }
    }

    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, NBTTagCompound nbt) {
        CellType type = getType(stack);
        CellType.CellFluidHandler handler;
        if (type.isFluidContainer()) {
            handler = new CellType.CellFluidHandler(stack, type);
        } else {
            handler = null;
        }

        return handler;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(
            World world, EntityPlayer player, EnumHand hand
    ) {

        RayTraceResult position = this.rayTrace(world, player, true);
        if (position == null) {
            return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));
        }
        if (position.typeOfHit == RayTraceResult.Type.BLOCK) {
            final BlockPos pos = position.getBlockPos();
            final ItemStack stack = player.getHeldItem(hand);
            if (stack.getItemDamage() != 0) {
                Fluid fluid = IUItem.celltype1.get(stack.getItemDamage());
                boolean flag1 = world.getBlockState(pos).getBlock().isReplaceable(world, pos);
                BlockPos blockpos1 = flag1 && position.sideHit == EnumFacing.UP ? pos : pos.offset(position.sideHit);

                if (tryPlaceContainedLiquid(new FluidStack(fluid, 1000), player, world, blockpos1)) {
                    player.getHeldItem(hand).shrink(1);
                    if (!ModUtils.storeInventoryItem(new ItemStack(IUItem.cell_all, 1),
                            player, false
                    )) {
                        if (!world.isRemote) {
                            ModUtils.dropAsEntity(world, pos, new ItemStack(IUItem.cell_all, 1));
                        }
                    }
                    return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
                }

            } else {

                if (!world.canMineBlockBody(player, pos)) {
                    return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));
                }

                if (!player.canPlayerEdit(pos, position.sideHit, player.getHeldItem(hand))) {
                    return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));
                }
                IBlockState state = world.getBlockState(pos);
                Block block = state.getBlock();

                if (block instanceof IFluidBlock && ((IFluidBlock) block).canDrain(world, pos)) {
                    Fluid liquid = ((IFluidBlock) block).getFluid();
                    if (IUItem.celltype.containsKey(liquid) && player.getHeldItem(hand).getItemDamage() == 0) {
                        if (IUItem.celltype.containsKey(liquid)) {
                            world.setBlockToAir(pos);
                            player.getHeldItem(hand).shrink(1);
                            if (!player.inventory.addItemStackToInventory(new ItemStack(IUItem.cell_all, 1,
                                    IUItem.celltype.get(liquid)
                            ))) {
                                if (!world.isRemote) {
                                    ModUtils.dropAsEntity(world, pos, new ItemStack(IUItem.cell_all, 1,
                                            IUItem.celltype.get(liquid)
                                    ));
                                }

                            }
                            return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
                        }
                    }
                } else if (state.getMaterial().isLiquid()) {


                    final FluidStack ret = new FluidStack(FluidRegistry.getFluid(block.getUnlocalizedName().substring(5)), 1000);
                    System.out.println(player.getHeldItem(hand).getItemDamage());
                    if (IUItem.celltype.containsKey(ret.getFluid()) && player.getHeldItem(hand).getItemDamage() == 0) {
                        world.setBlockToAir(pos);
                        player.getHeldItem(hand).shrink(1);
                        if (!player.inventory.addItemStackToInventory(new ItemStack(IUItem.cell_all, 1,
                                IUItem.celltype.get(ret.getFluid())
                        ))) {
                            if (!world.isRemote) {
                                ModUtils.dropAsEntity(world, pos, new ItemStack(IUItem.cell_all, 1,
                                        IUItem.celltype.get(ret.getFluid())
                                ));
                            }

                        }
                        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
                    }
                }
            }
        }

        return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));

    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }


    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(
                        Constants.MOD_ID + ":" + NAME + "/" + "itemcell" + CellType.getFromID(meta).getName(),
                        null
                )
        );
    }


}
