package pacman.entries.pacman.behaviorTree;

import pacman.game.Game;

public interface Task {
    Status run(Game game);

    enum Status {
        SUCCESS,
        FAILURE,
        RUNNING
    }
}
