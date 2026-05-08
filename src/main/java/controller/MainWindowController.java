package controller;

import domain.Event;
import domain.Friendship;
import domain.Message;
import domain.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import observer.CRUDActions;
import observer.ObserverGUI;
import service.Network;

import java.util.Date;
import java.util.List;

public class MainWindowController implements ObserverGUI {

    private final Network network;
    private final Stage stage;
    private BorderPane root;

    private TabPane tabPane;
    private Tab chatTab, prieteniTab, evenimenteTab, notificariTab;

    
    private Runnable refreshMessages;      
    private Runnable refreshConversations; 
    private Runnable refreshFriends;       
    private Runnable refreshEvents;        
    private Runnable refreshNotifications; 

 
    private Long openConversationWithId = null;
    private User<Long> logged;

    public MainWindowController(Network network, Stage stage,User<Long> logged) {
        this.network = network;
        this.stage = stage;
        this.logged = logged;
        buildView();
        registerAsObserver();
    }

    private void registerAsObserver() {
        network.getAdminMesaje().addObserver(this);
        network.getAdminPrietenii().addObserver(this);
        network.getAdminEvents().addObserver(this);
        stage.setOnCloseRequest(e -> {
            network.getAdminMesaje().removeObserver(this);
            network.getAdminPrietenii().removeObserver(this);
            network.getAdminEvents().removeObserver(this);
            network.tryLogout();
        });
    }


    @Override
    public void update(CRUDActions action, Object payload) {
        switch (action) {

            case MESSAGE_RECEIVED -> {
                Long senderId = (Long) payload;
                Long myId = logged.getId();

                if (senderId.equals(myId)) return;

                if (isChatTabActive()) {
                    if (senderId.equals(openConversationWithId)) {
                        if (refreshMessages != null) Platform.runLater(refreshMessages);
                    } else {
                        
                        if (refreshConversations != null) Platform.runLater(refreshConversations);
                    }
                }
                
                if (refreshNotifications != null) Platform.runLater(refreshNotifications);
            }

            case FRIEND_ADDED, FRIEND_REMOVED -> {
                if (isPrieteniTabActive()) {
                    if (refreshFriends != null) Platform.runLater(refreshFriends);
                }
            }

            case EVENT_ADDED, EVENT_REMOVED -> {
                if (isEvenimenteTabActive()) {
                    if (refreshEvents != null) Platform.runLater(refreshEvents);
                }
            
                if (refreshNotifications != null) Platform.runLater(refreshNotifications);
            }

            case add, delete -> {

            }
        }
    }


    private boolean isChatTabActive() {
        return tabPane != null && tabPane.getSelectionModel().getSelectedItem() == chatTab;
    }

    private boolean isPrieteniTabActive() {
        return tabPane != null && tabPane.getSelectionModel().getSelectedItem() == prieteniTab;
    }

    private boolean isEvenimenteTabActive() {
        return tabPane != null && tabPane.getSelectionModel().getSelectedItem() == evenimenteTab;
    }


    private void buildView() {
        root = new BorderPane();

        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(8, 12, 8, 12));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #e8e8e8; -fx-border-color: #ccc; -fx-border-width: 0 0 1 0;");

        Label userInfo = new Label("Logat ca: " + logged.getUsername());
        userInfo.setStyle("-fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> stage.close());
        topBar.getChildren().addAll(userInfo, spacer, logoutBtn);
        root.setTop(topBar);

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        chatTab = new Tab("Chat",buildChatPanel());
        prieteniTab = new Tab("Prieteni",buildPrieteniPanel());
        evenimenteTab = new Tab("Evenimente",buildEvenimentePanel());
        notificariTab = new Tab("Notificari",buildNotificariPanel());

        tabPane.getTabs().addAll(chatTab, prieteniTab, evenimenteTab, notificariTab);
        root.setCenter(tabPane);
    }

