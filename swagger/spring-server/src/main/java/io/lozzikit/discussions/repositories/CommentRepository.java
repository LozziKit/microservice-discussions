package io.lozzikit.discussions.repositories;

import io.lozzikit.discussions.entities.CommentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Olivier Liechti on 26/07/17.
 */
public interface CommentRepository extends CrudRepository<CommentEntity, Long> {
    List<CommentEntity> findByArticleID(long articleId);

    List<CommentEntity> findByRootAndArticleID(boolean root, long articleID);
}
