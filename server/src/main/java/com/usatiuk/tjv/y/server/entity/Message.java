package com.usatiuk.tjv.y.server.entity;

import jakarta.persistence.*;
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
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Chat chat;

    @ManyToOne
    private Person author;

    @CreationTimestamp
    private Instant createdAt;

    @Lob
    @NotBlank
    private String contents;
}
