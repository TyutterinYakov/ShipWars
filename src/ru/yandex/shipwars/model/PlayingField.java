package ru.yandex.shipwars.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlayingField {
    private long id;
    private final int SIZE_VERTICAL = 10;
    private final long userId;
    private final Map<LetterField, List<CellField>> fields = new LinkedHashMap<>();
    private int countShip;


    public PlayingField(long userId, int countShip) {
        this.userId = userId;
        this.countShip = countShip;
        for (LetterField letterField : LetterField.values()) {
            List<CellField> cells = new ArrayList<>(SIZE_VERTICAL);
            fields.put(letterField, cells);
            for(int i = 0; i < SIZE_VERTICAL; i++) {
                cells.add(new CellField(letterField.ordinal(), i));
            }
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSIZE_VERTICAL() {
        return SIZE_VERTICAL;
    }
    public int getSIZE_HORIZONTAL() {
        return LetterField.values().length;
    }

    public long getUserId() {
        return userId;
    }

    public Map<LetterField, List<CellField>> getFields() {
        return fields;
    }

    public int getCountShip() {
        return countShip;
    }

    public void setCountShip(int countShip) {
        this.countShip = countShip;
    }
}
