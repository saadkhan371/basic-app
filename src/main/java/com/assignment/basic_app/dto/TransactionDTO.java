package com.assignment.basic_app.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionDTO implements Serializable {

    private String accountNumber;

    private String trxAmount;

    private String description;

    private String trxDate;

    private String trxTime;

    private String customerId;
}