    private Parent buildChatPanel() {
        SplitPane split = new SplitPane();

        VBox leftPane = new VBox(8);
        leftPane.setPadding(new Insets(10));
        Label convLabel = new Label("Conversatii");
        convLabel.setStyle("-fx-font-weight: bold;");

        ListView<String> userListView = new ListView<>();
        ObservableList<String> userItems = FXCollections.observableArrayList();
        userListView.setItems(userItems);
        VBox.setVgrow(userListView, Priority.ALWAYS);

        TextField newConvField = new TextField();
        newConvField.setPromptText("ID user nou...");
        Button startConvBtn = new Button("Deschide");
        leftPane.getChildren().addAll(convLabel, userListView, newConvField, startConvBtn);

        VBox rightPane = new VBox(8);
        rightPane.setPadding(new Insets(10));
        Label chatWithLabel = new Label("Selectati o conversatie");
        chatWithLabel.setStyle("-fx-font-weight: bold;");

        ListView<String> messagesView = new ListView<>();
        ObservableList<String> messageItems = FXCollections.observableArrayList();
        messagesView.setItems(messageItems);
        VBox.setVgrow(messagesView, Priority.ALWAYS);

        HBox sendBar = new HBox(8);
        TextField msgField = new TextField();
        msgField.setPromptText("Scrie mesaj...");
        HBox.setHgrow(msgField, Priority.ALWAYS);
        Button sendBtn = new Button("Trimite");
        sendBar.getChildren().addAll(msgField, sendBtn);
        rightPane.getChildren().addAll(chatWithLabel, messagesView, sendBar);

        refreshConversations = () -> {
            String sel = userListView.getSelectionModel().getSelectedItem();
            userItems.clear();
            try {
                List<Message> msgs = network.getAdminMesaje().gasesteDupaMembru(logged);
                if (msgs == null) return;
                Long myId = logged.getId();
                msgs.stream()
                        .map(m -> m.getSender().equals(myId) ? m.getReciever() : m.getSender())
                        .distinct()
                        .forEach(uid -> {
                            User other = network.getAdminPersoane().getById(uid);
                            if (other == null) other = network.getAdminRate().getById(uid);
                            String label = uid + " - " + (other != null ? other.getUsername() : "?");
                            if (!userItems.contains(label)) userItems.add(label);
                        });
            } catch (Exception e) { showError("Eroare conversatii: " + e.getMessage()); }
            if (sel != null && userItems.contains(sel))
                userListView.getSelectionModel().select(sel);
        };

        refreshMessages = () -> {
            if (openConversationWithId == null) return;
            messageItems.clear();
            try {
                Long myId = logged.getId();
                Long otherId = openConversationWithId;
                List<Message> msgs = network.getAdminMesaje().gasesteDupaMembru(logged);
                if (msgs == null) return;
                msgs.stream()
                        .filter(m -> (m.getSender().equals(myId) && m.getReciever().equals(otherId))
                                  || (m.getSender().equals(otherId) && m.getReciever().equals(myId)))
                        .sorted((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()))
                        .forEach(m -> {
                            String who = m.getSender().equals(myId) ? "Tu" : "El/Ea";
                            messageItems.add("[" + who + "] " + m.getContent());
                        });
            } catch (Exception e) { showError("Eroare mesaje: " + e.getMessage()); }
        };

        refreshConversations.run();

        startConvBtn.setOnAction(e -> {
            String idStr = newConvField.getText().trim();
            try {
                Long uid = Long.parseLong(idStr);
                User other = network.getAdminPersoane().getById(uid);
                if (other == null) other = network.getAdminRate().getById(uid);
                if (other == null) { showError("Nu exista user cu ID " + uid); return; }
                String label = uid + " - " + other.getUsername();
                if (!userItems.contains(label)) userItems.add(label);
                userListView.getSelectionModel().select(label);
            } catch (NumberFormatException ex) { showError("ID invalid"); }
        });

        userListView.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel == null) return;
            Long uid = Long.parseLong(sel.split(" - ")[0]);
            openConversationWithId = uid;
            User other = network.getAdminPersoane().getById(uid);
            if (other == null) other = network.getAdminRate().getById(uid);
            chatWithLabel.setText("Chat cu: " + (other != null ? other.getUsername() : uid));
            refreshMessages.run();
        });

        sendBtn.setOnAction(e -> {
            if (openConversationWithId == null) { showError("Selectati o conversatie."); return; }
            String txt = msgField.getText().trim();
            if (txt.isEmpty()) return;
            Long myId = logged.getId();
            if (network.getAdminMesaje().adauga(new Message(0L, myId, openConversationWithId, txt, new Date()))) {
                messageItems.add("[Tu] " + txt);
                msgField.clear();
            } else {
                showError("Nu s-a putut trimite mesajul.");
            }
        });
        msgField.setOnAction(e -> sendBtn.fire());

        split.getItems().addAll(leftPane, rightPane);
        split.setDividerPositions(0.3);
        return split;
    }


    private Parent buildPrieteniPanel() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(12));

        HBox searchBar = new HBox(8);
        TextField searchField = new TextField();
        searchField.setPromptText("Cauta dupa username...");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        Button searchBtn = new Button("Cauta");
        searchBar.getChildren().addAll(searchField, searchBtn);

        Label searchResultLabel = new Label();
        Button addFriendBtn = new Button("Adauga prieten");
        addFriendBtn.setVisible(false);
        final User[] foundUser = {null};

        searchBtn.setOnAction(e -> {
            String username = searchField.getText().trim();
            if (username.isEmpty()) return;
            User gasit = network.getAdminPersoane().getByUsername(username);
            if (gasit == null) gasit = network.getAdminRate().getByUsername(username);
            if (gasit == null) {
                searchResultLabel.setText("Nu s-a gasit: \"" + username + "\"");
                addFriendBtn.setVisible(false); foundUser[0] = null;
            } else {
                foundUser[0] = gasit;
                searchResultLabel.setText("Gasit: " + gasit.getUsername() + " (ID: " + gasit.getId() + ")");
                addFriendBtn.setVisible(true);
            }
        });

        addFriendBtn.setOnAction(e -> {
            if (foundUser[0] == null) return;
            Long myId = (Long) network.getLogged_in().getId();
            Long otherId = (Long) foundUser[0].getId();
            Friendship fr = new Friendship(myId, otherId);
            if (network.getAdminPrietenii().contine(fr)) {
                searchResultLabel.setText("Esti deja prieten cu " + foundUser[0].getUsername());
            } else {
                boolean ok = network.getAdminPrietenii().adauga(fr);
                searchResultLabel.setText(ok ? "Prieten adaugat!" : "Nu s-a putut adauga.");
            }
        });

        Label friendsLabel = new Label("Prietenii mei:");
        friendsLabel.setStyle("-fx-font-weight: bold;");

        ListView<String> friendsListView = new ListView<>();
        ObservableList<String> friendsItems = FXCollections.observableArrayList();
        friendsListView.setItems(friendsItems);
        VBox.setVgrow(friendsListView, Priority.ALWAYS);

        Label actionLabel = new Label();

        refreshFriends = () -> {
            friendsItems.clear();
            try {
                Long myId = (Long) network.getLogged_in().getId();
                List<Friendship> all = network.getAdminPrietenii().utilizatori();
                if (all == null) return;
                all.stream()
                        .filter(f -> f.getFirst().equals(myId) || f.getSecond().equals(myId))
                        .forEach(f -> {
                            Long otherId = f.getFirst().equals(myId) ? f.getSecond() : f.getFirst();
                            User other = network.getAdminPersoane().getById(otherId);
                            if (other == null) other = network.getAdminRate().getById(otherId);
                            friendsItems.add(otherId + " - " + (other != null ? other.getUsername() : "?"));
                        });
            } catch (Exception ex) { showError("Eroare: " + ex.getMessage()); }
        };
        refreshFriends.run();

        Button removeFriendBtn = new Button("Sterge prieten selectat");
        Button refreshBtn = new Button("Reincarca");
        refreshBtn.setOnAction(e -> { refreshFriends.run(); actionLabel.setText(""); });

        removeFriendBtn.setOnAction(e -> {
            String sel = friendsListView.getSelectionModel().getSelectedItem();
            if (sel == null) { actionLabel.setText("Selectati un prieten."); return; }
            Long otherId = Long.parseLong(sel.split(" - ")[0]);
            Long myId = (Long) network.getLogged_in().getId();
            boolean ok = network.getAdminPrietenii().sterge(new Friendship(myId, otherId));
            actionLabel.setText(ok ? "Prietenie stearsa." : "Eroare la stergere.");
        });

        pane.getChildren().addAll(
                new Label("Cauta utilizatori:"), searchBar, searchResultLabel, addFriendBtn,
                new Separator(), friendsLabel, friendsListView,
                new HBox(8, removeFriendBtn, refreshBtn), actionLabel);
        return pane;
    }

    private Parent buildEvenimentePanel() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(12));

        Label createLabel = new Label("Creeaza eveniment nou:");
        createLabel.setStyle("-fx-font-weight: bold;");
        HBox createBar = new HBox(8);
        TextField eventNameField = new TextField();
        eventNameField.setPromptText("Numele evenimentului...");
        HBox.setHgrow(eventNameField, Priority.ALWAYS);
        Button createBtn = new Button("Creeaza");
        createBar.getChildren().addAll(eventNameField, createBtn);
        Label createStatus = new Label();

        Label listLabel = new Label("Toate evenimentele:");
        listLabel.setStyle("-fx-font-weight: bold;");
        ListView<String> eventsListView = new ListView<>();
        ObservableList<String> eventsItems = FXCollections.observableArrayList();
        eventsListView.setItems(eventsItems);
        VBox.setVgrow(eventsListView, Priority.ALWAYS);

        refreshEvents = () -> {
            eventsItems.clear();
            List<Event> events = network.getAdminEvents().utilizatori();
            if (events == null) return;
            events.forEach(ev -> eventsItems.add("ID:" + ev.getId() + " | " + ev.getNume()));
        };
        refreshEvents.run();

        createBtn.setOnAction(e -> {
            String name = eventNameField.getText().trim();
            if (name.isEmpty()) { createStatus.setText("Introduceti un nume."); return; }
            String erori = network.getAdminEvents().valideaza(new String[]{name});
            if (!erori.isEmpty()) { createStatus.setText(erori); return; }
            Long myId = logged.getId();
            boolean ok = network.getAdminEvents().adauga(new Event(0L, name, myId));
            createStatus.setText(ok ? "Eveniment creat!" : "Eroare la creare.");
            eventNameField.clear();
        });

        Label subLabel = new Label("Aboneaza-te / Dezaboneaza-te:");
        subLabel.setStyle("-fx-font-weight: bold;");
        Label subStatus = new Label();
        Button subscribeBtn   = new Button("Aboneaza-te");
        Button unsubscribeBtn = new Button("Dezaboneaza-te");
        Button refreshEventsBtn = new Button("Reincarca");
        HBox subBar = new HBox(8, subscribeBtn, unsubscribeBtn, refreshEventsBtn);

        subscribeBtn.setOnAction(e -> {
            String sel = eventsListView.getSelectionModel().getSelectedItem();
            if (sel == null) { subStatus.setText("Selectati un eveniment."); return; }
            Long evId = Long.parseLong(sel.split("\\|")[0].replace("ID:", "").trim());
            Event ev = network.getAdminEvents().getById(evId);
            if (ev == null) { subStatus.setText("Eveniment negasit."); return; }
            boolean ok = network.getAdminEvents().aboneaza(ev, network.getLogged_in());
            subStatus.setText(ok ? "Abonat!" : "Deja abonat sau eroare.");
        });

        unsubscribeBtn.setOnAction(e -> {
            String sel = eventsListView.getSelectionModel().getSelectedItem();
            if (sel == null) { subStatus.setText("Selectati un eveniment."); return; }
            Long evId = Long.parseLong(sel.split("\\|")[0].replace("ID:", "").trim());
            Event ev = network.getAdminEvents().getById(evId);
            if (ev == null) { subStatus.setText("Eveniment negasit."); return; }
            boolean ok = network.getAdminEvents().dezaboneaza(ev, network.getLogged_in());
            subStatus.setText(ok ? "Dezabonat!" : "Nu erai abonat sau eroare.");
        });

        refreshEventsBtn.setOnAction(e -> refreshEvents.run());

        pane.getChildren().addAll(
                createLabel, createBar, createStatus,
                new Separator(), listLabel, eventsListView,
                subLabel, subBar, subStatus);
        return pane;
    }

    private Parent buildNotificariPanel() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(12));

        Label title = new Label("Notificari");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        ListView<String> notifListView = new ListView<>();
        ObservableList<String> notifItems = FXCollections.observableArrayList();
        notifListView.setItems(notifItems);
        VBox.setVgrow(notifListView, Priority.ALWAYS);

        Button refreshBtn = new Button("Reincarca");

        refreshNotifications = () -> {
            notifItems.clear();
            try {
                Long myId = (Long) network.getLogged_in().getId();
                List<Message> msgs = network.getAdminMesaje().gasesteDupaMembru(network.getLogged_in());
                if (msgs != null) {
                    msgs.stream()
                            .filter(m -> m.getReciever().equals(myId))
                            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                            .forEach(m -> {
                                User sender = network.getAdminPersoane().getById(m.getSender());
                                if (sender == null) sender = network.getAdminRate().getById(m.getSender());
                                String name = sender != null ? sender.getUsername() : "ID:" + m.getSender();
                                notifItems.add("[" + m.getTimestamp() + "] De la " + name + ": " + m.getContent());
                            });
                }
                if (notifItems.isEmpty()) notifItems.add("Nu ai notificari.");
            } catch (Exception ex) { notifItems.add("Eroare: " + ex.getMessage()); }
        };
        refreshNotifications.run();

        refreshBtn.setOnAction(e -> refreshNotifications.run());

        Label eventsSubLabel = new Label("Evenimente la care esti abonat:");
        eventsSubLabel.setStyle("-fx-font-weight: bold;");

        ListView<String> myEventsView = new ListView<>();
        ObservableList<String> myEventsItems = FXCollections.observableArrayList();
        myEventsView.setItems(myEventsItems);
        myEventsView.setPrefHeight(150);

        Runnable loadMyEvents = () -> {
            myEventsItems.clear();
            List<Event> allEvents = network.getAdminEvents().utilizatori();
            if (allEvents == null) return;
            allEvents.forEach(ev -> {
                List<User> abonati = network.getAdminEvents().abonati(ev);
                if (abonati != null && abonati.stream().anyMatch(u -> u.getId().equals(network.getLogged_in().getId())))
                    myEventsItems.add(ev.getNume() + " (ID:" + ev.getId() + ")");
            });
            if (myEventsItems.isEmpty()) myEventsItems.add("Nu esti abonat la niciun eveniment.");
        };
        loadMyEvents.run();

        pane.getChildren().addAll(
                title, new Label("Mesaje primite:"), notifListView, refreshBtn,
                new Separator(), eventsSubLabel, myEventsView);
        return pane;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    public Parent getView() { return root; }
}
