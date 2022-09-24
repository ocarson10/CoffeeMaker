package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update operations for the Ingredient.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {
	 /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private IngredientService service;
    
    /**
     * REST API method to provide GET access to all ingredients in the system
     *
     * @return JSON representation of all ingredients
     */
    @GetMapping ( BASE_PATH + "/ingredients" )
    public List<Ingredient> getIngredients () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific ingredient, as indicated
     * by the path variable provided (the name of the ingredient desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "ingredients/{name}" )
    public ResponseEntity getIngredient ( @PathVariable final String name ) {

        final Ingredient ingr = service.findByName( name );

        if ( null == ingr ) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        return new ResponseEntity( ingr, HttpStatus.OK );
    }
    

  
    /**
     * REST API method to provide POST access to the ingredient model. This is used
     * to create a new ingredient by automatically converting the JSON RequestBody
     * provided to a ingredient object. Invalid JSON will fail.
     *
     * @param ingredient
     *            The valid Recipe to be saved.
     * @return ResponseEntity indicating success if the ingredient could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingredient ) {
        if ( null != service.findByName( ingredient.getIngredient() ) ) {
            return new ResponseEntity( errorResponse( "Ingredient with the name " + ingredient.getIngredient() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        if ( service.findAll().size() < 3 ) {
            service.save( ingredient );
            return new ResponseEntity( successResponse( ingredient.getIngredient() + " successfully created" ), HttpStatus.OK );
        }
        else {
            return new ResponseEntity(
                    errorResponse( "Insufficient space in Ingredient book for ingredient " + ingredient.getIngredient() ),
                    HttpStatus.INSUFFICIENT_STORAGE );
        }

    }

    /**
     * REST API method to allow deleting a ingredient from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the ingredient to delete (as a path variable)
     *
     * @param name
     *            The name of the ingredient to delete
     * @return Success if the ingredient could be deleted; an error if the ingredient
     *         does not exist
     */
    @DeleteMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity deleteIngredient ( @PathVariable final String name ) {
        final Ingredient ingredient = service.findByName( name );
        if ( null == ingredient ) {
            return new ResponseEntity( errorResponse( "No ingredient found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( ingredient );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }
}
