package io.lozzikit.discussions.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 */
@Entity
public class CommentEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long authorID;
    private String author;
    private String message;
    private long articleID;

    @ManyToOne
    private CommentEntity parent;
    private Date date;

    public long getId() {
        return id;
    }

    public long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(long authorIDah) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
