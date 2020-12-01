package cz.buben.sre.repository;

import cz.buben.sre.model.Image;
import cz.buben.sre.model.User;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {

    Iterable<Image> findByOwner(User owner);
}
