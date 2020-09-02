package pacman.entries.pacman.behaviorTree.decorators;

import pacman.entries.pacman.behaviorTree.Task;

public abstract class DecoratorTask implements Task {
    protected Task child;

    public DecoratorTask(Task child) {
        this.child = child;
    }
}
