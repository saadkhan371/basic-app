package com.assignment.basic_app.batch;

import com.assignment.basic_app.entity.Transaction;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

	private static final Logger log = Logger.getLogger(JobCompletionNotificationListener.class.getName());

	private final JdbcTemplate jdbcTemplate;

	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");

			jdbcTemplate
					.query("SELECT ACCOUNT_NUMBER,TRX_AMOUNT,DESCRIPTION,TRX_DATE,TRX_TIME,CUSTOMER_ID FROM transaction", new DataClassRowMapper<>(Transaction.class))
					.forEach(transaction -> log.info("Found <"+transaction+"> in the database."));
		}
	}
}
