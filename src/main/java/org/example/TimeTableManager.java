package org.example;

public class TimeTableManager {

    //
    // Värdena i stringen avbryts av "!" och avslutas av "?"
    // Värdena kan sååklart inehålla flera teckenl.
    // Formatet, Namn!t!n!t!n?
    //

    private void StringDecoder(String time){

        String name = "";
        int[] times = new int[100];
        String[] names = new String[100];

        boolean Ok = true;
        boolean nameDone = false;
        boolean read = false;
        int i = 0;
        int plats = 0;


        // hämta namnet
        for (; i < time.length() || nameDone; i++) {
            char bokstav = time.charAt(i);
            if(bokstav != '!'){
                name += time.charAt(i);
            }else{
                nameDone = true;
            }
        }



        for (; i < time.length() || read; i++) {
            String t = "";
            String n = "";

            for (; i < time.length(); i++) {
                char bokstav = time.charAt(i);
                if(bokstav == '?'){
                    read = true;
                }
                if (bokstav != '!') {
                    t += time.charAt(i);
                    } else if (bokstav != '?') {
                        read = true;
                    } else {
                        break;
                    }
            }

            for (; i < time.length(); i++) {
                char bokstav = time.charAt(i);
                if(bokstav == '?'){
                    read = true;
                }
                if (bokstav != '!') {
                    t += time.charAt(i);
                } else {
                    break;
                }
            }

            plats++;
            int T = Integer.parseInt(t);
            times[plats] = T;
            names[plats] = n;

        }

        TimeTable timeTable = new TimeTable(name,times,names);


    }

    private void StringCreator(String time){

    }


}
