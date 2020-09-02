package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.entries.pacman.artificialNeuralNetwork.Entry;
import pacman.entries.pacman.artificialNeuralNetwork.KeyValuePair;
import pacman.entries.pacman.artificialNeuralNetwork.NeuralNet;
import pacman.entries.pacman.behaviorTree.GameUtils;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static pacman.game.Constants.GHOST.*;
import static pacman.game.Constants.MOVE.*;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacManANN extends Controller<MOVE> {
    public static final String FILE_PATH = "C:\\Users\\Administrator\\Documents\\Malmö universitet\\Artificiell intelligens för digitala spel\\HT2018-DA308A-TS981\\Ms_Pacman\\myData\\customTrainingData.txt";
    public static NeuralNet myNeuralNet;
    public static boolean saveNeuralNet;
    public static boolean trainingMode;

    private LinkedHashMap<float[], KeyValuePair<float[], MOVE>> history;

    public MyPacManANN() {
        setUpArtificialNeuralNetwork();
    }

    public void setUpArtificialNeuralNetwork() {
        try {
            myNeuralNet = new NeuralNet(28, 4, 1, 16);
            history = new LinkedHashMap<>();

            if (new File(FILE_PATH).exists())
                myNeuralNet.readFromFile(FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reinforceArtificialNeuralNetwork(float reward) {
        ArrayList<float[]> inputs = new ArrayList<>(history.keySet());
        Collections.reverse(inputs);

        for (int i = 0; i < inputs.size(); i++) {
            float[] input = inputs.get(i);
            KeyValuePair<float[], MOVE> keyValue = history.get(input);
            float[] output = keyValue.getKey();

            switch (keyValue.getValue()) {
                case UP:
                    output[0] *= reward;
                    break;
                case RIGHT:
                    output[1] *= reward;
                    break;
                case DOWN:
                    output[2] *= reward;
                    break;
                case LEFT:
                    output[3] *= reward;
                    break;
                case NEUTRAL:
                    break;
            }

            myNeuralNet.train(input, output);
        }
    }

    private MOVE getHighestMoveOutput(Game game, float[] out) {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(out[0], UP));
        entries.add(new Entry(out[1], RIGHT));
        entries.add(new Entry(out[2], DOWN));
        entries.add(new Entry(out[3], LEFT));
        Collections.sort(entries);

        List<MOVE> possibleMoves = Arrays.asList(game.getPossibleMoves(game.getPacmanCurrentNodeIndex()));

        for (int i = 0; i < entries.size(); i++) {
            MOVE move = entries.get(i).getValue();

            if (possibleMoves.contains(move))
                return move;
        }

        return NEUTRAL;
    }

    public MOVE getMove(Game game, long timeDue) {
        MOVE nextMoveTowardsBlinky = GameUtils.getNextMoveTowardsGhost(game, BLINKY);
        MOVE nextMoveTowardsPinky = GameUtils.getNextMoveTowardsGhost(game, PINKY);
        MOVE nextMoveTowardsInky = GameUtils.getNextMoveTowardsGhost(game, INKY);
        MOVE nextMoveTowardsSue = GameUtils.getNextMoveTowardsGhost(game, SUE);
        int[] pillCount = GameUtils.getPillCountByMove(game);

        float[] in = new float[]{
                game.isGhostEdible(BLINKY) ? 1 : 0,
                game.isGhostEdible(PINKY) ? 1 : 0,
                game.isGhostEdible(INKY) ? 1 : 0,
                game.isGhostEdible(SUE) ? 1 : 0,
                game.getShortestPathDistance(game.getGhostCurrentNodeIndex(BLINKY), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(BLINKY)) / (float) game.getNumberOfNodes(),
                game.getShortestPathDistance(game.getGhostCurrentNodeIndex(PINKY), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(PINKY)) / (float) game.getNumberOfNodes(),
                game.getShortestPathDistance(game.getGhostCurrentNodeIndex(INKY), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(INKY)) / (float) game.getNumberOfNodes(),
                game.getShortestPathDistance(game.getGhostCurrentNodeIndex(SUE), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(SUE)) / (float) game.getNumberOfNodes(),
                nextMoveTowardsBlinky == MOVE.UP ? 1 : 0,
                nextMoveTowardsBlinky == MOVE.RIGHT ? 1 : 0,
                nextMoveTowardsBlinky == MOVE.DOWN ? 1 : 0,
                nextMoveTowardsBlinky == MOVE.LEFT ? 1 : 0,
                nextMoveTowardsPinky == MOVE.UP ? 1 : 0,
                nextMoveTowardsPinky == MOVE.RIGHT ? 1 : 0,
                nextMoveTowardsPinky == MOVE.DOWN ? 1 : 0,
                nextMoveTowardsPinky == MOVE.LEFT ? 1 : 0,
                nextMoveTowardsInky == MOVE.UP ? 1 : 0,
                nextMoveTowardsInky == MOVE.RIGHT ? 1 : 0,
                nextMoveTowardsInky == MOVE.DOWN ? 1 : 0,
                nextMoveTowardsInky == MOVE.LEFT ? 1 : 0,
                nextMoveTowardsSue == MOVE.UP ? 1 : 0,
                nextMoveTowardsSue == MOVE.RIGHT ? 1 : 0,
                nextMoveTowardsSue == MOVE.DOWN ? 1 : 0,
                nextMoveTowardsSue == MOVE.LEFT ? 1 : 0,
                pillCount[0] / (float) game.getNumberOfActivePills(),   /* MOVE.UP */
                pillCount[1] / (float) game.getNumberOfActivePills(),   /* MOVE.RIGHT */
                pillCount[2] / (float) game.getNumberOfActivePills(),   /* MOVE.DOWN */
                pillCount[3] / (float) game.getNumberOfActivePills()    /* MOVE.LEFT */
        };

        float[] out = myNeuralNet.getOutput(in);
        MOVE nextMove = getHighestMoveOutput(game, out);
        history.put(in, new KeyValuePair<>(out, nextMove));
        return nextMove;
    }
}