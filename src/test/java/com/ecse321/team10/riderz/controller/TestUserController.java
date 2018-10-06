package com.ecse321.team10.riderz.controller;


import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ecse321.team10.riderz.sql.MySQLJDBC;
import com.ecse321.team10.riderz.model.User;
import com.ecse321.team10.riderz.dto.UserDto;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class TestUserController {
    private static final Logger logger = LogManager.getLogger(TestUserController.class);
    @Autowired
    private MockMvc mockMvc;
    private MySQLJDBC sql = new MySQLJDBC();

    @Autowired
    private UsersController userController;

    public User user = new User("test-ty","test","test-ty@gmail.com","514-638-4109","test-ty-first-name","test-ty-last-name");
    // public UserDto userTestDto = new UserDto("test-ty","test","test-ty@gmail.com","514-638-4109","test-ty-first-name","test-ty-last-name");

    @Before
    public void setup(){
        if(sql.connect()){
            sql.insertUser(user);
            sql.closeConnection();
        }
        logger.info("*****************TestUserController START*****************");
    }

    @After
    public void tearDown(){
        if(sql.connect()){
            sql.deleteUser("test-ty");
            sql.closeConnection();
            logger.info("*****************TestUserController END*****************");
        }
    }
    

    @Test
    public void testAddAndDeleteUser() throws Exception {
        this.mockMvc.perform(delete("/users/deleteUser?username=test-ty&password=test"))
                                .andExpect(status().isOk())
                                .andDo(print());
        this.mockMvc.perform(post("/users/addUser?username=test-ty&firstName=test-ty-first-name&lastName=test-ty-last-name&phone=514-638-4109&email=ty-test@gmail.com&password=test"))
                                .andExpect(status().isOk())
                                .andDo(print())
                                .andExpect(jsonPath("$.username").value("test-ty"))
                                .andExpect(jsonPath("$.firstName").value("test-ty-first-name"))
                                .andExpect(jsonPath("$.lastName").value("test-ty-last-name"))
                                .andExpect(jsonPath("$.phone").value("514-638-4109"))
                                .andExpect(jsonPath("$.email").value("ty-test@gmail.com"))
                                .andExpect(jsonPath("$.password").value("test"));
                                
    }


    @Test
    public void testGetUser() throws Exception {
        logger.info("*****************TestUserController GETUSER*****************");
        this.mockMvc.perform(get("/users/getUser?username=test-ty"))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andExpect(jsonPath("$.username").value("test-ty"))
                            .andExpect(jsonPath("$.firstName").value("test-ty-first-name"))
                            .andExpect(jsonPath("$.lastName").value("test-ty-last-name"))
                            .andExpect(jsonPath("$.phone").value("514-638-4109"))
                            .andExpect(jsonPath("$.email").value("test-ty@gmail.com"))
                            .andExpect(jsonPath("$.password").value("9F86D081884C7D659A2FEAA0C55AD015A3BF4F1B2B0B822CD15D6C15B0F00A08"));
       
    }

    @Test
    public void getAllUser() throws Exception{
        this.mockMvc.perform(get("/users/getAllUsers"))
                            .andExpect(status().isOk());
    }

    @Test
    public void testLogin() throws Exception {
        this.mockMvc.perform(post("/users/login?username=test-ty&password=testing"))
                            .andExpect(status().isOk());
    }

    @Test
    public void testSetPassword() throws Exception {
        this.mockMvc.perform(put("/users/setPassword?username=test-ty&password=testing&newPassword=testing2"))
                            .andExpect(status().isOk());
    }


    @Test
    public void testGetPhone() throws Exception {
        this.mockMvc.perform(get("/users/getPhone?username=test-ty"))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andExpect(content().string(containsString("514-638-4109")));
    }

    @Test
    public void testSetPhone() throws Exception {
        this.mockMvc.perform(put("/users/setPhone?username=test-ty&phone=514-111-1111"))
                            .andExpect(status().isOk())
                            .andDo(print());
        this.mockMvc.perform(get("/users/getPhone?username=test-ty"))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andExpect(content().string(containsString("514-111-1111")));
    }

    @Test
    public void testGetEmail() throws Exception {
        this.mockMvc.perform(get("/users/getEmail?username=test-ty"))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andExpect(content().string(containsString("test-ty@gmail.com")));
    }

    @Test
    public void testSetEmail() throws Exception {    
        this.mockMvc.perform(put("/users/setEmail?username=test-ty&email=changed@gmail.com"))
                            .andExpect(status().isOk())
                            .andDo(print());
        this.mockMvc.perform(get("/users/getEmail?username=test-ty"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().string(containsString("changed@gmail.com")));
        
    }



}