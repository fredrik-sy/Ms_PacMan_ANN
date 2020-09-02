package pacman.entries.pacman.behaviorTree.actions;

import pacman.entries.pacman.behaviorTree.GameUtils;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import static pacman.game.Constants.DM;

public class GoToClosestPacDot extends ActionTask<MOVE> {
    public GoToClosestPacDot(ActionListener listener) {
        super(listener);
    }

    @Override
    public Status run(Game game) {
        try {
            int pNode = game.getPacmanCurrentNodeIndex();
            int closestPacDot = GameUtils.getClosestPacDot(game);

            MOVE nextMove = game.getNextMoveTowardsTarget(pNode, closestPacDot, DM.PATH);
            listener.actionPerformed(nextMove);
            return Status.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return Status.FAILURE;
        }
    }
}
