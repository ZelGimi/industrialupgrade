package com.denfop.blocks.state;

import com.denfop.api.blockentity.MultiBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TypeProperty extends Property<State> {


    public final String resourceLocationName;
    private final ConcurrentHashMap<MultiBlockEntity, StatesBlocks> mapStates;
    public List<State> allowedValues;
    List<StatesBlocks> locationBlocks;

    public TypeProperty(final ResourceLocation identifier, final MultiBlockEntity teBlock) {
        super("type", State.class);
        this.resourceLocationName = identifier.toString();
        this.mapStates = new ConcurrentHashMap<>();
        this.locationBlocks = new LinkedList<>();

        String stateName = teBlock.hasActive() ? "active" : "";
        String[] multiModels = teBlock.getMultiModels(teBlock);
        final StatesBlocks state = new StatesBlocks(teBlock, stateName, multiModels);
        locationBlocks.add(state);
        mapStates.put(teBlock, state);
        this.allowedValues = new LinkedList<>();
        for (StatesBlocks teBlockPair : locationBlocks) {
            this.allowedValues.addAll(teBlockPair.statesBlocks);
        }
        if (this.allowedValues.size() == 1)
            this.allowedValues.add(new State(teBlock, "invalid"));
        this.allowedValues = new ArrayList<>(allowedValues);
        this.locationBlocks = new ArrayList<>(locationBlocks);
    }

    public List<StatesBlocks> getAllStates() {
        return this.locationBlocks;
    }

    public State getState(MultiBlockEntity teBlock) {
        return getState(teBlock, "");
    }


    public State getState(MultiBlockEntity teBlock, String active) {
        StatesBlocks state = mapStates.get(teBlock);
        return state == null ? null : state.getState(active);
    }

    public String getName() {
        return "type";
    }

    public Collection<State> getPossibleValues() {
        return this.allowedValues;
    }

    public Class<State> getValueClass() {
        return State.class;
    }


    public java.util.Optional<State> getValue(String value) {
        for (State block : allowedValues) {
            if (getName(block).equals(value)) {
                return java.util.Optional.of(block);
            }
        }
        return java.util.Optional.empty();
    }

    public String getName(State value) {
        return !value.state.isEmpty() ? value.teBlock.getName() + "_" + value.state : value.teBlock.getName();
    }


    public String toString() {
        return "TypeProperty{For " + this.resourceLocationName + '}';
    }

    public static class StatesBlocks {

        public final boolean hasActive;
        public List<State> statesBlocks = new ArrayList<>();


        public StatesBlocks(MultiBlockEntity block, String state, String[] multiModels) {
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


        public MultiBlockEntity getBlock() {
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

        public String getName() {
            return this.getBlock().getName();
        }

    }

}
