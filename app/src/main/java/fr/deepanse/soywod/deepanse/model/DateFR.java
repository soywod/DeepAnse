package fr.deepanse.soywod.deepanse.model;

/**
 * Created by soywod on 09/02/2015.
 */
public enum DateFR {
    janvier("janvier", 0),
    février("février", 1),
    mars("mars", 2),
    avril("avril", 3),
    mai("mai", 4),
    juin("juin", 5),
    juillet("juillet", 6),
    août("août", 7),
    septembre("septembre", 8),
    octobre("octobre", 9),
    novembre("novembre", 10),
    décembre("décembre", 11),
    lundi("lundi", 2),
    mardi("mardi", 3),
    mercredi("mercredi", 4),
    jeudi("jeudi", 5),
    vendredi("vendredi", 6),
    samedi("samedi", 7),
    dimanche("dimanche", 1);

    private String dateLitteral;
    private int dateNumeric;

    private DateFR(String dateLitteral, int dateNumeric) {
        this.dateLitteral = dateLitteral;
        this.dateNumeric = dateNumeric;
    }

    public static String[] getMonthList() {
        String list = "";
        for (DateFR type : DateFR.values())
            if (!type.dateLitteral.equals("lundi") &&
                !type.dateLitteral.equals("mardi") &&
                !type.dateLitteral.equals("mercredi") &&
                !type.dateLitteral.equals("jeudi") &&
                !type.dateLitteral.equals("vendredi") &&
                !type.dateLitteral.equals("samedi") &&
                !type.dateLitteral.equals("dimanche")) list += Conversion.firstCharToUpperCase(type.dateLitteral) + " ";

        return list.trim().split(" ");
    }

    public String getDateLitteral() {
        return dateLitteral;
    }

    public int getDateNumeric() {
        return dateNumeric;
    }

    public static String findDateLitteral(int dateNumeric) {

        String dateLitteral = "";
        for (DateFR type : DateFR.values())
        {
            if (dateNumeric == type.getDateNumeric())
            {
                dateLitteral = type.getDateLitteral();
                break;
            }
        }
        return dateLitteral;
    }

    public static int findDateNumeric(String dateLitteral) {

        int dateNumeric = -1;
        for (DateFR type : DateFR.values())
        {
            if (dateLitteral.equals(type.getDateLitteral()))
            {
                dateNumeric = type.getDateNumeric();
                break;
            }
        }
        return dateNumeric;
    }


}
