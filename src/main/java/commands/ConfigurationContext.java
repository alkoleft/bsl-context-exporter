package commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.nio.file.Path;

@Slf4j
@CommandLine.Command(helpCommand = true, name = "configuration")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationContext {
  @CommandLine.Parameters(description = "path")
  private Path path;
  @CommandLine.Parameters(description = "out")
  private Path output;
}
