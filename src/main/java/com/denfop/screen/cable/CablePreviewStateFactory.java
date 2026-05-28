package com.denfop.screen.cable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;

public final class CablePreviewStateFactory {

    private static final double CABLE_MIN = 5.0D / 16.0D;
    private static final double CABLE_MAX = 11.0D / 16.0D;
    private static final double FACE_THICKNESS = 1.0D / 16.0D;

    private static final String[] DISCONNECTED_ENUM_VALUES = new String[]{"none", "false", "disabled", "disconnected", "off"};
    private static final String[] CONNECTED_ENUM_VALUES = new String[]{"side", "connected", "true", "enabled", "on"};

    private CablePreviewStateFactory() {
    }

    public static PreparedPreview prepare(BlockState sourceState, Collection<Direction> blacklist) {
        if (sourceState == null) {
            throw new IllegalArgumentException("sourceState cannot be null");
        }

        EnumSet<Direction> disabled = EnumSet.noneOf(Direction.class);
        if (blacklist != null) {
            disabled.addAll(blacklist);
        }

        BlockState previewState = applyConnectionState(sourceState, disabled);
        EnumMap<CablePreviewPart, List<AABB>> boxes = buildBoxes(previewState, disabled);

        return new PreparedPreview(previewState, boxes);
    }

    private static BlockState applyConnectionState(BlockState state, EnumSet<Direction> disabled) {
        BlockState result = state;

        for (Direction direction : Direction.values()) {
            result = applyDirection(result, direction, !disabled.contains(direction));
        }

        return result;
    }

    private static BlockState applyDirection(BlockState state, Direction direction, boolean connected) {
        BlockState result = state;

        for (Property<?> property : state.getProperties()) {
            String propertyName = property.getName().toLowerCase(Locale.ROOT);
            if (!matchesDirectionProperty(propertyName, direction)) {
                continue;
            }

            if (property instanceof BooleanProperty booleanProperty) {
                result = result.setValue(booleanProperty, connected);
                continue;
            }

            if (property instanceof EnumProperty<?> enumProperty) {
                Object match = findEnumMatch(enumProperty.getPossibleValues(), connected, direction);
                if (match != null) {
                    result = setEnumValue(result, property, match);
                }
            }
        }

        return result;
    }

    private static boolean matchesDirectionProperty(String propertyName, Direction direction) {
        String dir = direction.getName();
        return propertyName.equals(dir)
                || propertyName.equals(dir + "_connected")
                || propertyName.equals(dir + "_connection")
                || propertyName.equals(dir + "_side")
                || propertyName.startsWith(dir + "_")
                || propertyName.endsWith("_" + dir);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> BlockState setEnumValue(BlockState state, Property<T> property, Object value) {
        return state.setValue(property, (T) value);
    }

    private static Object findEnumMatch(Collection<?> values, boolean connected, Direction direction) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        if (connected) {
            for (Object value : values) {
                if (getValueName(value).equals(direction.getName())) {
                    return value;
                }
            }

            for (String preferred : CONNECTED_ENUM_VALUES) {
                for (Object value : values) {
                    if (getValueName(value).equals(preferred)) {
                        return value;
                    }
                }
            }

            for (Object value : values) {
                String name = getValueName(value);
                boolean disconnectedName = false;
                for (String off : DISCONNECTED_ENUM_VALUES) {
                    if (off.equals(name)) {
                        disconnectedName = true;
                        break;
                    }
                }
                if (!disconnectedName) {
                    return value;
                }
            }
        } else {
            for (String preferred : DISCONNECTED_ENUM_VALUES) {
                for (Object value : values) {
                    if (getValueName(value).equals(preferred)) {
                        return value;
                    }
                }
            }
        }

        return null;
    }

    private static String getValueName(Object value) {
        if (value instanceof StringRepresentable representable) {
            return representable.getSerializedName().toLowerCase(Locale.ROOT);
        }
        if (value instanceof Enum<?> enumValue) {
            return enumValue.name().toLowerCase(Locale.ROOT);
        }
        return String.valueOf(value).toLowerCase(Locale.ROOT);
    }

