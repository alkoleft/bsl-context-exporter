package ru.alkoleft.context.bsl;

import lombok.Builder;
import lombok.Value;
import ru.alkoleft.context.bsl.symbols.MethodSymbol;

@Value
@Builder
public class MethodInfo {
  MethodSymbol method;
  ModuleInfo module;
  boolean publishing;
}
