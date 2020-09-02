package pacman.entries.pacman.behaviorTree.conditions;

import pacman.entries.pacman.behaviorTree.GameUtils;
import pacman.entries.pacman.behaviorTree.Task;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class CanEatClosestGhost implements Task {
    @Override
    public Status run(Game game) {
        GHOST closestGhostType = GameUtils.getClosestGhostByPath(game);

        if (closestGhostType == null)
            return Status.FAILURE;

        if (game.isGhostEdible(closestGhostType))
            return Status.SUCCESS;

        return Status.FAILURE;
    }
}
