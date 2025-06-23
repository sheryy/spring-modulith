package dev.sheraz.events.post.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Post entity representing a blog post in the system.
 * Demonstrates a domain entity with lifecycle states that trigger events.
 */
@Entity
@Table(name = "posts")
@Getter
@Setter
@Accessors(fluent = true, chain = true)
class Post {
    /**
     * Post lifecycle states that trigger different domain events.
     * CREATED → PUBLISHED → ARCHIVED
     */
    public enum Status {
        CREATED,
        PUBLISHED,
        ARCHIVED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    @SequenceGenerator(name = "post_seq", sequenceName = "post_id_seq", allocationSize = 10)
    private Integer id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private Status status = Status.CREATED;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
