package org.example.codecs;

public class CustomMessage {
    private final int statusCode;

    public CustomMessage(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
