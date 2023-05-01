package bobpack.Patterns;

import java.io.IOException;

public interface Observer<T> {
    void update(T observable) throws IOException;
}