import model.Direction;
import model.Hotel;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.HibernateUtil;
import org.hibernate.query.Query;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class test {
    public static void main(String[] args) throws ParseException {
//        Date date = new java.sql.Date(
//                ((java.util.Date) new SimpleDateFormat("dd.MM.yyyy").parse("23.04.2022")).getTime());
        long i =Long.parseLong("9803295312");
        System.out.println("Maven + Hibernate + MySQL");
        Session session = HibernateUtil.getSessionFactory().openSession();

//        session.getTransaction().begin();
//        User user = new User();
//        user.setChatId(1234142l);
//        user.setName("Alexander");
//        user.setSurname("Barchuk");
//        user.setPhoneNumber(i);
//        user.setEmail("email@email.com");
//        session.save(user);
//        session.getTransaction().commit();
//        List<Direction> directions = session.createQuery("from Direction", Direction.class).list();
//        Set<Direction> countries = directions.stream().filter(distinctByKey(Direction::getCountry)).collect(Collectors.toSet());
//        countries.forEach(country -> {
//            System.out.println(
//                    "Страна: " + country
//            );
//        });

        User u = session.byNaturalId(User.class).using(" chat_id",Long.parseLong("262634454")).load();
        System.out.println(u);

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
