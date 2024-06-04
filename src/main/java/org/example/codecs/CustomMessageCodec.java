package org.example.codecs;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

public class CustomMessageCodec implements MessageCodec<CustomMessage, CustomMessage> {

  @Override
  public void encodeToWire(Buffer buffer, CustomMessage customMessage) {
    JsonObject jsonToEncode = new JsonObject();
    jsonToEncode.put("statusCode", customMessage.getStatusCode());

    String jsonStr = jsonToEncode.encode();
    buffer.appendInt(jsonStr.getBytes().length);
    buffer.appendBytes(jsonStr.getBytes());
  }

  @Override
  public CustomMessage decodeFromWire(int pos, Buffer buffer) {
    int length = buffer.getInt(pos);

    String jsonStr = buffer.getString(pos + 4, pos + length);
    JsonObject contentJson = new JsonObject(jsonStr);

    int statusCode = contentJson.getInteger("statusCode");

    return new CustomMessage(statusCode);
  }

  @Override
  public CustomMessage transform(CustomMessage testObject) {
    return testObject;
  }

  @Override
  public String name() {
    return "CustomMessageCodec";
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }
}
