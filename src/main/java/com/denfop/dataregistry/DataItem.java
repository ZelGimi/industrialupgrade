package com.denfop.dataregistry;

import com.denfop.Constants;
import com.denfop.blocks.SubEnum;
import com.denfop.mixin.access.DeferredRegisterAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

import static com.denfop.register.Register.ITEMS;

public class DataItem<T extends Enum<T> & SubEnum, E extends Item> {
    public static List<RegistryObject<Item>> objects = new LinkedList<>();
    List<RegistryObject<E>> registryObjectList;

    public DataItem(Class<T> typeClass, Class<E> itemClass) {
        this(typeClass, itemClass, Constants.MOD_ID);
    }

    public DataItem(Class<T> typeClass, Class<E> itemClass, String constants) {
        Map<T, RegistryObject<E>> map = new HashMap<>();
        T[] collections = typeClass.getEnumConstants();
        int indexMax = 0;
        for (T type : collections) {
            try {
                if (!Item.class.isAssignableFrom(itemClass)) {
                    throw new IllegalArgumentException("E must extend net.minecraft.world.item.Item");
                }
                Constructor<E> constructor = (Constructor<E>) itemClass.getConstructors()[0];
                Supplier<? extends E> supplier = () -> {
                    try {
                        return constructor.newInstance(type);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };

                final ResourceLocation key = new ResourceLocation(constants, type.getMainPath() + "/" + type.getSerializedName() + type.getOtherPart());
                if (indexMax < type.getId())
                    indexMax = type.getId();
                if (!type.register())
                    continue;
                RegistryObject<E> ret = RegistryObject.create(key, ITEMS.getRegistryKey(), constants);
                objects.add((RegistryObject<Item>) ret);
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
        for (Map.Entry<T, RegistryObject<E>> entry : map.entrySet()) {
            registryObjectList.set(entry.getKey().getId(), entry.getValue());
        }
    }

    public E getStack(T meta) {
        return registryObjectList.get(meta.getId()).get();
    }

    public E getStack(int meta) {
        return registryObjectList.get(meta % registryObjectList.size()).get();
    }

    public ItemStack getItemStack(int meta) {
        return new ItemStack(registryObjectList.get(meta % registryObjectList.size()).get());
    }

    public ItemStack getItemStack(int meta, int col) {
        return new ItemStack(registryObjectList.get(meta % registryObjectList.size()).get(), col);
    }

    public E getItemFromMeta(int meta) {
        return registryObjectList.get(meta % registryObjectList.size()).get();
    }

    public RegistryObject<E> getRegistryObject(int meta) {

        return registryObjectList.get(meta % registryObjectList.size());
    }

    public int getMeta(E item) {
        int i = 0;
        for (RegistryObject<E> registryObject : registryObjectList) {
            if (registryObject.get() == item) {
                return i;
            }
            i++;
        }
        return 0;
    }

    public int getMeta(ItemStack itemStack) {
        int i = 0;
        E item = (E) itemStack.getItem();
        for (RegistryObject<E> registryObject : registryObjectList) {
            if (registryObject.get().equals(item)) {
                return i;
            }
            i++;
        }
        return 0;
    }

    public E getItem(T meta) {
        return registryObjectList.get(meta.getId()).get();
    }

}
