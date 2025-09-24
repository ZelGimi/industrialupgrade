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

import static com.denfop.IUCore.register;

public class PacketUpdateRecipe implements IPacket {

    private CustomPacketBuffer buffer;

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

            CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
            buffer.writeByte(this.getId());

            try {
                EncoderHandler.encode(buffer, recipeKey);
                EncoderHandler.encode(buffer, isFluid);
                int count = 0;
                CustomPacketBuffer buffer1 = new CustomPacketBuffer(player.registryAccess());
                for (T t : chunk) {
                    try {
                        EncoderHandler.encode(buffer1, t);
                        count++;
                    } catch (Exception ignored) {

                    }
                }
                EncoderHandler.encode(buffer, count);
                buffer.writeBytes(buffer1);

            } catch (IOException e) {
                throw new RuntimeException("Failed to encode recipe chunk", e);
            }

            buffer.flip();
            this.buffer = buffer;
            IUCore.network.getServer().sendPacket(this, buffer, player);
        }
    }


    @Override
    public byte getId() {
        return 60;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            if (!register) {
                String recipe = (String) DecoderHandler.decode(customPacketBuffer);
                boolean isFluid = (boolean) DecoderHandler.decode(customPacketBuffer);
                int count = (int) DecoderHandler.decode(customPacketBuffer);

                if (isFluid) {
                    for (int i = 0; i < count; i++)
                        Recipes.recipes.getRecipeFluid().addRecipe(recipe, (BaseFluidMachineRecipe) DecoderHandler.decode(customPacketBuffer));
                } else {
                    for (int i = 0; i < count; i++)
                        Recipes.recipes.addRecipe(recipe, (BaseMachineRecipe) DecoderHandler.decode(customPacketBuffer));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        buffer = customPacketBuffer;
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
