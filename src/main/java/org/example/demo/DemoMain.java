
package org.example.demo;

import org.example.api.Connection;
import org.example.api.TimerDetails;

public class DemoMain {
    public static void main(String[] args) {

        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MTIzQGV4YW1wbGUuY29tIiwiaWF0IjoxNzQ2MDk4MTMwLCJleHAiOjE3NDYxODQ1MzB9.a3BiVxgvq_PkIIIixP0hoItwqF2K_Ndjrmq3DGmCITU";
        Connection conn = new Connection(jwt);

        try {
            conn.createTimer("Connection test",1,1,0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long idToFetch = 12L; // Add the id of timer u r lokking for, check pgadmin.
        TimerDetails timer = null;

        try {
            timer = conn.getTimerDetails(idToFetch);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Found timer " + timer.getId());

//        This method is completed, needs some method in api.
//
//        try {
//            List<TimerDetails> timers = conn.getAllTimers();
//            System.out.println("The list of timers is ");
//
//            for (TimerDetails t : timers) {
//                System.out.println("Id: " + t.getId()
//                        + ", name: " + t.getName()
//                        + ", count; " + t.getPomodoroCount()
//                        + ", WD: " +  t.getWorkDuration()
//                        + ", BD: " + t.getBreakDuration());
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        System.out.println("Connection Test...");
    }
}

