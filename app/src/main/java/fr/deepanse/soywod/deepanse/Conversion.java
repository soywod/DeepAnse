package fr.deepanse.soywod.deepanse;

import android.database.Cursor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.model.DeepAnse;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

/**
 * Created by soywod on 05/02/2015.
 */
public class Conversion {

    /**
     *  Convertit un curseur en dépense DeepAnse
     *
     *  @param cursor   Le curseur à convertir de type Cursor
     *
     *  @return
     *      Le curseur converti en dépense de type DeepAnse
     */
    public static DeepAnse cursorToDeepAnse(Cursor cursor, DeepAnseGroup group)
    {
        DeepAnse deepAnse = new DeepAnse();

        deepAnse.setId(cursor.getInt(0));
        deepAnse.setAmount(cursor.getDouble(1));
        deepAnse.setDate(stringToDate(cursor.getString(2)));
        deepAnse.setGroup(group);
        deepAnse.setComment(cursor.getString(4));
        deepAnse.setRecursive(cursor.getInt(5) == 1);

        return deepAnse;
    }

    /**
     *  Convertit un curseur en rubrique DeepAnseGroup
     *
     *  @param cursor   Le curseur à convertir de type Cursor
     *
     *  @return
     *      Le curseur converti en rubrique de type DeepAnseGroup
     */
    public static DeepAnseGroup cursorToDeepAnseGroup(Cursor cursor)
    {
        DeepAnseGroup group = new DeepAnseGroup();

        group.setId(cursor.getInt(0));
        group.setName(cursor.getString(1));
        group.setColor(cursor.getInt(2));

        return group;
    }

    /**
     *  Convertit un GregorianCalendar en String
     *
     *  @param date     La date à convertir de type GregorianCalendar
     *
     *  @return La date convertie en String
     */
    public static String dateToString(GregorianCalendar date)
    {
        return date.get(GregorianCalendar.YEAR) + "-" +
                ((date.get(GregorianCalendar.MONTH) < 10)?("0" + date.get(GregorianCalendar.MONTH)):("" + date.get(GregorianCalendar.MONTH))) + "-" +
                ((date.get(GregorianCalendar.DAY_OF_MONTH) < 10)?("0" + date.get(GregorianCalendar.DAY_OF_MONTH)):("" + date.get(GregorianCalendar.DAY_OF_MONTH)));
    }

    /**
     *  Convertit un GregorianCalendar en String (version FR)
     *
     *  @param date     La date à convertir de type GregorianCalendar
     *
     *  @return La date convertie en String
     */
    public static String dateToStringFr(GregorianCalendar date)
    {
        return ((date.get(GregorianCalendar.DAY_OF_MONTH) < 10)?("0" + date.get(GregorianCalendar.DAY_OF_MONTH)):("" + date.get(GregorianCalendar.DAY_OF_MONTH))) + "/" +
                ((date.get(GregorianCalendar.MONTH) < 9)?("0" + (date.get(GregorianCalendar.MONTH)+1)):("" + (date.get(GregorianCalendar.MONTH)+1))) + "/" +
                date.get(GregorianCalendar.YEAR);
    }

    /**
     *  Convertit un GregorianCalendar en String (version FR) avec le mois et l'année uniquement
     *
     *  @param date     La date à convertir de type GregorianCalendar
     *
     *  @return La date convertie en String
     */
    public static String dateToStringMonthYearFr(GregorianCalendar date)
    {
        return ((date.get(GregorianCalendar.MONTH) < 9)?("0" + (date.get(GregorianCalendar.MONTH)+1)):("" + (date.get(GregorianCalendar.MONTH)+1))) + "/" +
                date.get(GregorianCalendar.YEAR);
    }

    /**
     *  Convertit un String en GregorianCalendar
     *
     *  @param date     La date à convertir de type String
     *
     *  @return
     *      La date convertie en GregorianCalendar
     */
    public static GregorianCalendar stringToDate(String date) {
        GregorianCalendar tmpDate = new GregorianCalendar();

        String[] splitDate = date.replace(" ", "-").replace(":", "-").split("-");

        tmpDate.set(GregorianCalendar.YEAR, Integer.parseInt(splitDate[0]));
        tmpDate.set(GregorianCalendar.MONTH, Integer.parseInt(splitDate[1]));
        tmpDate.set(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(splitDate[2]));

        return tmpDate;
    }

    public static String spellOutToNumber(String phrase) {
        String[] splitPhrase = phrase.replace("-", " ").split(" ");
        ArrayList<Integer> arrayNumber = new ArrayList<>();
        ArrayList<String> arrayString = new ArrayList<>();
        int total = 0;

        int numberNumeric;

        for(String word : splitPhrase)
        {
            if ((numberNumeric = Number.findNumberNumeric(word)) != -1)
                arrayNumber.add(numberNumeric);
            else if(arrayNumber.size() > 0)
            {
                for(int i=0 ; i<arrayNumber.size() ; i++)
                {
                    if(arrayNumber.get(i) == 4 && arrayNumber.get(i+1) == 20)
                    {
                        total += 80;
                        i++;
                    }
                    else
                        total += arrayNumber.get(i);
                }

                arrayString.add(total+"");
                arrayString.add(word);
                total = 0;
                arrayNumber.clear();
            }
            else
                arrayString.add(word);
        }

        if(arrayNumber.size() > 0)
        {
            for(int amount : arrayNumber)
                total += amount;
            arrayString.add(total+"");
        }

        if(arrayString.size() > 2)
        {
            for(int i=1 ; i<arrayString.size()-1 ; i++)
            {
                boolean isNumbers = true;
                try
                {
                    Integer.parseInt(arrayString.get(i-1));
                    Integer.parseInt(arrayString.get(i+1));
                }
                catch(NumberFormatException e){isNumbers = false;}

                if(arrayString.get(i).equals("et") && isNumbers)
                {
                    arrayString.set(i-1, Integer.parseInt(arrayString.get(i-1))+Integer.parseInt(arrayString.get(i+1))+"");
                    arrayString.remove(i+1);
                    arrayString.remove(i);
                }
            }
        }

        return arrayString.toString().replace("[", "").replace("]", "").replace(",", "");
    }
}
