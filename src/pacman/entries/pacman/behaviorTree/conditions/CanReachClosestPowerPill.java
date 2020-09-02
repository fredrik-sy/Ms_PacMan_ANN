package pacman.entries.pacman.behaviorTree.conditions;

import pacman.entries.pacman.behaviorTree.GameUtils;
import pacman.entries.pacman.behaviorTree.Task;
import pacman.game.Game;

import static pacman.game.Constants.GHOST;
import static pacman.game.Constants.MOVE;

public class CanReachClosestPowerPill implements Task {
    @Override
    public Status run(Game game) {
        if (game.getNumberOfActivePowerPills() == 0)
            return Status.FAILURE;

        int pNode = game.getPacmanCurrentNodeIndex();
        int pPillNode = GameUtils.getClosestPowerPill(game);
        int[] pPath = game.getShortestPath(pNode, pPillNode);

        /* Check Ghost Collision */
        for (GHOST ghostType : GHOST.values()) {
            int gNode = game.getGhostCurrentNodeIndex(ghostType);
            MOVE gLastMove = game.getGhostLastMoveMade(ghostType);

            int[] gPath = game.getShortestPath(gNode, pNode, gLastMove);

            if (2 * pPath.length > gPath.length) { // Check if it's possible for collision.
                /* Simulate Turn Movement */
                int i = 0; // Ghost Turn Index
                int j = 0; // Pacman Turn Index

                while (i < gPath.length && j < pPath.length) {
                    if (i < j) {
                        /* Ghosts Turn */
                        if (pPath[j] == gPath[i])
                            return Status.FAILURE;
                        else
                            i++;
                    } else {
                        /* Pacmans Turn */
                        if (pPath[j] == gPath[i])
                            return Status.FAILURE;
                        else
                            j++;
                    }
                }
            }
        }

        return Status.SUCCESS;
    }
}
