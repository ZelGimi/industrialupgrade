package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.blocks.BlockFoam;
import com.denfop.blocks.FluidName;
import com.denfop.items.ItemFluidContainer;
import com.denfop.utils.ModUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class ItemSprayer extends ItemFluidContainer {

    public ItemSprayer() {
        super("foam_sprayer", 8000);
        this.setMaxStackSize(1);
    }

    private static boolean canPlaceFoam(World world, BlockPos pos, Target target) {
        if (Objects.requireNonNull(target) == Target.Any) {
            return IUItem.foam.canPlaceBlockOnSide(world, pos, EnumFacing.DOWN);
        } else {
            assert false;
        }

        return false;
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu.").replace(".name", ""));
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            subItems.add(new ItemStack(this));
            subItems.add(this.getItemStack(FluidName.fluidconstruction_foam));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(Constants.MOD_ID + ":" + "tools" + "/" + "foam_sprayer", null)
        );
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (IUCore.proxy.isSimulating() && IUCore.keyboard.isChangeKeyDown(player)) {
            ItemStack stack = ModUtils.get(player, hand);
            NBTTagCompound nbtData = ModUtils.nbt(stack);
            int mode = nbtData.getInteger("mode");
            mode = mode == 0 ? 1 : 0;
            nbtData.setInteger("mode", mode);
            String sMode = mode == 0 ? "iu.tooltip.mode.normal" : "iu.tooltip.mode.single";
            IUCore.proxy.messagePlayer(player, "iu.tooltip.mode", sMode);
        }

        return super.onItemRightClick(world, player, hand);
    }

    public EnumActionResult onItemUse(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumHand hand,
            EnumFacing side,
            float xOffset,
            float yOffset,
            float zOffset
    ) {
        if (!IUCore.proxy.isSimulating()) {
            return EnumActionResult.SUCCESS;
        } else {
            int maxFoamBlocks = 0;
            ItemStack stack = ModUtils.get(player, hand);
            FluidStack fluid = FluidUtil.getFluidContained(stack);
            if (fluid != null && fluid.amount > 0) {
                maxFoamBlocks += fluid.amount / this.getFluidPerFoam();
            }

            ItemStack pack = player.inventory.armorInventory.get(2);


            if (maxFoamBlocks == 0) {
                return EnumActionResult.FAIL;
            } else {
                maxFoamBlocks = Math.min(maxFoamBlocks, this.getMaxFoamBlocks(stack));
                Target target;
                if (canPlaceFoam(world, pos, Target.Scaffold)) {
                    target = Target.Scaffold;
                } else {
                    pos = pos.offset(side);
                    target = Target.Any;
                }

                Vec3d viewVec = player.getLookVec();
                EnumFacing playerViewFacing = EnumFacing.getFacingFromVector(
                        (float) viewVec.x,
                        (float) viewVec.y,
                        (float) viewVec.z
                );
                int amount = this.sprayFoam(world, pos, playerViewFacing.getOpposite(), target, maxFoamBlocks);
                amount *= this.getFluidPerFoam();
                if (amount > 0) {
                    IFluidHandlerItem handler;
                    if (!pack.isEmpty()) {
                        handler = (IFluidHandlerItem) this.initCapabilities(stack, new NBTTagCompound());


                        fluid = handler.drain(amount, true);
                        amount -= fluid.amount;
                        player.inventory.armorInventory.set(2, handler.getContainer());
                    }

                    if (amount > 0) {
                        handler = FluidUtil.getFluidHandler(stack);

                        assert handler != null;

                        handler.drain(amount, true);
                        player.inventory.mainInventory.set(player.inventory.currentItem, handler.getContainer());
                    }

                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.PASS;
                }
            }
        }
    }

    public int sprayFoam(World world, BlockPos pos, EnumFacing excludedDir, Target target, int maxFoamBlocks) {
        if (!canPlaceFoam(world, pos, target)) {
            return 0;
        } else {
            Queue<BlockPos> toCheck = new ArrayDeque<>();
            Set<BlockPos> positions = new HashSet<>();
            toCheck.add(pos);

            BlockPos cPos;
            while ((cPos = toCheck.poll()) != null && positions.size() < maxFoamBlocks) {
                if (canPlaceFoam(world, cPos, target) && positions.add(cPos)) {
                    EnumFacing[] var9 = EnumFacing.VALUES;
                    for (EnumFacing dir : var9) {
                        if (dir != excludedDir) {
                            toCheck.add(cPos.offset(dir));
                        }
                    }
                }
            }

            toCheck.clear();
            int failedPlacements = 0;

            for (final BlockPos targetPos : positions) {
                IBlockState state = world.getBlockState(targetPos);
                if (!world.setBlockState(targetPos, IUItem.foam.getState(BlockFoam.FoamType.reinforced))) {
                    ++failedPlacements;
                }
            }

            return positions.size() - failedPlacements;
        }
    }

    protected int getMaxFoamBlocks(ItemStack stack) {
        NBTTagCompound nbtData = ModUtils.nbt(stack);
        return nbtData.getInteger("mode") == 0 ? 10 : 1;
    }

    protected int getFluidPerFoam() {
        return 100;
    }


    public boolean canfill(Fluid fluid) {
        return fluid == FluidName.fluidconstruction_foam.getInstance();
    }

    private enum Target {
        Any,
        Scaffold;

        Target() {
        }
    }

}
