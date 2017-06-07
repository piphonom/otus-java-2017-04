package ru.otus.lesson9;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.lesson9.connectors.H2SimpleConnector;
import ru.otus.lesson9.dao.DbService;
import ru.otus.lesson9.datasets.User;

/**
 * Created by piphonom
 */
public class SaveLoadTest {
    @Test
    public void saveLoadTest() {
        try (DbService dbService = DbService.newInstance(H2SimpleConnector::new)){
            User user = new User("Sherlock Holms", 33);
            boolean result = dbService.save(user);
            Assert.assertEquals(true, result);

            User newUser = dbService.load(1, User.class);
            Assert.assertNotNull(newUser);

            Assert.assertEquals(user.getName(), newUser.getName());
            Assert.assertEquals(user.getAge(), newUser.getAge());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void updateTest() {
        try (DbService dbService = DbService.newInstance(H2SimpleConnector::new)) {
            User user = new User("Sherlock Holms", 33);
            boolean result = dbService.save(user);
            Assert.assertEquals(true, result);

            user.setName("Jim Moriarty");
            user.setAge(45);
            result = dbService.update(1, user);
            Assert.assertEquals(true, result);

            User newUser = dbService.load(1, User.class);
            Assert.assertNotNull(newUser);

            Assert.assertEquals("Jim Moriarty", newUser.getName());
            Assert.assertEquals(45, newUser.getAge());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
