package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long    id;
    /** amount of ingredient */
    @Min ( 0 )
    private Integer ingredient;
   

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param ingredient
     *            amt of ingredient
     */
    public Inventory ( final Integer ingredient ) {
        setIngredient(ingredient); 
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the current number of chocolate units in the inventory.
     *
     * @return amount of chocolate
     */
    public Integer getIngredient () {
        return ingredient;
    }

    /**
     * Sets the number of ingredient units in the inventory to the specified
     * amount.
     *
     * @param amtIngredient
     *     amount of ingredient to set
     */
    public void setIngredient ( Integer amtIngredient ) {
        if ( amtIngredient >= 0 ) {
            ingredient = amtIngredient;
        }
    }


    /**
     * Add the number of ingredient units in the inventory to the current amount of
     * ingredient units.
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
     * Sets the number of milk units in the inventory to the specified amount.
     *
     * @param amtMilk
     *            amount of milk to set
     */
    public void setMilk ( final Integer amtMilk ) {
        if ( amtMilk >= 0 ) {
            milk = amtMilk;
        }
    }

    /**
     * Add the number of milk units in the inventory to the current amount of
     * milk units.
     *
     * @param milk
     *            amount of milk
     * @return checked amount of milk
     * @throws IllegalArgumentException
     *             if the parameter isn't a positive integer
     */
    public Integer checkMilk ( final String milk ) throws IllegalArgumentException {
        Integer amtMilk = 0;
        try {
            amtMilk = Integer.parseInt( milk );
        }
        catch ( final NumberFormatException e ) {
            throw new IllegalArgumentException( "Units of milk must be a positive integer" );
        }
        if ( amtMilk < 0 ) {
            throw new IllegalArgumentException( "Units of milk must be a positive integer" );
        }

        return amtMilk;
    }

    /**
     * Returns the current number of sugar units in the inventory.
     *
     * @return int
     */
    public Integer getSugar () {
        return sugar;
    }

    /**
     * Sets the number of sugar units in the inventory to the specified amount.
     *
     * @param amtSugar
     *            amount of sugar to set
     */
    public void setSugar ( final Integer amtSugar ) {
        if ( amtSugar >= 0 ) {
            sugar = amtSugar;
        }
    }

    /**
     * Add the number of sugar units in the inventory to the current amount of
     * sugar units.
     *
     * @param sugar
     *            amount of sugar
     * @return checked amount of sugar
     * @throws IllegalArgumentException
     *             if the parameter isn't a positive integer
     */
    public Integer checkSugar ( final String sugar ) throws IllegalArgumentException {
        Integer amtSugar = 0;
        try {
            amtSugar = Integer.parseInt( sugar );
        }
        catch ( final NumberFormatException e ) {
            throw new IllegalArgumentException( "Units of sugar must be a positive integer" );
        }
        if ( amtSugar < 0 ) {
            throw new IllegalArgumentException( "Units of sugar must be a positive integer" );
        }

        return amtSugar;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        boolean isEnough = true;
        if ( coffee < r.getCoffee() ) {
            isEnough = false;
        }
        if ( milk < r.getMilk() ) {
            isEnough = false;
        }
        if ( sugar < r.getSugar() ) {
            isEnough = false;
        }
        if ( chocolate < r.getChocolate() ) {
            isEnough = false;
        }
        return isEnough;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            setCoffee( coffee - r.getCoffee() );
            setMilk( milk - r.getMilk() );
            setSugar( sugar - r.getSugar() );
            setChocolate( chocolate - r.getChocolate() );
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param coffee
     *            amt of coffee
     * @param milk
     *            amt of milk
     * @param sugar
     *            amt of sugar
     * @param chocolate
     *            amt of chocolate
     * @return true if successful, false if not
     */
    public boolean addIngredients ( final Integer coffee, final Integer milk, final Integer sugar,
            final Integer chocolate ) {
        if ( coffee < 0 || milk < 0 || sugar < 0 || chocolate < 0 ) {
            throw new IllegalArgumentException( "Amount cannot be negative" );
        }

        setCoffee( this.coffee + coffee );
        setMilk( this.milk + milk );
        setSugar( this.sugar + sugar );
        setChocolate( this.chocolate + chocolate );

        return true;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        buf.append( "Coffee: " );
        buf.append( getCoffee() );
        buf.append( "\n" );
        buf.append( "Milk: " );
        buf.append( getMilk() );
        buf.append( "\n" );
        buf.append( "Sugar: " );
        buf.append( getSugar() );
        buf.append( "\n" );
        buf.append( "Chocolate: " );
        buf.append( getChocolate() );
        buf.append( "\n" );
        return buf.toString();
    }

}
