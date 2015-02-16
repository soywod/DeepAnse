package fr.deepanse.soywod.deepanse.model;

import android.graphics.Color;

/**
 * Created by soywod on 05/02/2015.
 *
 * ViewByDay modélise une rubrique de dépense, caractérisée par :
 * <ul>
 *     <li>Un id unique inchangeable</li>
 *     <li>Un nom unique</li>
 * </ul>
 *
 * @author soywod
 * @version 1.0
 */
public class DeepAnseGroup {

    /**
     *  L'id de la rubrique
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#DeepAnseGroup()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#DeepAnseGroup(long, java.lang.String, int)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#getId()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#setId(long)
     */
    private long id;

    /**
     *  Le nom de la rubrique
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#DeepAnseGroup()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#DeepAnseGroup(long, java.lang.String, int)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse(long, double, java.util.GregorianCalendar, fr.deepanse.soywod.deepanse.model.DeepAnseGroup, java.lang.String, boolean)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#getName()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#setName(java.lang.String)
     */
    private String name;

    /**
     *  La couleur de la rubrique
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#DeepAnseGroup()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#DeepAnseGroup(long, java.lang.String, int)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#getColor()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#setColor(int)
     */
    private int color;










    /**
     *  Constructeur DeepAnseGroup vide
     */
    public DeepAnseGroup(){}

    /**
     *  Constructeur DeepAnseGroup standard
     *
     *  @param id           L'id de la rubrique de type long
     *  @param name         Le nom de la rubrique de type String
     *  @param color        La couleur de la rubrique de type int
     */
    public DeepAnseGroup(long id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }










    /**
     *  Getter de l'id de la rubrique
     *
     *  @return L'id de la rubrique de type long
     */
    public long getId() {
        return id;
    }

    /**
     *  Getter du nom de la rubrique
     *
     *  @return Le nom de la rubrique de type String
     */
    public String getName() {
        return name;
    }

    /**
     *  Getter de la couleur de la rubrique
     *
     *  @return La couleur de la rubrique de type int
     */
    public int getColor() {
        return color;
    }










    /**
     *  Setter de l'id de la rubrique
     *
     *  @param id
     *      L'id de la rubrique de type long
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *  Setter du montant de la rubrique
     *
     *  @param name
     *     Le nom de la rubrique de type String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *  Setter de la couleur de la rubrique
     *
     *  @param color
     *     La couleur de la rubrique de type int
     */
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return Conversion.firstCharToUpperCase(name);
    }
}
