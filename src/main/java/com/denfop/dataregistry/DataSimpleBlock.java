package com.denfop.dataregistry;

import com.denfop.IUCore;
import com.denfop.mixin.access.DeferredRegisterAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import static com.denfop.dataregistry.DataBlock.objects;
import static com.denfop.register.Register.BLOCKS;
import static com.denfop.register.Register.ITEMS;

public class DataSimpleBlock<E extends Block, F extends BlockItem> {
    private final DeferredHolder<Block, E> block;
    private DeferredHolder<Item, F> itemBlock;


    public DataSimpleBlock(Class<E> blockClass, Class<F> itemClass, String mainPath, String name) {


        try {
            Constructor<E> constructor = (Constructor<E>) blockClass.getConstructors()[0];
            Supplier<? extends E> supplier = () -> {
                try {
                    return constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };

            final ResourceLocation key = ResourceLocation.tryBuild(IUCore.MODID, mainPath + "/" + name.toLowerCase());

            DeferredHolder<Block, E> ret = DeferredHolder.create(BLOCKS.getRegistryKey(), key);

            var entries = ((DeferredRegisterAccessor) BLOCKS).getEntries();
            if (entries.putIfAbsent(ret, supplier) != null) {
                throw new IllegalArgumentException("Duplicate registration " + key.toString());
            }
            this.block = ret;
            registerBlockItem(ret, itemClass, mainPath, name);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void registerBlockItem(DeferredHolder<Block, E> block, Class<F> itemClass, String mainPath, String name) {
        int indexMax = 0;
        try {
            Constructor<F> constructor = (Constructor<F>) itemClass.getConstructors()[0];
            Supplier<? extends F> supplier = () -> {
                try {
                    return constructor.newInstance(block.get());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };

            final ResourceLocation key = ResourceLocation.tryBuild(IUCore.MODID, mainPath + "/" + name.toLowerCase());
            DeferredHolder<Item, F> ret = DeferredHolder.create(ITEMS.getRegistryKey(), key);
            var entries = ((DeferredRegisterAccessor) ITEMS).getEntries();
            objects.add(ret);
            if (entries.putIfAbsent(ret, supplier) != null) {
                throw new IllegalArgumentException("Duplicate registration " + name);
            }
            itemBlock = ret;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public DeferredHolder<Block, E> getBlock() {
        return block;
    }

    public BlockState getBlockState(int meta) {
        return block.get().defaultBlockState();
    }

    public BlockState getStateFromMeta(int meta) {
        return block.get().defaultBlockState();
    }

    public BlockState getDefaultState() {
        return block.get().defaultBlockState();
    }


    public F getItem() {
        return itemBlock.get();
    }

    public ItemStack getItemStack() {
        return new ItemStack(itemBlock.get());
    }


}
