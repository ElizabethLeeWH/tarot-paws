package com.tarotpaws.vttpminiproject.models;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Tarot {
    private String name;
    private Integer number;
    private String arcana;
    private String suit;
    private String meaningUp;
    private String meaningRev;
    private String desc;
    private String img;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarot tarot = (Tarot) o;
        return Objects.equals(name, tarot.name);
    }
}
