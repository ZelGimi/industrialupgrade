package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.register.Register;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFeature extends Item implements IModelRegister {

    private final String name;

    public ItemFeature() {
        super();
        this.setMaxStackSize(1);
        this.canRepair = false;
        this.name = "item_feature";
        this.setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void registerModels() {

    }


    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer playerIn, EnumHand handIn) {
        if (world.isRemote) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }
        final int centerX = (int) playerIn.posX;
        final int centerY = (int) playerIn.posY;
        final int centerZ = (int) playerIn.posZ;
        final BlockPos center = new BlockPos(centerX, centerY, centerZ);
        int radius = 10; // Радиус круга

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    if (distance <= radius && distance > radius * 0.45) {
                        world.setBlockState(center.add(x, y, z), Blocks.STONE.getDefaultState());
                    } else if (distance <= radius && distance >= radius * 0.35) {
                        world.setBlockState(center.add(x, y, z), Blocks.IRON_ORE.getDefaultState());
                    }
                }
            }
        }


        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

}
