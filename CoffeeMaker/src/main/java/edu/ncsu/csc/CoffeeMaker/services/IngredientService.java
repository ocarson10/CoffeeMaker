package edu.ncsu.csc.CoffeeMaker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;

@Component
@Transactional
public class IngredientService extends Service<Ingredient, Long> {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    protected JpaRepository<Ingredient, Long> getRepository () {
        // TODO Auto-generated method stub
        return ingredientRepository;
    }

}
