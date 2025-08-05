package com.denfop;

import com.denfop.mixin.access.DeferredRegisterAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.denfop.DataItem.objects;
import static com.denfop.register.Register.ITEMS;

public class DataSimpleItem<E extends Item, T extends ResourceLocation> {

    DeferredHolder<Item, E> registryObject;

    public DataSimpleItem(T resource, Supplier<E> supplier) {
        this(resource, supplier, Constants.MOD_ID, ITEMS);
    }

    public DataSimpleItem(T resource, Supplier<E> supplier, String id, DeferredRegister<Item> ITEMS) {
        String namespace = resource.getNamespace().isEmpty() ? "" : resource.getNamespace() + "/";
        final ResourceLocation key = ResourceLocation.tryBuild(id, namespace + resource.getPath());
        DeferredHolder<Item, E> ret = DeferredHolder.create(ITEMS.getRegistryKey(), key);

        objects.add((DeferredHolder<Item, Item>) ret);
        var entries = ((DeferredRegisterAccessor) ITEMS).getEntries();
        if (entries.putIfAbsent(ret, supplier) != null) {
            throw new IllegalArgumentException("Duplicate registration " + resource);
        }

        registryObject = ret;

    }

    public DeferredHolder<Item, E> getRegistryObject() {
        return registryObject;
    }

    public ItemStack getItemStack() {
        return new ItemStack(registryObject.get());
    }

    public ItemStack getItem(int count) {
        return new ItemStack(registryObject.get(), count);
    }

    public E getItem() {
        return registryObject.get();
    }
}
