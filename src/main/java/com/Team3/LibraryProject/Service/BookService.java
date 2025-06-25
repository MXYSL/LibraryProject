package com.Team3.LibraryProject.Service;

import com.Team3.LibraryProject.Entity.Book;
import com.Team3.LibraryProject.Entity.Genre;
import com.Team3.LibraryProject.Repository.BookRepository;
import com.Team3.LibraryProject.Repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private GenreRepository genreRepository;
    
    public Book createBook(String title, String author, String publisher, String edition,
                          int quantity, String isbn, String synopsis, Long genreId,
                          String coverImageUrl, String materialType) {
        Optional<Genre> genre = genreRepository.findById(genreId);
        if (genre.isEmpty()) {
            throw new RuntimeException("Género no encontrado");
        }
        
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setEdition(edition);
        book.setEntryDate(LocalDate.now());
        book.setTotalQuantity(quantity);
        book.setAvailableQuantity(quantity);
        book.setIsbn(isbn);
        book.setSynopsis(synopsis);
        book.setGenre(genre.get());
        book.setCoverImageUrl(coverImageUrl);
        book.setMaterialType(materialType);
        
        return bookRepository.save(book);
    }
    
    public Book updateBook(Long bookId, String title, String author, String publisher, 
                          String edition, String synopsis, Long genreId, String coverImageUrl) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new RuntimeException("Libro no encontrado");
        }
        
        Book book = bookOpt.get();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setEdition(edition);
        book.setSynopsis(synopsis);
        book.setCoverImageUrl(coverImageUrl);
        
        if (genreId != null) {
            Optional<Genre> genre = genreRepository.findById(genreId);
            if (genre.isPresent()) {
                book.setGenre(genre.get());
            }
        }
        
        return bookRepository.save(book);
    }
    
    public void deleteBook(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new RuntimeException("Libro no encontrado");
        }
        
        // Verificar que no tenga préstamos activos
        if (book.get().getAvailableQuantity() < book.get().getTotalQuantity()) {
            throw new RuntimeException("No se puede eliminar un libro con préstamos activos");
        }
        
        bookRepository.deleteById(bookId);
    }
    
    public List<Book> searchBooks(String title, String author, String publisher, 
                                String edition, Long genreId) {
        return bookRepository.findByAdvancedSearch(title, author, publisher, edition, genreId);
    }
    
    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailableBooks();
    }
    
    public void updateBookQuantity(Long bookId, int newTotalQuantity) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new RuntimeException("Libro no encontrado");
        }
        
        Book book = bookOpt.get();
        int difference = newTotalQuantity - book.getTotalQuantity();
        book.setTotalQuantity(newTotalQuantity);
        book.setAvailableQuantity(book.getAvailableQuantity() + difference);
        
        if (book.getAvailableQuantity() < 0) {
            throw new RuntimeException("La cantidad disponible no puede ser negativa");
        }
        
        bookRepository.save(book);
    }
    
    public Genre createGenre(String name, String description) {
        Optional<Genre> existingGenre = genreRepository.findByName(name);
        if (existingGenre.isPresent()) {
            throw new RuntimeException("Ya existe un género con ese nombre");
        }
        
        Genre genre = new Genre();
        genre.setName(name);
        genre.setDescription(description);
        return genreRepository.save(genre);
    }
    
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
}
