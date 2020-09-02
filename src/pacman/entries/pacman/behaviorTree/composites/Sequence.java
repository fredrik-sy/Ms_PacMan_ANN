package pacman.entries.pacman.behaviorTree.composites;

import pacman.entries.pacman.behaviorTree.Task;
import pacman.game.Game;

public class Sequence extends CompositeTask {
    @Override
    public Status run(Game game) {
        for (Task c : children) {
            Status status = c.run(game);

            if (status == Status.FAILURE || status == Status.RUNNING)
                return status;
        }

        return Status.SUCCESS;
    }
}
