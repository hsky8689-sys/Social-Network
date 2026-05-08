package repository;
import service.Network;
import java.util.List;
public interface EventRepoActions<T,E> {
    public boolean gaseste(T o);
    public boolean adauga(T o);
    public List<T> utilizatori();
    public boolean inscrieParticipant(T o,E p);
    public boolean retrageParticipant(T o,E p);
    public boolean aboneaza(T o,E p);
    public boolean dezaboneaza(T o,E p);
    public List<E> gasesteMembrii(T o, Network network);
}
