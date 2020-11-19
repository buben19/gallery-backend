package cz.buben.sre.repository;

import cz.buben.sre.model.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {
}
