package ru.alkoleft.context.commands;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
//import com.github._1c_syntax.bsl.context.PlatformContextGrabber;
//import com.github._1c_syntax.bsl.context.api.AccessMode;
//import com.github._1c_syntax.bsl.context.api.Availability;
//import com.github._1c_syntax.bsl.context.platform.PlatformGlobalContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@CommandLine.Command(helpCommand = true, name = "platform")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformContext implements Runnable {
  @CommandLine.Parameters(description = "path")
  private Path path;
  @CommandLine.Parameters(description = "out")
  private Path output;

  @Override
  @SneakyThrows
  public void run() {
    Path syntaxContextFile;
    var fileName = "shcntx_ru.hbk";
    try (var walk = Files.walk(path)) {
      syntaxContextFile = walk.filter(p -> p.toFile().isFile())
          .filter(p -> p.toString().endsWith(fileName))
          .findAny()
          .orElse(null);
    }

    if (syntaxContextFile == null) {
      throw new FileNotFoundException(String.format("Не удалось найти файл %s в каталоге %s", fileName, path));
    }

    Files.createDirectories(output);
//
//    var tmpDir = Files.createTempDirectory("test");
//
//    var grabber = new PlatformContextGrabber(syntaxContextFile, tmpDir);
//    grabber.parse();
//
//    var provider = grabber.getProvider();
//
//    writeProperties(provider.getGlobalContext());
//    writeMethods(provider.getGlobalContext());
  }

//  private void writeProperties(PlatformGlobalContext context) throws IOException {
//    JsonFactory jfactory = new JsonFactory();
//    var file = output.resolve("global-context-properties.json").toFile();
//    System.out.println(file);
//
//    try (var generator = jfactory.createGenerator(file, JsonEncoding.UTF8)) {
//      generator.writeStartArray();
//
//      for (var property : context.properties()) {
//        generator.writeStartObject();
//        generator.writeStringField("name", property.name().getName());
//        generator.writeStringField("name_en", property.name().getAlias());
//        generator.writeStringField("description", property.description());
//        generator.writeBooleanField("readonly", AccessMode.READ.equals(property.accessMode()));
//        if (!property.types().isEmpty()) {
//          generator.writeStringField("type", property.types().get(0).name().getName());
//        }
//        generator.writeEndObject();
//      }
//      generator.writeEndArray();
//    }
//  }
//
//  private void writeMethods(PlatformGlobalContext context) throws IOException {
//    JsonFactory jfactory = new JsonFactory();
//    var file = output.resolve("global-context-methods.json").toFile();
//    System.out.println(file);
//
//    try (JsonGenerator generator = jfactory.createGenerator(file, JsonEncoding.UTF8)) {
//      generator.writeStartArray();
//
//      for (var method : context.methods()) {
//        if (!(method.availabilities().contains(Availability.SERVER) || method.availabilities().contains(Availability.THIN_CLIENT))) {
//          continue;
//        }
//        generator.writeStartObject();
//        generator.writeStringField("name", method.name().getName());
//        generator.writeStringField("name_en", method.name().getAlias());
//        generator.writeStringField("description", method.description());
//        generator.writeArrayFieldStart("signature");
//        for (var sig : method.signatures()) {
//          generator.writeStartObject();
//          if (sig.name().getName().equals("Основной")) {
//            generator.writeStringField("description", sig.description());
//          } else {
//            generator.writeStringField("description", sig.name().getName() + ". " + sig.description());
//          }
//          generator.writeArrayFieldStart("params");
//          for (var param : sig.parameters()) {
//            generator.writeStartObject();
//            generator.writeStringField("name", param.name().getName());
//            generator.writeStringField("description", param.description());
//            if (!param.types().isEmpty()) {
//              generator.writeStringField("type", param.types().get(0).name().getName());
//            }
//            if (!param.isRequired()) {
//              generator.writeBooleanField("required", param.isRequired());
//            }
//            generator.writeEndObject();
//          }
//          generator.writeEndArray();
//          generator.writeEndObject();
//
//        }
//        generator.writeEndArray();
//
//        if (method.hasReturnValue()) {
//          generator.writeStringField("return", method.returnValues().get(0).name().getName());
//        }
//
//        generator.writeEndObject();
//      }
//      generator.writeEndArray();
//    }
//  }
//
}
