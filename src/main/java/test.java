import model.Hotel;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.HibernateUtil;
import org.hibernate.query.Query;

import java.util.List;


public class test {
    public static void main(String[] args) {
        long i =Long.parseLong("9803295312");
        System.out.println("Maven + Hibernate + MySQL");
        Session session = HibernateUtil.getSessionFactory().openSession();

//        session.beginTransaction();
        User user = new User();
        user.setChatId(1234142l);
        user.setName("Alexander");
        user.setSurname("Barchuk");
        user.setPhoneNumber(i);
        user.setEmail("email@email.com");
        session.persist(user);
//        session.getTransaction().commit();

    }
}
