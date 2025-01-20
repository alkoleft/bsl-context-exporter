package ru.alkoleft.context.commands;

import picocli.CommandLine;

@CommandLine.Command(subcommands = {PlatformContext.class, ConfigurationContext.class})
public class MainCommand {
}