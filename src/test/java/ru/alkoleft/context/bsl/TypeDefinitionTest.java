package ru.alkoleft.context.bsl;

import org.junit.jupiter.api.Test;
import ru.alkoleft.context.bsl.symbols.TypeDescription;

import static org.assertj.core.api.Assertions.assertThat;

class TypeDefinitionTest {

  private static TypeDescription type(String name, String description) {
    return new TypeDescription(name,
            description,
            null, null, false);
  }

  @Test
  void getType() {
    assertThat(TypeDefinition.getType(type("ОбщийМодуль", "Утверждения, см. ЮТУтверждения")))
            .isEqualTo("ОбщийМодуль.ЮТУтверждения");

    assertThat(TypeDefinition.getType(type("ОбщийМодуль", "См. ЮТПредикаты.")))
            .isEqualTo("ОбщийМодуль.ЮТПредикаты");

    assertThat(TypeDefinition.getType(type("ОбщийМодуль", "Варианты, см. ЮТКонструкторВариантов.")))
            .isEqualTo("ОбщийМодуль.ЮТКонструкторВариантов");

    assertThat(TypeDefinition.getType(type("CommonModule.ЮТТесты", "Модуль регистрации тестов")))
            .isEqualTo("ОбщийМодуль.ЮТТесты");
  }
}