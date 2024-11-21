package me.vifez.core.profile;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProfileOptions {

    private boolean frozen = false;

    private boolean receivingChatMessages = true;
    private boolean receivingPrivateMessages = true;
    private Set<UUID> ignored = new HashSet<>();

    private UUID replyTo;

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isReceivingChatMessages() {
        return receivingChatMessages;
    }

    public void setReceivingChatMessages(boolean receivingChatMessages) {
        this.receivingChatMessages = receivingChatMessages;
    }

    public boolean isReceivingPrivateMessages() {
        return receivingPrivateMessages;
    }

    public void setReceivingPrivateMessages(boolean receivingPrivateMessages) {
        this.receivingPrivateMessages = receivingPrivateMessages;
    }

    public Set<UUID> getIgnored() {
        return ignored;
    }

    public UUID getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(UUID replyTo) {
        this.replyTo = replyTo;
    }

}