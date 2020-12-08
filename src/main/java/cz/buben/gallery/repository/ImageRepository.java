package cz.buben.gallery.repository;

import cz.buben.gallery.model.Image;
import cz.buben.gallery.model.User;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {

    Iterable<Image> findByOwner(User owner);
}
