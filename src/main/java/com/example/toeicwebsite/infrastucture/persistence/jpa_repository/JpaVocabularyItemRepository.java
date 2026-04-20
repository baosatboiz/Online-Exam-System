package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.VocabularyItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaVocabularyItemRepository extends JpaRepository<VocabularyItemEntity, UUID> {
    List<VocabularyItemEntity> findBySetIdAndUserIdOrderByCreatedAtDesc(UUID setId, UUID userId);
    long countBySetIdAndUserId(UUID setId, UUID userId);

    @Query("""
            select v.setId as setId, count(v.id) as itemCount
            from VocabularyItemEntity v
            where v.userId = :userId and v.setId in :setIds
            group by v.setId
            """)
    List<SetCountProjection> countBySetIdsAndUserId(@Param("setIds") List<UUID> setIds,
                                                    @Param("userId") UUID userId);

    interface SetCountProjection {
        UUID getSetId();
        long getItemCount();
    }
}
