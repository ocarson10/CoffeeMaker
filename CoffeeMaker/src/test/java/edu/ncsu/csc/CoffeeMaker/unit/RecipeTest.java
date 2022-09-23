package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;
import org.junit.Before;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.enums.IngredientType;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;
    


    @BeforeEach
    public void setup () {
    	service.deleteAll();

    }

    @Test
    @Transactional
    public void createRecipe () {
        Recipe r1 = new Recipe();

        r1.setName( "Delicious Coffee" );

        r1.setPrice( 50 );

        r1.addIngredient( new Ingredient( IngredientType.COFFEE, 10 ) );
        r1.addIngredient( new Ingredient( IngredientType.PUMPKIN_SPICE, 3 ) );
        r1.addIngredient( new Ingredient( IngredientType.MILK, 2 ) );

        service.save( r1 );

    }
    
    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        
        r1.setPrice( 1 );
        final Ingredient coffee = new Ingredient(IngredientType.COFFEE, 1); 
        r1.addIngredient(coffee);
        
        final Ingredient milk = new Ingredient(IngredientType.MILK, 0); 
        r1.addIngredient(milk);
        
        final Ingredient cream = new Ingredient(IngredientType.CREAM, 0); 
        r1.addIngredient(cream);
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Matcha" );
        r2.setPrice( 1 );
        final Ingredient matcha = new Ingredient(IngredientType.MATCHA, 2); 
        
        r2.addIngredient(matcha);
        coffee.setAmount(2);
        r2.addIngredient(coffee);
        
        milk.setAmount(4);
        r2.addIngredient(milk);

        cream.setAmount(3);
        r2.addIngredient(cream);
        
        service.save( r1 );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        r1.addIngredient( new Ingredient( IngredientType.COFFEE, -10 ) );
        r1.addIngredient( new Ingredient( IngredientType.PUMPKIN_SPICE, 0) );
        r1.addIngredient( new Ingredient( IngredientType.MILK, 0 ) );
        service.save(r1); 
        
        final Recipe r2 = new Recipe();
        r2.setName( "Matcha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( IngredientType.COFFEE, -2) );
        r2.addIngredient( new Ingredient( IngredientType.MILK, 0 ) );
        r2.addIngredient( new Ingredient( IngredientType.MATCHA, 0 ) );
        service.save(r2); 

        final List<Recipe> recipes = List.of( r1, r2 );

        try {
            service.saveAll( recipes );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

    }


    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        Recipe r1 = new Recipe();

        r1.setName( "Delicious Coffee" );

        r1.setPrice( 50 );

        r1.addIngredient( new Ingredient( IngredientType.COFFEE, 10 ) );
        r1.addIngredient( new Ingredient( IngredientType.PUMPKIN_SPICE, 3 ) );
        r1.addIngredient( new Ingredient( IngredientType.MILK, 2 ) );

        service.save( r1 );
        

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }
    

}
