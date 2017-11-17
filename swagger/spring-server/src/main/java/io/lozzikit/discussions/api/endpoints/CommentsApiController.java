package io.lozzikit.discussions.api.endpoints;

import io.lozzikit.discussions.api.CommentsApi;
import io.lozzikit.discussions.api.model.CommentRequest;
import io.lozzikit.discussions.api.model.CommentResponse;
import io.lozzikit.discussions.entities.CommentEntity;
import io.lozzikit.discussions.repositories.CommentRepository;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-07-26T19:36:34.802Z")

@Controller
public class CommentsApiController implements CommentsApi {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public ResponseEntity<List<CommentResponse>> commentsGet( @NotNull @ApiParam(value = "The articleID the user want to list comment from", required = true) @RequestParam(value = "articleID", required = true) Long articleID) {
        List<CommentEntity> commentsEntities = commentRepository.findByArticleID((long) articleID);

        List<CommentResponse> responses = commentsEntities.stream()
                .map(s -> toCommentResponse(s)).collect(Collectors.toList());

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
    public ResponseEntity<Void> commentsPost(@ApiParam(value = "The comment the user want to post" ,required=true ) @RequestBody CommentRequest comment){
        CommentEntity entity = toCommentEntity(comment);
        commentRepository.save(entity);

        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    private CommentEntity toCommentEntity(CommentRequest comment) {
        CommentEntity entity = new CommentEntity();
        entity.setArticleID(comment.getArticleID());
        entity.setAuthor(comment.getAuthor());
        entity.setAuthorID(comment.getAuthorID());
        entity.setDate(new Date());
        entity.setMessage(comment.getMessage());

        if (comment.getParentID() != null)
            entity.setParent(commentRepository.findOne(comment.getParentID()));

        return entity;
    }

    public CommentResponse toCommentResponse(CommentEntity comment) {
        CommentResponse response = new CommentResponse();

        response.setArticleID(comment.getArticleID());
        response.setAuthor(comment.getAuthor());
        response.setAuthorID(comment.getAuthorID());
        response.setDate(new DateTime(comment.getDate()));
        response.setId(comment.getId());
        response.setMessage(comment.getMessage());

        if (comment.getParent() != null)
            response.setParentID(comment.getParent().getId());

        return response;
    }
}
