package com.usatiuk.tjv.y.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

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

    @NotBlank
    private String text;

    @CreationTimestamp
    private Instant createdAt;

    @Override
    public Long getId() {
        return id;
    }
}
