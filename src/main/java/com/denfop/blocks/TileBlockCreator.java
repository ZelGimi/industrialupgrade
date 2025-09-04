package com.denfop.blocks;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.item.MultiBlockItem;
import com.denfop.blockentity.base.BlockEntityBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.material.Material;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class TileBlockCreator {


    public static TileBlockCreator instance;
    public int index = 0;
    private List<InfoAboutTile<?>> dataInfo = new LinkedList<>();

    public TileBlockCreator() {
        instance = this;
    }


    public <E extends Enum<E> & MultiBlockEntity> BlockTileEntity<E> create(E enumClass, ResourceLocation location) {
        InfoAboutTile<E> instance = new InfoAboutTile<>(enumClass, index);
        final BlockTileEntity<E> block = BlockTileEntity.create(
                enumClass,
                location,
                instance
        );
        instance.setBlock(block);
        dataInfo.add(instance);
        index++;
        return block;
    }


    public void buildBlocks() {
        for (InfoAboutTile<?> tile : dataInfo) {
            for (MultiBlockEntity multiTileBlock : tile.teBlocks) {
                multiTileBlock.buildDummies();
            }
        }
        dataInfo = new ArrayList<>(dataInfo);
    }


    public BlockTileEntity get(int index) {
        return dataInfo.get(index).getBlock();
    }

    public List<MultiBlockEntity> getAllTiles() {
        List<MultiBlockEntity> tileBlocks = new ArrayList<>();
        for (InfoAboutTile<?> tile : dataInfo) {
            tileBlocks.addAll(tile.teBlocks);
        }
        return tileBlocks;
    }

    public static class InfoAboutTile<E extends Enum<E> & MultiBlockEntity> {

        private final boolean specialModels;
        private final int index;
        private final CreativeModeTab tab;
        private final Material defaultMaterial;
        private final List<? extends MultiBlockEntity> listBlock;
        private List<MultiBlockEntity> teBlocks;
        private List<MultiBlockEntity> idMap;
        private BlockTileEntity<E> block;

        InfoAboutTile(E universe, int index) {
            this.idMap = new LinkedList<>();
            this.index = index;
            this.teBlocks = new LinkedList<>();
            this.specialModels = MultiBlockItem.class.isAssignableFrom(universe.getClass());
            this.register(universe);
            this.idMap = new ArrayList<>(idMap);
            this.teBlocks = new ArrayList<>(this.teBlocks);
            this.defaultMaterial = teBlocks.get(0).getMaterial();
            this.tab = teBlocks.get(0).getCreativeTab();
            this.listBlock = teBlocks;
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

        public List<? extends MultiBlockEntity> getListBlock() {
            return listBlock;
        }

        public CreativeModeTab getTab() {
            return tab;
        }

        public Class<? extends BlockEntityBase> getClassFromName(String name) {
            for (MultiBlockEntity e : listBlock) {
                if (e.getName().equals(name)) {
                    return e.getTeClass();
                }
            }
            return null;
        }

        void register(E block) {
            this.teBlocks.add(block);
            block.setIdBlock(index);
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

        public List<MultiBlockEntity> getTeBlocks() {
            return teBlocks;
        }

        public BlockTileEntity<E> getBlock() {
            return this.block;
        }

        void setBlock(BlockTileEntity<E> block) {
            this.block = block;
        }


        public Material getDefaultMaterial() {
            return this.defaultMaterial;
        }

        public boolean hasSpecialModels() {
            return this.specialModels;
        }

        public List<MultiBlockEntity> getIdMap() {
            return idMap;
        }

    }

}
