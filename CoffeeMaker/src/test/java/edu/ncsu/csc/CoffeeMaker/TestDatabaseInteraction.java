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

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
/**
 * Tests the Databases and their functionality
 * @author csc326
 *
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {
	/** RecipeService to test for recipes*/
    @Autowired
    private RecipeService     recipeService;
	/** InventoryService to test for inventory*/
    @Autowired
    private InventoryService  iService;
	/** IngredientService to test for ingredients*/
    @Autowired
    private IngredientService ingredientService;

    /**
     * Tests adding and deleting recipes 
     */
    @Test
    @Transactional
    public void testRecipes () {
        /* We'll fill this out in a bit */
        recipeService.deleteAll();

        final Inventory ivt = iService.getInventory();

        ivt.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt );

        final Recipe recipe = new Recipe();
        recipe.setName( "Mocha" );
        final Ingredient i1 = new Ingredient( "Coffee", 2 );
        final Ingredient i2 = new Ingredient( "Sugar", 4 );
        final Ingredient i3 = new Ingredient( "Milk", 1 );
        final Ingredient i4 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i1 );
        ingredientService.save( i2 );
        ingredientService.save( i3 );
        ingredientService.save( i4 );
        recipe.addIngredient( i1 );
        recipe.addIngredient( i2 );
        recipe.addIngredient( i3 );
        recipe.addIngredient( i4 );

        recipe.setPrice( 5 );
        recipeService.save( recipe );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( recipe.getName(), dbRecipe.getName() );
        /* Other fields would get tested one at a time here too */

        assertEquals( recipe.getPrice(), dbRecipe.getPrice() );
        assertEquals( recipe.getIngredients(), dbRecipe.getIngredients() );

        final Recipe dbRecipeByName = recipeService.findByName( "Mocha" );

        // assertEquals( r.getChocolate(), dbRecipeByName.getChocolate() );

        // edit recipe
        dbRecipe.setPrice( 12 );
        dbRecipe.setIngredient( "Sugar", 2 );
        dbRecipe.setIngredient( "Milk", 14 );

        recipeService.save( dbRecipe );

        assertEquals( 1, recipeService.count() );

        // verify edited recipe
        assertEquals( 12, (int) recipeService.findAll().get( 0 ).getPrice() );

        assertEquals( 2, (int) recipeService.findAll().get( 0 ).getIngredients().get( 1 ).getAmount() );
        assertEquals( 14, (int) recipeService.findAll().get( 0 ).getIngredients().get( 2 ).getAmount() );

        // test delete

        recipeService.delete( dbRecipe );

        // recipe list should be empty
        assertEquals( 0, recipeService.findAll().size() );

        // tests for deleting all recipes
        final Recipe r1 = new Recipe();

        final Inventory ivt1 = iService.getInventory();

        ivt1.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt1.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt1.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt1.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt1 );

        r1.setName( "Mocha" );
        final Ingredient i5 = new Ingredient( "Coffee", 2 );
        final Ingredient i6 = new Ingredient( "Sugar", 4 );
        final Ingredient i7 = new Ingredient( "Milk", 1 );
        final Ingredient i8 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i5 );
        ingredientService.save( i6 );
        ingredientService.save( i7 );
        ingredientService.save( i8 );
        r1.addIngredient( i5 );
        r1.addIngredient( i6 );
        r1.addIngredient( i7 );
        r1.addIngredient( i8 );

        r1.setPrice( 5 );
        recipeService.save( r1 );

        final Recipe r2 = new Recipe();

        final Inventory ivt2 = iService.getInventory();

        ivt2.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt2.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt2.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt2.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt2 );

        r2.setName( "Coffee" );
        final Ingredient i9 = new Ingredient( "Coffee", 2 );
        final Ingredient i10 = new Ingredient( "Sugar", 4 );
        final Ingredient i11 = new Ingredient( "Milk", 1 );
        final Ingredient i12 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i9 );
        ingredientService.save( i10 );
        ingredientService.save( i11 );
        ingredientService.save( i12 );
        r2.addIngredient( i9 );
        r2.addIngredient( i10 );
        r2.addIngredient( i11 );
        r2.addIngredient( i12 );

        r2.setPrice( 5 );
        recipeService.save( r2 );

        final Recipe r3 = new Recipe();

        final Inventory ivt3 = iService.getInventory();

        ivt3.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt3.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt3.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt3.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt3 );

        r3.setName( "Latte" );
        final Ingredient i13 = new Ingredient( "Coffee", 2 );
        final Ingredient i14 = new Ingredient( "Sugar", 4 );
        final Ingredient i15 = new Ingredient( "Milk", 1 );
        final Ingredient i16 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i13 );
        ingredientService.save( i14 );
        ingredientService.save( i15 );
        ingredientService.save( i16 );
        r3.addIngredient( i13 );
        r3.addIngredient( i14 );
        r3.addIngredient( i15 );
        r3.addIngredient( i16 );

        r3.setPrice( 5 );
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

        final Inventory ivt1 = iService.getInventory();

        ivt1.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt1.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt1.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt1.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt1 );

        r1.setName( "Mocha" );
        final Ingredient i5 = new Ingredient( "Coffee", 2 );
        final Ingredient i6 = new Ingredient( "Sugar", 4 );
        final Ingredient i7 = new Ingredient( "Milk", 1 );
        final Ingredient i8 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i5 );
        ingredientService.save( i6 );
        ingredientService.save( i7 );
        ingredientService.save( i8 );
        r1.addIngredient( i5 );
        r1.addIngredient( i6 );
        r1.addIngredient( i7 );
        r1.addIngredient( i8 );

        r1.setPrice( 5 );
        recipeService.save( r1 );

        final Recipe r2 = new Recipe();

        final Inventory ivt2 = iService.getInventory();

        ivt2.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt2.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt2.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt2.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt2 );

        r2.setName( "Coffee" );
        final Ingredient i9 = new Ingredient( "Coffee", 2 );
        final Ingredient i10 = new Ingredient( "Sugar", 4 );
        final Ingredient i11 = new Ingredient( "Milk", 1 );
        final Ingredient i12 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i9 );
        ingredientService.save( i10 );
        ingredientService.save( i11 );
        ingredientService.save( i12 );
        r2.addIngredient( i9 );
        r2.addIngredient( i10 );
        r2.addIngredient( i11 );
        r2.addIngredient( i12 );

        r2.setPrice( 5 );
        recipeService.save( r2 );

        final Recipe r3 = new Recipe();

        final Inventory ivt3 = iService.getInventory();

        ivt3.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt3.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt3.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt3.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt3 );

        r3.setName( "Latte" );
        final Ingredient i13 = new Ingredient( "Coffee", 2 );
        final Ingredient i14 = new Ingredient( "Sugar", 4 );
        final Ingredient i15 = new Ingredient( "Milk", 1 );
        final Ingredient i16 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i13 );
        ingredientService.save( i14 );
        ingredientService.save( i15 );
        ingredientService.save( i16 );
        r3.addIngredient( i13 );
        r3.addIngredient( i14 );
        r3.addIngredient( i15 );
        r3.addIngredient( i16 );

        r3.setPrice( 5 );
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

        final Inventory ivt1 = iService.getInventory();

        ivt1.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt1.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt1.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt1.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt1 );

        r1.setName( "Mocha" );
        final Ingredient i5 = new Ingredient( "Coffee", 2 );
        final Ingredient i6 = new Ingredient( "Sugar", 4 );
        final Ingredient i7 = new Ingredient( "Milk", 1 );
        final Ingredient i8 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i5 );
        ingredientService.save( i6 );
        ingredientService.save( i7 );
        ingredientService.save( i8 );
        r1.addIngredient( i5 );
        r1.addIngredient( i6 );
        r1.addIngredient( i7 );
        r1.addIngredient( i8 );

        r1.setPrice( 5 );
        recipeService.save( r1 );

        final Recipe r2 = new Recipe();

        final Inventory ivt2 = iService.getInventory();

        ivt2.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt2.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt2.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt2.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt2 );

        r2.setName( "Coffee" );
        final Ingredient i9 = new Ingredient( "Coffee", 2 );
        final Ingredient i10 = new Ingredient( "Sugar", 4 );
        final Ingredient i11 = new Ingredient( "Milk", 1 );
        final Ingredient i12 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i9 );
        ingredientService.save( i10 );
        ingredientService.save( i11 );
        ingredientService.save( i12 );
        r2.addIngredient( i9 );
        r2.addIngredient( i10 );
        r2.addIngredient( i11 );
        r2.addIngredient( i12 );

        r2.setPrice( 5 );
        recipeService.save( r2 );

        final Recipe r3 = new Recipe();

        final Inventory ivt3 = iService.getInventory();

        ivt3.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt3.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt3.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt3.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt3 );

        r3.setName( "Latte" );
        final Ingredient i13 = new Ingredient( "Coffee", 2 );
        final Ingredient i14 = new Ingredient( "Sugar", 4 );
        final Ingredient i15 = new Ingredient( "Milk", 1 );
        final Ingredient i16 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i13 );
        ingredientService.save( i14 );
        ingredientService.save( i15 );
        ingredientService.save( i16 );
        r3.addIngredient( i13 );
        r3.addIngredient( i14 );
        r3.addIngredient( i15 );
        r3.addIngredient( i16 );

        r3.setPrice( 5 );
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

        final Inventory ivt1 = iService.getInventory();

        ivt1.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt1.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt1.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt1.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt1 );

        r1.setName( "Mocha" );
        final Ingredient i5 = new Ingredient( "Coffee", 2 );
        final Ingredient i6 = new Ingredient( "Sugar", 4 );
        final Ingredient i7 = new Ingredient( "Milk", 1 );
        final Ingredient i8 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i5 );
        ingredientService.save( i6 );
        ingredientService.save( i7 );
        ingredientService.save( i8 );
        r1.addIngredient( i5 );
        r1.addIngredient( i6 );
        r1.addIngredient( i7 );
        r1.addIngredient( i8 );

        r1.setPrice( 5 );
        recipeService.save( r1 );

        final Recipe r2 = new Recipe();

        final Inventory ivt2 = iService.getInventory();

        ivt2.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt2.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt2.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt2.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt2 );

        r2.setName( "Coffee" );
        final Ingredient i9 = new Ingredient( "Coffee", 2 );
        final Ingredient i10 = new Ingredient( "Sugar", 4 );
        final Ingredient i11 = new Ingredient( "Milk", 1 );
        final Ingredient i12 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i9 );
        ingredientService.save( i10 );
        ingredientService.save( i11 );
        ingredientService.save( i12 );
        r2.addIngredient( i9 );
        r2.addIngredient( i10 );
        r2.addIngredient( i11 );
        r2.addIngredient( i12 );

        r2.setPrice( 5 );
        recipeService.save( r2 );

        final Recipe r3 = new Recipe();

        final Inventory ivt3 = iService.getInventory();

        ivt3.addIngredient( new Ingredient( "Coffee", 5 ) );
        ivt3.addIngredient( new Ingredient( "Sugar", 5 ) );
        ivt3.addIngredient( new Ingredient( "Milk", 5 ) );
        ivt3.addIngredient( new Ingredient( "Chocolate", 5 ) );

        iService.save( ivt3 );

        r3.setName( "Latte" );
        final Ingredient i13 = new Ingredient( "Coffee", 2 );
        final Ingredient i14 = new Ingredient( "Sugar", 4 );
        final Ingredient i15 = new Ingredient( "Milk", 1 );
        final Ingredient i16 = new Ingredient( "Chocolate", 1 );
        ingredientService.save( i13 );
        ingredientService.save( i14 );
        ingredientService.save( i15 );
        ingredientService.save( i16 );
        r3.addIngredient( i13 );
        r3.addIngredient( i14 );
        r3.addIngredient( i15 );
        r3.addIngredient( i16 );

        r3.setPrice( 5 );
        recipeService.save( r3 );

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
