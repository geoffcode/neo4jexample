package com.neo4j.example.controller;

import java.util.List;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.neo4j.example.model.Employee;

@RestController
@RequestMapping(value = "/rest/employees")
public class EmployeeRestController {

	@Autowired
	Driver driver;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public List<Employee> getEmployee(@PathVariable Long id) {
		try (Session session = driver.session()) {
			return session.writeTransaction(new TransactionWork<List<Employee>>() {
				@Override
				public List<Employee> execute(Transaction tx) {
					StatementResult result = tx.run(String.format("MATCH (e:Employee { emp_id : '%s' }) RETURN e", id));
					return result.list(entry -> new Employee(Integer.parseInt(entry.get("e").get("emp_id").asString()),
							entry.get("e").get("name").asString(), entry.get("e").get("email").asString()));
				}
			});
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<Employee> getAllEmployees() {
		try (Session session = driver.session()) {
			return session.writeTransaction(new TransactionWork<List<Employee>>() {
				@Override
				public List<Employee> execute(Transaction tx) {
					StatementResult result = tx.run("MATCH (e:Employee) RETURN e ORDER BY e.emp_id");
					return result.list(entry -> new Employee(Integer.parseInt(entry.get("e").get("emp_id").asString()),
							entry.get("e").get("name").asString(), entry.get("e").get("email").asString()));
				}
			});
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public Employee addEmployee(@RequestBody Employee employee) {
		try (Session session = driver.session()) {
			return session.writeTransaction(new TransactionWork<Employee>() {
				@Override
				public Employee execute(Transaction tx) {
					StatementResult result = tx.run(
							String.format("CREATE (e:Employee { emp_id : '%d', name : '%s', email : '%s' }) RETURN e",
									employee.getEmpId(), employee.getName(), employee.getEmail()));
					Record single = result.single();
					return new Employee(Integer.parseInt(single.get("e").get("emp_id").asString()),
							single.get("e").get("name").asString(), single.get("e").get("email").asString());
				}
			});
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteEmployee(@PathVariable Integer id) {
		try (Session session = driver.session()) {
			session.writeTransaction(new TransactionWork<Boolean>() {
				@Override
				public Boolean execute(Transaction tx) {
					tx.run(String.format("MATCH (e:Employee { emp_id: '%d' }) DELETE e", id));
					return Boolean.TRUE;
				}
			});
		}
	}

	@RequestMapping(value = "", method = RequestMethod.DELETE)
	public void deleteAllEmployees() {
		try (Session session = driver.session()) {
			session.writeTransaction(new TransactionWork<Boolean>() {
				@Override
				public Boolean execute(Transaction tx) {
					tx.run("MATCH (e:Employee) DELETE e");
					return Boolean.TRUE;
				}
			});
		}
	}
}
