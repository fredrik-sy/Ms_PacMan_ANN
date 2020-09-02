package pacman.entries.pacman.behaviorTree.actions;

import pacman.entries.pacman.behaviorTree.GameUtils;
import pacman.game.Constants.*;
import pacman.game.Game;

public class GoToClosestGhost extends ActionTask<MOVE> {
    public GoToClosestGhost(ActionListener listener) {
        super(listener);
    }

    @Override
    public Status run(Game game) {
        GHOST ghostType = GameUtils.getClosestGhostByPath(game);

        if (ghostType == null)
            return Status.FAILURE;

        int pNode = game.getPacmanCurrentNodeIndex();
        int gNode = game.getGhostCurrentNodeIndex(ghostType);

        MOVE nextMove = game.getNextMoveTowardsTarget(pNode, gNode, DM.PATH);
        listener.actionPerformed(nextMove);

        return Status.SUCCESS;
    }
}
