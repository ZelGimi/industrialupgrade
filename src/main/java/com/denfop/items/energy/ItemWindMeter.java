package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.items.BaseEnergyItem;
import com.denfop.utils.ModUtils;
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
import org.jetbrains.annotations.NotNull;

public class ItemWindMeter extends BaseEnergyItem implements IModelRegister {

    public ItemWindMeter() {
        super("wind_meter", 5000, 500, 1);
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "energy" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    public @NotNull ActionResult<ItemStack> onItemRightClick(
            @NotNull World world,
            @NotNull EntityPlayer player,
            @NotNull EnumHand hand
    ) {
        ItemStack stack = ModUtils.get(player, hand);
        if (!IUCore.proxy.isSimulating() || world.provider.getDimension() != 0) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        } else if (!ElectricItem.manager.canUse(stack, 10)) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        } else {
            ElectricItem.manager.use(stack, 10, player);


            IUCore.proxy.messagePlayer(
                    player,
                    Localization.translate("iu.wind_meter.info") + String.format(
                            "%.1f",
                            WindSystem.windSystem.getWind_Strength()
                    ) + " m/s"
            );
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name, null);
    }


}
