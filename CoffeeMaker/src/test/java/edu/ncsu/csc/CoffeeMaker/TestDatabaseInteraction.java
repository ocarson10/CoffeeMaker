package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

public class TestDatabaseInteraction {

    @Autowired
    private RecipeService recipeService;

    @Test
    @Transactional
    public void testRecipes () {
        /* We'll fill this out in a bit */
        recipeService.deleteAll();
        final Recipe r = new Recipe();

        /* set fields here */
        r.setName( "Mocha" );
        r.setPrice( 350 );
        r.setCoffee( 2 );
        r.setSugar( 1 );
        r.setMilk( 1 );
        r.setChocolate( 1 );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r.getName(), dbRecipe.getName() );
        /* Other fields would get tested one at a time here too */

        assertEquals( r.getPrice(), dbRecipe.getPrice() );
        assertEquals( r.getCoffee(), dbRecipe.getCoffee() );
        assertEquals( r.getSugar(), dbRecipe.getSugar() );
        assertEquals( r.getMilk(), dbRecipe.getMilk() );
        assertEquals( r.getChocolate(), dbRecipe.getChocolate() );

        final Recipe dbRecipeByName = recipeService.findByName( "Mocha" );

        assertEquals( r.getChocolate(), dbRecipeByName.getChocolate() );

    }
}
