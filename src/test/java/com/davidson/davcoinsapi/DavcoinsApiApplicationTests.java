package com.davidson.davcoinsapi;

import com.davidson.davcoinsapi.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith({SpringExtension.class})
class DavcoinsApiApplicationTests {

    @Autowired
    private TransactionService transactionService;

    @Test
    void contextLoads() {
        assertThat(transactionService).isNotNull();
    }

}
