package ru.asherbakov.controllers;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class DocumentControllerTest {
    @Autowired
    MockMvc mockMvc;
//    JSONObject jsonObject =new JSONObject();
//    jsonObject.put("");
    @Test
    public void name() throws Exception {
        mockMvc.perform(get("/document/{id}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
