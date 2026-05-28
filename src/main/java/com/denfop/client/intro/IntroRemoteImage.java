package com.denfop.client.intro;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class IntroRemoteImage {

    private final String source;
    private final AtomicBoolean started = new AtomicBoolean(false);
    private volatile State state = State.IDLE;
    private volatile int width = 0;
    private volatile int height = 0;
    private volatile List<Tile> tiles = List.of();
    @Nullable
    private volatile String errorMessage;
    public IntroRemoteImage(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public boolean tryStart() {
        return started.compareAndSet(false, true);
    }

    public void setLoading() {
        this.state = State.LOADING;
    }

    public void setReady(List<Tile> tiles, int width, int height) {
        this.tiles = List.copyOf(tiles);
        this.width = width;
        this.height = height;
        this.state = State.READY;
        this.errorMessage = null;
    }

    public State getState() {
        return state;
    }

    public boolean isReady() {
        return state == State.READY && !tiles.isEmpty() && width > 0 && height > 0;
    }

    public boolean isFailed() {
        return state == State.FAILED;
    }

    public void setFailed(String errorMessage) {
        this.state = State.FAILED;
        this.errorMessage = errorMessage;
        this.tiles = List.of();
    }

    public boolean isLoading() {
        return state == State.LOADING;
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }

    public int getWidth() {
        return Math.max(width, 1);
    }

    public int getHeight() {
        return Math.max(height, 1);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public enum State {
        IDLE,
        LOADING,
        READY,
        FAILED
    }

    public static final class Tile {
        private final ResourceLocation textureLocation;
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        public Tile(ResourceLocation textureLocation, int x, int y, int width, int height) {
            this.textureLocation = textureLocation;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public ResourceLocation getTextureLocation() {
            return textureLocation;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}