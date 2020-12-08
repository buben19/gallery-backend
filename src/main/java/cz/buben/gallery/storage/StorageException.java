package cz.buben.gallery.storage;

@SuppressWarnings({"WeakerAccess", "unused"})
public class StorageException extends RuntimeException {

    public StorageException() {
    }

    public StorageException(String s) {
        super(s);
    }

    public StorageException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public StorageException(Throwable throwable) {
        super(throwable);
    }
}
