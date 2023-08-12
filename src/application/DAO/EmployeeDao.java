package application.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import application.PrimaryController;
import application.DTO.Employee;

public class EmployeeDao implements ICrud {

//Ez az adatbázishozkapcsolódás -> "employee_db" refers to a persistence unit name defined in the persistence.xml 
	EntityManagerFactory factory = Persistence.createEntityManagerFactory("employee_db");
	EntityManager entityManager = factory.createEntityManager();



	@Override
	public void save(Employee obj) {
		entityManager.getTransaction().begin();
		entityManager.persist(obj);
		entityManager.getTransaction().commit();
		entityManager.close();
		factory.close();
	}



	@Override
	public void updateById(Long id) {
		entityManager.getTransaction().begin();
		Employee employeeObj = entityManager.find(Employee.class, id);


		employeeObj.setEmail(PrimaryController.modifiedEmployeeObject.getEmail());
		employeeObj.setFirstName(PrimaryController.modifiedEmployeeObject.getFirstName());
		employeeObj.setGender(PrimaryController.modifiedEmployeeObject.getGender());
		employeeObj.setJobTitle(PrimaryController.modifiedEmployeeObject.getJobTitle());
		employeeObj.setLanguage(PrimaryController.modifiedEmployeeObject.getLanguage());
		employeeObj.setLastName(PrimaryController.modifiedEmployeeObject.getLastName());
		employeeObj.setSalary(PrimaryController.modifiedEmployeeObject.getSalary());
		entityManager.getTransaction().commit();
		entityManager.close();
		factory.close();
	}



	@Override
	public void deleteById(Long id) {

		Employee employeeObj = findById(id);
		entityManager.getTransaction().begin(); // elindítjuk a managert
		entityManager.remove(employeeObj);
		entityManager.getTransaction().commit(); // ez teszi be a db-be
		entityManager.close();
		factory.close();

	}

	@Override
	public Employee findById(Long id) {
		Employee employeeObj = entityManager.getReference(Employee.class, id);
		return employeeObj;
	}

	@Override
	public List<Employee> getAll() {
		String jpql = "SELECT e FROM Employee e"; // e az employee aliasa
		Query query = entityManager.createQuery(jpql, Employee.class);
		List<Employee> employees = query.getResultList();

		return employees;
	}

	public List<Employee> searchEmployees(String firstName, String lastName,
			String email, String gender, String jobTitle, String language, int salary) {


		String hql = "FROM Employee WHERE "
				+ "firstName LIKE :firstName AND "
				+ "lastName LIKE :lastName AND "
				+ "email LIKE :email AND "
				+ "gender LIKE :gender AND "
				+ "jobTitle LIKE :jobTitle AND "
				+ "language LIKE :language AND "
				+ "salary >= :salary";
		Query query = entityManager.createQuery(hql, Employee.class);
		query.setParameter("firstName", "%" + firstName + "%");
		query.setParameter("lastName", "%" + lastName + "%");
		query.setParameter("email", "%" + email + "%");
		query.setParameter("gender", gender + "%");
		query.setParameter("jobTitle", "%" + jobTitle + "%");
		query.setParameter("language", "%" + language + "%");
		query.setParameter("salary", salary);
		List<Employee> employees = query.getResultList();
		return employees;



	}
	// Erre végül nem volt szükség, de nem törlöm ki
//	public void updateById(Long id, Employee modifiedEmployee) {
//		entityManager.getTransaction().begin();
//		Employee employeeObj = entityManager.find(Employee.class, id);
//
//
//		employeeObj.setEmail(modifiedEmployee.getEmail());
//		employeeObj.setFirstName(modifiedEmployee.getFirstName());
//		employeeObj.setGender(modifiedEmployee.getGender());
//		employeeObj.setJobTitle(modifiedEmployee.getJobTitle());
//		employeeObj.setLanguage(modifiedEmployee.getLanguage());
//		employeeObj.setLastName(modifiedEmployee.getLastName());
//		employeeObj.setSalary(modifiedEmployee.getSalary());
//		entityManager.getTransaction().commit();
//		entityManager.close();
//		factory.close();
//	}
}


