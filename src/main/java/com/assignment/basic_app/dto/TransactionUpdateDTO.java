package com.assignment.basic_app.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TransactionUpdateDTO {
    @NotEmpty
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
