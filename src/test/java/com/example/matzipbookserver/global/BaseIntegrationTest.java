package com.example.matzipbookserver.global;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUpSpy() {
        //DATABASE
        databaseCleanUp.execute();
    }
}