package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messageStorage = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        storeCopy(msg);
    }

    private void storeCopy(Message msg) {
        this.messageStorage.put(msg.getId(), new Message(msg));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(this.messageStorage.get(id));
    }
}
