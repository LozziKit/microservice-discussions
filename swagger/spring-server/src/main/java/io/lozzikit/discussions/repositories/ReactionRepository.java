package io.lozzikit.discussions.repositories;

import io.lozzikit.discussions.entities.ReactionEntity;
import org.springframework.data.repository.CrudRepository;

public interface ReactionRepository extends CrudRepository<ReactionEntity, Long> {
}
