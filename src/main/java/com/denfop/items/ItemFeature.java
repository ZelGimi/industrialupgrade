package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.FluidName;
import com.denfop.register.Register;
import com.denfop.world.WorldGenOil;
import com.denfop.world.vein.AlgorithmVein;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.TypeVein;
import com.denfop.world.vein.VeinType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ItemFeature extends Item implements IModelRegister {

    private final String name;

    public ItemFeature(){
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
       // new WorldGenOil(FluidName.fluidneft.getInstance().getBlock(), FluidName.fluidneft.getInstance().getBlock())
         //       .generate(worldIn, new Random(), new BlockPos(playerIn.posX, playerIn.posY, playerIn.posZ));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
