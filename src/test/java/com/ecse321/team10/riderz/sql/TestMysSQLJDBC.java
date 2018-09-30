package com.ecse321.team10.riderz.sql;
import static org.junit.Assert.*;

import org.junit.Test;

import com.ecse321.team10.riderz.model.Car;
import com.ecse321.team10.riderz.model.User;

public class TestMysSQLJDBC {
	MySQLJDBC sql = new MySQLJDBC();

	@Test
	public void testInsertUser() {
		sql.connect();
		User testUser = new User("heqian", "password", "heqian@gmail.com", "514888888", "He Qian", "Wang");
		assertEquals(true, sql.insertUser(testUser));
		assertEquals(true, sql.deleteUser("heqian"));
		sql.closeConnection();
	}
}
