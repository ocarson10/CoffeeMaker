package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIIngredientTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService         service;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

    }

    @Test
    @Transactional
    public void ensureIngredient () throws Exception {
        service.deleteAll();
        //final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
        final Ingredient i1 = new Ingredient( "Matcha", 100 );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1) ) ).andExpect( status().isOk() );

    }


    @Test
    @Transactional
    public void testDuplicateIngredient () throws Exception {

        /* Tests a ingredient with a duplicate name to make sure it's rejected */

        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        final Ingredient i2 = new Ingredient( "Matcha", 100 );
        service.save(i1);
        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) ).andExpect( status().is4xxClientError() );

        Assert.assertEquals( "There should only one ingredient in the CoffeeMaker", 1, service.findAll().size() );
    }
    
    @Test
    @Transactional
    public void testDeleteIngredient () throws Exception {

        /* Tests a ingredient with a duplicate name to make sure it's rejected */

        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        final Ingredient i2 = new Ingredient( "Milk", 100 );
        service.save(i1);
        service.save(i2);

        assertEquals(2, service.count());
        mvc.perform( delete( "/api/v1/ingredient/{name}", "Matcha" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1) ) ).andExpect( status().isOk() );
        
        assertEquals(1, service.count());

    }

    // Chitra Srinivasan (csriniv) milestone 1 individual test #2
    @Test
    @Transactional
    public void testGetIngredient1 () throws Exception {
        final String ingredient = mvc.perform( get( "/api/v1/ingredient" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        if ( !ingredient.contains( "Matcha" ) ) {

            final Ingredient i1 = new Ingredient( "Matcha", 100 );

            mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( i1 ) ) ).andExpect( status().isOk() );
        }

        mvc.perform( get( String.format( "/api/v1/ingredient/%s", "Matcha" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isOk() );
    }

}
