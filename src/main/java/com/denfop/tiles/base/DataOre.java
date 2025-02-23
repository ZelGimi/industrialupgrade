package com.denfop.tiles.base;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

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
    private IBlockState iBlockState;

    private int number;
    private int y;

    public DataOre(String name, int number, int y, BlockPos pos, ItemStack stack, IBlockState state) {
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

        if (state == null) {
            this.iBlockState = Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getItemDamage());
        } else {
            this.iBlockState = state;
        }
    }

    public DataOre(NBTTagCompound tagCompound) {
        name = tagCompound.getString("name");
        number = tagCompound.getInteger("number");
        y = tagCompound.getInteger("y");
        int size = tagCompound.getInteger("size");
        this.listPos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            NBTTagCompound posTag = tagCompound.getCompoundTag("blockpos_" + i);
            this.listPos.add(new BlockPos(posTag.getInteger("x"), posTag.getInteger("y"), posTag.getInteger("z")));
        }
        NBTTagCompound stackTag = tagCompound.getCompoundTag("stackTag");
        stack = new ItemStack(stackTag);
        this.iBlockState = Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getItemDamage());
        this.veinsList = new ArrayList<>();
        int size1 = tagCompound.getInteger("size1");
        for (int i = 0; i < size1; i++) {
            NBTTagCompound veinTag = tagCompound.getCompoundTag("vein_" + i);
            ChunkPos chunkPos = new ChunkPos(veinTag.getInteger("x"), veinTag.getInteger("z"));
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
            this.iBlockState = ((Block) DecoderHandler.decode(customPacketBuffer)).getStateFromMeta(stack.getItemDamage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.veinsList = (List<Vein>) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ItemStack> getRecipe_stack(TileAnalyzer tileAnalyzer, BlockPos pos) {
        if (recipe_stack != null) {
            return recipe_stack;
        }
        final List<ItemStack> drops = iBlockState.getBlock().getDrops(
                tileAnalyzer.getWorld(),
                pos,
                iBlockState,
                tileAnalyzer.lucky
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
                        if (recipe1.getOutput().items.get(0).isItemEqual(stack)) {
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
            for (ItemStack stack1 : drops) {
                final ItemStack smelt = FurnaceRecipes.instance().getSmeltingResult(stack1).copy();
                if (!smelt.isEmpty()) {
                    smelt.setCount(stack1.getCount() * smelt.getCount());
                    list.add(smelt);
                } else {
                    list.add(stack1);
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

    public NBTTagCompound getTagCompound() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("name", name);
        tagCompound.setInteger("number", number);
        tagCompound.setInteger("y", y);
        tagCompound.setInteger("size", this.listPos.size());
        for (int i = 0; i < listPos.size(); i++) {
            NBTTagCompound posTag = new NBTTagCompound();
            BlockPos pos = this.listPos.get(i);
            posTag.setInteger("x", pos.getX());
            posTag.setInteger("y", pos.getY());
            posTag.setInteger("z", pos.getZ());
            tagCompound.setTag("blockpos_" + i, posTag);
        }
        NBTTagCompound stackTag = new NBTTagCompound();
        stack.writeToNBT(stackTag);
        tagCompound.setTag("stackTag", stackTag);
        tagCompound.setInteger("size1", this.veinsList.size());
        for (int i = 0; i < veinsList.size(); i++) {
            NBTTagCompound VeinTag = new NBTTagCompound();
            Vein vein = this.veinsList.get(i);
            VeinTag.setInteger("x", vein.getChunk().x);
            VeinTag.setInteger("z", vein.getChunk().z);
            tagCompound.setTag("vein_" + i, VeinTag);
        }
        return tagCompound;
    }

    public CustomPacketBuffer getCustomPacket() {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
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

    public IBlockState getiBlockState() {
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
