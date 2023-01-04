import org.apache.commons.net.util.SubnetUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InsertNewIp {
    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = cfg.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        List<String> vsCode = new ArrayList<>();

        String file = "NewIp.txt";
        List<String> distinctIp = new ArrayList<>();
        int count_line = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String vpn = br.readLine();
            for(String line; (line = br.readLine()) != null; count_line++) {
                System.out.println(count_line);
                if(!line.contains("#")) {

                    if (line.contains("/")) {
                        SubnetUtils utils = new SubnetUtils(line);
                        for (String ip : utils.getInfo().getAllAddresses()) {
                            Query query = session.createQuery("from Ip ip where ip.ip=:ip");
                            query.setString("ip", ip);
                            if (query.stream().count() == 0) {
                                System.out.println(ip);
                                Ip ipModel = new Ip();

                                ipModel.setIP(ip);
                                ipModel.setProtocol(vpn);
                                session.persist(ipModel);
                                vsCode.add("    externalip " + ip + " => \"" + vpn + "\";");
                            }
                            //distinctIp.add(ip);
                        }
                    } else {

                        Query query = session.createQuery("from Ip ip where ip.ip=:ip");
                        query.setString("ip", line);
                        if (query.stream().count() == 0) {
                            System.out.println(line);
                            Ip ipModel = new Ip();

                            ipModel.setIP(line);
                            ipModel.setProtocol(vpn);
                            session.persist(ipModel);
                            vsCode.add("    externalip " + line + " => \"" + vpn + "\";");
                        }

                    }
                }
                //tx.commit();
            }
            // line is not visible here.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("VsCode.txt"));
            for(String vs:vsCode) {
                writer.write(vs + "\n");
            }
            writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        tx.commit();
        session.close();
    }
}

