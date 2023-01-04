import org.apache.commons.net.util.SubnetUtils;
import model.Course;
import model.Grade;
import model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class Main {
    public static void main(String[] args) {

        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = cfg.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();



        String file = "drdlIp.txt";
        List<String> distinctIp = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            for(String line; (line = br.readLine()) != null; ) {
                String[] lineArray = line.split(";");

                if (lineArray[0].contains("/")) {
                    SubnetUtils utils = new SubnetUtils(lineArray[0]);
                    for(String ip:utils.getInfo().getAllAddresses()) {
                        Ip ipModel = new Ip();

                        ipModel.setIP(ip);
                        ipModel.setProtocol(lineArray[1]);
                        session.persist(ipModel);

                        //distinctIp.add(ip);
                    }
                }
                else {
                    Ip ipModel = new Ip();

                    ipModel.setIP(lineArray[0]);
                    ipModel.setProtocol(lineArray[1]);
                    session.persist(ipModel);

                }
                //tx.commit();
            }
            // line is not visible here.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

tx.commit();
        session.close();


        //writeIp2Db(distinctIp);

    }


}
