package org.infraarmor.app;

import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CommandLineArgParserTest {

    @Test
    void parse_shouldReturnCommandLine_withValidArguments() {
        String[] validArgs = {"-f", "testfile.txt"};

        CommandLine cmd = CommandLineArgParser.parse(validArgs);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("f"));
        assertEquals("testfile.txt", cmd.getOptionValue("f"));
    }
}
