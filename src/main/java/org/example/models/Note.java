package org.example.models;

import io.vertx.core.json.JsonObject;

public class Note {
  private String id;
  private String content;

  public static JsonObject addToJson(JsonObject object, Note note) {
    return object.put(note.id, note.content);
  }

  public Note(String id, String content) {
    this.id = id;
    this.content = content;
  }

  public Note(JsonObject jsonObject) {
    this.id = jsonObject.getString("id");
    this.content = jsonObject.getString("content");
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public JsonObject toJson() {
    return new JsonObject().put("content", content).put("id", id);
  }

  @Override
  public String toString() {
    return content;
  }
}
