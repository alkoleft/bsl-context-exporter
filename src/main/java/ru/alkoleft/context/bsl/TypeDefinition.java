package ru.alkoleft.context.bsl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github._1c_syntax.bsl.mdo.Module;
import lombok.Getter;
import lombok.Value;
import ru.alkoleft.context.bsl.symbols.MethodSymbol;
import ru.alkoleft.context.bsl.symbols.ParameterSymbol;
import ru.alkoleft.context.bsl.symbols.TypeDescription;

import java.util.regex.Pattern;

@Getter
public class TypeDefinition {
  private final static Pattern SEE_PATTERN = Pattern.compile("см\\.\\s+([\\wа-яА-Я0-9][\\wа-яА-Я0-9.]*[\\wа-яА-Я0-9])", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
  String name;
  String description;
  MethodDefinition[] methods;

  public TypeDefinition(Module module) {
    var moduleInfo = BslContext.getCurrent().getModuleContext(module);
    name = getTypeName(moduleInfo);

    methods = moduleInfo.getMethods().stream()
            .filter(MethodSymbol::isExport)
            .map(MethodDefinition::new)
            .toArray(MethodDefinition[]::new);
  }

  private static String getTypeName(ModuleInfo moduleInfo) {
    var type = moduleInfo.getOwner().getMdoType().getNameRu();
    var name = moduleInfo.getOwner().getName();
    var kind = "";
    switch (moduleInfo.getModule().getModuleType()) {
      case CommonModule -> kind = "";
      case ObjectModule -> kind = "Объект";
      case ManagerModule -> kind = "Менеджер";
      case RecordSetModule -> kind = "НаборЗаписей";
    }

    return type + kind + '.' + name;
  }

  public static String getType(TypeDescription type) {
    if (type.getName().startsWith("CommonModule.")) {
      return type.getName().replace("CommonModule", "ОбщийМодуль");
    }

    if (!type.getName().equalsIgnoreCase("ОбщийМодуль")) {
      return type.getName();
    }

    var match = SEE_PATTERN.matcher(type.getDescription());
    if (match.find()) {
      return type.getName() + '.' + match.group(1);
    } else {
      return type.getName();
    }
  }

  public static String getType(com.github._1c_syntax.bsl.parser.description.support.TypeDescription type) {
    if (type.getName().startsWith("CommonModule.")) {
      return type.getName().replace("CommonModule", "ОбщийМодуль");
    }

    if (!type.getName().equalsIgnoreCase("ОбщийМодуль")) {
      return type.getName();
    }

    var match = SEE_PATTERN.matcher(type.getDescription());
    if (match.find()) {
      return type.getName() + '.' + match.group(1);
    } else {
      return type.getName();
    }
  }

  @Value
  public static class MethodDefinition {
    String name;
    String description;
    ParameterDefinition[] params;
    @JsonProperty("return")
    String returnType;

    public MethodDefinition(MethodSymbol method) {
      name = method.getName();
      description = method.getDescription();
      if (method.getReturnedValue() != null) {
        returnType = getType(method.getReturnedValue());
      } else {
        returnType = null;
      }
      params = method.getParameters().stream()
              .map(ParameterDefinition::new)
              .toArray(ParameterDefinition[]::new);
    }
  }

  @Value
  private static class ParameterDefinition {
    boolean required;
    String name;
    String description;
    String type;

    public ParameterDefinition(ParameterSymbol parameter) {
      name = parameter.getName();
      description = parameter.getDescription();
      required = parameter.isRequired();
      var types = parameter.getTypes();
      if (types.isEmpty()) {
        type = null;
      } else {
        type = TypeDefinition.getType(types.getFirst());
      }
    }
  }
}
