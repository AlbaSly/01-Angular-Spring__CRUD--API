package com.raxelarias._spring_crud_api.controllers;

import com.raxelarias._spring_crud_api.models.Tutorial;
import com.raxelarias._spring_crud_api.repositories.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing all related to Tutorial CRUD operations
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tutorials")
public class TutorialController {

    /**
     * Dependency Injection (TutorialRepository)
     */
    @Autowired
    TutorialRepository tutorialRepository;

    /**
     * Get all tutorials from the TutorialsRepository
     * @param title If required, this will return all tutorials whose title coincides with the proportioned title.
     * @return An HTTP response with a List of Tutorial
     */
    @GetMapping("/all")
    public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {

        try {
            List<Tutorial> tutorials = new ArrayList<Tutorial>();

            if (title == null) title = "";

            this.tutorialRepository.findByTitleContainingIgnoreCase(title).forEach(tutorials::add);

            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Get a tutorial according its id
     * @param id Tutorial id
     * @return An HTTP response with the obtained Tutorial
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {

        try {
            Optional<Tutorial> tutorialFound = this.tutorialRepository.findById(id);
            if (tutorialFound.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(tutorialFound.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Search and filters all Tutorials where the published status is TRUE
     * @return An HTTP response with a List of Tutorial
     */
    @GetMapping("/published")
    public ResponseEntity<List<Tutorial>> getPublishedTutorials() {

        try {
            List<Tutorial> tutorials = this.tutorialRepository.findByPublished(true);

            if (tutorials.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Creates and saves a new Tutorial object in the database
     * @param tutorial Tutorial object
     * @return An HTTP response with the created Tutorial
     */
    @PostMapping("/create")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {

        try {
            Tutorial _tutorial = this.tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
            return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Updates an existing Tutorial object in the database according its id
     * @param id Tutorial id
     * @param tutorial Tutorial object with the updated data
     * @return An HTTP response with the updated Tutorial
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {

        try {
            Optional<Tutorial> tutorialFound = this.tutorialRepository.findById(id);

            if (tutorialFound.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            Tutorial _tutorial = tutorialFound.get();
            _tutorial.setTitle(tutorial.getTitle());
            _tutorial.setDescription(tutorial.getDescription());
            _tutorial.setPublished(tutorial.isPublished());

            return new ResponseEntity<>(this.tutorialRepository.save(_tutorial), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Deletes an existing tutorial according its id
     * @param id Tutorial id
     * @return An HTTP response without content
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Tutorial> deleteTutorial(@PathVariable("id") long id) {

        try {
            this.tutorialRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Deletes all tutorials from the database
     * @return An HTTP response without content
     */
    @DeleteMapping("/delete/all")
    public ResponseEntity<HttpStatus> deleteAllTutorials() {

        try {
            this.tutorialRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
