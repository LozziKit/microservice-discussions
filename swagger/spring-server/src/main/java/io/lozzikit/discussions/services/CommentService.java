package io.lozzikit.discussions.services;

import io.lozzikit.discussions.api.model.CommentRequest;
import io.lozzikit.discussions.api.model.CommentResponse;
import io.lozzikit.discussions.entities.CommentEntity;
import io.lozzikit.discussions.entities.ReactionEntity;
import io.lozzikit.discussions.repositories.CommentRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
@Component
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public List<CommentResponse> getCommentsTreeFromArticleID(long articleID) {
        List<CommentEntity> commentsEntities = commentRepository.findByRootAndArticleID(true, articleID);

        return toCommentResponse(commentsEntities, true);
    }

    public List<CommentResponse> getCommentsFlatFromArticleID(long articleID) {
        List<CommentEntity> commentsEntities = commentRepository.findByArticleID(articleID);

        return toCommentResponse(commentsEntities, false);
    }

    public CommentResponse getComment(long commentID) {
        CommentEntity commentsEntities = commentRepository.findOne(commentID);

        return toCommentResponse(commentsEntities);
    }

    public boolean containsMessage(CommentRequest commentRequest) {
        String message = commentRequest.getMessage();

        return message != null && !message.isEmpty();
    }

    public long addNewComments(CommentRequest commentRequest, long articleID, Long parentID) {
        System.out.println("parentID : " + parentID);
        CommentEntity entity = toCommentEntity(commentRequest, articleID, parentID);

        return commentRepository.save(entity).getId();
    }

    public boolean asAuthor(CommentRequest commentRequest) {
        return commentRequest.getAuthorID() != null;
    }

    public boolean asSameAuthor(long commentID, CommentRequest commentRequest) {
        CommentEntity commentEntity = commentRepository.findOne(commentID);

        return commentEntity.getAuthorID() == commentRequest.getAuthorID();
    }

    public boolean commentIsDeleted(long id) {
        return commentRepository.findOne(id).isDeleted();
    }

    public boolean commentExist(long commentID) {
        return commentRepository.exists(commentID);
    }

    public void deleteComment(long commentID) {
        CommentEntity commentEntity = commentRepository.findOne(commentID);
        if (commentEntity.isLeaf()) {
            commentRepository.delete(commentEntity);
        } else {
            commentEntity.setMessage(null);
            commentEntity.setAuthor(null);
            commentEntity.setDeleted(true);
            commentRepository.save(commentEntity);
        }
    }

    public long updateComment(long commentID, CommentRequest commentRequest) {
        CommentEntity commentEntity = commentRepository.findOne(commentID);
        commentEntity.setMessage(commentRequest.getMessage());

        return commentRepository.save(commentEntity).getId();
    }

    private CommentEntity toCommentEntity(CommentRequest comment, long articleID, Long parentID) {
        CommentEntity entity = new CommentEntity();
        entity.setArticleID(articleID);
        entity.setAuthor(comment.getAuthor());
        entity.setAuthorID(comment.getAuthorID());
        entity.setMessage(comment.getMessage());

        System.out.println("parentID : " + parentID);

        if (parentID == null) {
            entity.setRoot(true);
        } else if (entity.getId() == parentID) {
            entity.setRoot(true);
        } else {
            entity.setParent(commentRepository.findOne(parentID));
            entity.setRoot(false);
        }

        return entity;
    }

    public CommentResponse toCommentResponse(CommentEntity comment) {
        return toCommentResponse(comment, false);
    }

    public CommentResponse toCommentResponse(CommentEntity comment, boolean isTree) {
        CommentResponse response = new CommentResponse();

        response.setArticleID(comment.getArticleID());
        response.setAuthor(comment.getAuthor());
        response.setAuthorID(comment.getAuthorID());
        response.setDate(new DateTime(comment.getDate()));
        response.setId(comment.getId());
        response.setMessage(comment.getMessage());
        response.setRoot(comment.isRoot());

        if (isTree && comment.getChildren() != null)
            response.setChildren(toCommentResponse(comment.getChildren(), isTree));

        if (comment.getParent() != null)
            response.setParentID(comment.getParent().getId());

        return response;
    }

    public boolean containsUpvoter(Long commentID, CommentRequest commentRequest) {
        CommentEntity comment = commentRepository.findOne(commentID);
        Set<ReactionEntity> upvoters = comment.getUpvoters();
        return upvoters.contains(commentRequest.getAuthorID());
    }

    public long getNbrUpvotes(Long commentID) {
        CommentEntity comment = commentRepository.findOne(commentID);
        return comment.getUpvoters().size();
    }

    public long addUpvote(Long commentID, Long authorID) {
        CommentEntity comment = commentRepository.findOne(commentID);
        comment.addUpvoter(authorID);
        return  commentRepository.save(comment).getId();
    }

    private List<CommentResponse> toCommentResponse(List<CommentEntity> comments, boolean isTree) {
        return comments.stream()
                .map(s -> toCommentResponse(s, isTree))
                .collect(Collectors.toList());
    }

    private List<CommentResponse> toCommentResponse(Set<CommentEntity> comments, boolean isTree) {
        return comments.stream()
                .map(s -> toCommentResponse(s, isTree))
                .collect(Collectors.toList());
    }
}
