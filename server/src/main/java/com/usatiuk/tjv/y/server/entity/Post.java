package com.usatiuk.tjv.y.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class Post implements EntityWithId<Long> {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Person author;

    @Lob
    private String text;

    @Override
    public Long getId() {
        return id;
    }
}
