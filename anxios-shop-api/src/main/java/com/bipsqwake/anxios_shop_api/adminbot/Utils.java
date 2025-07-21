package com.bipsqwake.anxios_shop_api.adminbot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {

    private static List<List<String>> formatTable(List<List<String>> table, int[] maxColumnLength) {
        int[] realColumnLength = new int[maxColumnLength.length];
        for (List<String> row : table) {
            for (int column = 0; column < realColumnLength.length; column++) {
                if (column >= row.size()) {
                    break;
                }
                realColumnLength[column] = Math.max(realColumnLength[column], row.get(column).length());
            }
        }
        for (int i = 0; i < realColumnLength.length; i++) {
            realColumnLength[i] = Math.min(realColumnLength[i], maxColumnLength[i]);
        }
        return table.stream()
                .flatMap(row -> wrapRow(row, realColumnLength).stream())
                .collect(Collectors.toList());
    }

    private static List<List<String>> wrapRow(List<String> row, int[] columnLength) {
        if (row.size() != columnLength.length) {
            log.error("row {} have greater size than {}", row.toString(), columnLength.length);
            throw new IllegalArgumentException("Column lengths should be the size of columns");
        }
        List<List<String>> preResult = new LinkedList<>();
        int maxChunks = 0;
        for (int i = 0; i < columnLength.length; i++) {
            if (i >= row.size()) {
                break;
            }
            List<String> splitted = splitByChunkSize(row.get(i), columnLength[i]);
            maxChunks = Math.max(maxChunks, splitted.size());
            preResult.add(splitted);
        }
        for (int i = 0; i < columnLength.length; i++) {
            if (preResult.get(i).size() < maxChunks) {
                for (int j = preResult.get(i).size(); j < maxChunks; j++) {
                    preResult.get(i).add(" ".repeat(columnLength[i]));
                }
            }
        }
        int resultRows = preResult.get(0).size();
        int resultCols = preResult.size();
        List<List<String>> result = new LinkedList<>();
        for (int i = 0; i < resultRows; i++) {
            List<String> toAdd = new LinkedList<>();
            for (int j = 0; j < resultCols; j++) {
                toAdd.add(preResult.get(j).get(i));
            }
            result.add(toAdd);
        }
        return result;
    }

    private static List<String> splitByChunkSize(String input, int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("chunk size should be positive");
        }
        List<String> result = new ArrayList<>();
        if (input == null || input.isEmpty()) {
            return result;
        }
        for (int i = 0; i < input.length(); i += chunkSize) {
            int end = Math.min(input.length(), i + chunkSize);
            String toAdd = input.substring(i, end);
            if (toAdd.length() < chunkSize) {
                toAdd = toAdd + " ".repeat(chunkSize - toAdd.length());
            }
            result.add(toAdd);
        }
        return result;
    }

    public static String getTextTable(List<List<String>> table, int[] columnLength) {
        List<List<String>> formatedTable = formatTable(table, columnLength);
        StringBuilder builder = new StringBuilder();
        builder.append("<pre>");
        for (List<String> row : formatedTable) {
            for (String column : row) {
                builder.append(column).append("|");
            }
            builder.append("\n");
        }
        builder.append("</pre>");
        return builder.toString();
    }
}
