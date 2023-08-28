//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IEnergyItem;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBatterySU extends Item implements IModelRegister {

    private final String name;
    public int capacity;
    public int tier;

    public ItemBatterySU(String internalName, int capacity1, int tier1) {
        super();
        this.capacity = capacity1;
        this.tier = tier1;
        this.setCreativeTab(IUCore.RecourseTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(internalName)).setUnlocalizedName(internalName);
        IUCore.proxy.addIModelRegister(this);
        this.name = internalName;
    }

    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(Constants.MOD_ID + ":" + name, null)
        );
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu."));
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = ModUtils.get(player, hand);
        double energy = this.capacity;

        for (int i = 0; i < 9 && energy > 0.0; ++i) {
            ItemStack target = player.inventory.mainInventory.get(i);
            if (!target.isEmpty() && target != stack && target.getItem() instanceof IEnergyItem) {
                energy -= ElectricItem.manager.charge(target, energy, this.tier, true, false);
            }
        }

        return new ActionResult(EnumActionResult.PASS, stack);
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return true;
    }


}
