package pacman.entries.pacman.behaviorTree.composites;

import pacman.entries.pacman.behaviorTree.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeTask implements Task {
    protected List<Task> children;

    public CompositeTask() {
        children = new ArrayList<>();
    }

    public void add(Task child) {
        children.add(child);
    }

}
