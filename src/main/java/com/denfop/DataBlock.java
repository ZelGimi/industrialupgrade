package com.denfop;

import com.denfop.blocks.ISubEnum;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.mixin.access.DeferredRegisterAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static com.denfop.register.Register.BLOCKS;
import static com.denfop.register.Register.ITEMS;

public class DataBlock<T extends Enum<T> & ISubEnum, E extends Block, F extends ItemBlockCore> {
    public static List<DeferredHolder<Item, ?>> objects = new ArrayList<>();
    public static List<DeferredHolder<Block, ?>> objectsBlock = new ArrayList<>();
    private final Map<T, DeferredHolder<Block, E>> block = new ConcurrentHashMap<>();
    private final Map<Integer, T> elementsMeta = new ConcurrentHashMap<>();
    private final T[] collections;
    Map<T, DeferredHolder<Item, F>> registryObjectList = new ConcurrentHashMap<>();

    public DataBlock(Class<T> typeClass, Class<E> blockClass, Class<F> itemClass) {
        T[] collections = typeClass.getEnumConstants();
        T element = collections[0];
        this.collections = collections;
        boolean needRegistryElements = element.register();
        for (T type : collections) {
            if (type.register()) {
                elementsMeta.put(type.getId(), type);
                try {
                    Constructor<E> constructor = (Constructor<E>) blockClass.getConstructors()[0];
                    Supplier<? extends E> supplier = () -> {
                        try {
                            return constructor.newInstance(collections, type, this);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    };

                    final ResourceLocation key = ResourceLocation.tryBuild(IUCore.MODID, type.getMainPath() + "/" + type.getSerializedName().toLowerCase());
                    DeferredHolder<Block, E> ret = DeferredHolder.create(BLOCKS.getRegistryKey(), key);
                    objectsBlock.add(ret);
                    var entries = ((DeferredRegisterAccessor) BLOCKS).getEntries();
                    if (entries.putIfAbsent(ret, supplier) != null) {
                        throw new IllegalArgumentException("Duplicate registration " + type.getMainPath());
                    }
                    this.block.put(type, ret);
                    if (!type.registerOnlyBlock())
                        registerBlockItem(type, ret, itemClass);
                    else
                        registerBlockItem(type, ret, itemClass);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void registerBlockItem(T type, DeferredHolder<Block, E> block, Class<F> itemClass) {
        int indexMax = 0;
        if (!type.register())
            return;
        try {
            Constructor<F> constructor = (Constructor<F>) itemClass.getConstructors()[0];
            Supplier<? extends F> supplier = () -> {
                try {
                    return constructor.newInstance(block.get(), type);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };

            final ResourceLocation key = ResourceLocation.tryBuild(IUCore.MODID, type.getMainPath() + "/" + type.getSerializedName().toLowerCase());
            if (indexMax < type.getId())
                indexMax = type.getId();

            DeferredHolder<Item, F> ret = DeferredHolder.create(ITEMS.getRegistryKey(), key);
            objects.add(ret);
            var entries = ((DeferredRegisterAccessor) ITEMS).getEntries();
            if (entries.putIfAbsent(ret, supplier) != null) {
                throw new IllegalArgumentException("Duplicate registration " + type);
            }
            registryObjectList.put(type, ret);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public T getElementFromID(int meta) {
        return elementsMeta.getOrDefault(meta, collections[0]);
    }

    public DeferredHolder<Block, E> getBlock(T element) {
        return block.get(element);
    }

    public E getBlock(int meta) {
        return block.get(getElementFromID(meta)).get();
    }

    public BlockState getBlockState(int meta) {

        return block.get(getElementFromID(meta)).get().defaultBlockState();
    }

    public BlockState getStateFromMeta(int meta) {
        return block.get(getElementFromID(meta)).get().defaultBlockState();
    }

    public BlockState getState(T element) {
        return block.get(element).get().defaultBlockState();
    }

    public BlockState getDefaultState() {
        return block.get(collections[0]).get().defaultBlockState();
    }


    public F getItem(T element) {
        return registryObjectList.get(element).get();
    }

    public F getItem() {
        return registryObjectList.get(getElementFromID(0)).get();
    }

    public F getItemStack(int meta) {
        return registryObjectList.get(getElementFromID(meta)).get();
    }

    public ItemStack getStack(int meta) {
        return new ItemStack(registryObjectList.get(getElementFromID(meta)).get());
    }

    public F getItem(int meta) {
        return registryObjectList.get(getElementFromID(meta)).get();
    }

    public ItemStack getItemStack(T element) {
        return new ItemStack(registryObjectList.get(element).get());
    }

    public int getMeta(F item) {
        int i = 0;
        for (DeferredHolder<Item, F> registryObject : registryObjectList.values()) {
            if (registryObject.get() == item) {
                return ((ISubEnum) item.getElement()).getId();
            }
            i++;
        }
        return 0;
    }
}
