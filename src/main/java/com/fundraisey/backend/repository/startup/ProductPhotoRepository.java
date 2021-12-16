package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.ProductPhoto;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductPhotoRepository extends PagingAndSortingRepository<ProductPhoto, Long> {

}
