package com.denfop;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.mixin.access.DeferredRegisterAccessor;
import com.denfop.register.Register;
import com.denfop.tiles.base.TileEntityBlock;
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
import java.util.function.Supplier;

import static com.denfop.DataBlock.objectsBlock;
import static com.denfop.register.Register.*;

public class DataBlockEntity<T extends Enum<T> & IMultiTileBlock> {
    public static TileBlockCreator instance;
    private final Map<T, RegistryObject<BlockTileEntity<T>>> block = new ConcurrentHashMap<>();
    private final Map<Integer, T> elementsMeta = new ConcurrentHashMap<>();
    private final T[] collections;
    public int index = 0;
    Map<T, RegistryObject<ItemBlockTileEntity<T>>> registryObjectList = new ConcurrentHashMap<>();


    public DataBlockEntity(Class<T> typeClass){
        this(typeClass,Constants.MOD_ID, BLOCKS, BLOCK_ENTITIES,ITEMS);
    }
    public DataBlockEntity(Class<T> typeClass, String location, DeferredRegister<Block> BLOCKS,DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES,DeferredRegister<Item> ITEMS) {
        T[] collections = typeClass.getEnumConstants();
        this.collections = collections;
        for (T type : collections) {
            elementsMeta.put(type.getId(), type);
            try {
                final ResourceLocation key = new ResourceLocation(location, type.getMainPath() + "/" + type.getSerializedName().toLowerCase());
                Supplier<BlockTileEntity<T>> supplier = () -> TileBlockCreator.instance.create(type, key);
                RegistryObject<BlockTileEntity<T>> ret = RegistryObject.create(key, BLOCKS.getRegistryKey(),location);
                objectsBlock.add(ret);
                var entries = ((DeferredRegisterAccessor) BLOCKS).getEntries();
                if (entries.putIfAbsent(ret, supplier) != null) {
                    throw new IllegalArgumentException("Duplicate registration " + type.getMainPath());
                }
                Supplier<BlockEntityType<? extends TileEntityBlock>> supplierType = () -> create(Objects.requireNonNull(type.getTeClass()), ret);
                RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockEntityType = RegistryObject.create(key, BLOCK_ENTITIES.getRegistryKey(), location);
                type.setType(blockEntityType);
                var entries1 = ((DeferredRegisterAccessor) BLOCK_ENTITIES).getEntries();
                if (entries1.putIfAbsent(blockEntityType, supplierType) != null) {
                    throw new IllegalArgumentException("Duplicate registration " + type.getMainPath());
                }
                this.block.put(type, ret);
                registerBlockItem(type, ret,location,ITEMS);

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public BlockEntityType<? extends TileEntityBlock> create(
            Class<? extends TileEntityBlock> typeClass,
            RegistryObject<BlockTileEntity<T>>... block
    ) {
        Constructor<TileEntityBlock> constructor = (Constructor<TileEntityBlock>) typeClass.getConstructors()[0];

        return BlockEntityType.Builder.of(
                (pos, state) -> {
                    try {
                        return constructor.newInstance(pos, state);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                },
                Arrays.stream(block).map(RegistryObject::get).toArray(Block[]::new)
        ).build(null);

    }
    public static List<RegistryObject<?>> objects = new LinkedList<>();

    private void registerBlockItem(T type, RegistryObject<BlockTileEntity<T>> block, String location,DeferredRegister<Item> ITEMS) {
        int indexMax = 0;
        if (!type.register())
            return;
        try {
            final ResourceLocation key = new ResourceLocation(location, type.getMainPath() + "/" + type.getSerializedName().toLowerCase());

            Supplier<? extends ItemBlockTileEntity<T>> supplier = () -> new ItemBlockTileEntity<>(block.get(), type, key);
            if (indexMax < type.getId())
                indexMax = type.getId();

            RegistryObject<ItemBlockTileEntity<T>> ret = RegistryObject.create(key, ITEMS.getRegistryKey(), location);
            objects.add(ret);
            var entries = ((DeferredRegisterAccessor) ITEMS).getEntries();
            if (entries.putIfAbsent(ret, supplier) != null) {
                throw new IllegalArgumentException("Duplicate registration " + type);
            }
            registryObjectList.put(type, ret);
        } catch (Exception e) {
            e.printStackTrace();
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
        return new ItemStack(registryObjectList.get(element).get(),col);
    }
    public ItemStack getItemStack(int meta) {
        return new ItemStack(registryObjectList.get(getElementFromID(meta)).get());
    }
    public ItemStack getItemStack(int meta, int col) {
        return new ItemStack(registryObjectList.get(getElementFromID(meta)).get(),col);
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
            if (item1.get() == item)
                return true;
        }
        return false;
    }
    public ItemBlockTileEntity<T> getItem(ItemStack itemStack) {
        Item item = itemStack.getItem();
        for (RegistryObject<ItemBlockTileEntity<T>> item1 : this.registryObjectList.values()) {
            if (item1.get() == item)
                return item1.get();
        }
        return null;
    }
    public BlockTileEntity<T> getBlock(IMultiTileBlock teBlock) {
        return block.get(getElementFromID(teBlock.getId())).get();
    }
}
