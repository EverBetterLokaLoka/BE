package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.RelationshipType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IRelationshipTypeRepository extends JpaRepository<RelationshipType, Long> {
    Optional<RelationshipType> findByTypeName(String typeName);

}
