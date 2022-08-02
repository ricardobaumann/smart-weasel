package com.github.ricardobaumann.banktransactions.input.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardobaumann.banktransactions.config.ProblemConfig;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@Import(ProblemConfig.class)
@WebMvcTest(controllers = BulkTransactionController.class)
class BulkTransactionControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BulkTransferUseCase mockBulkTransferUseCase;

    @Value("classpath:fixtures/empty-transaction-result.json")
    private Resource emptyTransactionResult;

    @Value("classpath:fixtures/incomplete-bulk-request.json")
    private Resource incompleteBulkRequest;

    @Value("classpath:fixtures/incomplete-transaction-result.json")
    private Resource incompleteTransactionResult;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testEmptyRequest() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/v1/transactions")
                        .content("{}").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(objectMapper.readTree(response.getContentAsString())).isEqualTo(
                (objectMapper.readTree(ResourceUtil.asString(emptyTransactionResult)))
        );
    }

    @Test
    void testIncompleteRequest() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/v1/transactions")
                        .content(ResourceUtil.asString(incompleteBulkRequest)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(objectMapper.readTree(response.getContentAsString())).isEqualTo(
                (objectMapper.readTree(ResourceUtil.asString(incompleteTransactionResult)))
        );
    }

}
