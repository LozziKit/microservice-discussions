package io.lozzikit.discussions.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "upvote")
public class ReactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private long id;

    @NotNull
    private long authorID;

    @ManyToOne
    private CommentEntity commentEntity;

    public ReactionEntity(){

    }

    public ReactionEntity(long authorID, CommentEntity commentEntity){
        this.commentEntity = commentEntity;
        this.authorID = authorID;
    }

    public long getAuthorID(){
        return this.authorID;
    }

    public long getId() {
        return id;
    }

    public CommentEntity getCommentEntity(){
        return this.commentEntity;
    }

    public void setAuthorID(long authorID){
        this.authorID = authorID;
    }

    public void setCommentEntity(CommentEntity commentEntity){
        this.commentEntity = commentEntity;
    }

}
