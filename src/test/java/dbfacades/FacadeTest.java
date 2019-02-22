package dbfacades;

import entity.Car;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * UNIT TEST example that mocks away the database with an in-memory database See
 * Persistence unit in persistence.xml in the test packages
 *
 * Use this in your own project by: - Delete everything inside the setUp method,
 * but first, observe how test data is created - Delete the single test method,
 * and replace with your own tests
 *
 */
public class FacadeTest
{

    // Test mod in-memory test database.
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu-test", null);
    // Test mod lokal database.
    //EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu", null);

    DemoFacade facade = new DemoFacade(emf);
    Car e1 = null, e2 = null;
    
    /**
     * Setup test data in the database to a known state BEFORE Each test
     */
    @Before
    public void setUp()
    {
        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            //Delete all, since some future test cases might add/change data
            em.createQuery("delete from Car").executeUpdate();
            //Add our test data
            e1 = new Car("Volvo");
            e2 = new Car("WW");
            em.persist(e1);
            em.persist(e2);
            em.getTransaction().commit();
        } finally
        {
            em.close();
        }
    }

    // Test the single method in the Facade
    @Test
    public void countEntities()
    {
        EntityManager em = emf.createEntityManager();
        try
        {
            long count = facade.countCars();
            Assert.assertEquals(2, count);
        } finally
        {
            em.close();
        }
    }

    @Test
    public void testGetAllCars()
    {
        List<Car> allCars = facade.getAllCars();
        Assert.assertEquals(2, allCars.size());
        //Assert.assertEquals("Volve", allCars.get(0).getMake()); // bad.

    }

    @Test
    public void testGetCarsByMake()
    {
        List<Car> carsByMake = facade.getCarsByMake("Volvo");
        Assert.assertEquals(1, carsByMake.size());
    }

    @Test
    public void testGetCarById()
    {
        Integer idToFind = e1.getId();
        Car carById = facade.getCarById(idToFind);
        Assert.assertEquals(carById.getId(), idToFind);
    }
    
    @Test
    public void testDeleteCarById()
    {
        // Arrange        
        Integer idToDelete = e1.getId();
        boolean deleted = facade.deleteCar(idToDelete);
        Assert.assertEquals(deleted, true);
    }

}
