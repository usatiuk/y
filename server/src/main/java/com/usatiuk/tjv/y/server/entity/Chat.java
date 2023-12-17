package com.usatiuk.tjv.y.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class Chat implements EntityWithId<Long> {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "chat")
    private Collection<Message> messages = new ArrayList<>();

    @ManyToMany(mappedBy = "chats")
    private Collection<Person> users;

}
