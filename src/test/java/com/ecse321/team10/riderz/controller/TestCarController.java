package com.ecse321.team10.riderz.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecse321.team10.riderz.model.User;
import com.ecse321.team10.riderz.sql.MySQLJDBC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestCarController {
    @Autowired
    private MockMvc mockMvc;

    MySQLJDBC sql = new MySQLJDBC();

    @Before
    public void setup() {
        User user1 = new User("abc", "uNiTtEsTmEi", "abc@abc.com", "1234", "Allo", "Goodbye");
        User user2 = new User("Tyrone", "dfasdfew", "tyrone@mail.com", "1234445555", "Tyrone", "WWW");
        User user3 = new User("Ryan", "hello", "ryan.servera@mail.ca", "12451235", "Ryan", "Servera");
        sql.connect();
        sql.insertUser(user1);
        sql.insertUser(user2);
        sql.insertUser(user3);
        sql.closeConnection();
    }

    @After
    public void tearDown() {
        sql.connect();
        sql.deleteUser("abc");
        sql.deleteUser("Tyrone");
        sql.deleteUser("Ryan");
        sql.closeConnection();
    }

    @Test
    public void test1() throws Exception {
        this.mockMvc.perform(post("/car?operator=abc&make=Tesla&model=Model S&year=2017&numberOfSeats=5&fuelEfficiency=0.0&licensePlate=L32NEW")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operator").value("abc"))
                .andExpect(jsonPath("$.make").value("Tesla"))
                .andExpect(jsonPath("$.model").value("Model S"))
                .andExpect(jsonPath("$.year").value("2017"))
                .andExpect(jsonPath("$.numOfSeats").value("5"))
                .andExpect(jsonPath("$.fuelEfficiency").value("0.0"))
                .andExpect(jsonPath("$.licensePlate").value("L32NEW"));

        this.mockMvc.perform(get("/car?operator=abc")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operator").value("abc"))
                .andExpect(jsonPath("$.make").value("Tesla"))
                .andExpect(jsonPath("$.model").value("Model S"))
                .andExpect(jsonPath("$.year").value("2017"))
                .andExpect(jsonPath("$.numOfSeats").value("5"))
                .andExpect(jsonPath("$.fuelEfficiency").value("0.0"))
                .andExpect(jsonPath("$.licensePlate").value("L32NEW"));

        this.mockMvc.perform(put("/car?operator=abc&make=Tesla&model=Model 3&year=2018&numberOfSeats=5&fuelEfficiency=0.0&licensePlate=T32NDS")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operator").value("abc"))
                .andExpect(jsonPath("$.make").value("Tesla"))
                .andExpect(jsonPath("$.model").value("Model 3"))
                .andExpect(jsonPath("$.year").value("2018"))
                .andExpect(jsonPath("$.numOfSeats").value("5"))
                .andExpect(jsonPath("$.fuelEfficiency").value("0.0"))
                .andExpect(jsonPath("$.licensePlate").value("T32NDS"));

        this.mockMvc.perform(delete("/car?operator=abc")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }


    @Test
    public void test2() throws Exception {

        this.mockMvc.perform(post("/car?operator=Ryan&make=Honda&model=Civic&year=2016&numberOfSeats=5&fuelEfficiency=6.5&licensePlate=S53JWO")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operator").value("Ryan"))
                .andExpect(jsonPath("$.make").value("Honda"))
                .andExpect(jsonPath("$.model").value("Civic"))
                .andExpect(jsonPath("$.year").value("2016"))
                .andExpect(jsonPath("$.numOfSeats").value("5"))
                .andExpect(jsonPath("$.fuelEfficiency").value("6.5"))
                .andExpect(jsonPath("$.licensePlate").value("S53JWO"));

        this.mockMvc.perform(get("/car?operator=Ryan")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operator").value("Ryan"))
                .andExpect(jsonPath("$.make").value("Honda"))
                .andExpect(jsonPath("$.model").value("Civic"))
                .andExpect(jsonPath("$.year").value("2016"))
                .andExpect(jsonPath("$.numOfSeats").value("5"))
                .andExpect(jsonPath("$.fuelEfficiency").value("6.5"))
                .andExpect(jsonPath("$.licensePlate").value("S53JWO"));

        this.mockMvc.perform(put("/car?operator=Ryan&make=Toyota&model=Matrix&year=2014&numberOfSeats=5&fuelEfficiency=8.5&licensePlate=S53JWO")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operator").value("Ryan"))
                .andExpect(jsonPath("$.make").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Matrix"))
                .andExpect(jsonPath("$.year").value("2014"))
                .andExpect(jsonPath("$.numOfSeats").value("5"))
                .andExpect(jsonPath("$.fuelEfficiency").value("8.5"))
                .andExpect(jsonPath("$.licensePlate").value("S53JWO"));

        this.mockMvc.perform(delete("/car?operator=Tyrone")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    @Test
    public void test3() throws Exception {

        this.mockMvc.perform(post("/car?operator=Tyrone&make=Tesla&model=Model X&year=2015&numberOfSeats=5&fuelEfficiency=0.0&licensePlate=J53ISF")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operator").value("Tyrone"))
                .andExpect(jsonPath("$.make").value("Tesla"))
                .andExpect(jsonPath("$.model").value("Model X"))
                .andExpect(jsonPath("$.year").value("2015"))
                .andExpect(jsonPath("$.numOfSeats").value("5"))
                .andExpect(jsonPath("$.fuelEfficiency").value("0.0"))
                .andExpect(jsonPath("$.licensePlate").value("J53ISF"));

        this.mockMvc.perform(get("/car?operator=Tyrone")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operator").value("Tyrone"))
                .andExpect(jsonPath("$.make").value("Tesla"))
                .andExpect(jsonPath("$.model").value("Model X"))
                .andExpect(jsonPath("$.year").value("2015"))
                .andExpect(jsonPath("$.numOfSeats").value("5"))
                .andExpect(jsonPath("$.fuelEfficiency").value("0.0"))
                .andExpect(jsonPath("$.licensePlate").value("J53ISF"));

        this.mockMvc.perform(put("/car?operator=Tyrone&make=Tesla&model=Roadster&year=2020&numberOfSeats=4&fuelEfficiency=0.0&licensePlate=J53ISF")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operator").value("Tyrone"))
                .andExpect(jsonPath("$.make").value("Tesla"))
                .andExpect(jsonPath("$.model").value("Roadster"))
                .andExpect(jsonPath("$.year").value("2020"))
                .andExpect(jsonPath("$.numOfSeats").value("4"))
                .andExpect(jsonPath("$.fuelEfficiency").value("0.0"))
                .andExpect(jsonPath("$.licensePlate").value("J53ISF"));


        this.mockMvc.perform(delete("/car?operator=Ryan")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

}
