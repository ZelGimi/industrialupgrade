package buildcraft.api.mj;

import javax.annotation.Nonnull;


public interface IMjConnector {

    boolean canConnect(@Nonnull IMjConnector other);

}
