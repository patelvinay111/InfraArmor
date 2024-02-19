package org.infraarmor.app;

import java.util.List;

public class ReportFormatter {

    public static String generateMarkdownTable(List<ReportItem> list) {
        StringBuilder markdownTable = new StringBuilder("| Line Numbers | OWASP Category | Suggestion | Recommendation |\n");
        markdownTable.append("|------|-----|------|------|\n");

        for (ReportItem person : list) {
            markdownTable.append("| ")
                    .append(person.getLineNumbers()).append(" | ")
                    .append(person.getOwaspCategory()).append(" | ")
                    .append(person.getSuggestion()).append(" | ")
                    .append(person.getRecommendation()).append(" |\n");
        }

        return markdownTable.toString();
    }

}
