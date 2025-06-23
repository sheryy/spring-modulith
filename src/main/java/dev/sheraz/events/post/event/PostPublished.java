package dev.sheraz.events.post.event;

/**
 * Domain event published when a post is published.
 * This event is handled by the Archiver module to automatically archive the post.
 */
public record PostPublished(Integer id) {

}
