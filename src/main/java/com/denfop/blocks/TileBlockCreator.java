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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class TileBlockCreator {


    public static TileBlockCreator instance;
    public int index = 0;
    private List<InfoAboutTile<?>> dataInfo = new LinkedList<>();

    public TileBlockCreator() {
        instance = this;
    }


    public <E extends Enum<E> & IMultiTileBlock> BlockTileEntity create(Class<E> enumClass) {
        InfoAboutTile<E> instance = new InfoAboutTile<>(enumClass,index);
        final BlockTileEntity block = BlockTileEntity.create(
                "industrialupgrade_" + instance.teBlocks.get(0).getIdentifier().getResourcePath(),
                instance.teBlocks.get(0).getIdentifier(),
                instance
        );
        instance.setBlock(block);
        dataInfo.add(instance);
        index++;
        return block;
    }


    public void buildBlocks() {


        for (InfoAboutTile<?> tile : dataInfo) {
            for (IMultiTileBlock multiTileBlock : tile.teBlocks) {
                multiTileBlock.buildDummies();
            }
        }
        dataInfo = new ArrayList<>(dataInfo);
    }


    public BlockTileEntity get(int index) {
        return dataInfo.get(index).getBlock();
    }


    public static class InfoAboutTile<E extends Enum<E> & IMultiTileBlock> {

        private final boolean specialModels;
        private List<IMultiTileBlock> teBlocks;
        private final int index;
        private List<IMultiTileBlock> idMap;
        private final CreativeTabs tab;
        private final Material defaultMaterial;
        private final List<? extends IMultiTileBlock> listBlock;
        private BlockTileEntity block;

        InfoAboutTile(Class<E> universe, int index) {
            this.idMap = new LinkedList<>();
            this.index = index;
            this.teBlocks = new LinkedList<>();
            this.specialModels = IMultiBlockItem.class.isAssignableFrom(universe);
            for (final E e : EnumSet.allOf(universe)) {
                this.register(e);
            }
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

        public List<? extends IMultiTileBlock> getListBlock() {
            return listBlock;
        }

        public CreativeTabs getTab() {
            return tab;
        }

        public Class<? extends TileEntityBlock> getClassFromName(String name) {
            for (IMultiTileBlock e : listBlock) {
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

        public List<IMultiTileBlock> getTeBlocks() {
            return teBlocks;
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

        public List<IMultiTileBlock> getIdMap() {
            return idMap;
        }

    }

}
