package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.entries.pacman.behaviorTree.actions.*;
import pacman.entries.pacman.behaviorTree.composites.*;
import pacman.entries.pacman.behaviorTree.conditions.*;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacManBT extends Controller<MOVE> implements ActionListener<MOVE> {
    private CompositeTask myBehaviorTree;
    private MOVE myMove = MOVE.NEUTRAL;

    public MyPacManBT() {
        setUpBehaviourTree();
    }

    private void setUpBehaviourTree() {
        myBehaviorTree = new Selector();

        Selector selSurvivalChoice = new Selector();

        Sequence seqSurvival = new Sequence();
        seqSurvival.add(new GhostNearby());
        seqSurvival.add(selSurvivalChoice);
        myBehaviorTree.add(seqSurvival);

        Sequence seqChaseGhost = new Sequence();
        seqChaseGhost.add(new CanEatClosestGhost());
        seqChaseGhost.add(new GoToClosestGhost(this));
        selSurvivalChoice.add(seqChaseGhost);

        Sequence seqGetPowerPill = new Sequence();
        seqGetPowerPill.add(new CanReachClosestPowerPill());
        seqGetPowerPill.add(new GoToClosestPowerPill(this));
        selSurvivalChoice.add(seqGetPowerPill);

        selSurvivalChoice.add(new Escape(this));

        myBehaviorTree.add(new GoToClosestPacDot(this));
    }

    public MOVE getMove(Game game, long timeDue) {
        //Place your game logic here to play the game as Ms Pac-Man
        myBehaviorTree.run(game);
        System.out.println(game.getNumberOfNodes());
        return myMove;
    }

    @Override
    public void actionPerformed(MOVE e) {
        myMove = e;
    }
}