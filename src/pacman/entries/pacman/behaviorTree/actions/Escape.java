package pacman.entries.pacman.behaviorTree.actions;

import pacman.entries.pacman.behaviorTree.GameUtils;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import static pacman.game.Constants.DM.PATH;

public class Escape extends ActionTask<MOVE> {
    public static final int SEARCH_DISTANCE = 30;

    public Escape(ActionListener listener) {
        super(listener);
    }

    @Override
    public Status run(Game game) {
        int[] gNodes = GameUtils.searchNotEdibleGhostNodes(game, SEARCH_DISTANCE);

        if (gNodes.length == 0)
            return Status.FAILURE;

        int pNode = game.getPacmanCurrentNodeIndex();
        int safeNode = game.getFarthestNodeIndexFromNodeIndex(pNode, gNodes, PATH);

        MOVE nextMove = game.getNextMoveAwayFromTarget(pNode, safeNode, PATH);
        listener.actionPerformed(nextMove);

        return Status.SUCCESS;
    }
}
