package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.pollution.ChunkLevel;
import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemPollutionDevice extends Item implements IModelRegister {

    private final String name;

    public ItemPollutionDevice() {
        super();
        this.setMaxStackSize(1);
        this.canRepair = false;
        this.name = "pollution_device";
        this.setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
        if (!world.isRemote) {
            ChunkPos playerChunk = new ChunkPos(player.getPosition());

            ChunkLevel airChunkLevel = PollutionManager.pollutionManager.getChunkLevelAir(playerChunk);
            ChunkLevel soilChunkLevel = PollutionManager.pollutionManager.getChunkLevelSoil(playerChunk);

            if (airChunkLevel != null) {
                sendPollutionMessage(player, airChunkLevel.getLevelPollution(), "message.pollution.air");
            }

            if (soilChunkLevel != null) {
                sendPollutionMessage(player, soilChunkLevel.getLevelPollution(), "message.pollution.soil");
            }

            return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    private void sendPollutionMessage(EntityPlayer player, LevelPollution level, String messageKey) {
        switch (level) {
            case LOW:
            case VERY_LOW:
            case MEDIUM:
            case HIGH:
            case VERY_HIGH:
                IUCore.proxy.messagePlayer(player, Localization.translate(messageKey + "." + level.name().toLowerCase()));
                break;
        }
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu."));
    }

    public String getItemStackDisplayName() {
        return I18n.translateToLocal(this.getUnlocalizedName().replace("item.", "iu."));
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

}
