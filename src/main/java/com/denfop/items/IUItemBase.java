package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.recipes.ScrapboxRecipeManager;
import com.denfop.register.Register;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class IUItemBase extends Item implements IModelRegister {

    private final String name;
    private final String path;

    public IUItemBase(String name) {
        this(name, "");
    }

    public IUItemBase(String name, String path) {
        super();
        this.setCreativeTab(IUCore.ItemTab);
        this.setMaxStackSize(64);

        this.name = name;
        this.path = path;
        setUnlocalizedName(name);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    public String getUnlocalizedName() {
        return super.getUnlocalizedName() + ".name";
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu.") + ".name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(Constants.MOD_ID + ":" + path + this.name, null)
        );
    }


    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(
            @Nonnull final World world,
            @Nonnull final EntityPlayer player,
            @Nonnull final EnumHand hand
    ) {
        if (!player.getHeldItem(hand).getItem().equals(IUItem.doublescrapBox)) {
            return super.onItemRightClick(world, player, hand);
        } else {
            int i = 0;
            ItemStack stack = player.getHeldItem(hand);
            while (i < 9) {
                if (!player.getEntityWorld().isRemote) {
                    ItemStack drop = ScrapboxRecipeManager.instance.getDrop(IUItem.scrapBox);
                    player.dropItem(drop, false);
                }
                i++;
            }
            stack.shrink(1);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

        }
    }

}
