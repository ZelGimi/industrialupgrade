package com.denfop;

import com.denfop.mixin.access.DeferredRegisterAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.denfop.DataItem.objects;
import static com.denfop.register.Register.ITEMS;

public class DataSimpleItem<E extends Item, T extends ResourceLocation> {

    RegistryObject<E> registryObject;

    public DataSimpleItem(T typeClass, Supplier<E> supplier){
        this(typeClass,supplier,Constants.MOD_ID);
    }
    public DataSimpleItem(T resource, Supplier<E> supplier, String constants) {
        String namespace = resource.getNamespace().isEmpty() ? "" : resource.getNamespace() + "/";
        final ResourceLocation key = new ResourceLocation(constants, namespace + resource.getPath());
        RegistryObject<E> ret = RegistryObject.create(key, ITEMS.getRegistryKey(), constants);
        var entries = ((DeferredRegisterAccessor) ITEMS).getEntries();
        objects.add((RegistryObject<Item>) ret);
        if (entries.putIfAbsent(ret, supplier) != null) {
            throw new IllegalArgumentException("Duplicate registration " + resource);
        }

        registryObject = ret;

    }

    public RegistryObject<E> getRegistryObject() {
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
