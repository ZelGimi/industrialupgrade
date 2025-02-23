package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemFacadeItem extends Item implements IModelRegister, IItemStackInventory {

    private final String name;

    public ItemFacadeItem(String name) {
        super();
        this.setMaxStackSize(1);
        this.canRepair = false;
        this.name = name;
        this.setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }


    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu."));
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, @NotNull ITooltipFlag advanced) {

    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(Constants.MOD_ID + ":" + name, null)
        );
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {

        ItemStack stack = ModUtils.get(player, hand);
        if (IUCore.proxy.isSimulating() && !player.isSneaking()) {
            RayTraceResult position = this.rayTrace(world, player, false);
            if (position == null || position.typeOfHit != RayTraceResult.Type.BLOCK) {
                save(stack, player);
                player.openGui(IUCore.instance, 1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
                return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    public void save(ItemStack stack, EntityPlayer player) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setBoolean("open", true);
        nbt.setInteger("slot_inventory", player.inventory.currentItem);
    }

    @Override
    public IAdvInventory getInventory(final EntityPlayer var1, final ItemStack var2) {
        return new FacadeItemInventory(var1, var2);
    }

    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, EntityPlayer player) {
        if (!player.getEntityWorld().isRemote && !ModUtils.isEmpty(stack) && player.openContainer instanceof ContainerFacadeItem) {
            FacadeItemInventory toolbox = ((ContainerFacadeItem) player.openContainer).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAndThrow(stack);
                player.closeScreen();
            }
        }

        return true;
    }

}
