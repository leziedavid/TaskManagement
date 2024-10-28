package com.mobisoft.taskmanagement;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TaskManagementApplication.class)
public class TaskManagementApplicationTests {

    @Test
    public void contextLoads() {
        // Votre test de chargement de contexte ici
    }
}

