package io.lozzikit.discussions.api.endpoints;

import io.lozzikit.discussions.api.CommentsApi;
import io.lozzikit.discussions.api.model.CommentRequest;
import io.lozzikit.discussions.api.model.CommentResponse;
import io.lozzikit.discussions.entities.CommentEntity;
import io.lozzikit.discussions.repositories.CommentRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseStatus;
import sun.security.provider.certpath.OCSPResponse;

import javax.xml.stream.events.Comment;
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-07-26T19:36:34.802Z")

@Controller
public class CommentsApiController implements CommentsApi {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public ResponseEntity<List<CommentResponse>> commentsGet(Long articleID) {
        List<CommentEntity> commentsEntities = commentRepository.findByArticleID((long) articleID);

        List<CommentResponse> responses = commentsEntities.stream()
                .map( s -> toCommentResponse(s)).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<Void> commentsIdDelete(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<CommentResponse> commentsIdPut(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> commentsPost(CommentRequest comment) {
        CommentEntity entity = toCommentEntity(comment);
        commentRepository.save(entity);

        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    private CommentEntity toCommentEntity(CommentRequest comment){
        CommentEntity entity = new CommentEntity();
        entity.setArticleID(comment.getArticleID());
        entity.setAuthor(comment.getAuthor());
        entity.setAuthorID(comment.getAuthorID());
        entity.setDate(new Date());
        entity.setMessage(comment.getMessage());
        entity.setParentID(comment.getParentID());

        return entity;
    }

    public CommentResponse toCommentResponse(CommentEntity comment){
        CommentResponse response = new CommentResponse();

        response.setArticleID(comment.getArticleID());
        response.setAuthor(comment.getAuthor());
        response.setAuthorID(comment.getAuthorID());
        response.setDate(new DateTime(comment.getDate()));
        response.setId(comment.getId());
        response.setMessage(comment.getMessage());
        response.setParentID(comment.getParentID());

        return response;
    }

    /*
    public ResponseEntity<Object> createFruit(@ApiParam(value = "", required = true) @Valid @RequestBody Fruit fruit) {
        CommentEntity newFruitEntity = toFruitEntity(fruit);
        commentRepository.save(newFruitEntity);
        Long id = newFruitEntity.getId();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newFruitEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }


    public ResponseEntity<List<Fruit>> getFruits() {
        List<Fruit> fruits = new ArrayList<>();
        for (CommentEntity fruitEntity : commentRepository.findAll()) {
            fruits.add(toFruit(fruitEntity));
        }

        Fruit staticFruit = new Fruit();
        staticFruit.setColour("red");
        staticFruit.setKind("banana");
        staticFruit.setSize("medium");
        List<Fruit> fruits = new ArrayList<>();
        fruits.add(staticFruit);

        return ResponseEntity.ok(fruits);
    }


    private CommentEntity toFruitEntity(Fruit fruit) {
        CommentEntity entity = new CommentEntity();
        entity.setColour(fruit.getColour());
        entity.setKind(fruit.getKind());
        entity.setSize(fruit.getSize());
        return entity;
    }

    private Fruit toFruit(CommentEntity entity) {
        Fruit fruit = new Fruit();
        fruit.setColour(entity.getColour());
        fruit.setKind(entity.getKind());
        fruit.setSize(entity.getSize());
        return fruit;
    }
    */
}
