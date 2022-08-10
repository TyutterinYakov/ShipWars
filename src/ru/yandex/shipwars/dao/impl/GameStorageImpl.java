package ru.yandex.shipwars.dao.impl;

import ru.yandex.shipwars.dao.GameStorage;
import ru.yandex.shipwars.model.PlayingField;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameStorageImpl implements GameStorage {
    private long generatorId = 1;
    private final Map<Long, PlayingField> fields = new LinkedHashMap<>();

    @Override
    public void createPlayingField(PlayingField playingField) {
        playingField.setId(generatorId++);
        fields.put(playingField.getId(), playingField);

    }

    @Override
    public PlayingField getPlayingFiled(long id) {
        return fields.get(id);
    }

    @Override
    public PlayingField getPlayingFieldByUserId(long id) {
        return fields.values().stream().filter((f) -> f.getUserId() == id).findFirst().get(); //TODO
    }

    @Override
    public void deleteAll() {
        generatorId = 1L;
        fields.clear();
    }

}
