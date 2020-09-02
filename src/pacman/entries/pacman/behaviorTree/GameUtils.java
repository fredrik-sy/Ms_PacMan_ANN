package pacman.entries.pacman.behaviorTree;

import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.List;

import static pacman.game.Constants.*;
import static pacman.game.Constants.DM.*;
import static pacman.game.Constants.MOVE.NEUTRAL;

public final class GameUtils {
    public static GHOST getClosestGhostByPath(Game game) {
        int pNode = game.getPacmanCurrentNodeIndex();
        GHOST closestGhostType = null;
        int shortestGhostDist = Integer.MAX_VALUE;

        for (GHOST ghostType : GHOST.values()) {
            if (game.getGhostLairTime(ghostType) == 0) {
                int gNode = game.getGhostCurrentNodeIndex(ghostType);
                int gDist = game.getShortestPathDistance(pNode, gNode);

                if (gDist < shortestGhostDist) {
                    closestGhostType = ghostType;
                    shortestGhostDist = gDist;
                }
            }
        }

        return closestGhostType;
    }

    public static int[] searchNotEdibleGhostNodes(Game game, int searchDistance) {
        int pNode = game.getPacmanCurrentNodeIndex();
        List<Integer> ghostNodes = new ArrayList<>();

        for (GHOST ghostType : GHOST.values()) {
            if (game.getGhostLairTime(ghostType) == 0 && !game.isGhostEdible(ghostType)) {
                int gNode = game.getGhostCurrentNodeIndex(ghostType);
                int gDist = game.getShortestPathDistance(pNode, gNode);

                if (gDist <= searchDistance)
                    ghostNodes.add(gNode);
            }
        }

        return ghostNodes.stream().mapToInt(Integer::intValue).toArray();
    }

    public static int getClosestPowerPill(Game game) {
        int pNode = game.getPacmanCurrentNodeIndex();
        int pPillNode = Integer.MAX_VALUE;
        int pPillMinDist = Integer.MAX_VALUE;

        for (int pPillIndice : game.getActivePowerPillsIndices()) {
            int pPillIndiceDist = game.getManhattanDistance(pNode, pPillIndice);

            if (pPillIndiceDist < pPillMinDist) {
                pPillNode = pPillIndice;
                pPillMinDist = pPillIndiceDist;
            }
        }

        return pPillNode;
    }

    public static int getClosestPacDot(Game game) throws Exception {
        int pNode = game.getPacmanCurrentNodeIndex();
        int[] pills = game.getActivePillsIndices();

        if (pills.length == 0)
            throw new Exception();

        int closestPill = Integer.MAX_VALUE;
        int shortestPillDist = Integer.MAX_VALUE;

        for (int pill : pills) {
            int pillDist = game.getShortestPathDistance(pNode, pill);

            if (pillDist < shortestPillDist) {
                closestPill = pill;
                shortestPillDist = pillDist;
            }
        }

        return closestPill;
    }

    public static int[] getPillCountByMove(Game game) {
        int pNode = game.getPacmanCurrentNodeIndex();
        int[] pills = game.getActivePillsIndices();
        int[] pillCount = new int[4];

        for (int pill : pills) {
            switch (game.getNextMoveTowardsTarget(pNode, pill, PATH)) {
                case UP:
                    pillCount[0]++;
                    break;
                case RIGHT:
                    pillCount[1]++;
                    break;
                case DOWN:
                    pillCount[2]++;
                    break;
                case LEFT:
                    pillCount[3]++;
                    break;
                case NEUTRAL:
                    break;
            }
        }

        return pillCount;
    }

    public static int[] countReachablePowerPillByMove(Game game) {
        int pNode = game.getPacmanCurrentNodeIndex();
        int[] powerPills = game.getActivePowerPillsIndices();
        int[] count = new int[4];

        for (int powerPill : powerPills) {
            switch (game.getNextMoveTowardsTarget(pNode, powerPill, PATH)) {
                case UP:
                    count[0]++;
                    break;
                case RIGHT:
                    count[1]++;
                    break;
                case DOWN:
                    count[2]++;
                    break;
                case LEFT:
                    count[3]++;
                    break;
                case NEUTRAL:
                    break;
            }
        }

        return count;
    }

    public static int[] countReachableGhostByMove(Game game) {
        int pNode = game.getPacmanCurrentNodeIndex();
        int[] count = new int[4];

        for (GHOST ghost : GHOST.values()) {
            int[] shortestPath = game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), pNode, game.getGhostLastMoveMade(ghost));

            if (shortestPath.length == 0)
                continue;

            MOVE nextMove = shortestPath.length == 1 ?
                    game.getNextMoveTowardsTarget(pNode, game.getGhostCurrentNodeIndex(ghost), PATH) :
                    game.getNextMoveTowardsTarget(pNode, shortestPath[shortestPath.length - 2], PATH);

            switch (nextMove) {
                case UP:
                    count[0]++;
                    break;
                case RIGHT:
                    count[1]++;
                    break;
                case DOWN:
                    count[2]++;
                    break;
                case LEFT:
                    count[3]++;
                    break;
                case NEUTRAL:
                    break;
            }
        }

        return count;
    }

    public static MOVE getNextMoveTowardsGhost(Game game, GHOST ghost) {
        int pNode = game.getPacmanCurrentNodeIndex();
        int[] shortestPath = game.isGhostEdible(ghost) ?
                game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), pNode) :
                game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), pNode, game.getGhostLastMoveMade(ghost));

        if (shortestPath.length == 0)
            return NEUTRAL;

        return shortestPath.length == 1 ?
                game.getNextMoveTowardsTarget(pNode, game.getGhostCurrentNodeIndex(ghost), PATH) :
                game.getNextMoveTowardsTarget(pNode, shortestPath[shortestPath.length - 2], PATH);
    }
}
