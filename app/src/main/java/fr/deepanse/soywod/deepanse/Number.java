package fr.deepanse.soywod.deepanse;

/**
 * Created by soywod on 06/02/2015.
 */
public enum Number {
    un("un", 1),
    deux("deux", 2),
    trois("trois", 3),
    quatre("quatre", 4),
    cinq("cinq", 5),
    six("six", 6),
    sept("sept", 7),
    huit("huit", 8),
    neuf("neuf", 9),
    dix("dix", 10),
    onze("onze", 11),
    douze("douze", 12),
    treize("treize", 13),
    quatorze("quatorze", 14),
    quinze("quinze", 15),
    seize("seize", 16),
    vingt("vingt", 20),
    vingts("vingts", 20),
    trente("trente", 30),
    quarante("quarante", 40),
    cinquante("cinquante", 50),
    soixante("soixante", 60),
    cent("cent", 100),
    cents("cents", 100),
    mille("mille", 1000),
    milles("milles", 1000);

    private String numberLitteral;
    private int numberNumeric;

    private Number(String numberLitteral, int numberNumeric) {
        this.numberLitteral = numberLitteral;
        this.numberNumeric = numberNumeric;
    }

    public String getNumberLitteral() {
        return numberLitteral;
    }

    public int getNumberNumeric() {
        return numberNumeric;
    }

    public static int findNumberNumeric(String numberLitteral) {

        int numberNumeric = -1;
        for (Number type : Number.values())
        {
            if (numberLitteral.equals(type.getNumberLitteral()))
            {
                numberNumeric = type.getNumberNumeric();
                break;
            }
        }
        return numberNumeric;
    }
}
