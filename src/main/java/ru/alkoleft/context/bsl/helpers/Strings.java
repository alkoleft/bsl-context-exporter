package ru.alkoleft.context.bsl.helpers;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Strings {
  public boolean isNullOrEmpty(String value) {
    return value == null || value.isEmpty();
  }
}
