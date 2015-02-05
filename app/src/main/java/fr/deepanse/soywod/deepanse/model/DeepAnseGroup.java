package fr.deepanse.soywod.deepanse.model;

/**
 * Created by soywod on 05/02/2015.
 *
 * DeepAnse modélise une rubrique de dépense, caractérisée par :
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
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#DeepAnseGroup(int, java.lang.String)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#getId()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#setId(int)
     */
    private int id;

    /**
     *  Le nom de la rubrique
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#DeepAnseGroup()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#DeepAnseGroup(int, java.lang.String)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnse#DeepAnse(int, double, java.util.GregorianCalendar, fr.deepanse.soywod.deepanse.model.DeepAnseGroup, java.lang.String, boolean)
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#getName()
     *  @see fr.deepanse.soywod.deepanse.model.DeepAnseGroup#setName(java.lang.String)
     */
    private String name;










    /**
     *  Constructeur DeepAnseGroup vide
     */
    public DeepAnseGroup(){}

    /**
     *  Constructeur DeepAnseGroup standard
     *
     *  @param id           L'id de la rubrique de type int
     *  @param name         Le nom de la rubrique de type String
     */
    public DeepAnseGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }










    /**
     *  Getter de l'id de la rubrique
     *
     *  @return L'id de la rubrique de type int
     */
    public int getId() {
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
     *  Setter de l'id de la dépense
     *
     *  @param id
     *      L'id de la rubrique de type int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *  Setter du montant de la dépense
     *
     *  @param name
     *     Le nom de la rubrique de type String
     */
    public void setName(String name) {
        this.name = name;
    }
}
