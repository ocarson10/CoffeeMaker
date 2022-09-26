package edu.ncsu.csc.CoffeeMaker.models;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long                   id;

    /** Recipe name */
    private String                 name;

    /** Recipe price */
    @Min ( 0 )
    private Integer                price;

    /** List of new ingredients */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        ingredients = new LinkedList<Ingredient>();
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
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

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Adds an ingredient
     *
     * @param ing
     *            ingredient to add
     *
     */
    public void addIngredient ( final Ingredient ing ) {
    	if(ing != null && ing.getAmount() >= 0) {
        ingredients.add( ing );
    	}
    }
    

    private Integer findIngredientByName ( final String name ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getName().equals( name ) && name != null ) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Sets  ingredient amount
     *
     * @param name
     *            name of ingredient to be set
     * @param amount 
     * 			 amount of ingredient to be set for recipe             
     *
     */
    public void setIngredient (String name, Integer amount ) {
    	int index = findIngredientByName(name); 
    	ingredients.get(index).setAmount(amount);
    	
    }

    /**
     * Gets ingredient list
     *
     *
     * @return ingredient list
     *
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Returns the name of the recipe and the ingredients.
     *
     * @return String
     */
    @Override
    public String toString () {
        return name + " " + ingredients.toString();
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
