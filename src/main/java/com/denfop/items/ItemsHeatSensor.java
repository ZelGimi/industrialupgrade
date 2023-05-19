package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.componets.HeatComponent;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemsHeatSensor extends Item implements   IModelRegister {

    public static String NAME = "heat_sensor";

    public ItemsHeatSensor() {
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

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {

        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(Localization.translate("module.wireless"));
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
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityInventory) {
                TileEntityInventory tileEntityInventory = (TileEntityInventory) tileEntity;
                HeatComponent component = tileEntityInventory.getComp(HeatComponent.class);
                if(component == null)
                    return EnumActionResult.PASS;
                IC2.platform.messagePlayer(
                        player,
                         String.format("%.2f", component.getEnergy()) + "°C" + "/" + component.getCapacity()+ "°C"
                );
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
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
}
