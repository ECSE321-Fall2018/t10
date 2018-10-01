package com.ecse321.team10.riderz.sql;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ecse321.team10.riderz.model.Car;
import com.ecse321.team10.riderz.model.User;

public class TestMySQLJDBC {
	MySQLJDBC sql = new MySQLJDBC();
	
	@Before
	public void setup() {
		sql.connect();
	}
	
	@After
	public void tearDown() {
		sql.closeConnection();
	}
	
	@Test
	public void testInsertUser() {
		User testUser = new User("heqiantest", "password", "heqian@gmail.com", "514888888", "He Qian", "Wang");
		assertEquals(true, sql.insertUser(testUser));
		assertEquals(true, sql.deleteUser("heqiantest"));
	}
}
