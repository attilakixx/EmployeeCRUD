package application.DAO;

import java.util.List;

import application.DTO.Employee;

public interface ICrud {

	public void save(Employee obj);

	public void updateById(Long id);

	public void deleteById(Long id);

	public Employee findById(Long id);



	public List<Employee> getAll();


}


