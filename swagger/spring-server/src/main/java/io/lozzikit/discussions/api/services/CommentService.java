package io.lozzikit.discussions.api.services;

import io.lozzikit.discussions.api.model.CommentRequest;
import io.lozzikit.discussions.api.model.CommentResponse;
import io.lozzikit.discussions.entities.CommentEntity;
import io.lozzikit.discussions.repositories.CommentRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
@Component
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public List<CommentResponse> getCommentFromArticleID(long articleID) {
        List<CommentEntity> commentsEntities = commentRepository.findByArticleID((long) articleID);

        return toCommentResponse(commentsEntities);
    }

    public boolean containsMessage(CommentRequest commentRequest) {
        String message = commentRequest.getMessage();
        return message != null && !message.isEmpty();
    }

    public void addNewComments(CommentRequest commentRequest) {
        CommentEntity entity = toCommentEntity(commentRequest);
        commentRepository.save(entity);
    }

    public boolean commentExist(long commentID) {
        return commentRepository.exists(commentID);
    }

    public void deleteComment(long commentID) {
        CommentEntity commentEntity = commentRepository.findOne(commentID);
        if (commentEntity.isLeaf()) {
            commentRepository.delete(commentEntity);
        } else {
            commentEntity.setMessage("");
            commentEntity.setAuthor("");
            commentRepository.save(commentEntity);
        }
    }

    public void updateComment(long commentID, CommentRequest commentRequest) {
        CommentEntity commentEntity = commentRepository.findOne(commentID);
        commentEntity.setMessage(commentRequest.getMessage());;
        commentRepository.save(commentEntity);
    }

    private CommentEntity toCommentEntity(CommentRequest comment) {
        CommentEntity entity = new CommentEntity();
        entity.setArticleID(comment.getArticleID());
        entity.setAuthor(comment.getAuthor());
        entity.setAuthorID(comment.getAuthorID());
        entity.setMessage(comment.getMessage());

        if (comment.getParentID() != null)
            entity.setParent(commentRepository.findOne(comment.getParentID()));

        return entity;
    }

    private List<CommentEntity> toCommentEntity(List<CommentRequest> comments) {
        return comments.stream()
                .map(s -> toCommentEntity(s))
                .collect(Collectors.toList());
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

    private List<CommentResponse> toCommentResponse(List<CommentEntity> comments) {
        return comments.stream()
                .map(s -> toCommentResponse(s))
                .collect(Collectors.toList());
    }

    private CommentRequest CommentResponseToRequest(CommentResponse response) {
        CommentRequest commentRequest = new CommentRequest();
        return commentRequest;
    }

    private CommentResponse CommentRequestToResponse(CommentRequest request) {
        CommentResponse commentRequest = new CommentResponse();
        return commentRequest;
    }

    private List<CommentRequest> ListCommentResponseToRequest(List<CommentResponse> listResponse) {
        return listResponse.stream()
                .map(s -> CommentResponseToRequest(s))
                .collect(Collectors.toList());
    }

    private List<CommentResponse> ListCommentRequestToResponse(List<CommentRequest> listRequest) {
        return listRequest.stream()
                .map(s -> CommentRequestToResponse(s))
                .collect(Collectors.toList());
    }


}
