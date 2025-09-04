package com.denfop.dataregistry;

import com.denfop.Constants;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.mixin.access.DeferredRegisterAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static com.denfop.dataregistry.DataBlock.objectsBlock;
import static com.denfop.register.Register.*;

public class DataBlockEntity<T extends Enum<T> & MultiBlockEntity> {
    public static TileBlockCreator instance;
    public static List<DeferredHolder<Block, ? extends BlockTileEntity>> objectsBlock1 = new ArrayList<>();
    public static List<DeferredHolder<Item, ?>> objects = new ArrayList<>();
    private final Map<T, DeferredHolder<Block, BlockTileEntity<T>>> block = new ConcurrentHashMap<>();
    private final Map<Integer, T> elementsMeta = new ConcurrentHashMap<>();
    private final T[] collections;
    public int index = 0;
    Map<T, DeferredHolder<Item, ItemBlockTileEntity<T>>> registryObjectList = new ConcurrentHashMap<>();

    public DataBlockEntity(Class<T> typeClass) {
        this(typeClass, Constants.MOD_ID, BLOCKS, BLOCK_ENTITIES, ITEMS);
    }

    public DataBlockEntity(Class<T> typeClass, String location, DeferredRegister<Block> BLOCKS, DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES, DeferredRegister<Item> ITEMS) {
        T[] collections = typeClass.getEnumConstants();
        this.collections = collections;
        for (T type : collections) {
            elementsMeta.put(type.getId(), type);
            try {
                final ResourceLocation key = ResourceLocation.tryBuild(location, type.getMainPath() + "/" + type.getSerializedName().toLowerCase());
                Supplier<BlockTileEntity<T>> supplier = () -> TileBlockCreator.instance.create(type, key);
                DeferredHolder<Block, BlockTileEntity<T>> ret = DeferredHolder.create(BLOCKS.getRegistryKey(), key);
                objectsBlock.add(ret);
                objectsBlock1.add(ret);
                var entries = ((DeferredRegisterAccessor) BLOCKS).getEntries();
                if (entries.putIfAbsent(ret, supplier) != null) {
                    throw new IllegalArgumentException("Duplicate registration " + type.getMainPath());
                }
                Supplier<BlockEntityType<? extends BlockEntityBase>> supplierType = () -> create(type.getTeClass(), ret);
                DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockEntityType = DeferredHolder.create(BLOCK_ENTITIES.getRegistryKey(), key);
                type.setType(blockEntityType);
                var entries1 = ((DeferredRegisterAccessor) BLOCK_ENTITIES).getEntries();
                if (entries1.putIfAbsent(blockEntityType, supplierType) != null) {
                    throw new IllegalArgumentException("Duplicate registration " + type.getMainPath());
                }
                this.block.put(type, ret);
                registerBlockItem(type, ret, location, ITEMS);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public BlockEntityType<? extends BlockEntityBase> create(
            Class<? extends BlockEntityBase> typeClass,
            DeferredHolder<Block, BlockTileEntity<T>>... block
    ) {
        Constructor<BlockEntityBase> constructor = (Constructor<BlockEntityBase>) typeClass.getConstructors()[0];

        return BlockEntityType.Builder.of(
                (pos, state) -> {
                    try {
                        return constructor.newInstance(pos, state);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                },
                Arrays.stream(block).map(DeferredHolder::get).toArray(Block[]::new)
        ).build(null);

    }

    private void registerBlockItem(T type, DeferredHolder<Block, BlockTileEntity<T>> block, String location, DeferredRegister<Item> ITEMS) {
        int indexMax = 0;
        if (!type.register())
            return;
        try {
            final ResourceLocation key = ResourceLocation.tryBuild(location, type.getMainPath() + "/" + type.getSerializedName().toLowerCase());

            Supplier<? extends ItemBlockTileEntity<T>> supplier = () -> new ItemBlockTileEntity<>(block.get(), type, key);
            if (indexMax < type.getId())
                indexMax = type.getId();

            DeferredHolder<Item, ItemBlockTileEntity<T>> ret = DeferredHolder.create(ITEMS.getRegistryKey(), key);
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
        return elementsMeta.get(meta);
    }

    public BlockTileEntity<T> getBlock(T element) {
        return block.get(element).get();
    }

    public BlockTileEntity<T> getBlock(int meta) {
        return block.get(getElementFromID(meta)).get();
    }

    public BlockTileEntity<T> getBlock() {
        return block.get(getElementFromID(0)).get();
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


    public ItemBlockTileEntity<T> getItem(T element) {
        return registryObjectList.get(element).get();
    }

    public ItemBlockTileEntity<T> getItem(int meta) {
        return registryObjectList.get(getElementFromID(meta)).get();
    }

    public ItemBlockTileEntity<T> getItem() {
        return registryObjectList.get(getElementFromID(0)).get();
    }

    public ItemStack getItemStack(T element) {
        return new ItemStack(registryObjectList.get(element).get());
    }

    public ItemStack getItemStack(T element, int col) {
        return new ItemStack(registryObjectList.get(element).get(), col);
    }

    public ItemStack getItemStack(int meta) {
        return new ItemStack(registryObjectList.get(getElementFromID(meta)).get());
    }

    public ItemStack getItemStack(int meta, int col) {
        return new ItemStack(registryObjectList.get(getElementFromID(meta)).get(), col);
    }

    public int getMetaFromItemStack(ItemStack itemStack) {
        for (DeferredHolder<Item, ItemBlockTileEntity<T>> item1 : this.registryObjectList.values()) {
            if (item1.get() == itemStack.getItem()) {
                return item1.get().getElement().getId();
            }
        }
        return 0;
    }

    public boolean contains(ItemStack itemStack) {
        Item item = itemStack.getItem();
        for (DeferredHolder<Item, ItemBlockTileEntity<T>> item1 : this.registryObjectList.values()) {
            if (item1.get() == item)
                return true;
        }
        return false;
    }

    public ItemBlockTileEntity<T> getItem(ItemStack itemStack) {
        Item item = itemStack.getItem();
        for (DeferredHolder<Item, ItemBlockTileEntity<T>> item1 : this.registryObjectList.values()) {
            if (item1.get() == item)
                return item1.get();
        }
        return null;
    }

    public BlockTileEntity<T> getBlock(MultiBlockEntity teBlock) {
        return block.get(getElementFromID(teBlock.getId())).get();
    }

    public DeferredHolder<Block, BlockTileEntity<T>> getObject(int i) {
        return block.get(getElementFromID(i));
    }
}
