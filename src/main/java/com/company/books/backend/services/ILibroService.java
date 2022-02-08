package com.company.books.backend.services;

import org.springframework.http.ResponseEntity;

import com.company.books.backend.model.Libro;
import com.company.books.backend.response.LibroResponseRest;


public interface ILibroService {
	
	public ResponseEntity<LibroResponseRest> buscarLibros();
	public ResponseEntity<LibroResponseRest> buscarPorId(Long id);
	public ResponseEntity<LibroResponseRest> guardarLibro(Libro libro);
	public ResponseEntity<LibroResponseRest> eliminar(Long id);
	ResponseEntity<LibroResponseRest> actualizar(Libro libro, Long id);
	


}
