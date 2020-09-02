package pacman.entries.pacman.behaviorTree.actions;

import pacman.entries.pacman.behaviorTree.GameUtils;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToClosestPowerPill extends ActionTask<MOVE> {
    public GoToClosestPowerPill(ActionListener listener) {
        super(listener);
    }

    @Override
    public Status run(Game game) {
        int pNode = game.getPacmanCurrentNodeIndex();
        int pPillNode = GameUtils.getClosestPowerPill(game);

        if (pPillNode == Integer.MAX_VALUE)
            return Status.FAILURE;

        MOVE nextMove = game.getNextMoveTowardsTarget(pNode, pPillNode, Constants.DM.PATH);
        listener.actionPerformed(nextMove);

        return Status.SUCCESS;
    }
}
