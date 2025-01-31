package ru.alkoleft.context.bsl;

import com.github._1c_syntax.bsl.mdo.MD;
import com.github._1c_syntax.bsl.mdo.Module;
import lombok.Builder;
import lombok.Value;
import ru.alkoleft.context.bsl.symbols.MethodSymbol;

import java.util.List;
import java.util.Optional;

@Builder
@Value
public class ModuleInfo {
  MD owner;
  Module module;
  List<MethodSymbol> methods;
  String description;

  public String getName() {
    return owner.getName();
  }

  public boolean isNotEmpty() {
    return methods != null && !methods.isEmpty();
  }

  public boolean isEmpty() {
    return methods == null || methods.isEmpty();
  }

  public Optional<MethodSymbol> getMethod(String name) {
    return getMethods().stream().filter(it -> name.equalsIgnoreCase(it.getName())).findAny();
  }
}
