package org.example.handlers;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.example.codecs.CustomMessage;
import org.example.db.SimpleDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(VertxExtension.class)
public class GetNoteListHandlerTest {

  private SimpleDatabase db;
  private GetNoteListHandler handler;

  @BeforeEach
  void setUp(Vertx vertx) throws Exception {
    db = mock(SimpleDatabase.class);
    handler = new GetNoteListHandler(db);
  }

  @Test
  void handle_shouldReturnNoteList(VertxTestContext testContext) {
    JsonObject expectedNotes = new JsonObject().put("1", "Test note content");

    when(db.get("notes")).thenReturn(expectedNotes.toString());

    Message<CustomMessage> message = mock(Message.class);
    when(message.body()).thenReturn(new CustomMessage(999));

    handler.handle(message);


    verify(message, times(1)).reply(expectedNotes);
    testContext.completeNow();
  }
}
