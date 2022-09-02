package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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

        // edit recipe
        dbRecipe.setPrice( 12 );
        dbRecipe.setSugar( 2 );
        dbRecipe.setMilk( 14 );
        recipeService.save( dbRecipe );

        assertEquals( 1, recipeService.count() );

        // verify edited recipe
        assertEquals( 12, (int) recipeService.findAll().get( 0 ).getPrice() );

        assertEquals( 2, (int) recipeService.findAll().get( 0 ).getSugar() );
        assertEquals( 14, (int) recipeService.findAll().get( 0 ).getMilk() );

        // test delete

        recipeService.delete( dbRecipe );

        // recipe list should be empty
        assertEquals( 0, recipeService.findAll().size() );

        // tests for deleting all recipes
        final Recipe r1 = new Recipe();
        final Recipe r2 = new Recipe();
        final Recipe r3 = new Recipe();

        /* set fields here */
        r1.setName( "Mocha" );
        r1.setPrice( 350 );
        r1.setCoffee( 2 );
        r1.setSugar( 1 );
        r1.setMilk( 1 );
        r1.setChocolate( 1 );
        recipeService.save( r1 );

        r2.setName( "Coffee" );
        r2.setPrice( 360 );
        r2.setCoffee( 3 );
        r2.setSugar( 4 );
        r2.setMilk( 2 );
        r2.setChocolate( 3 );
        recipeService.save( r2 );

        r3.setName( "Latte" );
        r3.setPrice( 370 );
        r3.setCoffee( 5 );
        r3.setSugar( 3 );
        r3.setMilk( 2 );
        r3.setChocolate( 2 );

        recipeService.save( r3 );

        final List<Recipe> dbAllRecipes = recipeService.findAll();

        assertEquals( 3, dbAllRecipes.size() );

        assertEquals( "Mocha", dbAllRecipes.get( 0 ).getName() );
        assertEquals( "Coffee", dbAllRecipes.get( 1 ).getName() );
        assertEquals( "Latte", dbAllRecipes.get( 2 ).getName() );

        recipeService.delete( r1 );
        assertEquals( 2, recipeService.findAll().size() );
        assertEquals( "Coffee", recipeService.findAll().get( 0 ).getName() );

        recipeService.deleteAll();
        assertEquals( 0, recipeService.findAll().size() );

    }

    /**
     * Olivia Carson (orcarson) Milestone 1 : Individual Test #1 This test was
     * created to ensure that the existsById method from the Service abstract
     * class properly works.
     */

    @Test
    @Transactional
    public void testExistsById () {
        recipeService.deleteAll();

        final Recipe r1 = new Recipe();
        final Recipe r2 = new Recipe();
        final Recipe r3 = new Recipe();

        /* set fields here */
        r1.setName( "Mocha" );
        r1.setPrice( 350 );
        r1.setCoffee( 2 );
        r1.setSugar( 1 );
        r1.setMilk( 1 );
        r1.setChocolate( 1 );
        recipeService.save( r1 );

        r2.setName( "Coffee" );
        r2.setPrice( 360 );
        r2.setCoffee( 3 );
        r2.setSugar( 4 );
        r2.setMilk( 2 );
        r2.setChocolate( 3 );
        recipeService.save( r2 );

        r3.setName( "Latte" );
        r3.setPrice( 370 );
        r3.setCoffee( 5 );
        r3.setSugar( 3 );
        r3.setMilk( 2 );
        r3.setChocolate( 2 );
        recipeService.save( r3 );

        final long id1 = r1.getId();
        final long id2 = r2.getId();
        final long id3 = r3.getId();
        final long fakeId = id3 + 2;

        assertTrue( recipeService.existsById( id1 ) );
        assertTrue( recipeService.existsById( id2 ) );
        assertTrue( recipeService.existsById( id3 ) );
        assertFalse( recipeService.existsById( fakeId ) );

    }

    /**
     * Olivia Carson (orcarson) Milestone 1 : Individual Test #2 This test was
     * created to ensure that the findById method from the Service abstract
     * class properly works.
     */

    @Test
    @Transactional
    public void testFindById () {
        recipeService.deleteAll();

        final Recipe r1 = new Recipe();
        final Recipe r2 = new Recipe();
        final Recipe r3 = new Recipe();

        /* set fields here */
        r1.setName( "Mocha" );
        r1.setPrice( 350 );
        r1.setCoffee( 2 );
        r1.setSugar( 1 );
        r1.setMilk( 1 );
        r1.setChocolate( 1 );
        recipeService.save( r1 );

        r2.setName( "Coffee" );
        r2.setPrice( 360 );
        r2.setCoffee( 3 );
        r2.setSugar( 4 );
        r2.setMilk( 2 );
        r2.setChocolate( 3 );
        recipeService.save( r2 );

        r3.setName( "Latte" );
        r3.setPrice( 370 );
        r3.setCoffee( 5 );
        r3.setSugar( 3 );
        r3.setMilk( 2 );
        r3.setChocolate( 2 );
        recipeService.save( r3 );

        final long id1 = r1.getId();
        final long id2 = r2.getId();
        final long id3 = r3.getId();
        final long fakeId = id3 + 2;

        assertEquals( r1, recipeService.findById( id1 ) );
        assertEquals( r2, recipeService.findById( id2 ) );
        assertEquals( r3, recipeService.findById( id3 ) );
        assertNull( recipeService.findById( fakeId ) );
        assertNull( recipeService.findById( null ) );

    }

    /**
     * Olivia Carson (orcarson) Milestone 1 : Individual Test #3 This test was
     * created to ensure that the saveAll method from the Service abstract class
     * properly works.
     */
    @Test
    @Transactional
    public void testSaveAll () {
        recipeService.deleteAll();

        final Recipe r1 = new Recipe();
        final Recipe r2 = new Recipe();
        final Recipe r3 = new Recipe();

        /* set fields here */
        r1.setName( "Mocha" );
        r1.setPrice( 350 );
        r1.setCoffee( 2 );
        r1.setSugar( 1 );
        r1.setMilk( 1 );
        r1.setChocolate( 1 );
        // recipeService.save( r1 );

        r2.setName( "Coffee" );
        r2.setPrice( 360 );
        r2.setCoffee( 3 );
        r2.setSugar( 4 );
        r2.setMilk( 2 );
        r2.setChocolate( 3 );
        // recipeService.save( r2 );

        r3.setName( "Latte" );
        r3.setPrice( 370 );
        r3.setCoffee( 5 );
        r3.setSugar( 3 );
        r3.setMilk( 2 );
        r3.setChocolate( 2 );
        // recipeService.save( r3 );

        final List<Recipe> allRecipes = new ArrayList<Recipe>();

        allRecipes.add( r1 );
        allRecipes.add( r2 );
        allRecipes.add( r3 );

        recipeService.saveAll( allRecipes );

        final long id1 = r1.getId();
        final long id2 = r2.getId();
        final long id3 = r3.getId();

        assertEquals( r1, recipeService.findById( id1 ) );
        assertEquals( r2, recipeService.findById( id2 ) );
        assertEquals( r3, recipeService.findById( id3 ) );

    }

}
