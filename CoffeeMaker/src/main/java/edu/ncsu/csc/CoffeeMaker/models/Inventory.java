package edu.ncsu.csc.CoffeeMaker.models;

import java.util.List;

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
  
    private List<Ingredient> ingredientsList;

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
    public Integer getIngredient (String name) {
    	Integer index =findIngredientByName(name);
    	Ingredient ing = null;
    	if(index >= 0) {
    		 ing = ingredientsList.get(index);
        	if(ing != null) {
        		return ing.getAmount();
        	}
    	}
    	
       return null; 
    }

    private Integer findIngredientByName(String name) {
    	for(int i = 0 ; i < ingredientsList.size(); i++) {
    		if(ingredientsList.get(i).getName().equals(name) && name != null){
    			return i;
    		}
    	}
    	return -1;
    }
    /**
     * Sets the number of ingredient units in the inventory to the specified
     * amount.
     *
     * @param amtIngredient
     *     amount of ingredient to set
     */
    public void setIngredient ( String name, Integer amtIngredient ) {
    	Integer index = findIngredientByName(name);
    	if(index != null) {
    		 if ( amtIngredient >= 0 ) {
    	           ingredientsList.get(index).setAmount(amtIngredient);
    		 }
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
    public Integer checkIngredientAmount (final String amtIngredient ) throws IllegalArgumentException {
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
        for(int i = 0; i < r.getIngredients().size(); i++ ) {
        	Ingredient ing = r.getIngredients().get(i);
        	for( Ingredient in : ingredientsList) {
        		if(in.getName().equals(ing.getName())) {
        			if(in.getAmount() < ing.getAmount()) {
        				isEnough = false;
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
//        	setIngredient(ingredient - r.get)
        	for(int i = 0; i < r.getIngredients().size(); i++) {
        		Ingredient ing = r.getIngredients().get(i);
        		for(Ingredient in : ingredientsList) {
        			if(in.getName().equals(ing.getName())) {
        				
        			}
        		}
        	}
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredient - amount of ingredient
     * @return true if successful, false if not
     */
    public boolean addIngredients (final String name, final Integer amount) {
    	if(amount < 0) {
    		throw new IllegalArgumentException("Amount cannot be negative");
    	}
        Integer index = findIngredientByName(name);
        Integer amountAdd = ingredientsList.get(index).getAmount();
        setIngredient(name, amountAdd + amount);
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
        for(Ingredient i : ingredientsList) {
        buf.append(i.getName() + ": " );
        buf.append(i.getAmount() );
        buf.append( "\n" );
        }
        return buf.toString();
    }

}
