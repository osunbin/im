package com.bin.im.common.mini.json;

public class ParseException extends RuntimeException {

  private final Location location;

  ParseException(String message, Location location) {
    super(message + " at " + location);
    this.location = location;
  }

  /**
   * Returns the location at which the error occurred.
   *
   * @return the error location
   */
  public Location getLocation() {
    return location;
  }

}