package com.neo4j.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.neo4j.example.model.Employee;

@Controller
@RequestMapping(value = "/ui/employees")
public class FrontendController {

	@Autowired
	EmployeeRestController backend;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String employeesList(Model model) {
		model.addAttribute("employees", backend.getAllEmployees());
		return "employees";
	}

	@RequestMapping(value = "/delete/{empId}", method = RequestMethod.GET)
	public String deleteEmployee(@PathVariable Integer empId, Model model) {
		backend.deleteEmployee(empId);
		model.addAttribute("employees", backend.getAllEmployees());
		return "redirect:/ui/employees";
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String addEmployee(@RequestParam Integer empId, @RequestParam String name, @RequestParam String email,
			Model model) {
		backend.addEmployee(new Employee(empId, name, email));
		model.addAttribute("employees", backend.getAllEmployees());
		return "employees";
	}
}
