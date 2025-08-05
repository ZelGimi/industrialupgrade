package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TagsUpdatedEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketFixerRecipe implements IPacket {
    public PacketFixerRecipe() {

    }

    public PacketFixerRecipe(Level level, ServerPlayer player, int type) {
        if (type == 0) {
            for (String nameRecipe : Recipes.recipes.getMap_recipe_managers()) {
                CustomPacketBuffer buffer = new CustomPacketBuffer();
                buffer.writeByte(this.getId());
                buffer.writeByte(type);
                List<BaseMachineRecipe> baseMachineRecipeList = Recipes.recipes.getRecipeList(nameRecipe);
                buffer.flip();
                CompoundTag tag = new CompoundTag();
                tag.putString("name", nameRecipe);
                ListTag recipeListTag = new ListTag();
                for (BaseMachineRecipe recipe : baseMachineRecipeList) {
                    recipeListTag.add(recipe.writeNBT());
                }
                tag.put("recipes", recipeListTag);
                try {
                    EncoderHandler.encode(buffer, tag);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                IUCore.network.getServer().sendPacket(buffer, player);
            }
        }else if (type == 1) {
            for (String nameRecipe : Recipes.recipes.getRecipeFluid().getRecipes()) {
                CustomPacketBuffer buffer = new CustomPacketBuffer();
                buffer.writeByte(this.getId());
                buffer.writeByte(type);
                List<BaseFluidMachineRecipe> baseMachineRecipeList = Recipes.recipes.getRecipeFluid().getRecipeList(nameRecipe);
                buffer.flip();
                CompoundTag tag = new CompoundTag();
                tag.putString("name", nameRecipe);
                ListTag recipeListTag = new ListTag();
                for (BaseFluidMachineRecipe recipe : baseMachineRecipeList) {
                    recipeListTag.add(recipe.writeNBT());
                }
                tag.put("recipes", recipeListTag);
                try {
                    EncoderHandler.encode(buffer, tag);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                IUCore.network.getServer().sendPacket(buffer, player);
            }
        }else if (type == 2){
            CustomPacketBuffer buffer = new CustomPacketBuffer();
            buffer.writeByte(this.getId());
            buffer.writeByte(type);
            try {
                EncoderHandler.encode(buffer,level);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public byte getId() {
        return 56;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
        int type = is.readInt();
        if (type == 0 && !IUCore.register1) {
            CompoundTag tag;
            try {
                tag = (CompoundTag) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String name = tag.getString("name");
            List<BaseMachineRecipe> recipeList = new ArrayList<>();
            ListTag recipeListTag = tag.getList("recipes", Tag.TAG_COMPOUND);

            for (Tag t : recipeListTag) {
                if (t instanceof CompoundTag recipeTag) {
                    recipeList.add(BaseMachineRecipe.readNBT(recipeTag));
                }
            }
            for (BaseMachineRecipe baseMachineRecipe : recipeList)
                Recipes.recipes.addRecipe(name, baseMachineRecipe);
        }else if (type == 1 && !IUCore.register1) {
            CompoundTag tag;
            try {
                tag = (CompoundTag) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String name = tag.getString("name");
            List<BaseFluidMachineRecipe> recipeList = new ArrayList<>();
            ListTag recipeListTag = tag.getList("recipes", Tag.TAG_COMPOUND);

            for (Tag t : recipeListTag) {
                if (t instanceof CompoundTag recipeTag) {
                    recipeList.add(BaseFluidMachineRecipe.readNBT(recipeTag));
                }
            }
            for (BaseFluidMachineRecipe baseMachineRecipe : recipeList)
                Recipes.recipes.getRecipeFluid().addRecipe(name, baseMachineRecipe);
        }else if (type == 2){
            Level level;
            try {
                level = (Level) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (level != null){
                IUCore.instance.getore(new TagsUpdatedEvent(level.registryAccess(),true,true));
                IUCore.instance.registerData(level);
            }
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
