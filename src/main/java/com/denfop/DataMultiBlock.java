package com.denfop;

import com.denfop.blocks.ISubEnum;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.mixin.access.DeferredRegisterAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

import static com.denfop.register.Register.BLOCKS;
import static com.denfop.register.Register.ITEMS;

public class DataMultiBlock<T extends Enum<T> & ISubEnum, E extends Block, F extends ItemBlockCore> {
    private final RegistryObject<E> block;
    List<RegistryObject<F>> registryObjectList;

    public DataMultiBlock(Class<T> typeClass, Class<E> blockClass, Class<F> itemClass) {
        T[] collections = typeClass.getEnumConstants();

        try {
            Constructor<E> constructor = (Constructor<E>) blockClass.getConstructors()[0];
            Supplier<? extends E> supplier = () -> {
                try {
                    return constructor.newInstance(collections, this);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };

            final ResourceLocation key = new ResourceLocation(IUCore.MODID, collections[0].getMainPath());

            RegistryObject<E> ret = RegistryObject.create(key, BLOCKS.getRegistryKey(), IUCore.MODID);

            var entries = ((DeferredRegisterAccessor) BLOCKS).getEntries();
            if (entries.putIfAbsent(ret, supplier) != null) {
                throw new IllegalArgumentException("Duplicate registration " + collections[0].getMainPath());
            }
            this.block = ret;
            if (!collections[0].registerOnlyBlock())
                registerBlockItem(collections, ret, itemClass);
            else
                registerBlockItem(collections[0], ret, itemClass);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void registerBlockItem(T[] collections, RegistryObject<E> block, Class<F> itemClass) {
        int indexMax = 0;
        Map<T, RegistryObject<F>> map = new HashMap<>();
        for (T type : collections) {
            try {
                Constructor<F> constructor = (Constructor<F>) itemClass.getConstructors()[0];
                Supplier<? extends F> supplier = () -> {
                    try {
                        return constructor.newInstance(block.get(), type);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };

                final ResourceLocation key = new ResourceLocation(IUCore.MODID, type.getMainPath() + "/" + type.getSerializedName());
                if (indexMax < type.getId())
                    indexMax = type.getId();
                if (!type.register())
                    continue;
                RegistryObject<F> ret = RegistryObject.create(key, ITEMS.getRegistryKey(), IUCore.MODID);
                var entries = ((DeferredRegisterAccessor) ITEMS).getEntries();
                if (entries.putIfAbsent(ret, supplier) != null) {
                    throw new IllegalArgumentException("Duplicate registration " + type);
                }
                map.put(type, ret);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        registryObjectList = new ArrayList<>(Collections.nCopies(indexMax + 1, null));
        for (Map.Entry<T, RegistryObject<F>> entry : map.entrySet()) {
            registryObjectList.set(entry.getKey().getId(), entry.getValue());
        }
    }

    public RegistryObject<E> getBlock() {
        return block;
    }

    private void registerBlockItem(T type, RegistryObject<E> block, Class<F> itemClass) {
        int indexMax = 0;
        Map<T, RegistryObject<F>> map = new HashMap<>();

        try {
            Constructor<F> constructor = (Constructor<F>) itemClass.getConstructors()[0];
            Supplier<? extends F> supplier = () -> {
                try {
                    return constructor.newInstance(block.get(), type);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };

            final ResourceLocation key = new ResourceLocation(IUCore.MODID, type.getMainPath() + "/" + type.getSerializedName());
            if (indexMax < type.getId())
                indexMax = type.getId();

            RegistryObject<F> ret = RegistryObject.create(key, ITEMS.getRegistryKey(), IUCore.MODID);
            var entries = ((DeferredRegisterAccessor) ITEMS).getEntries();
            if (entries.putIfAbsent(ret, supplier) != null) {
                throw new IllegalArgumentException("Duplicate registration " + type);
            }
            map.put(type, ret);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        registryObjectList = new ArrayList<>(Collections.nCopies(indexMax + 1, null));
        for (Map.Entry<T, RegistryObject<F>> entry : map.entrySet()) {
            registryObjectList.set(entry.getKey().getId(), entry.getValue());
        }
    }

    public F getItem(int meta) {
        return registryObjectList.get(meta%registryObjectList.size()).get();
    }

    public ItemStack getItemStack(int meta) {
        return new ItemStack(registryObjectList.get(meta).get());
    }

    public int getMeta(F item) {
        int i = 0;
        for (RegistryObject<F> registryObject : registryObjectList) {
            if (registryObject.get() == item) {
                return i;
            }
            i++;
        }
        return 0;
    }
}
