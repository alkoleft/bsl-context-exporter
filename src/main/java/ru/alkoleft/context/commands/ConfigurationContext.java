package ru.alkoleft.context.commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github._1c_syntax.bsl.mdo.Module;
import com.github._1c_syntax.bsl.mdo.ModuleOwner;
import com.github._1c_syntax.bsl.mdo.Subsystem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import ru.alkoleft.context.bsl.BslContext;
import ru.alkoleft.context.bsl.Filter;
import ru.alkoleft.context.bsl.ModuleInfo;
import ru.alkoleft.context.bsl.TypeDefinition;
import ru.alkoleft.context.bsl.symbols.RegionSymbol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Slf4j
@CommandLine.Command(helpCommand = true, name = "configuration")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationContext implements Runnable {
  ObjectWriter mapper;
  @CommandLine.Parameters(description = "path")
  private Path path;
  @CommandLine.Parameters(description = "out")
  private Path output;

  private static Stream<Subsystem> getChildSubsystemsRecursive(Subsystem subsystem) {
    if (subsystem.getSubsystems().isEmpty()) {
      return Stream.of(subsystem);
    }

    return Stream.concat(Stream.of(subsystem), subsystem.getSubsystems().stream()
            .flatMap(ConfigurationContext::getChildSubsystemsRecursive));
  }

  private static Stream<ModuleOwner> getChildren(Subsystem subsystem) {
    return BslContext.getCurrent().getSubsystemObjects(subsystem)
            .filter(ModuleOwner.class::isInstance)
            .map(ModuleOwner.class::cast);
  }

  @SneakyThrows
  private void dumpModule(Module module, JsonGenerator g) {
    var definition = new TypeDefinition(module);

    mapper.writeValue(g, definition);
  }

  @SneakyThrows
  @Override
  public void run() {
    var bslContext = new BslContext(path, Filter.builder()
            .rootSubsystem("ЮТПубличный")
            .region(RegionSymbol.PUBLIC_REGION_RU)
            .region(RegionSymbol.PUBLIC_REGION_EN)
            .isExport(true)
            .build());

    var modulesStream = bslContext.getRootSubsystems(true)
            .flatMap(ConfigurationContext::getChildSubsystemsRecursive)
            .flatMap(ConfigurationContext::getChildren)
            .flatMap(o -> o.getModules().stream());

    Files.createDirectories(output);
    var file = output.resolve("global-context-methods.json").toFile();
    System.out.println(file);

    mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .writerFor(TypeDefinition.class);
    try (var generator = mapper.createGenerator(file, JsonEncoding.UTF8)) {
      generator.writeStartArray();
      modulesStream.forEach(m -> dumpModule(m, generator));
      generator.writeEndArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
