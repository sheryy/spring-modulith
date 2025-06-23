package dev.sheraz.events.post.event;

/**
 * Domain event published when a post is created.
 * This event is handled by the Publisher module to automatically publish the post.
 */
public record PostCreated(Integer id) {

}
