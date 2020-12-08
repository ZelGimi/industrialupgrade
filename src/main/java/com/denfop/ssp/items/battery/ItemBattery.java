package com.denfop.ssp.items.battery;


import com.google.common.base.CaseFormat;
import ic2.api.item.ElectricItem;
import ic2.core.init.BlocksItems;
import ic2.core.item.BaseElectricItem;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBattery extends BaseElectricItem {
  private final String name;

public ItemBattery(String name, double maxCharge, double transferLimit, int tier) {
    super(null, maxCharge,  transferLimit,  tier);
    
    setMaxStackSize(16);
    BlocksItems.registerItem(this, new ResourceLocation("super_solar_panels", this.name = name)).setUnlocalizedName(name);
  }
  
  public String getUnlocalizedName() {
	    return "super_solar_panels." + super.getUnlocalizedName().substring(4);
	  }
@SideOnly(Side.CLIENT)
public void registerModels(ItemName name) {
  ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("super_solar_panels:" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.name), null));
}
  public boolean canProvideEnergy(ItemStack stack) {
    return true;
  }
  
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack stack = StackUtil.get(player, hand);
    if (world.isRemote || StackUtil.getSize(stack) != 1)
      return new ActionResult(EnumActionResult.PASS, stack); 
    if (ElectricItem.manager.getCharge(stack) > 0.0D) {
      boolean transferred = false;
      for (int i = 0; i < 9; i++) {
        ItemStack target = player.inventory.mainInventory.get(i);
        if (target != null && target != stack)
          if (ElectricItem.manager.discharge(target, Double.POSITIVE_INFINITY, 2147483647, true, true, true) <= 0.0D) {
            double transfer = ElectricItem.manager.discharge(stack, 2.0D * this.transferLimit, 2147483647, true, true, true);
            if (transfer > 0.0D) {
              transfer = ElectricItem.manager.charge(target, transfer, this.tier, true, false);
              if (transfer > 0.0D) {
                ElectricItem.manager.discharge(stack, transfer, 2147483647, true, true, false);
                transferred = true;
              } 
            } 
          }  
      } 
      if (transferred && !world.isRemote)
        player.openContainer.detectAndSendChanges(); 
    } 
    return new ActionResult(EnumActionResult.SUCCESS, stack);
  }
  
  private static final int maxLevel = 4;
}
