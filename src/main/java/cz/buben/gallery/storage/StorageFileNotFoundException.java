package cz.buben.gallery.storage;

@SuppressWarnings({"WeakerAccess", "unused"})
public class StorageFileNotFoundException extends StorageException {

    public StorageFileNotFoundException() {
    }

    public StorageFileNotFoundException(String s) {
        super(s);
    }

    public StorageFileNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public StorageFileNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
