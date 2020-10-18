package pl.coderslab.entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class UsersDBApp {
    public static void main(String[] args) {
        try (Connection connection = DBUtil.connectToDB()) {
            System.out.println("Udało się połączyć z bazą danych");

//            test dla create
//            User user1 = new User();
//            user1.setUsername("user1");
//            user1.setEmail("user1@buziaczek.com");
//            user1.setPassword("okon");
//            UserDao user1Dao = new UserDao();
//            user1 = user1Dao.create(user1);

//            User user2 = new User();
//            user2.setUsername("user2");
//            user2.setEmail("user2@onet.pl");
//            user2.setPassword("maslo");
//            UserDao user2Dao = new UserDao();
//            user2 = user2Dao.create(user2);


//            test dla read
            UserDao testDao = new UserDao();
            User test = testDao.read(3);

//            System.out.println(test.toString());
//            test.setUsername("paweu");
//            test.setEmail("p@op.pl");
//            test.setPassword("xdddddd");
//            testDao.create(test);
//            test = testDao.read(5);

//            test dla update
//            test.setUsername("leszke");
//            test.setEmail("smieszke@wp.pl");
//            test.setPassword("zgaslo");
//            testDao.update(test);

//            test dla delete
//            testDao.delete(test.getId());

//            test dla readAll
            System.out.println(Arrays.toString(testDao.findAll()));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
