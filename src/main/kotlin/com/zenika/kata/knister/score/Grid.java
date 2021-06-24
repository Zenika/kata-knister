package com.zenika.kata.knister.score;

import com.zenika.kata.knister.room.GridPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Grid {
    List<List<Integer>> lines = List.of(
            new ArrayList<>(List.of(0, 0, 0, 0, 0)),
            new ArrayList<>(List.of(0, 0, 0, 0, 0)),
            new ArrayList<>(List.of(0, 0, 0, 0, 0)),
            new ArrayList<>(List.of(0, 0, 0, 0, 0)),
            new ArrayList<>(List.of(0, 0, 0, 0, 0))
    );

    public Integer score() {
        // TODO
        return null;
    }

    public void placeDices(GridPosition gridPosition, Integer score) {
        if (lines.get(gridPosition.getY()).get(gridPosition.getX()) != 0) {
            throw new IllegalArgumentException();
        }
        lines.get(gridPosition.getY()).set(gridPosition.getX(), score);
    }

    public Integer dicesPlaced() {
        return (int) lines.stream().mapToLong(Collection::size).sum();
    }

    public List<List<Integer>> getLines() {
        return lines;
    }

    public void setLines(List<List<Integer>> lines) {
        this.lines = lines;
    }
}
