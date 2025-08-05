package com.denfop.tiles.base;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DataOre {

    private String name;
    private List<BlockPos> listPos;
    private List<Vein> veinsList;
    private ItemStack stack;

    private List<ItemStack> recipe_stack;
    private BlockState iBlockState;

    private int number;
    private int y;

    public DataOre(String name, int number, int y, BlockPos pos, ItemStack stack, BlockState state) {
        this.name = name;
        this.number = number;
        this.y = y;
        if (pos != null) {
            this.listPos = new ArrayList<>(Collections.singleton(pos));
        } else {
            this.listPos = new ArrayList<>();
        }
        this.stack = stack;
        this.veinsList = new ArrayList<>();

        this.iBlockState = state;
    }

    public DataOre(HolderLookup.Provider provider, CompoundTag tagCompound) {
        name = tagCompound.getString("name");
        number = tagCompound.getInt("number");
        y = tagCompound.getInt("y");
        int size = tagCompound.getInt("size");
        this.listPos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            CompoundTag posTag = tagCompound.getCompound("blockpos_" + i);
            this.listPos.add(new BlockPos(posTag.getInt("x"), posTag.getInt("y"), posTag.getInt("z")));
        }
        CompoundTag stackTag = tagCompound.getCompound("stackTag");
        stack = ItemStack.parseOptional(provider, stackTag);
        this.iBlockState = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
        this.veinsList = new ArrayList<>();
        int size1 = tagCompound.getInt("size1");
        for (int i = 0; i < size1; i++) {
            CompoundTag veinTag = tagCompound.getCompound("vein_" + i);
            ChunkPos chunkPos = new ChunkPos(veinTag.getInt("x"), veinTag.getInt("z"));
            Vein vein = VeinSystem.system.getVein(chunkPos);
            if (vein != VeinSystem.EMPTY) {
                this.veinsList.add(vein);
            }
        }
    }

    public DataOre(CustomPacketBuffer customPacketBuffer) {
        name = customPacketBuffer.readString();
        number = customPacketBuffer.readInt();
        y = customPacketBuffer.readInt();
        try {
            this.listPos = (List<BlockPos>) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.iBlockState = ((Block) DecoderHandler.decode(customPacketBuffer)).defaultBlockState();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.veinsList = (List<Vein>) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ItemStack> getRecipe_stack(com.denfop.tiles.base.TileAnalyzer tileAnalyzer, BlockPos pos) {
        if (recipe_stack != null) {
            return recipe_stack;
        }
        final List<ItemStack> drops = iBlockState.getBlock().getDrops(
                iBlockState,
                (ServerLevel) tileAnalyzer.getWorld(),
                pos, null
        );
        List<ItemStack> list = new ArrayList<>();

        if (tileAnalyzer.macerator || tileAnalyzer.comb_macerator) {
            if (tileAnalyzer.macerator) {
                for (ItemStack stack1 : drops) {
                    final BaseMachineRecipe rec = Recipes.recipes.getRecipeOutput(
                            "macerator",
                            false,
                            stack1
                    );


                    if (rec != null) {
                        ItemStack output = rec.output.items.get(0).copy();
                        output.setCount(stack1.getCount() * output.getCount());
                        list.add(output);
                    } else {
                        list.add(stack1);
                    }
                }
            } else {
                for (ItemStack stack1 : drops) {
                    final BaseMachineRecipe rec = Recipes.recipes.getRecipeOutput(
                            "comb_macerator",
                            false,
                            stack1
                    );
                    if (rec != null) {
                        ItemStack output = rec.output.items.get(0).copy();
                        output.setCount(stack1.getCount() * output.getCount());
                        list.add(output);
                    } else {
                        list.add(stack1);
                    }
                }
            }
        }
        if (!list.isEmpty()) {
            drops.clear();
            drops.addAll(list);
            list.clear();
        }
        if (tileAnalyzer.polisher) {

            final List<BaseMachineRecipe> recipes = Recipes.recipes.getRecipeList("laser");
            for (ItemStack stack : drops) {
                BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput("laser", false, stack);
                if (recipe != null) {
                    list.add(recipe.getOutput().items.get(0).copy());
                } else {
                    boolean can = true;
                    for (BaseMachineRecipe recipe1 : recipes) {
                        if (recipe1.getOutput().items.get(0).is(stack.getItem())) {
                            final ItemStack stack1 = stack.copy();
                            stack1.setCount(recipe1.getOutput().items.get(0).getCount());
                            list.add(stack1);
                            can = false;
                            break;
                        }
                    }
                    if (!can) {
                        list.add(stack);
                    }
                }
            }
        }
        if (tileAnalyzer.furnace) {
            final List<BaseMachineRecipe> recipes = Recipes.recipes.getRecipeList("furnace");
            BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput("furnace", false, stack);
            if (recipe != null) {
                list.add(recipe.getOutput().items.get(0).copy());
            } else {
                boolean can = true;
                for (BaseMachineRecipe recipe1 : recipes) {
                    if (recipe1.getOutput().items.get(0).is(stack.getItem())) {
                        final ItemStack stack1 = stack.copy();
                        stack1.setCount(recipe1.getOutput().items.get(0).getCount());
                        list.add(stack1);
                        can = false;
                        break;
                    }
                }
                if (!can) {
                    list.add(stack);
                }
            }
        }
        if (!list.isEmpty()) {
            drops.clear();
            drops.addAll(list);
            list.clear();
        }
        recipe_stack = drops;
        return recipe_stack;
    }

    public CompoundTag getTagCompound(HolderLookup.Provider provider) {
        CompoundTag tagCompound = new CompoundTag();
        tagCompound.putString("name", name);
        tagCompound.putInt("number", number);
        tagCompound.putInt("y", y);
        tagCompound.putInt("size", this.listPos.size());
        for (int i = 0; i < listPos.size(); i++) {
            CompoundTag posTag = new CompoundTag();
            BlockPos pos = this.listPos.get(i);
            posTag.putInt("x", pos.getX());
            posTag.putInt("y", pos.getY());
            posTag.putInt("z", pos.getZ());
            tagCompound.put("blockpos_" + i, posTag);
        }
        CompoundTag stackTag = new CompoundTag();
        stack.save(provider, stackTag);
        tagCompound.put("stackTag", stackTag);
        tagCompound.putInt("size1", this.veinsList.size());
        for (int i = 0; i < veinsList.size(); i++) {
            CompoundTag VeinTag = new CompoundTag();
            Vein vein = this.veinsList.get(i);
            VeinTag.putInt("x", vein.getChunk().x);
            VeinTag.putInt("z", vein.getChunk().z);
            tagCompound.put("vein_" + i, VeinTag);
        }
        return tagCompound;
    }

    public CustomPacketBuffer getCustomPacket(RegistryAccess registryAccess) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(registryAccess);
        customPacketBuffer.writeString(name);
        customPacketBuffer.writeInt(number);
        customPacketBuffer.writeInt(y);
        try {
            EncoderHandler.encode(customPacketBuffer, this.listPos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            EncoderHandler.encode(customPacketBuffer, stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            EncoderHandler.encode(customPacketBuffer, iBlockState.getBlock());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            EncoderHandler.encode(customPacketBuffer, this.veinsList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    public BlockState getiBlockState() {
        return iBlockState;
    }

    public ItemStack getStack() {
        return stack;
    }

    public List<BlockPos> getListPos() {
        return listPos;
    }

    public void addPos(BlockPos pos) {
        this.listPos.add(pos);
    }

    public void addVein(Vein vein) {
        this.veinsList.add(vein);
    }

    public void addY(int y) {
        this.y += y;
    }

    public List<Vein> getVeinsList() {
        return veinsList;
    }

    public void addNumber(int number) {
        this.number += number;
    }

    public int getAverage() {
        return this.y / this.number;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataOre dataOre = (DataOre) o;
        return Objects.equals(name, dataOre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getY() {
        return y;
    }

    public boolean contains(BlockPos pos1) {
        return this.listPos.contains(pos1);
    }

}
