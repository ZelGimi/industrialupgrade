package com.denfop.client.intro;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntroMarkdownParser {

    private static final Pattern IMAGE_PATTERN = Pattern.compile("!\\[(.*?)]\\((.*?)\\)", Pattern.DOTALL);
    private static final Pattern HEADING_PATTERN = Pattern.compile("^(#{1,6})\\s+(.*)$");
    private static final Pattern BULLET_PATTERN = Pattern.compile("^[-*+]\\s+(.*)$");
    private static final Pattern ORDERED_PATTERN = Pattern.compile("^\\d+\\.\\s+(.*)$");

    public List<IntroMarkdownBlock> parse(String markdown) {
        String normalized = markdown == null ? "" : markdown.replace("\r\n", "\n");
        List<IntroMarkdownBlock> result = new ArrayList<>();

        Matcher matcher = IMAGE_PATTERN.matcher(normalized);
        int last = 0;
        while (matcher.find()) {
            String before = normalized.substring(last, matcher.start());
            parseTextSection(before, result);

            String alt = normalizeInlineText(matcher.group(1));
            String source = matcher.group(2).trim();
            result.add(new IntroImageBlock(alt, source));

            last = matcher.end();
        }

        if (last < normalized.length()) {
            parseTextSection(normalized.substring(last), result);
        }

        return result;
    }

    private void parseTextSection(String text, List<IntroMarkdownBlock> out) {
        String[] lines = text.split("\n");
        StringBuilder paragraph = new StringBuilder();
        List<String> listItems = new ArrayList<>();

        for (String rawLine : lines) {
            String line = rawLine.strip();

            if (line.isEmpty()) {
                flushParagraph(paragraph, out);
                flushList(listItems, out);
                continue;
            }

            Matcher headingMatcher = HEADING_PATTERN.matcher(line);
            if (headingMatcher.matches()) {
                flushParagraph(paragraph, out);
                flushList(listItems, out);

                int level = headingMatcher.group(1).length();
                String headingText = normalizeInlineText(headingMatcher.group(2));
                out.add(new IntroHeadingBlock(level, headingText));
                continue;
            }

            Matcher bulletMatcher = BULLET_PATTERN.matcher(line);
            if (bulletMatcher.matches()) {
                flushParagraph(paragraph, out);
                listItems.add(normalizeInlineText(bulletMatcher.group(1)));
                continue;
            }

            Matcher orderedMatcher = ORDERED_PATTERN.matcher(line);
            if (orderedMatcher.matches()) {
                flushParagraph(paragraph, out);
                listItems.add(normalizeInlineText(orderedMatcher.group(1)));
                continue;
            }

            flushList(listItems, out);

            if (!paragraph.isEmpty()) {
                paragraph.append(' ');
            }
            paragraph.append(normalizeInlineText(line));
        }

        flushParagraph(paragraph, out);
        flushList(listItems, out);
    }

    private void flushParagraph(StringBuilder paragraph, List<IntroMarkdownBlock> out) {
        if (!paragraph.isEmpty()) {
            out.add(new IntroParagraphBlock(paragraph.toString().trim()));
            paragraph.setLength(0);
        }
    }

    private void flushList(List<String> listItems, List<IntroMarkdownBlock> out) {
        if (!listItems.isEmpty()) {
            out.add(new IntroListBlock(new ArrayList<>(listItems)));
            listItems.clear();
        }
    }

    private String normalizeInlineText(String value) {
        return value.replace('\n', ' ').replaceAll("\\s+", " ").trim();
    }
}