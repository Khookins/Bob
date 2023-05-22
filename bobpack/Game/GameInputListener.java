package bobpack.Game;

import java.util.function.Consumer;

public interface GameInputListener {
    void ask(String prompt, Consumer<String> callback);
}
