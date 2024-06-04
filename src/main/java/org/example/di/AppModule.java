package org.example.di;

import dagger.Module;
import dagger.Provides;
import io.vertx.core.Vertx;
import javax.inject.Named;
import org.example.db.SimpleDatabase;
import org.example.handlers.CreateNoteHandler;
import org.example.handlers.GetNoteListHandler;

import javax.inject.Singleton;

@Module
public class AppModule {

  private final Vertx vertx;

  public AppModule(Vertx vertx) {
    this.vertx = vertx;
  }

  @Provides
  @Singleton
  Vertx provideVertx() {
    return Vertx.vertx();
  }

  @Provides
  @Singleton
  String provideFilePath() {
    return "notes.txt";
  }
//
//  @Provides
//  @Singleton
//  SimpleDatabase provideSimpleDatabase(Vertx vertx) {
//    return new SimpleDatabase(vertx, "notes.txt");
//  }
//
//  @Provides
//  @Singleton
//  CreateNoteHandler provideCreateNoteHandler(SimpleDatabase db) {
//    return new CreateNoteHandler(db);
//  }
//
//  @Provides
//  @Singleton
//  GetNoteListHandler provideGetNoteListHandler(SimpleDatabase db) {
//    return new GetNoteListHandler(db);
//  }
}

