package domain;

import service.Network;

public interface Observer {
    boolean subscribe(User user);
    boolean unsubscribe(User user);
    boolean notifyAllSubscribers(Network network);
    void runEvent(Network network);
}
