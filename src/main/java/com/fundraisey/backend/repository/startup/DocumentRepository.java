package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.Document;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DocumentRepository extends PagingAndSortingRepository<Document, Long> {
}
