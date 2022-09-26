package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.LinkedList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
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

    private Recipe createRecipe ( final String name, final Integer price, final List<Ingredient> list ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );

        for ( int i = 0; i <= list.size() - 1; i++ ) {
            recipe.addIngredient( list.get( i ) );
        }

        return recipe;
    }

    @Test
    @Transactional
    public void testcreateRecipe () {
        final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        final Ingredient i2 = new Ingredient( "Milk", 100 );
        ingredients.add( i1 );
        ingredients.add( i2 );

        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, ingredients );

        service.save( r1 );

    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        final Ingredient i2 = new Ingredient( "Milk", 100 );
        ingredients.add( i1 );
        ingredients.add( i2 );

        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, ingredients );

        final LinkedList<Ingredient> ingredients2 = new LinkedList<Ingredient>();
        final Ingredient i3 = new Ingredient( "Mocha", 100 );
        final Ingredient i4 = new Ingredient( "Milk", 100 );
        ingredients2.add( i3 );
        ingredients2.add( i4 );
        final String name1 = "Mocha";
        final Recipe r2 = createRecipe( name1, 50, ingredients2 );

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

        final String name1 = "Tasty Drink";
        final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
        final Recipe r1 = createRecipe( name1, -50, ingredients );
        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        final Ingredient i2 = new Ingredient( "Milk", 100 );
        ingredients.add( i1 );
        ingredients.add( i2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name1 ),
                    "A recipe was able to be created with a negative price" );

        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

        final String name = "Mocha";
        final LinkedList<Ingredient> ingredients2 = new LinkedList<Ingredient>();
        final Recipe r2 = createRecipe( name, -50, ingredients2 );

        final Ingredient i3 = new Ingredient( "Mocha", 100 );
        final Ingredient i4 = new Ingredient( "Milk", 100 );
        ingredients2.add( i3 );
        ingredients2.add( i4 );

        try {
            service.save( r2 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

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
        final String name = "Mocha";
        final LinkedList<Ingredient> ingredients2 = new LinkedList<Ingredient>();
        final Recipe r2 = createRecipe( name, 50, ingredients2 );

        final Ingredient i3 = new Ingredient( "Mocha", 100 );
        final Ingredient i4 = new Ingredient( "Milk", 100 );
        ingredients2.add( i3 );
        ingredients2.add( i4 );
        service.save( r2 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r2 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void recipesNotEqual () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        final Ingredient i2 = new Ingredient( "Milk", 100 );
        ingredients.add( i1 );
        ingredients.add( i2 );

        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, ingredients );

        final LinkedList<Ingredient> ingredients2 = new LinkedList<Ingredient>();
        final Ingredient i3 = new Ingredient( "Mocha", 100 );
        final Ingredient i4 = new Ingredient( "Milk", 100 );
        ingredients2.add( i3 );
        ingredients2.add( i4 );

        final String name1 = "Mocha";
        final Recipe r2 = createRecipe( name1, 50, ingredients2 );

        final LinkedList<Ingredient> ingredients3 = new LinkedList<Ingredient>();
        final Ingredient i5 = new Ingredient( "Mocha", 100 );
        final Ingredient i6 = new Ingredient( "Milk", 100 );
        ingredients3.add( i5 );
        ingredients3.add( i6 );

        final String name2 = "Mocha";
        final Recipe r3 = createRecipe( name2, 50, ingredients3 );

        service.save( r1 );
        service.save( r2 );
        service.save( r3 );

        Assert.assertFalse( r1.equals( r2 ) );
        Assert.assertFalse( r1.toString().equals( r3.toString() ) );
        Assert.assertFalse( r1.hashCode() == r3.hashCode() );
    }

}
