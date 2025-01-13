package com.assignment.basic_app.batch;

import com.assignment.basic_app.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class TransactionItemProcessor implements ItemProcessor<Transaction, Transaction> {
    private static final Logger log = LoggerFactory.getLogger(TransactionItemProcessor.class);

    @Override
    public Transaction process(Transaction item) {
        //ACCOUNT_NUMBER|TRX_AMOUNT|DESCRIPTION|TRX_DATE|TRX_TIME|CUSTOMER_ID

        final Transaction transformedTransaction = new Transaction();
        transformedTransaction.setAccountNumber(item.getAccountNumber());
        transformedTransaction.setDescription(item.getDescription());
        transformedTransaction.setCustomerId(item.getCustomerId());
        transformedTransaction.setTrxDate(item.getTrxDate());
        transformedTransaction.setTrxTime(item.getTrxTime());
        transformedTransaction.setTrxAmount(item.getTrxAmount());

        log.info("Converting ({}) into ({})", item, transformedTransaction);

        return transformedTransaction;
    }
}
