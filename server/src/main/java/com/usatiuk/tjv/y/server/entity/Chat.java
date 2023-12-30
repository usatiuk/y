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
public class Chat {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "chat", orphanRemoval = true)
    private Collection<Message> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "chat_person",
            joinColumns = @JoinColumn(name = "chat"),
            inverseJoinColumns = @JoinColumn(name = "person"))
    private Collection<Person> members = new ArrayList<>();

    @ManyToOne
    private Person creator;

}
