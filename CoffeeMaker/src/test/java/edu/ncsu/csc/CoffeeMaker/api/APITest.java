package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private InventoryService      iService;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
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
    public void testAPI () throws Exception {

        String recipe1 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        if ( !recipe1.contains( "Mocha" ) ) {
            // create a new Mocha recipe

            final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
            final Ingredient i1 = new Ingredient( "Chocolate", 2 );
            final Ingredient i2 = new Ingredient( "Coffee", 3 );
            final Ingredient i3 = new Ingredient( "Milk", 4 );
            final Ingredient i4 = new Ingredient( "Sugar", 6 );
            ingredients.add( i1 );
            ingredients.add( i2 );
            ingredients.add( i3 );
            ingredients.add( i4 );
            final Recipe r1 = createRecipe( "Mocha", 50, ingredients );
            // final Recipe r = new Recipe();
            // r.setChocolate( 2 );
            // r.setCoffee( 3 );
            // r.setMilk( 4 );
            // r.setName( "Mocha" );
            // r.setPrice( 5 );
            // r.setSugar( 6 );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        }

        recipe1 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        assertTrue( recipe1.contains(
                "Mocha" ) ); /* Make sure that now our recipe is there */

        final Inventory ivt = iService.getInventory();

        ivt.addIngredient( new Ingredient( "Chocolate", 50 ) );
        ivt.addIngredient( new Ingredient( "Coffee", 50 ) );
        ivt.addIngredient( new Ingredient( "Milk", 50 ) );
        ivt.addIngredient( new Ingredient( "Sugar", 50 ) );
        iService.save( ivt );
        // Assert.assertEquals( ivt.getIngredients().size(), 4 );

        // final Inventory i = new Inventory( 50, 50, 50, 50 );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ivt ) ) ).andExpect( status().isOk() );

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", "Mocha" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 100 ) ) ).andExpect( status().isOk() ).andDo( print() );

        mvc.perform( delete( String.format( "/api/v1/recipes/%s", "Mocha" ) ).contentType( MediaType.APPLICATION_JSON )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andDo( print() );

        // assertFalse( recipe.contains( "Mocha" ) );
    }
}
