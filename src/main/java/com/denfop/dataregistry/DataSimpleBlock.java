package com.denfop.dataregistry;

import com.denfop.IUCore;
import com.denfop.mixin.access.DeferredRegisterAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import static com.denfop.register.Register.BLOCKS;
import static com.denfop.register.Register.ITEMS;

public class DataSimpleBlock<E extends Block, F extends BlockItem> {
    private final RegistryObject<E> block;
    private RegistryObject<F> itemBlock;


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

            final ResourceLocation key = new ResourceLocation(IUCore.MODID, mainPath + "/" + name.toLowerCase());

            RegistryObject<E> ret = RegistryObject.create(key, BLOCKS.getRegistryKey(), IUCore.MODID);

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

    private void registerBlockItem(RegistryObject<E> block, Class<F> itemClass, String mainPath, String name) {
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

            final ResourceLocation key = new ResourceLocation(IUCore.MODID, mainPath + "/" + name.toLowerCase());
            RegistryObject<F> ret = RegistryObject.create(key, ITEMS.getRegistryKey(), IUCore.MODID);
            var entries = ((DeferredRegisterAccessor) ITEMS).getEntries();
            if (entries.putIfAbsent(ret, supplier) != null) {
                throw new IllegalArgumentException("Duplicate registration " + name);
            }
            itemBlock = ret;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public RegistryObject<E> getBlock() {
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

/*    private void registerBlockItem(T type, RegistryObject<E> block, Class<F> itemClass) {
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
            map.set(type, ret);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        registryObjectList = new ArrayList<>(Collections.nCopies(indexMax + 1, null));
        for (Map.Entry<T, RegistryObject<F>> entry : map.entrySet()) {
            registryObjectList.set(entry.getKey().getId(), entry.getValue());
        }
    }*/

    public F getItem() {
        return itemBlock.get();
    }

    public ItemStack getItemStack() {
        return new ItemStack(itemBlock.get());
    }


}
