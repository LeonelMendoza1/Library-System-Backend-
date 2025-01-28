package library_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    private String author;

    @NotBlank(message = "El ISBN es obligatorio")
    @Column(unique = true)
    private String isbn;

    @NotNull(message = "El año de publicación es obligatorio")
    private Integer publicationYear;

    @NotBlank(message = "El género es obligatorio")
    private String genre;

    private String imageUrl;

    private boolean available = true;

}
