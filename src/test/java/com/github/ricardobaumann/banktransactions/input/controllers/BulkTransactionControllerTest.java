package com.github.ricardobaumann.banktransactions.input.controllers;

import com.github.ricardobaumann.banktransactions.config.ProblemConfig;
import com.github.ricardobaumann.banktransactions.domain.exceptions.BankAccountNotFoundException;
import com.github.ricardobaumann.banktransactions.domain.exceptions.NotEnoughBalanceException;
import com.github.ricardobaumann.banktransactions.domain.usecases.BulkTransferUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Import(ProblemConfig.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(BulkTransactionController.class)
class BulkTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("classpath:fixtures/sample1.json")
    private Resource sample;

    @MockBean
    private BulkTransferUseCase mockBulkTransferUseCase;

    @Test
    void testPost() throws Exception {
        // Setup
        doNothing().when(mockBulkTransferUseCase).create(any());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/v1/transactions")
                        .content(ResourceUtil.asString(sample)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void testPost_NotEnoughBalance() throws Exception {
        // Setup
        doThrow(NotEnoughBalanceException.class)
                .when(mockBulkTransferUseCase)
                .create(any());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/v1/transactions")
                        .content(ResourceUtil.asString(sample)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"title\":\"Account does not have enough balance\",\"status\":422}");
    }

    @Test
    void testPost_AccountNotFound() throws Exception {
        // Setup
        doThrow(BankAccountNotFoundException.class)
                .when(mockBulkTransferUseCase)
                .create(any());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/v1/transactions")
                        .content(ResourceUtil.asString(sample)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"title\":\"Account does not exist\",\"status\":422}");
    }

}
