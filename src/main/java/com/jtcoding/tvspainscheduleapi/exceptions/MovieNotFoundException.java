package com.jtcoding.tvspainscheduleapi.exceptions;

public class MovieNotFoundException extends Exception {
  public MovieNotFoundException(long id) {
    super("Movie " + id + " not found");
  }
}
