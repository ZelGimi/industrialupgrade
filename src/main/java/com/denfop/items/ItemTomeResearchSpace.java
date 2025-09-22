package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.Data;
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
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemTomeResearchSpace extends Item implements IModelRegister {

    private final String name;

    public ItemTomeResearchSpace() {
        super();
        this.setMaxStackSize(1);
        this.canRepair = false;
        this.name = "tome_research";
        this.setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        if (!worldIn.isRemote) {
            final NBTTagCompound nbt = ModUtils.nbt(playerIn.getHeldItem(handIn));
            if (playerIn.isSneaking()) {

                nbt.setUniqueId("uuid", playerIn.getUniqueID());
            } else {
                final UUID uuid = nbt.getUniqueId("uuid");
                final Map<IBody, Data> data = SpaceNet.instance
                        .getFakeSpaceSystem()
                        .getDataFromUUID(uuid);
                if (data != null && !data.isEmpty()) {
                    SpaceNet.instance.getFakeSpaceSystem().copyData(data, playerIn.getUniqueID());
                }
            }
            return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu."));
    }

    public String getItemStackDisplayName() {
        return I18n.translateToLocal(this.getUnlocalizedName().replace("item.", "iu."));
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, @NotNull ITooltipFlag advanced) {
        tooltip.add(Localization.translate("iu.tome_research1"));
        tooltip.add(Localization.translate("iu.tome_research2"));
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

}
