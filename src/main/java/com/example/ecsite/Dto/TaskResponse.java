package com.example.ecsite.Dto;

public record TaskResponse (
    Long id,
    String title,
    boolean completed
) {
}