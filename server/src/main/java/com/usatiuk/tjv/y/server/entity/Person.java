package com.usatiuk.tjv.y.server.entity;

import jakarta.persistence.*;
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
public class Person implements EntityWithId<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(unique = true)
    private String username;

    private String fullName;
    private String password;

    @OneToMany(mappedBy = "author")
    private Collection<Post> posts = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_follows",
            joinColumns = @JoinColumn(name = "follower"),
            inverseJoinColumns = @JoinColumn(name = "followee"))
    private Collection<Person> following;

    @ManyToMany(mappedBy = "following")
    private Collection<Person> followers;

    @Override
    public String getId() {
        return uuid;
    }
}
