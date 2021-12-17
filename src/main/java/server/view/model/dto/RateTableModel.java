package server.view.model.dto;

import server.view.Model;

import java.util.LinkedHashMap;

public record RateTableModel(
        LinkedHashMap<String, Integer> rateByUsers,
        LinkedHashMap<String, Integer> paginationOptions,
        int selectedPage
) implements Model {}
