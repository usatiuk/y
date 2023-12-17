package com.usatiuk.tjv.y.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Message implements EntityWithId<Long> {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Chat chat;
    
    @ManyToOne
    private Person author;

    @Lob
    @NotBlank
    private String contents;
}
