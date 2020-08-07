package com.epam.esm.dto;

public class View {
  private View(){}
  public static class Public { }
  public static class ExtendedPublic extends Public { }
  public static class Internal extends ExtendedPublic { }
}