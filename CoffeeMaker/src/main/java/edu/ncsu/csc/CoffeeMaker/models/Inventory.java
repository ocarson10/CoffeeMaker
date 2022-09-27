package edu.ncsu.csc.CoffeeMaker.models;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
    private Long             id;

    /**
     * List of Ingredients in inventory that can map to other classes
     */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> ingredientsList;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
        ingredientsList = new LinkedList<Ingredient>();
    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param ingredients list of ingredients that will contain the amount of each ingredient
     *            amt of ingredient
     */
    public Inventory ( final List<Ingredient> ingredients ) {
        ingredientsList = new LinkedList<Ingredient>();
        if ( ingredients == null ) {
            return;
        }
        else {
            for ( final Ingredient i : ingredients ) {
                ingredientsList.add( i );
            }
        }
    }
    // public Inventory ( final Integer ingredient ) {
    // setIngredient(ingredient);
    // }

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
    public List<Ingredient> getIngredients () {
        if ( ingredientsList.size() == 0 ) {
            return null;
        }
        return ingredientsList;
    }

    /**
     * Returns the current number of chocolate units in the inventory.
     * @param name of ingredient
     *
     * @return amount of chocolate
     */
    public Ingredient getIngredientsByName ( final String name ) {
        if ( ingredientsList.size() == 0 || name == null ) {
            return null;
        }
        final int index = findIngredientByName( name );
        return ingredientsList.get( index );
    }

    private Integer findIngredientByName ( final String name ) {
        if ( ingredientsList == null ) {
            return -1;
        }
        else {
            for ( int i = 0; i < ingredientsList.size(); i++ ) {
                if ( ingredientsList.get( i ).getName().equals( name ) ) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Sets the number of ingredient units in the inventory to the specified
     * amount. INGREDIENT MUST EXISTING IN INGREDIENTSLIST ALREADY.
     * @param name of ingredient
     * @param amtIngredient
     *            amount of ingredient to set
     */
    public void setIngredient ( final String name, final Integer amtIngredient ) {
        final Integer index = findIngredientByName( name );

        if ( index != -1 ) {
            if ( amtIngredient >= 0 ) {
                ingredientsList.get( index ).setAmount( amtIngredient );
            }
        }

    }

    /**
     * Sets the number of ingredient units in the inventory to the specified
     * amount. INGREDIENT MUST EXISTING IN INGREDIENTSLIST ALREADY. adds amount
     * onto preexisting amount.
     * @param name of ingredient
     * @param amtIngredient
     *            amount of ingredient to set
     */
    public void addIngredientAmount ( final String name, final Integer amtIngredient ) {
        final Integer index = findIngredientByName( name );

        if ( index != -1 ) {
            if ( amtIngredient >= 0 ) {
                ingredientsList.get( index ).setAmount( amtIngredient + ingredientsList.get( index ).getAmount() );
            }
        }

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
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        boolean isEnough = true;
        if ( ingredientsList.size() < r.getIngredients().size() ) {
            isEnough = false;
        }
        else {
            for ( final Ingredient i : r.getIngredients() ) {
                for ( final Ingredient in : ingredientsList ) {
                    if ( in.getName().equals( i.getName() ) ) {
                        if ( in.getAmount() < i.getAmount() ) {
                            isEnough = false;
                        }
                    }
                }
            }
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
            for ( final Ingredient i : r.getIngredients() ) {
                for ( final Ingredient in : ingredientsList ) {
                    if ( i.getName().equals( in.getName() ) ) {
                        in.setAmount( in.getAmount() - i.getAmount() );
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    // /**
    // * Adds ingredients to the inventory
    // *
    // * @param ingredientList
    // * list of ingredients - amount of ingredient
    // * @return true if successful, false if not
    // */
    // public boolean addIngredients ( final String name, final Integer amount )
    // {
    //
    // if ( amount < 0 ) {
    // throw new IllegalArgumentException( "Amount cannot be negative" );
    // }
    // if ( name != null ) {
    // final Ingredient result = new Ingredient( name, amount );
    //
    // if ( ingredientsList.size() != 0 ) {
    // for ( final Ingredient i : ingredientsList ) {
    // if ( i.getName().equals( name ) && i.getAmount().equals( amount ) ) {
    // return false;
    // }
    // }
    // }
    //
    // ingredientsList.add( result );
    // return true;
    // }
    // else {
    // throw new IllegalArgumentException( "Please enter name of Ingredient" );
    // }
    //
    // }
    //
    /**
     * Adds ingredients to the inventory
     *
     * @param ing Ingredient to be added to inventory
     *            list of ingredients - amount of ingredient
     * @return true if successful, false if not
     */
    public boolean addIngredient ( final Ingredient ing ) {

        if ( ing != null && ing.getAmount() >= 0 ) {
            ingredientsList.add( ing );
            return true;
        }
        return false;
    }

    // /**
    // * Adds ingredients to the inventory
    // *
    // * @param ingredient
    // * - amount of ingredient
    // * @return true if successful, false if not
    // */
    // public boolean addNewIngredients ( final Ingredient ing ) { // ing object
    // ingredientsList.add( ing );
    // // for ( final Ingredient i : ingredientsList ) {
    // // if ( i.getAmount() < 0 ) {
    // // throw new IllegalArgumentException( "Amount cannot be negative" );
    // // }
    // // ingredientsList.add(i);
    // // }
    // //
    //
    // return true;
    // }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( final Ingredient i : ingredientsList ) {
            buf.append( i.getName() + ": " );
            buf.append( i.getAmount() );
            buf.append( "\n" );
        }
        return buf.toString();
    }

}