    private static EnumMap<CablePreviewPart, List<AABB>> buildBoxes(BlockState previewState, EnumSet<Direction> disabled) {
        EnumMap<CablePreviewPart, List<AABB>> result = new EnumMap<>(CablePreviewPart.class);

        VoxelShape shape = previewState.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
        List<AABB> rawBoxes = shape.isEmpty() ? Collections.emptyList() : shape.toAabbs();

        if (!isUsableShape(rawBoxes)) {
            rawBoxes = buildFallbackShapeBoxes(disabled);
        }

        for (AABB box : rawBoxes) {
            CablePreviewPart part = classifyBox(box);
            result.computeIfAbsent(part, ignored -> new ArrayList<>()).add(box);
        }

        if (!result.containsKey(CablePreviewPart.CENTER)) {
            result.put(CablePreviewPart.CENTER, new ArrayList<>(List.of(centerBox())));
        }

        for (Direction direction : Direction.values()) {
            CablePreviewPart part = CablePreviewPart.fromDirection(direction);
            if (!result.containsKey(part) || result.get(part).isEmpty()) {
                result.computeIfAbsent(part, ignored -> new ArrayList<>())
                        .add(buildFallbackDirectionBox(direction, !disabled.contains(direction)));
            }
        }

        for (Map.Entry<CablePreviewPart, List<AABB>> entry : result.entrySet()) {
            entry.setValue(Collections.unmodifiableList(new ArrayList<>(entry.getValue())));
        }

        return result;
    }

    private static boolean isUsableShape(List<AABB> boxes) {
        if (boxes == null || boxes.isEmpty()) {
            return false;
        }

        if (boxes.size() == 1) {
            AABB box = boxes.get(0);
            boolean fullCube =
                    nearly(box.minX, 0.0D) &&
                            nearly(box.minY, 0.0D) &&
                            nearly(box.minZ, 0.0D) &&
                            nearly(box.maxX, 1.0D) &&
                            nearly(box.maxY, 1.0D) &&
                            nearly(box.maxZ, 1.0D);

            return !fullCube;
        }

        return true;
    }

    private static boolean nearly(double a, double b) {
        return Math.abs(a - b) <= 0.0001D;
    }

    private static CablePreviewPart classifyBox(AABB box) {
        double cx = (box.minX + box.maxX) * 0.5D;
        double cy = (box.minY + box.maxY) * 0.5D;
        double cz = (box.minZ + box.maxZ) * 0.5D;

        double dx = Math.abs(cx - 0.5D);
        double dy = Math.abs(cy - 0.5D);
        double dz = Math.abs(cz - 0.5D);

        double max = Math.max(dx, Math.max(dy, dz));
        if (max < (1.5D / 16.0D)) {
            return CablePreviewPart.CENTER;
        }

        if (max == dx) {
            return cx > 0.5D ? CablePreviewPart.EAST : CablePreviewPart.WEST;
        }
        if (max == dy) {
            return cy > 0.5D ? CablePreviewPart.UP : CablePreviewPart.DOWN;
        }
        return cz > 0.5D ? CablePreviewPart.SOUTH : CablePreviewPart.NORTH;
    }

    private static List<AABB> buildFallbackShapeBoxes(EnumSet<Direction> disabled) {
        List<AABB> boxes = new ArrayList<>();
        boxes.add(centerBox());

        for (Direction direction : Direction.values()) {
            boxes.add(buildFallbackDirectionBox(direction, !disabled.contains(direction)));
        }

        return boxes;
    }

    private static AABB centerBox() {
        return new AABB(CABLE_MIN, CABLE_MIN, CABLE_MIN, CABLE_MAX, CABLE_MAX, CABLE_MAX);
    }

    private static AABB buildFallbackDirectionBox(Direction direction, boolean connected) {
        double min = CABLE_MIN;
        double max = CABLE_MAX;

        return switch (direction) {
            case NORTH -> connected
                    ? new AABB(min, min, 0.0D, max, max, min)
                    : new AABB(min, min, min - FACE_THICKNESS, max, max, min);
            case SOUTH -> connected
                    ? new AABB(min, min, max, max, max, 1.0D)
                    : new AABB(min, min, max, max, max, max + FACE_THICKNESS);
            case WEST -> connected
                    ? new AABB(0.0D, min, min, min, max, max)
                    : new AABB(min - FACE_THICKNESS, min, min, min, max, max);
            case EAST -> connected
                    ? new AABB(max, min, min, 1.0D, max, max)
                    : new AABB(max, min, min, max + FACE_THICKNESS, max, max);
            case DOWN -> connected
                    ? new AABB(min, 0.0D, min, max, min, max)
                    : new AABB(min, min - FACE_THICKNESS, min, max, min, max);
            case UP -> connected
                    ? new AABB(min, max, min, max, 1.0D, max)
                    : new AABB(min, max, min, max, max + FACE_THICKNESS, max);
        };
    }

    public record PreparedPreview(BlockState state, Map<CablePreviewPart, List<AABB>> boxes) {

        public List<AABB> boxes(CablePreviewPart part) {
            return this.boxes.getOrDefault(part, Collections.emptyList());
        }

        public AABB mergedBox(CablePreviewPart part) {
            List<AABB> list = boxes(part);
            if (list.isEmpty()) {
                return centerBox();
            }

            AABB merged = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                merged = merged.minmax(list.get(i));
            }
            return merged;
        }
    }
}