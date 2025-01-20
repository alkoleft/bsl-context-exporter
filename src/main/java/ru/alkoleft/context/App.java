package ru.alkoleft.context;

import ru.alkoleft.context.commands.MainCommand;
import picocli.CommandLine;

public class App {
  public static void main(String[] args){
    new CommandLine(new MainCommand()).execute(args);
  }
}
