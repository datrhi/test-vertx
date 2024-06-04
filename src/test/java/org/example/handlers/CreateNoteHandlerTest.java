package org.example.handlers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.example.db.SimpleDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class CreateNoteHandlerTest {

  private SimpleDatabase db;
  private CreateNoteHandler handler;

  @BeforeEach
  void setUp(Vertx vertx) {
    db = mock(SimpleDatabase.class);
    handler = new CreateNoteHandler(db);
  }

  @Test
  void handle_shouldAddNoteToDatabase(VertxTestContext testContext) {
    JsonObject noteJson = new JsonObject()
        .put("id", "123")
        .put("content", "Test note content");

    Message<JsonObject> message = mock(Message.class);
    when(message.body()).thenReturn(noteJson);

    handler.handle(message);

    verify(db, times(1)).set("notes", "{\"123\":\"Test note content\"}");
    verify(db, times(1)).save();
    verify(message, times(1)).reply(noteJson);

    testContext.completeNow();
  }
}
