package com.bin.im.common.mini.json;

import java.io.Writer;

public abstract class WriterConfig {

  /**
   * Write JSON in its minimal form, without any additional whitespace. This is the default.
   */
  public static final WriterConfig MINIMAL = new WriterConfig() {
    @Override
    JsonWriter createWriter(Writer writer) {
      return new JsonWriter(writer);
    }
  };

  abstract JsonWriter createWriter(Writer writer);

}