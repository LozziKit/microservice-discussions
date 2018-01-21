package io.lozzikit.discussions.api.endpoints;

import io.lozzikit.discussions.api.CommentsApi;
import io.lozzikit.discussions.api.model.CommentRequest;
import io.lozzikit.discussions.api.model.CommentResponse;
import io.lozzikit.discussions.services.CommentService;
import io.lozzikit.discussions.utils.JWTUtils;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-07-26T19:36:34.802Z")

@Controller
public class CommentsApiController implements CommentsApi {

    @Autowired
    CommentService commentService;

    @Override
    public ResponseEntity<List<CommentResponse>> commentsGet(@NotNull @ApiParam(value = "The articleID the user want to list comment from", required = true) @RequestParam(value = "articleID", required = true) Long articleID,
                                                             @ApiParam(value = "If the user wants to get a tree representation of the comments", defaultValue = "false") @RequestParam(value = "tree", required = false, defaultValue = "false") Boolean tree) {
        List<CommentResponse> responses;

        if (tree)
            responses = commentService.getCommentsTreeFromArticleID(articleID);
        else
            responses = commentService.getCommentsFlatFromArticleID(articleID);

        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<CommentResponse> commentsIdGet(@ApiParam(value = "ID of the comment we want to retrive.", required = true) @PathVariable("id") Long id) {
        if (!commentService.commentExist(id))
            return new ResponseEntity<CommentResponse>(HttpStatus.NOT_FOUND);

        CommentResponse response = commentService.getComment(id);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> commentsIdDelete(@ApiParam(value = "JWT bearer token containing \"userID\" and \"username\" as claims", required = true) @RequestHeader(value = "authorization", required = true) String authorization,
                                                 @ApiParam(value = "ID of the comment we want to delete", required = true) @PathVariable("id") Long id) {

        if (!commentService.commentExist(id))
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        if (authorization == null || authorization.isEmpty()) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        // User information
        Long userID = JWTUtils.getUserInfo(authorization).getUserId();

        if (!commentService.isCommentOwner(id, userID))
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);

        commentService.deleteComment(id);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> commentsIdPut(@ApiParam(value = "JWT bearer token containing \"userID\" and \"username\" as claims", required = true) @RequestHeader(value = "authorization", required = true) String authorization,
                                                @ApiParam(value = "ID of the comment we want to edit.", required = true) @PathVariable("id") Long id,
                                                @ApiParam(value = "The comment the user want to post", required = true) @RequestBody CommentRequest comment) {
        // User information
        if (authorization == null || authorization.isEmpty()) {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

        Long userID = JWTUtils.getUserInfo(authorization).getUserId();

        if (!commentService.containsMessage(comment))
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        if (!commentService.commentExist(id))
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        if (commentService.commentIsDeleted(id))
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        if (userID == null)
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        if (!commentService.isCommentOwner(id, userID))
            return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);

        long commentId = commentService.updateComment(id, comment);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + commentId)
                .build()
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Long> commentsIdReactionGet(@ApiParam(value = "ID of the comment having the reactions we're interessted in.", required = true) @PathVariable("id") Long id) {
        if (!commentService.commentExist(id))
            return new ResponseEntity<Long>(HttpStatus.NOT_FOUND);
        if (commentService.commentIsDeleted(id))
            return new ResponseEntity<Long>(HttpStatus.FORBIDDEN);

        return ResponseEntity.ok(commentService.getNbrReaction(id));
    }

    @Override
    public ResponseEntity<Long> commentsIdReactionPost(@ApiParam(value = "JWT bearer token containing \"userID\" and \"username\" as claims", required = true) @RequestHeader(value = "authorization", required = true) String authorization,
                                                       @ApiParam(value = "ID of the comment we want to react to.", required = true) @PathVariable("id") Long id)

    {
        if (authorization == null || authorization.isEmpty()) {
            return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);
        }

        // User information
        Long userID = JWTUtils.getUserInfo(authorization).getUserId();

        if (!commentService.commentExist(id))
            return new ResponseEntity<Long>(HttpStatus.NOT_FOUND);
        if (userID == null)
            return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);
        if (commentService.commentIsDeleted(id))
            return new ResponseEntity<Long>(HttpStatus.FORBIDDEN);
        if (commentService.containsReactioner(id, userID))
            return new ResponseEntity<Long>(HttpStatus.FORBIDDEN);

        long commentID = commentService.addReaction(id, userID);
        long nbrReactions = commentService.getNbrReaction(commentID);

        return ResponseEntity.ok(nbrReactions);
    }

    @Override
    public ResponseEntity<Long> commentsIdReactionDelete(@ApiParam(value = "JWT bearer token containing \"userID\" and \"username\" as claims", required = true) @RequestHeader(value = "authorization", required = true) String authorization,
                                                         @ApiParam(value = "ID of the comment we want to delete the reaction from.", required = true) @PathVariable("id") Long id) {

        if (authorization == null || authorization.isEmpty())
            return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);

        Long authorID = JWTUtils.getUserInfo(authorization).getUserId();
        if (commentService.commentIsDeleted(id))
            return new ResponseEntity<Long>(HttpStatus.FORBIDDEN);
        if (!commentService.containsReactioner(id, authorID))
            return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);

        long commentID = commentService.removeReacion(id, authorID);
        long nbrReactions = commentService.getNbrReaction(commentID);

        return ResponseEntity.ok(nbrReactions);
    }

    @Override
    public ResponseEntity<Object> commentsPost(@ApiParam(value = "JWT bearer token containing \"userID\" and \"username\" as claims", required = true) @RequestHeader(value = "authorization", required = true) String authorization,
                                               @NotNull @ApiParam(value = "The ID of the associated article.", required = true) @RequestParam(value = "articleID", required = true) Long articleID,
                                               @ApiParam(value = "The comment the user want to post", required = true) @RequestBody CommentRequest comment,
                                               @ApiParam(value = "(Optionnal) The ID of the responded comment.") @RequestParam(value = "parentID", required = false) Long parentID) {
        if (!commentService.containsMessage(comment))
            return new ResponseEntity<Object>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (authorization == null || authorization.isEmpty()) {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

        System.out.println("parentID : " + parentID);

        // Getting the user informations from token
        JWTUtils.UserInfo userInfo = JWTUtils.getUserInfo(authorization);

        long commentID = commentService.addNewComments(comment, articleID, parentID, userInfo);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(commentID)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
