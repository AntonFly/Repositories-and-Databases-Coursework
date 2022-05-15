import model.Hotel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.HibernateUtil;



public class test {
    public static void main(String[] args) {
        System.out.println("Maven + Hibernate + MySQL");
        Session session = HibernateUtil.getSessionFactory().openSession();
//        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//        sessionFactory

        session.beginTransaction();
        //User user = new User();

        //user.setFirstName("Alexander");
        //user.setLastName("Barchuk");
        Hotel hotel ;
//        hotel.setName("testHotel");
//        hotel.setDisposition("First line");
//        hotel.setTypeId(5);
        hotel = session.get(Hotel.class, 1);
        session.getTransaction().commit();
        System.out.print(hotel);
    }
}
