package fr.deepanse.soywod.deepanse.model;

import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.Conversion;

/**
 * Created by soywod on 05/02/2015.
 *
 * DeepAnse modélise une dépense, caractérisée par :
 * <ul>
 *     <li>Un id unique inchangeable</li>
 *     <li>Un montant</li>
 *     <li>Une date</li>
 *     <li>Une rubrique</li>
 *     <li>Un commentaire</li>
 *     <li>Une récurrence</li>
 * </ul>
 *
 * @author soywod
 * @version 1.0
 */
public class DeepAnse {

    /**
     *  L'id de la dépense
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse(long, double, java.util.GregorianCalendar, fr.deepanse.soywod.deepanse.model.DeepAnseGroup, java.lang.String, boolean)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#getId()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#setId(long)
     */
    private long id;

    /**
     *  Le montant de la dépense
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse(long, double, java.util.GregorianCalendar, fr.deepanse.soywod.deepanse.model.DeepAnseGroup, java.lang.String, boolean)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#getAmount()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#setAmount(double)
     */
    private double amount;

    /**
     *  La date de la dépense
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse(long, double, java.util.GregorianCalendar, fr.deepanse.soywod.deepanse.model.DeepAnseGroup, java.lang.String, boolean)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#getDate()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#setDate(java.util.GregorianCalendar)
     */
    private GregorianCalendar date;

    /**
     *  La rubrique de la dépense
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse(long, double, java.util.GregorianCalendar, fr.deepanse.soywod.deepanse.model.DeepAnseGroup, java.lang.String, boolean)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#DeepAnseGroup(long, java.lang.String)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#getGroup()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#setGroup(fr.deepanse.soywod.deepanse.model.DeepAnseGroup)
     */
    private DeepAnseGroup group;

    /**
     *  Le commentaire de la dépense
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse(long, double, java.util.GregorianCalendar, fr.deepanse.soywod.deepanse.model.DeepAnseGroup, java.lang.String, boolean)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#getComment()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#setComment(java.lang.String)
     */
    private String comment;

    /**
     *  La récursivité ou non de la dépense
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse(long, double, java.util.GregorianCalendar, fr.deepanse.soywod.deepanse.model.DeepAnseGroup, java.lang.String, boolean)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#isRecursive()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#setRecursive(boolean)
     */
    private boolean recursive;










    /**
     *  Constructeur DeepAnse vide
     */
    public DeepAnse(){}

    /**
     *  Constructeur DeepAnse standard
     *
     *  @param id           L'id de la dépense de type long
     *  @param amount       Le montant de la dépense de type double
     *  @param date         La date de la dépense de type GregorianCalendar
     *  @param group        La rubrique de la dépense de type DeepAnseGroup
     *  @param comment      Le commentaire de la dépense de type String
     *  @param recursive    La récursivité ou non de la dépense de type boolean
     */
    public DeepAnse(long id, double amount, GregorianCalendar date, DeepAnseGroup group, String comment, boolean recursive) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.group = group;
        this.comment = comment;
        this.recursive = recursive;
    }










    /**
     *  Getter de l'id de la dépense
     *
     *  @return L'id de la dépense de type long
     */
    public long getId() {
        return id;
    }

    /**
     *  Getter du montant de la dépense
     *
     *  @return Le montant de la dépense de type double
     */
    public double getAmount() {
        return amount;
    }

    /**
     *  Getter de la date de la dépense
     *
     *  @return La date de la dépense de type GregorianCalendar
     */
    public GregorianCalendar getDate() {
        return date;
    }

    /**
     *  Getter de la rubrique de la dépense
     *
     *  @return La rubrique de la dépense de type DeepAnseGroup
     */
    public DeepAnseGroup getGroup() {
        return group;
    }

    /**
     *  Getter du commentare de la dépense
     *
     *  @return Le commentaire de la dépense de type String
     */
    public String getComment() {
        return comment;
    }

    /**
     *  Getter de la récursivité de la dépense
     *
     *  @return La récursivité ou non de la dépense de type boolean
     */
    public boolean isRecursive() {
        return recursive;
    }










    /**
     *  Setter de l'id de la dépense
     *
     *  @param id
     *      L'id de la dépense de type long
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *  Setter du montant de la dépense
     *
     *  @param amount
     *     Le montant de la dépense de type double
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     *  Setter de la date de la dépense
     *
     *  @param date
     *     La date de la dépense de type GregorianCalendar
     */
    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    /**
     *  Setter de la rubrique de la dépense
     *
     *  @param group
     *     La rubrique de la dépense de type DeepAnseGroup
     */
    public void setGroup(DeepAnseGroup group) {
        this.group = group;
    }

    /**
     *  Setter du commentaire de la dépense
     *
     *  @param comment
     *     Le commentaire de la dépense de type String
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     *  Setter de la récursivité de la dépense
     *
     *  @param recursive
     *     La récursivité ou non de la dépense de type boolean
     */
    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }


    @Override
    public String toString() {
        return "DeepAnse{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + Conversion.dateToString(date) +
                ", group=" + group.toString() +
                ", comment='" + comment + '\'' +
                ", recursive=" + recursive +
                '}';
    }
}
