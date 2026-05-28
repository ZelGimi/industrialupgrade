package com.denfop.dataregistry;

import com.denfop.Constants;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.denfop.dataregistry.DataBlock.objectsBlock;
import static com.denfop.register.Register.*;

public class DataBlockEntity<T extends Enum<T> & MultiBlockEntity> {

    public static final List<RegistryObject<?>> objects = Collections.synchronizedList(new LinkedList<>());
    public static TileBlockCreator instance;
    private final Map<T, RegistryObject<BlockTileEntity<T>>> block = new ConcurrentHashMap<>();
    private final Map<Integer, T> elementsMeta = new ConcurrentHashMap<>();
    private final T[] collections;
    private final Map<T, RegistryObject<ItemBlockTileEntity<T>>> registryObjectList = new ConcurrentHashMap<>();
    public int index = 0;

    public DataBlockEntity(Class<T> typeClass) {
        this(typeClass, Constants.MOD_ID, BLOCKS, BLOCK_ENTITIES, ITEMS);
    }

    public DataBlockEntity(
            Class<T> typeClass,
            String location,
            DeferredRegister<Block> BLOCKS,
            DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES,
            DeferredRegister<Item> ITEMS
    ) {
        this.collections = typeClass.getEnumConstants();

        for (T type : this.collections) {
            elementsMeta.put(type.getId(), type);

            final String path = type.getMainPath() + "/" + type.getSerializedName().toLowerCase(Locale.ROOT);
            final ResourceLocation key = new ResourceLocation(location, path);

            final RegistryObject<BlockTileEntity<T>> blockObject = BLOCKS.register(
                    path,
                    () -> TileBlockCreator.instance.create(type, key)
            );

            objectsBlock.add(blockObject);
            this.block.put(type, blockObject);

            final RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockEntityType = BLOCK_ENTITIES.register(
                    path,
                    () -> create(
                            Objects.requireNonNull(type.getTeClass(), "BlockEntity class is null for " + type),
                            blockObject
                    )
            );

            type.setType(blockEntityType);

            registerBlockItem(type, blockObject, path, key, ITEMS);
        }
    }

    public BlockEntityType<? extends BlockEntityBase> create(
            Class<? extends BlockEntityBase> typeClass,
            RegistryObject<BlockTileEntity<T>>... blocks
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
                Arrays.stream(blocks).map(RegistryObject::get).toArray(Block[]::new)
        ).build(null);
    }

    private void registerBlockItem(
            T type,
            RegistryObject<BlockTileEntity<T>> block,
            String path,
            ResourceLocation key,
            DeferredRegister<Item> ITEMS
    ) {
        if (!type.register()) {
            return;
        }

        final RegistryObject<ItemBlockTileEntity<T>> itemObject = ITEMS.register(
                path,
                () -> new ItemBlockTileEntity<>(block.get(), type, key)
        );

        objects.add(itemObject);
        registryObjectList.put(type, itemObject);
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

    public RegistryObject<BlockTileEntity<T>> getObject(int meta) {
        return block.get(getElementFromID(meta));
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
        for (RegistryObject<ItemBlockTileEntity<T>> item1 : this.registryObjectList.values()) {
            if (item1.get() == itemStack.getItem()) {
                return item1.get().getElement().getId();
            }
        }
        return 0;
    }

    public boolean contains(ItemStack itemStack) {
        Item item = itemStack.getItem();
        for (RegistryObject<ItemBlockTileEntity<T>> item1 : this.registryObjectList.values()) {
            if (item1.get() == item) {
                return true;
            }
        }
        return false;
    }

    public ItemBlockTileEntity<T> getItem(ItemStack itemStack) {
        Item item = itemStack.getItem();
        for (RegistryObject<ItemBlockTileEntity<T>> item1 : this.registryObjectList.values()) {
            if (item1.get() == item) {
                return item1.get();
            }
        }
        return null;
    }

    public BlockTileEntity<T> getBlock(MultiBlockEntity teBlock) {
        return block.get(getElementFromID(teBlock.getId())).get();
    }
}