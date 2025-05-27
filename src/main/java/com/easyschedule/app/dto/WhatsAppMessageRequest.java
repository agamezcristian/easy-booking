package com.easyschedule.app.dto;

import lombok.Data;

@Data
public class WhatsAppMessageRequest {
    public Entry[] entry;

    public static class Entry {
        public Change[] changes;
    }

    public static class Change {
        public Value value;
    }

    public static class Value {
        public Message[] messages;
        public Contact[] contacts;
    }

    public static class Message {
        public String from;
        public String id;
        public String timestamp;
        public Text text;
        public String type;
    }

    public static class Contact {
        public Profile profile;
        public String wa_id;
    }

    public static class Profile {
        public String name;
    }

    public static class Text {
        public String body;
    }
}
