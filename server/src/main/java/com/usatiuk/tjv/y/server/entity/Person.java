package com.usatiuk.tjv.y.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Size(max = 100, message = "Username can't be longer than 100")
    @NotBlank(message = "Username can't be empty")
    @Column(unique = true)
    private String username;

    @Size(max = 100, message = "Name can't be longer than 100")
    @NotBlank(message = "Name can't be empty")
    private String fullName;

    @NotBlank(message = "Password can't be empty")
    private String password;

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private Collection<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "creator", orphanRemoval = true)
    private Collection<Chat> createdChats = new ArrayList<>();

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private Collection<Message> messages = new ArrayList<>();

    private boolean admin;

    @ManyToMany
    @JoinTable(name = "person_follows",
            joinColumns = @JoinColumn(name = "follower"),
            inverseJoinColumns = @JoinColumn(name = "followee"))
    private Collection<Person> following = new ArrayList<>();

    @ManyToMany(mappedBy = "following")
    private Collection<Person> followers = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    private Collection<Chat> chats = new ArrayList<>();

}
