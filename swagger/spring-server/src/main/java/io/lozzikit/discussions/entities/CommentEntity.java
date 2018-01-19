package io.lozzikit.discussions.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 */
@Entity
@Table(name = "comments")
public class CommentEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comments_id")
    private long id;

    private long authorID;
    private String author;
    private String message;
    private long articleID;

    private boolean root = false;
    private boolean deleted = false;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<CommentEntity> children = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "comments_id_children")
    private CommentEntity parent;

    private Date date = new Date();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commentEntity", cascade = CascadeType.ALL)
    private Set<ReactionEntity> reactions = new HashSet<>();

    public CommentEntity() {
    }

    public long getId() {
        return id;
    }

    public long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(long authorID) {
        this.authorID = authorID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getArticleID() {
        return articleID;
    }

    public void setArticleID(long articleID) {
        this.articleID = articleID;
    }

    public CommentEntity getParent() {
        return parent;
    }

    public void setParent(CommentEntity parent) {
        this.parent = parent;
    }

    public Set<CommentEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<CommentEntity> children) {
        this.children = children;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Transient
    public boolean isLeaf() {
        return children.isEmpty();
    }

    public Set<ReactionEntity> getReactions() { return reactions; }

    public void setReactions(Set<ReactionEntity> reaction) { this.reactions = reaction; }

    public void addReaction(ReactionEntity reaction) { reactions.add(reaction); }
}
