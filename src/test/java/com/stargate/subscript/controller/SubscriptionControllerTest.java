package com.stargate.subscript.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stargate.subscript.Subscription2Application;
import com.stargate.subscript.models.SubscriptionRequest;

import net.minidev.json.JSONUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Subscription2Application.class)
@AutoConfigureMockMvc 
@EnableAutoConfiguration(exclude=SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class SubscriptionControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @Test
    public void validate_save_thenStatus200() throws IOException, Exception{
    	SubscriptionRequest request=new SubscriptionRequest();
    	request.setSubscriptionType("WEEKLY");
    	request.setDayOfWeekMonthly("MONDAY");
    	request.setStartDate(new Date("17-10-2020"));
    	request.setEndDate(new Date("17-12-2020"));
    	request.setInvAmt(new Double("500"));
    	
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    	
    	mvc.perform(post("/api/Subscription").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(request)))
//    	mvc.perform(post("/api/Subscription").contentType(MediaType.APPLICATION_JSON))
    		.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void validate_save_thenStatus400() throws IOException, Exception{
    	SubscriptionRequest request=new SubscriptionRequest();
    	request.setSubscriptionType("MONTHLY");
    	request.setDayOfWeekMonthly("MONDAY");
    	request.setStartDate(new Date("17-01-2020"));
    	request.setEndDate(new Date("17-10-2020"));
    	request.setInvAmt(new Double("500.3729"));
    	
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    	
    	mvc.perform(post("/api/Subscription").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(request)))
//    	mvc.perform(post("/api/Subscription").contentType(MediaType.APPLICATION_JSON))
    		.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
