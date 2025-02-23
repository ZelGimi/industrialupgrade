package com.denfop.blocks;

import com.denfop.api.tile.IMultiTileBlock;
import com.google.common.base.Optional;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TypeProperty implements IProperty<State> {


    public static State invalid = new StatesBlocks(MultiTileBlock.invalid, "", null).statesBlocks.get(0);


    public final List<State> allowedValues;
    public final String resourceLocationName;
    private final HashMap<IMultiTileBlock, StatesBlocks> mapStates;
    List<StatesBlocks> locationBlocks;

    public TypeProperty(final ResourceLocation identifier, final TileBlockCreator.InfoAboutTile<?> value) {
        this.resourceLocationName = identifier.toString();
        this.mapStates = new HashMap<>();
        this.locationBlocks = new ArrayList<>();

        for (IMultiTileBlock teBlock : value.getTeBlocks()) {

            String stateName = teBlock.hasActive() ? "active" : "";
            String[] multiModels = teBlock.getMultiModels(teBlock);
            locationBlocks.add(new StatesBlocks(teBlock, stateName, multiModels));
            mapStates.put(teBlock, locationBlocks.get(locationBlocks.size() - 1));
        }


        this.allowedValues = new ArrayList<>();
        this.allowedValues.add(invalid);
        for (StatesBlocks teBlockPair : locationBlocks) {
            this.allowedValues.addAll(teBlockPair.statesBlocks);
        }

    }

    public List<StatesBlocks> getAllStates() {
        return this.locationBlocks;
    }

    public State getState(IMultiTileBlock teBlock) {
        return getState(teBlock, "");
    }


    public State getState(IMultiTileBlock teBlock, String active) {
        StatesBlocks state = mapStates.get(teBlock);
        return state == null ? invalid : state.getState(active);
    }

    public String getName() {
        return "type";
    }

    public @NotNull Collection<State> getAllowedValues() {
        return this.allowedValues;
    }

    public @NotNull Class<State> getValueClass() {
        return State.class;
    }

    public Optional<State> parseValue(String value) {
        for (State block : allowedValues) {
            if (getName(block).equals(value)) {
                return Optional.of(block);
            }
        }
        return Optional.absent();
    }

    public String getName(State value) {
        return !value.state.isEmpty() ? value.teBlock.getName() + "_" + value.state : value.teBlock.getName();
    }

    public String toString() {
        return "TypeProperty{For " + this.resourceLocationName + '}';
    }

    public static class StatesBlocks {

        public final boolean hasActive;
        List<State> statesBlocks = new ArrayList<>();


        public StatesBlocks(IMultiTileBlock block, String state, String[] multiModels) {
            statesBlocks.add(new State(block, ""));
            if (state.equals("active")) {
                statesBlocks.add(new State(block, state));
                this.hasActive = true;
            } else {
                this.hasActive = false;
            }
            if (multiModels != null) {
                for (final String multiModel : multiModels) {
                    statesBlocks.add(new State(block, multiModel));
                }
            }
        }


        public IMultiTileBlock getBlock() {
            return this.statesBlocks.get(0).teBlock;
        }

        public State getState(String stateName) {
            for (State state : this.statesBlocks) {
                if (state.state.equals(stateName)) {
                    return state;
                }
            }
            return this.statesBlocks.get(0);
        }

        public boolean hasActive() {
            return this.hasActive;
        }


        public boolean hasItem() {
            return this.getBlock().hasItem();
        }

        public ResourceLocation getIdentifier() {
            return this.getBlock().getIdentifier();
        }

        public String getName() {
            return this.getBlock().getName();
        }

    }

}
