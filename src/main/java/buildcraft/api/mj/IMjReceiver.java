package buildcraft.api.mj;


public interface IMjReceiver extends IMjConnector {

    long receivePower(long microJoules, boolean simulate);

    default boolean canReceive() {
        return true;
    }
}
