package observer;

/**
 * Interfata pe care o implementeaza orice controller de UI care vrea
 * sa fie notificat de servicii dupa operatii CRUD.
 *
 * @param action  tipul operatiei efectuate
 * @param payload date extra (ex: id-ul senderului pentru MESSAGE_RECEIVED) — poate fi null
 */
public interface ObserverGUI {
    void update(CRUDActions action, Object payload);
}
