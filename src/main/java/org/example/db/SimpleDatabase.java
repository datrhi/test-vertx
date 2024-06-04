package org.example.db;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import javax.inject.Inject;


public class SimpleDatabase {

  private final FileSystem fileSystem;
  private final String filePath;

  private JsonObject db;

  @Inject
  public SimpleDatabase(Vertx vertx, String filePath) {
    this.fileSystem = vertx.fileSystem();
    this.filePath = filePath;
    initDatabase();
  }

  private void initDatabase() {
    this.fileSystem.exists(filePath).compose(exist -> {
          if (!exist) {
            return this.fileSystem.createFile(filePath);
          }
          return Future.succeededFuture();
        }).compose(ar -> this.fileSystem.readFile(filePath))
        .onComplete(ar -> {
          if (ar.result().length() > 0) {
            db = new JsonObject(ar.result());
          } else {
            db = new JsonObject();
          }
        });
  }

  public void save() {
    this.fileSystem.writeFile(this.filePath, db.toBuffer());
  }

  public String get(String key) {
    return db.getString(key);
  }

  public void set(String key, Object value) {
    db.put(key, value);
  }

  public void remove(String key) {
    db.remove(key);
  }

}
