package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * This class will handle and maintain Ingredients to be added to any Recipes 
 * @author csriniv, orcarson, and tttruon5
 *
 */
@Entity
public class Ingredient extends DomainObject {

	/** id of type long*/
    @Id
    @GeneratedValue
    private Long id;

    /**
     * amount of type Integer
     */
    private Integer amount;
    
    /**
     * Ingredient's name
     */
    private String ingredient;

    /**
     * Constructor method that will initiate the amount of ingredient 
     * @param name of ingredient to be added
     * @param amount of type Integer
     */
    public Ingredient (  String name,  Integer amount ) {
        super();
    	setAmount(amount);
        this.ingredient = name;
       
    }

    /**
     * Calls to the parent DomainObject class
     */
    public Ingredient () {
        super();
    }

    /**
     * Retrieves name of ingredient
     * @return names of Ingredient
     */
    public String getIngredient () {
        return ingredient;
    }

    /**
     * sets name of ingredient
     * @param ingredient name to be set
     */
    public void setIngredient ( String ingredient ) {
        this.ingredient = ingredient;
    }
    
   
    	
       
    /**
     * returns the amount of the ingredient
     * @return amount of ingredient
     */
    public Integer getAmount () {
        return amount;
    }

    /**
     * Sets the amount of the ingredient
     * @param amount of ingredient
     */
    public void setAmount ( final Integer amount ) {
    	 if ( amount >= 0 ) {
    		 this.amount = amount;
         }
    }

    /**
     * Sets id of the ingredient
     * @param id of ingredient
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    @Override
    public Long getId () {
        return id;
    }

    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + ingredient + ", amount=" + amount + "]";
    }

}
