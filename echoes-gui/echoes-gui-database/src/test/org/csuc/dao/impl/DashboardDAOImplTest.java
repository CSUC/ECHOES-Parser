package org.csuc.dao.impl;

import org.csuc.client.Client;
import org.csuc.dao.AnalyseDAO;
import org.csuc.dao.DashboardDAO;
import org.csuc.dao.impl.loader.LoaderDAOImpl;
import org.csuc.dao.loader.LoaderDAO;
import org.csuc.dao.RecollectDAO;
import org.csuc.entities.Analyse;
import org.csuc.entities.loader.Loader;
import org.csuc.entities.Recollect;
import org.csuc.utils.Status;
import org.junit.Test;

import java.util.*;

import static org.mongodb.morphia.aggregation.Group.grouping;
import static org.mongodb.morphia.aggregation.Projection.projection;

public class DashboardDAOImplTest {

    private DashboardDAO dashboardDAO = new DashboardDAOImpl(new Client("localhost", 27017, "echoes").getDatastore());
    private RecollectDAO recollectDAO = new RecollectDAOImpl(Recollect.class, new Client("localhost", 27017, "echoes").getDatastore());
    private AnalyseDAO analyseDAO = new AnalyseDAOImpl(Analyse.class, new Client("localhost", 27017, "echoes").getDatastore());
    private LoaderDAO loaderDAO = new LoaderDAOImpl(Loader.class, new Client("localhost", 27017, "echoes").getDatastore());

    private String user = "github|32936334";

    @Test
    public void getStatus() throws Exception {

        System.out.println(String.format("ERROR %s", dashboardDAO.getStatus(Status.ERROR)));
        System.out.println(String.format("END %s", dashboardDAO.getStatus(Status.END)));
        System.out.println(String.format("QUEUE %s", dashboardDAO.getStatus(Status.QUEUE)));
        System.out.println(String.format("PROGRESS %s", dashboardDAO.getStatus(Status.PROGRESS)));
    }

    @Test
    public void getStatus1() throws Exception {

        System.out.println(String.format("ERROR %s", dashboardDAO.getStatus(Status.ERROR, user)));
        System.out.println(String.format("END %s", dashboardDAO.getStatus(Status.END, user)));
        System.out.println(String.format("QUEUE %s", dashboardDAO.getStatus(Status.QUEUE, user)));
        System.out.println(String.format("PROGRESS %s", dashboardDAO.getStatus(Status.PROGRESS, user)));
    }


    @Test
    public void getStatusMonth() throws Exception {
//        long end = dashboardDAO.getStatusMonth(Status.END, user);
//
//        long endRecollect = recollectDAO.getStatusMonth(Status.END, user);
//        long endAnalyse = analyseDAO.getStatusMonth(Status.END, user);
//
//        System.out.println(String.format("END : %s", end));
//
//        System.out.println(String.format("END Recollect: %s", endRecollect));
//        System.out.println(String.format("END Analyse:   %s", endAnalyse));
//
//
//        recollectDashboard();
//        analyseDashboard();

        //

        loaderDAO.deleteById("61c7338a-fe0c-4751-96cf-287e7869eada");



    }

    private void recollectDashboard() throws Exception {
        Map<Integer, Long> a = (Map<Integer, Long>) dashboardDAO.getDashboardRecollect(user);

        System.out.println(a.keySet());
        System.out.println(a.values());
    }

    private void analyseDashboard() throws Exception {
        Map<Integer, Long> a = (Map<Integer, Long>) dashboardDAO.getDashboardAnalyse(user);

        System.out.println(a.keySet());
        System.out.println(a.values());
    }
}