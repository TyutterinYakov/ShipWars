package ru.yandex.shipwars.dao;

import ru.yandex.shipwars.model.PlayingField;

public interface GameStorage {
    void createPlayingField(PlayingField playingField);
    PlayingField getPlayingFiled(long id);
    PlayingField getPlayingFieldByUserId(long id);
}
