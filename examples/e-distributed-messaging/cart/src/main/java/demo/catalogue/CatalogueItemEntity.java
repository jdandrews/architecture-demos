package demo.catalogue;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "catalogue_item")
public class CatalogueItemEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String caption;

    public CatalogueItemEntity() {}

    public CatalogueItemEntity(final Long id, final String caption) {
        this.id = id;
        this.caption = caption;
    }

    public Long id() {
        return id;
    }

    public String caption() {
        return caption;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final CatalogueItemEntity other = (CatalogueItemEntity) object;
        return Objects.equals(id, other.id)
                && Objects.equals(caption, other.caption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, caption);
    }

    @Override
    public String toString() {
        return "CatalogueItemEntity[id=%d, caption=%s]".formatted(id, caption);
    }
}
