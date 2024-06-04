package org.example.handlers;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import javax.inject.Inject;
import org.example.db.SimpleDatabase;
import org.example.models.Note;

public class CreateNoteHandler implements Handler<Message<JsonObject>> {


  public CreateNoteHandler() {
  }

  SimpleDatabase db;

  @Inject
  public CreateNoteHandler(SimpleDatabase db) {
    this.db = db;
  }

  @Override
  public void handle(Message<JsonObject> message) {
    String id = message.body().getString("id");
    String content = message.body().getString("content");
    if (id == null || content == null) {
      message.fail(400, "Invalid note");
      return;
    }
    Note note = new Note(id, content);
    String currentNoteList = db.get("notes");
    JsonObject noteList;
    if (currentNoteList == null) {
      noteList = new JsonObject();
    } else {
      noteList = new JsonObject(currentNoteList);
    }
    db.set("notes", Note.addToJson(noteList, note).toString());
    db.save();
    message.reply(note.toJson());
  }
}
