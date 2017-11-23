package io.lozzikit.discussions.api.endpoints;

import io.lozzikit.discussions.api.CommentsApi;
import io.lozzikit.discussions.api.model.CommentRequest;
import io.lozzikit.discussions.api.model.CommentResponse;
import io.lozzikit.discussions.services.CommentService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-07-26T19:36:34.802Z")

@Controller
public class CommentsApiController implements CommentsApi {

    @Autowired
    CommentService commentService;

    @Override
    public ResponseEntity<List<CommentResponse>> commentsGet(@NotNull @ApiParam(value = "The articleID the user want to list comment from", required = true) @RequestParam(value = "articleID", required = true) Long articleID) {
        List<CommentResponse> responses = commentService.getCommentFromArticleID(articleID);

        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<Void> commentsIdDelete(@ApiParam(value = "ID of the comment we want to delete", required = true) @PathVariable("id") Long id) {
        if (!commentService.commentExist(id)) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        commentService.deleteComment(id);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CommentResponse> commentsIdPut(@ApiParam(value = "ID of the comment we want to edit.", required = true) @PathVariable("id") Long id,
                                                         @ApiParam(value = "The comment the user want to post", required = true) @RequestBody CommentRequest comment) {
        if (!commentService.commentExist(id)) {
            return new ResponseEntity<CommentResponse>(HttpStatus.NOT_FOUND);
        }
        if (!commentService.containsMessage(comment)) {
            return new ResponseEntity<CommentResponse>(HttpStatus.NOT_FOUND);
        }

        commentService.updateComment(id, comment);

        return new ResponseEntity<CommentResponse>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> commentsPost(@ApiParam(value = "The comment the user want to post", required = true) @RequestBody CommentRequest comment) {
        if (!commentService.containsMessage(comment)) {
            return new ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        commentService.addNewComments(comment);

        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
