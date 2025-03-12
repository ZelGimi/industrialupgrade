package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.FluidName;
import com.denfop.register.Register;
import com.denfop.world.GenData;
import com.denfop.world.WorldGenGas;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGasSensor extends Item implements IModelRegister {

    private final String name;

    public ItemGasSensor() {
        super();
        this.setMaxStackSize(1);
        this.canRepair = false;
        this.name = "gas_sensor";
        this.setCreativeTab(IUCore.ItemTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "tools" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    public String getUnlocalizedName(ItemStack stack) {
        return "iu." + super.getUnlocalizedName().substring(5);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(
            final World world,
            final EntityPlayer player,
            final EnumHand p_77659_3_
    ) {
        if (world.isRemote) {
            return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(p_77659_3_));
        }
        ChunkPos chunkPos = new ChunkPos((int) player.posX >> 4, (int) player.posZ >> 4);
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                final ChunkPos chunkPos1 = new ChunkPos(chunkPos.x + i, chunkPos.z + j);
                GenData typeGas = WorldGenGas.gasMap.get(chunkPos1);
                if (typeGas != null) {
                    String text = "";
                    switch (typeGas.getTypeGas()) {
                        case GAS:
                            text = I18n.translateToLocal(FluidName.fluidgas.getInstance().getUnlocalizedName());
                            break;
                        case IODINE:
                            text = I18n.translateToLocal(FluidName.fluidiodine.getInstance().getUnlocalizedName());
                            break;
                        case BROMIDE:
                            text = I18n.translateToLocal(FluidName.fluidbromine.getInstance().getUnlocalizedName());
                            break;
                        case CHLORINE:
                            text = I18n.translateToLocal(FluidName.fluidchlorum.getInstance().getUnlocalizedName());
                            break;
                        case FLUORINE:
                            text = I18n.translateToLocal(FluidName.fluidfluor.getInstance().getUnlocalizedName());
                            break;

                    }
                    IUCore.proxy.messagePlayer(
                            player,
                            (chunkPos1.getXStart()+16) + " " + (typeGas.getY()) + " " + (chunkPos1.getZStart()+16) + " " + text
                    );
                }
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(p_77659_3_));
    }

    @Override
    public void registerModels() {
        registerModel(this, 0, this.name);
    }

}
