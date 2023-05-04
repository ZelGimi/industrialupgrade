package com.denfop.items;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IAdvEnergyTile;
import com.denfop.api.inv.IHasGui;
import com.denfop.container.ContainerMeter;
import com.denfop.invslot.HandHeldMeter;
import ic2.api.item.IBoxable;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolMeter extends Item implements IBoxable, IHandHeldInventory, IModelRegister {

    public static String NAME = "meter";

    public ItemToolMeter() {
        this.maxStackSize = 1;
        this.setMaxDamage(0);
        this.setCreativeTab(IUCore.EnergyTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public String getUnlocalizedName() {
        return "iu" + super.getUnlocalizedName().substring(4);
    }

    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName();
    }

    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return this.getUnlocalizedName(stack);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return Localization.translate(this.getUnlocalizedName(stack));
    }

    public EnumActionResult onItemUseFirst(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ,
            EnumHand hand
    ) {
        if (world.isRemote) {
            return EnumActionResult.PASS;
        } else {
            IAdvEnergyTile tile = EnergyNetGlobal.instance.getTile(world, pos);
            if (tile == EnergyNetGlobal.EMPTY) {
                IC2.platform.messagePlayer(player, "Not an energy net tile");
            } else if (IUCore.proxy.launchGui(player, this.getInventory(player, StackUtil.get(player, hand)))) {
                ContainerMeter container = (ContainerMeter) player.openContainer;
                container.setUut(tile);
                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.SUCCESS;
        }
    }

    public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
        if (!player.getEntityWorld().isRemote && !StackUtil.isEmpty(stack) && player.openContainer instanceof ContainerMeter) {
            HandHeldMeter euReader = ((ContainerMeter) player.openContainer).base;
            if (euReader.isThisContainer(stack)) {
                euReader.saveAsThrown(stack);
                player.closeScreen();
            }
        }

        return true;
    }

    @Override
    public void registerModels() {
        registerModel();
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel() {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(
                        Constants.MOD_ID + ":" + "tools" + "/" + NAME,
                        null
                )
        );
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return true;
    }

    public IHasGui getInventory(EntityPlayer player, ItemStack stack) {
        return new HandHeldMeter(player, stack);
    }


}
