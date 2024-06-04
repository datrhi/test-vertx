package org.example.verticles;

import io.vertx.core.AbstractVerticle;
import javax.inject.Inject;
import org.example.handlers.CreateNoteHandler;
import org.example.handlers.GetNoteListHandler;
;

public class NoteVerticle extends AbstractVerticle {

  public static final String CREATE = "create-note";
  public static final String GETS = "get-notes";

  @Inject
  CreateNoteHandler createNoteHandler;

  @Inject
  GetNoteListHandler getNoteListHandler;



  @Override
  public void start() throws Exception {
    vertx.eventBus().consumer(CREATE, this.createNoteHandler);
    vertx.eventBus().consumer(GETS, this.getNoteListHandler);
  }

  @Override
  public void stop() throws Exception {
    super.stop();
  }

}
