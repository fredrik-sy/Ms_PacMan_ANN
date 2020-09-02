package pacman.entries.pacman.behaviorTree.actions;

import pacman.entries.pacman.behaviorTree.Task;

public abstract class ActionTask<T> implements Task {
    protected ActionListener<T> listener;

    public ActionTask(ActionListener<T> listener) {
        this.listener = listener;
    }
}
