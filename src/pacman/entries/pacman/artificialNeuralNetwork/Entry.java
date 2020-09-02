package pacman.entries.pacman.artificialNeuralNetwork;

import pacman.game.Constants.MOVE;

import java.util.AbstractMap;

public class Entry extends AbstractMap.SimpleEntry<Float, MOVE> implements Comparable<Entry> {
    public Entry(Float key, MOVE value) {
        super(key, value);
    }

    @Override
    public int compareTo(Entry o) {
        return o.getKey().compareTo(getKey());
    }
}
