package org.example.handlers;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

import io.vertx.core.json.JsonObject;
import javax.inject.Inject;
import org.example.codecs.CustomMessage;
import org.example.db.SimpleDatabase;

public class GetNoteListHandler implements Handler<Message<CustomMessage>> {
  SimpleDatabase db;
  @Inject
  public GetNoteListHandler(SimpleDatabase db) {
    this.db = db;
  }
  @Override
  public void handle(Message<CustomMessage> message) {
    String noteListStringify = db.get("notes");
    System.out.println(message.body().getStatusCode());
    JsonObject noteList;

    if (noteListStringify == null) {
      noteList = new JsonObject();
    } else {
      noteList = new JsonObject(noteListStringify);
    }
    message.reply(noteList);
  }
}
