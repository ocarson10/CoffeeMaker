package edu.ncsu.csc.CoffeeMaker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;

@Component
@Transactional
public class IngredientService extends Service<Ingredient, Long> {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    protected JpaRepository<Ingredient, Long> getRepository () {
        return ingredientRepository;
    }
    
    /**
     * Find a recipe with the provided name
     * 
     * @param name
     *            Name of the recipe to find
     * @return found recipe, null if none
     */
    public Ingredient findByName ( final String name ) {
        return ingredientRepository.findByName( name );
    }

}
