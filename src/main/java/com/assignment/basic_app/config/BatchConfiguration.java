package com.assignment.basic_app.config;

import com.assignment.basic_app.batch.JobCompletionNotificationListener;
import com.assignment.basic_app.batch.TransactionItemProcessor;
import com.assignment.basic_app.entity.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class BatchConfiguration {
    @Bean
    public FlatFileItemReader<Transaction> reader() {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("dataSource.txt"));
        reader.setLinesToSkip(1);

        // Define a line mapper with correct tokenizers and the mapping
        DefaultLineMapper<Transaction> lineMapper = new DefaultLineMapper<>();

        // Tokenize the line by pipe symbol ('|')
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter("|");  // Set pipe delimiter
        tokenizer.setNames("accountNumber", "trxAmount", "description", "trxDate", "trxTime", "customerId");

        // Map the tokens to the Transaction object
        BeanWrapperFieldSetMapper<Transaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Transaction.class);
        fieldSetMapper.setCustomEditors(java.util.Map.of(
                LocalDate.class, new PropertyEditorSupport() {
                    @Override
                    public void setAsText(String text) throws IllegalArgumentException {
                        setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                },
                LocalTime.class, new PropertyEditorSupport() {
                    @Override
                    public void setAsText(String text) throws IllegalArgumentException {
                        setValue(LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss")));
                    }
                }
        ));
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        reader.setLineMapper(lineMapper);

        return reader;
    }

    @Bean
    public TransactionItemProcessor processor() {
        return new TransactionItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Transaction> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .sql("INSERT INTO transaction (ACCOUNT_NUMBER,TRX_AMOUNT,DESCRIPTION,TRX_DATE,TRX_TIME,CUSTOMER_ID)" +
                        " VALUES (:accountNumber, :trxAmount,:description,:trxDate,:trxTime,:customerId)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("importTransactionJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      FlatFileItemReader<Transaction> reader,
                      TransactionItemProcessor processor,
                      JdbcBatchItemWriter<Transaction> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Transaction, Transaction>chunk(3, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public DataSource dataSource() {
        return new org.springframework.jdbc.datasource.DriverManagerDataSource(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL", "sa", "");
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTransactionManager(transactionManager);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}
