package com.denfop.blocks.state;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

public class TileEntityBlockStateContainer extends BlockStateContainer {


    private final Material material;

    public TileEntityBlockStateContainer(Block blockIn, Material material, IProperty<?>... properties) {
        super(blockIn, properties);
        this.material = material;
    }

    protected BlockStateContainer.StateImplementation createState(
            Block block,
            @NotNull ImmutableMap<IProperty<?>, Comparable<?>> properties,
            ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties
    ) {
        return new PropertiesStateInstance(block, properties);
    }


    public class PropertiesStateInstance extends BlockStateContainer.StateImplementation {

        private final Map<IProperty<?>, Comparable<?>> Properties;
        private HashMap<ImmutableMap<IProperty<?>, Comparable<?>>, IBlockState> mapProperties = new HashMap<>();
        private Map<IUnlistedProperty<?>, Object> extraProperties = new HashMap<>();

        private PropertiesStateInstance(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties) {
            super(block, properties, null);
            this.Properties = new HashMap<>(PropertiesStateInstance.this.getProperties());
        }

        public PropertiesStateInstance(
                PropertiesStateInstance propertiesStateInstance,
                Map<IUnlistedProperty<?>, Object> properties
        ) {
            super(
                    propertiesStateInstance.getBlock(),
                    propertiesStateInstance.getProperties(),
                    propertiesStateInstance.propertyValueTable
            );
            this.Properties = new HashMap<>(PropertiesStateInstance.this.getProperties());
            this.extraProperties = properties;
        }

        @Override
        public Material getMaterial() {
            return TileEntityBlockStateContainer.this.material;
        }

        public HashMap<ImmutableMap<IProperty<?>, Comparable<?>>, IBlockState> getMapProperties() {
            if (this.mapProperties.isEmpty()) {
                this.mapProperties = new HashMap<>();
                for (final IBlockState rawState : TileEntityBlockStateContainer.this.getValidStates()) {
                    this.mapProperties.put(rawState.getProperties(), rawState);
                }
            }
            return mapProperties;
        }

        public boolean hasValue(IUnlistedProperty<?> property) {
            return this.extraProperties.containsKey(property);
        }

        public <T> T getValue(IUnlistedProperty<T> property) {
            T ret = (T) this.extraProperties.get(property);
            return ret;
        }

        public <T> PropertiesStateInstance withProperties(Object... properties) {
            if (properties.length % 2 != 0) {
                throw new IllegalArgumentException("property pairs expected");
            } else {
                Map<IUnlistedProperty<?>, Object> newExtraProperties = new IdentityHashMap(this.extraProperties);
                final IUnlistedProperty<?> property = (IUnlistedProperty<?>) properties[0];
                newExtraProperties.put(property, properties[1]);
                return TileEntityBlockStateContainer.this.new PropertiesStateInstance(this, newExtraProperties);
            }
        }

        public <T extends Comparable<T>, V extends T> PropertiesStateInstance withProperty(IProperty<T> property, V value) {
            for (T value1 : property.getAllowedValues()) {
                V value2 = (V) value1;
                if (value2.equals(value)) {
                    Properties.replace(property, value);
                    if (this.mapProperties.isEmpty()) {
                        this.mapProperties = new HashMap<>();
                        for (final IBlockState rawState : TileEntityBlockStateContainer.this.getValidStates()) {
                            this.mapProperties.put(rawState.getProperties(), rawState);
                        }
                    }
                    return (PropertiesStateInstance) this.mapProperties.get(Properties);
                }
            }
            return null;
        }


        public boolean hasExtraProperties() {
            return !this.extraProperties.isEmpty();
        }

    }

}
