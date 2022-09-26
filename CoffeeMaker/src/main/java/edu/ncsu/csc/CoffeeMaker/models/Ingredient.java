package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * This class will handle and maintain Ingredients to be added to any Recipes
 *
 * @author csriniv, orcarson, and tttruon5
 *
 */
@Entity
public class Ingredient extends DomainObject {

    /** id of type long */
    @Id
    @GeneratedValue
    private Long    id;

    /**
     * amount of type Integer
     */
    @Min ( 0 )
    private Integer amount;

    /**
     * Ingredient's name
     */
    private String  name;

    /**
     * Constructor method that will initiate the amount of ingredient
     *
     * @param name
     *            of ingredient to be added
     * @param amount
     *            of type Integer
     */
    public Ingredient ( final String name, final Integer amount ) {
        super();
        if ( amount >= 0 ) {
            setAmount( amount );
            setName( name );
        }
        else {
            throw new IllegalArgumentException( "Units of ingredient must be a positive integer" );
        }

    }

    /**
     * Calls to the parent DomainObject class
     */
    public Ingredient () {
        super();
    }

    /**
     * Retrieves name of ingredient
     *
     * @return names of Ingredient
     */
    public String getName () {
        return name;
    }

    /**
     * sets name of ingredient
     *
     * @param name
     *            name to be set
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * returns the amount of the ingredient
     *
     * @return amount of ingredient
     */
    public Integer getAmount () {
        return amount;
    }

    /**
     * Sets the amount of the ingredient
     *
     * @param amount
     *            of ingredient
     */
    public void setAmount ( final Integer amount ) {

        this.amount = amount;

    }

    /**
     * Add the number of ingredient units in the inventory to the current amount
     * of ingredient units.
     *
     * @param amtIngredient
     *            amount of ingredient
     * @return checked amount of ingredient
     * @throws IllegalArgumentException
     *             if the parameter isn't a positive integer
     */
    public Integer checkIngredientAmount ( final String amtIngredient ) throws IllegalArgumentException {
        Integer ingredientAmt = 0;
        try {
            ingredientAmt = Integer.parseInt( amtIngredient );
        }
        catch ( final NumberFormatException e ) {
            throw new IllegalArgumentException( "Units of ingredient must be a positive integer" );
        }
        if ( ingredientAmt < 0 ) {
            throw new IllegalArgumentException( "Units of ingredient must be a positive integer" );
        }

        return ingredientAmt;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    @Override
    public Long getId () {
        return id;
    }

    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + name + ", amount=" + amount + "]";
    }

}
