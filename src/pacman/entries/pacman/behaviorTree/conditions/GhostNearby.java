package pacman.entries.pacman.behaviorTree.conditions;

import pacman.entries.pacman.behaviorTree.Task;
import pacman.game.Game;

import static pacman.game.Constants.GHOST;

public class GhostNearby implements Task {
    public static final int DETECT_DISTANCE = 30;

    @Override
    public Status run(Game game) {
        int pNode = game.getPacmanCurrentNodeIndex();

        for (GHOST ghostType : GHOST.values()) {
            if (game.getGhostLairTime(ghostType) == 0) {
                int gNode = game.getGhostCurrentNodeIndex(ghostType);

                if (game.getEuclideanDistance(pNode, gNode) < DETECT_DISTANCE)
                    return Status.SUCCESS;
            }
        }

        return Status.FAILURE;
    }
}
