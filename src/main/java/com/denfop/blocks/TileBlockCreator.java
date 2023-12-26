package com.denfop.blocks;

import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class TileBlockCreator {


    public static TileBlockCreator instance;
    private final Map<ResourceLocation, InfoAboutTile<?>> dataInfo = new HashMap<>();

    public TileBlockCreator() {
        instance = this;
    }


    public <E extends Enum<E> & IMultiTileBlock> BlockTileEntity create(Class<E> enumClass) {
        InfoAboutTile<E> instance = new InfoAboutTile<>(enumClass);
        final BlockTileEntity block = BlockTileEntity.create(
                "industrialupgrade_" + new ArrayList<>(instance.teBlocks).get(0).getIdentifier().getResourcePath(),
                new ArrayList<>(instance.teBlocks).get(0).getIdentifier(),
                instance
        );
        instance.setBlock(block);
        dataInfo.put(new ArrayList<>(instance.teBlocks).get(0).getIdentifier(), instance);
        return block;
    }


    public void buildBlocks() {


        for(InfoAboutTile<?> tile : dataInfo.values()){
            for(IMultiTileBlock multiTileBlock:  tile.teBlocks)
                multiTileBlock.buildDummies();
        }
    }


    public BlockTileEntity get(ResourceLocation identifier) {
        return dataInfo.get(identifier).getBlock();
    }


    public static class InfoAboutTile<E extends Enum<E> & IMultiTileBlock> {

        private final boolean specialModels;
        private final Set<E> teBlocks;
        private final List<IMultiTileBlock> idMap;
        private final CreativeTabs tab;
        private final Material defaultMaterial;
        private final List<? extends IMultiTileBlock> listBlock;
        private BlockTileEntity block;

        InfoAboutTile(Class<E> universe) {
            this.idMap = new ArrayList<>();
            this.teBlocks = new HashSet<>();

            this.specialModels = IMultiBlockItem.class.isAssignableFrom(universe);
            for (final E e : EnumSet.allOf(universe)) {
                this.register(e);
            }
            this.defaultMaterial = new ArrayList<>(this.teBlocks).get(0).getMaterial();
            this.tab = new ArrayList<>(this.teBlocks).get(0).getCreativeTab();
            this.listBlock = new ArrayList<>(this.getTeBlocks());
            listBlock.sort((o1, o2) -> {
                if (o1.getId() < o2.getId()) {
                    return -1;
                }
                if (o1.getId() - o2.getId() == 0) {
                    return 0;
                } else {
                    return 1;
                }
            });
        }

        public List<? extends IMultiTileBlock> getListBlock() {
            return listBlock;
        }

        public CreativeTabs getTab() {
            return tab;
        }

        public Class<? extends TileEntityBlock> getClassFromName(String name) {
            final ArrayList<E> list = new ArrayList<>(this.teBlocks);
            for (E e : list) {
                if (e.getName().equals(name)) {
                    return e.getTeClass();
                }
            }
            return null;
        }

        void register(E block) {
            this.teBlocks.add(block);
            if (block.getId() > -1) {
                int ID = block.getId();

                while (this.idMap.size() < ID) {
                    this.idMap.add(null);
                }

                if (this.idMap.size() == ID) {
                    this.idMap.add(block);
                } else {
                    this.idMap.set(ID, block);
                }
            }


        }

        public BlockTileEntity getBlock() {
            return this.block;
        }

        void setBlock(BlockTileEntity block) {
            this.block = block;
        }


        public Material getDefaultMaterial() {
            return this.defaultMaterial;
        }

        public boolean hasSpecialModels() {
            return this.specialModels;
        }

        public Set<? extends IMultiTileBlock> getTeBlocks() {
            return Collections.unmodifiableSet(this.teBlocks);
        }

        public List<IMultiTileBlock> getIdMap() {
            return Collections.unmodifiableList(this.idMap);
        }

    }

}
