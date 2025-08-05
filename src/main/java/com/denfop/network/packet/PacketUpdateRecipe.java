package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.util.List;

public class PacketUpdateRecipe implements IPacket {

    public PacketUpdateRecipe() {
    }

    ;

    public PacketUpdateRecipe(String recipe, boolean isFluid, ServerPlayer player) {
        if (isFluid) {
            List<BaseFluidMachineRecipe> recipes = Recipes.recipes.getRecipeFluid().getRecipeList(recipe);
            sendChunkedRecipes(recipe, recipes, true, player);
        } else {
            List<BaseMachineRecipe> recipes = Recipes.recipes.getRecipeList(recipe);
            sendChunkedRecipes(recipe, recipes, false, player);
        }
    }
    private <T> void sendChunkedRecipes(String recipeKey, List<T> fullList, boolean isFluid, ServerPlayer player) {
        final int CHUNK_SIZE = 64;
        for (int i = 0; i < fullList.size(); i += CHUNK_SIZE) {
            int end = Math.min(i + CHUNK_SIZE, fullList.size());
            List<T> chunk = fullList.subList(i, end);

            CustomPacketBuffer buffer = new CustomPacketBuffer();
            buffer.writeByte(this.getId());

            try {
                EncoderHandler.encode(buffer, recipeKey);
                EncoderHandler.encode(buffer, isFluid);
                EncoderHandler.encode(buffer, chunk);
            } catch (IOException e) {
                throw new RuntimeException("Failed to encode recipe chunk", e);
            }

            buffer.flip();
            IUCore.network.getServer().sendPacket(buffer, player);
        }
    }


    @Override
    public byte getId() {
        return 60;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            String recipe = (String) DecoderHandler.decode(customPacketBuffer);
            boolean isFluid = (boolean) DecoderHandler.decode(customPacketBuffer);
            if (isFluid) {
                List<BaseFluidMachineRecipe> recipes = (List<BaseFluidMachineRecipe>) DecoderHandler.decode(customPacketBuffer);
                for (BaseFluidMachineRecipe baseFluidMachineRecipe : recipes)
                    Recipes.recipes.getRecipeFluid().addRecipe(recipe, baseFluidMachineRecipe);
            }else{
                List<BaseMachineRecipe> recipes = (List<BaseMachineRecipe>) DecoderHandler.decode(customPacketBuffer);
                for (BaseMachineRecipe baseFluidMachineRecipe : recipes)
                    Recipes.recipes.addRecipe(recipe, baseFluidMachineRecipe);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
