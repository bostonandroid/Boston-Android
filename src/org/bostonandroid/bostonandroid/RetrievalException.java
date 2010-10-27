package org.bostonandroid.bostonandroid;

public class RetrievalException extends Exception {
  private static final long serialVersionUID = -1121551120933051634L;

  public RetrievalException(Throwable e) {
    super(e);
  }

  public RetrievalException(String message, Throwable e) {
    super(message, e);
  }
}
