package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.IModelRegister;
import ic2.api.recipe.Recipes;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemIC2;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class IUItemBase extends ItemIC2 implements IModelRegister {

    private final String name;
    private final String path;

    public IUItemBase(String name) {
        this(name, "");
    }

    public IUItemBase(String name, String path) {
        super(null);
        this.setCreativeTab(IUCore.ItemTab);
        this.setMaxStackSize(64);

        this.name = name;
        this.path = path;
        setUnlocalizedName(name);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4) + ".name";
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
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
                    ItemStack drop = Recipes.scrapboxDrops.getDrop(Ic2Items.scrapBox, false);
                    player.dropItem(drop, false);
                }
                i++;
            }
            stack.shrink(1);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

        }
    }

}
