package com.assignment.basic_app;

import com.assignment.basic_app.entity.Transaction;
import com.assignment.basic_app.repo.TransactionRepository;
import com.assignment.basic_app.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TransactionServiceTest {
	@Autowired
	private TransactionService transactionService;

	@MockBean
	private TransactionRepository transactionRepository;

	@Test
	public void testUpdateDescription() {
		Transaction transaction = new Transaction();
		transaction.setId(1L);
		transaction.setDescription("Old Description");

		Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

		transactionService.updateDescription(1L, "New Description");

		Assertions.assertEquals("New Description", transaction.getDescription());
		Mockito.verify(transactionRepository).save(transaction);
	}
}
