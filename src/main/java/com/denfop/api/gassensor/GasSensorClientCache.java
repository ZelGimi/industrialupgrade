package com.denfop.api.gassensor;


public final class GasSensorClientCache {

    private static GasSensorScanState state = GasSensorScanState.empty(4);
    private static String selectedVeinId = "";
    private static String followedVeinId = "";

    private GasSensorClientCache() {
    }

    public static void clear() {
        state = GasSensorScanState.empty(4);
        selectedVeinId = "";
        followedVeinId = "";
    }

    public static void applyProgress(GasSensorScanState newState) {
        state = newState;
        normalizeSelection();
    }

    public static void applyResult(GasSensorScanState newState) {
        state = newState;
        normalizeSelection();
    }

    public static GasSensorScanState getState() {
        return state;
    }

    public static String getSelectedVeinId() {
        return selectedVeinId;
    }

    public static void setSelectedVeinId(String id) {
        selectedVeinId = id == null ? "" : id;
        normalizeSelection();
    }

    public static GasSensorVeinEntry getSelectedVein() {
        return state.getById(selectedVeinId);
    }

    public static GasSensorVeinEntry getFollowedVein() {
        return state.getById(followedVeinId);
    }

    public static boolean isFollowingSelected() {
        return !followedVeinId.isBlank() && followedVeinId.equals(selectedVeinId);
    }

    public static boolean toggleFollowSelected() {
        GasSensorVeinEntry selected = getSelectedVein();
        if (selected == null) {
            followedVeinId = "";
            return false;
        }

        if (selected.getId().equals(followedVeinId)) {
            followedVeinId = "";
            return false;
        }

        followedVeinId = selected.getId();
        return true;
    }

    private static void normalizeSelection() {
        if (state.getVeins().isEmpty()) {
            selectedVeinId = "";
            followedVeinId = "";
            return;
        }

        if (state.getById(selectedVeinId) == null) {
            selectedVeinId = state.getVeins().get(0).getId();
        }

        if (!followedVeinId.isBlank() && state.getById(followedVeinId) == null) {
            followedVeinId = "";
        }
    }
}