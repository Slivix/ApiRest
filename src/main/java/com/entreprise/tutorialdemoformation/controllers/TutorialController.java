package com.entreprise.tutorialdemoformation.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entreprise.tutorialdemoformation.dao.TutorialDAO;
import com.entreprise.tutorialdemoformation.models.Tutorial;

@RestController
@RequestMapping(path = "/api")
public class TutorialController {
    
    @Autowired
    TutorialDAO tutorialDAO;

    @GetMapping(path = "/tutorials", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Tutorial>> getTutorials() {
        try {
            List<Tutorial> tutorialsList = this.tutorialDAO.findAll();

            if (!tutorialsList.isEmpty()) {
                return ResponseEntity.ok(tutorialsList);
            } 
        } catch (Exception e) {
            return new ResponseEntity<List<Tutorial>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return null;
    }

    @PostMapping(path = "/tutorials", produces = "application/json")
    public ResponseEntity<Tutorial> addTutorial(@RequestBody Tutorial tutorial) {
        try {
            Tutorial tutorialSave = this.tutorialDAO.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), tutorial.isPublish()));
            return new ResponseEntity<>(tutorialSave, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<Tutorial>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/tutorials/{id}",produces = "application/json")
    public ResponseEntity<Tutorial> getById (@PathVariable Long id){
        try{
            Optional<Tutorial> retrievedTutoriel = this.tutorialDAO.findById(id);
            if(retrievedTutoriel.isPresent()){
                return new ResponseEntity<Tutorial>(retrievedTutoriel.get(), HttpStatus.OK);
            }
            return new ResponseEntity<Tutorial>(HttpStatus.NOT_FOUND);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }

    @DeleteMapping(path = "/tutorials/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        this.tutorialDAO.deleteById(id);

        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/tutorials")
    public ResponseEntity<HttpStatus> deleteTutorials() {
        this.tutorialDAO.deleteAll();
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }
}
