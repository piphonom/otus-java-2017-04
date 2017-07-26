package ru.otus.lesson15;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.lesson15.service.orm.connectors.H2SimpleConnector;
import ru.otus.lesson15.service.orm.ORMService;
import ru.otus.lesson15.base.datasets.UserDataSet;

/**
 * Created by piphonom
 */
public class MyOrmTest {
    @Test
    public void saveLoadTest() {
        try (ORMService ormService = ORMService.newInstance(H2SimpleConnector::new)){
            UserDataSet user = new UserDataSet("Donald Duck", 83);
            boolean result = ormService.save(user);
            Assert.assertEquals(true, result);

            UserDataSet newUser = ormService.read("Donald Duck", UserDataSet.class);
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
        try (ORMService ormService = ORMService.newInstance(H2SimpleConnector::new)) {
            UserDataSet user = new UserDataSet("Sherlock Holms", 33);
            boolean result = ormService.save(user);
            Assert.assertEquals(true, result);

            user.setName("Jim Moriarty");
            user.setAge(45);
            result = ormService.update(1, user);
            Assert.assertEquals(true, result);

            UserDataSet newUser = ormService.read(1, UserDataSet.class);
            Assert.assertNotNull(newUser);

            Assert.assertEquals("Jim Moriarty", newUser.getName());
            Assert.assertEquals(45, newUser.getAge());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
