package org.infraarmor.app;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineArgParser {

    public static CommandLine parse(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("f")
                .longOpt("filepath")
                .desc("Filepath of the terraform file to review")
                .hasArg()
                .required()
                .build());

        options.addOption(Option.builder("d")
                .longOpt("display_markdown")
                .desc("Set true if you would like to output the report as a markdown table")
                .hasArg()
                .required(false)
                .build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("CommandLineParserExample", options);
            System.exit(1);
        }
        return cmd;
    }
}
