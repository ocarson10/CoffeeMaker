package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIRecipeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RecipeService         service;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

    }

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();
        final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        final Ingredient i2 = new Ingredient( "Milk", 100 );
        ingredients.add( i1 );
        ingredients.add( i2 );

        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, ingredients );
        // service.save( r1 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        final Ingredient i2 = new Ingredient( "Milk", 100 );
        ingredients.add( i1 );
        ingredients.add( i2 );

        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, ingredients );
        // service.save( r1 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) );

        Assert.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        final Ingredient i2 = new Ingredient( "Milk", 100 );
        ingredients.add( i1 );
        ingredients.add( i2 );

        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, ingredients );
        service.save( r1 );

        final Recipe r2 = createRecipe( name, 50, ingredients );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assert.assertEquals( "There should only one recipe in the CoffeeMaker", 1, service.findAll().size() );
    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        final Ingredient i2 = new Ingredient( "Milk", 100 );
        ingredients.add( i1 );
        ingredients.add( i2 );

        final LinkedList<Ingredient> ingredients2 = new LinkedList<Ingredient>();
        final Ingredient i3 = new Ingredient( "Mocha", 100 );
        final Ingredient i4 = new Ingredient( "Milk", 100 );
        ingredients2.add( i3 );
        ingredients2.add( i4 );

        final LinkedList<Ingredient> ingredients3 = new LinkedList<Ingredient>();
        final Ingredient i5 = new Ingredient( "Coffee", 100 );
        final Ingredient i6 = new Ingredient( "Milk", 100 );
        ingredients3.add( i5 );
        ingredients3.add( i6 );

        final LinkedList<Ingredient> ingredients4 = new LinkedList<Ingredient>();
        final Ingredient i7 = new Ingredient( "Chocolate", 100 );
        final Ingredient i8 = new Ingredient( "Milk", 100 );
        ingredients4.add( i7 );
        ingredients4.add( i8 );
        final Recipe r1 = createRecipe( "Coffee", 50, ingredients );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, ingredients2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, ingredients3 );
        service.save( r3 );

        Assert.assertEquals( "Creating three recipes should result in three recipes in the database", 3,
                service.count() );

        final Recipe r4 = createRecipe( "Hot Chocolate", 75, ingredients4 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assert.assertEquals( "Creating a fourth recipe should not get saved", 3, service.count() );
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

    // Chitra Srinivasan (csriniv) milestone 1 individual test #1
    @Test
    @Transactional
    public void testGetInventory1 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        final Ingredient i2 = new Ingredient( "Milk", 100 );
        ingredients.add( i1 );
        ingredients.add( i2 );

        final LinkedList<Ingredient> ingredients2 = new LinkedList<Ingredient>();
        final Ingredient i3 = new Ingredient( "Mocha", 100 );
        final Ingredient i4 = new Ingredient( "Milk", 100 );
        ingredients2.add( i3 );
        ingredients2.add( i4 );

        final LinkedList<Ingredient> ingredients3 = new LinkedList<Ingredient>();
        final Ingredient i5 = new Ingredient( "Coffee", 100 );
        final Ingredient i6 = new Ingredient( "Milk", 100 );
        ingredients3.add( i5 );
        ingredients3.add( i6 );

        final LinkedList<Ingredient> ingredients4 = new LinkedList<Ingredient>();
        final Ingredient i7 = new Ingredient( "Chocolate", 100 );
        final Ingredient i8 = new Ingredient( "Milk", 100 );
        ingredients4.add( i7 );
        ingredients4.add( i8 );

        final Recipe r1 = createRecipe( "Coffee", 50, ingredients );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, ingredients2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, ingredients3 );
        service.save( r3 );

        Assert.assertEquals( "Creating three recipes should result in three recipes in the database", 3,
                service.count() );

        // final Recipe r4 = createRecipe( "Hot Chocolate", 75, 0, 2, 1, 2 );

        mvc.perform( get( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r3 ) ) ).andExpect( status().isOk() );
    }

    // Chitra Srinivasan (csriniv) milestone 1 individual test #2
    @Test
    @Transactional
    public void testGetRecipe1 () throws Exception {
        final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        if ( !recipe.contains( "Coffee" ) ) {

            final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
            final Ingredient i1 = new Ingredient( "Matcha", 100 );
            final Ingredient i2 = new Ingredient( "Milk", 100 );
            ingredients.add( i1 );
            ingredients.add( i2 );

            final Recipe r1 = createRecipe( "Coffee", 50, ingredients );
            // service.save( r1 );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );
        }

        mvc.perform( get( String.format( "/api/v1/recipes/%s", "Coffee" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) ).andExpect( status().isOk() );
    }

}
