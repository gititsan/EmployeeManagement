package com.nik.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.nik.modal.Employee;
import com.nik.service.EmployeeService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({ "/employees" })
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(Employee.class);

 

	@Value("${app.message}")
	private String welcomeMessage;

	@GetMapping("/welcome")
	public String getDataBaseConnectionDetails() {
		return welcomeMessage;
	}

	@GetMapping(produces = "application/json")
	public ResponseEntity<Object> firstPage() {
		LOGGER.info("Get Employee Data");
		ResponseEntity<Object> response = null;
		List<Employee> employees = new ArrayList();
		try {
			employees = employeeService.getAllEmployees();
			LOGGER.debug("Response from Employee endpoints {}", new Gson().toJson(employees));
			response = new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = "Exception while getting all Employees" + e;
			LOGGER.error(errorMessage);
			response = new ResponseEntity<>("Some error Occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;

	}

	/*
	 * @DeleteMapping(path = { "/{id}" }) public Employee delete(@PathVariable("id")
	 * Integer id) { System.out.println("delete employees call");
	 * employeeService.deleteEmployeeById(id); return null; }
	 */

	@DeleteMapping(path = { "/{id}" })
	public ResponseEntity<Object> deleteEmployee(@PathVariable("id") Integer id) {
		LOGGER.info("delete employees call for id {}", id);
		ResponseEntity<Object> responseEntity = null;
		Employee employee = new Employee();
		try {
			LOGGER.info("Checking the employee exist with given id {} ", id);
			employee = employeeService.findEmployeeById(id);
			if (employee == null) {
				responseEntity = new ResponseEntity<>("Employee doesnt exist", HttpStatus.EXPECTATION_FAILED);

			} else {
				employeeService.deleteEmployeeById(id);
				responseEntity = new ResponseEntity<>("Employee is deleted successfully", HttpStatus.OK);
			}

		} catch (Exception ex) {
			LOGGER.error("Exception while deleting employee {}", ex);
			responseEntity = new ResponseEntity<>("Exception while deleting employee",
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return responseEntity;
	}

	/*
	 * @GetMapping(path = { "/{id}" }) public Employee
	 * getEmployeeById(@PathVariable("id") Long id) { return
	 * employeeService.findEmployeeById(id); }
	 */

	@GetMapping(path = { "/{id}" })
	public ResponseEntity<Object> getEmployeeById(@PathVariable("id") Long id) {

		LOGGER.info("Get Employee by id  {}", id);
		ResponseEntity<Object> response = null;
		Employee employee = new Employee();
		try {
			employee = employeeService.findEmployeeById(id);
			if (employee == null) {
				response = new ResponseEntity<>("Employee Do not Exist", HttpStatus.EXPECTATION_FAILED);
			} else {
				response = new ResponseEntity<>(employee, HttpStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while adding employee {} : {}", employee, e);
			response = new ResponseEntity<>("Employee doesnt exist", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}


	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<Object> createEmployee(@RequestBody Employee emp) {
		LOGGER.info("Adding Employee  {}", emp);
		ResponseEntity<Object> response = null;
		try {
			employeeService.createEmployee(emp);
			response = new ResponseEntity<>("Employee Created", HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Exception while adding employee {} : {}", emp, e);
			response = new ResponseEntity<>("Some error Occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateEmployeeByID(@PathVariable("id") long id, @RequestBody Employee emp) {
		LOGGER.info("Adding Employee  {}", emp);
		ResponseEntity<Object> response = null;
		try {
			System.out.println(emp);

			if (emp==null) {
				response= new ResponseEntity<>("Employee doesnt exist", HttpStatus.EXPECTATION_FAILED);

			} else {
				employeeService.updateEmployee(emp);
				response= new ResponseEntity<>(emp, HttpStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while updating employee {} : {}", emp, e);
			response= new ResponseEntity<>("Some error Occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}

 

 

}
