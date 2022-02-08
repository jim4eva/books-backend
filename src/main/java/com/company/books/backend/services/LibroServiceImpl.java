package com.company.books.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.books.backend.model.Libro;
import com.company.books.backend.model.dao.ILibroDao;
import com.company.books.backend.response.LibroResponseRest;

@Service
public class LibroServiceImpl implements ILibroService{
	
	private static final Logger log = LoggerFactory.getLogger(LibroServiceImpl.class);

	
	@Autowired
	private ILibroDao libroDao;

//buscar todos los libros
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<LibroResponseRest> buscarLibros() {
		
		log.info("inicio metodo buscarLibros()");

		LibroResponseRest response = new LibroResponseRest();
		
		try {
			List<Libro> libro= (List<Libro>) libroDao.findAll();
			
			response.getLibroResponse().setLibro(libro);
			response.setMetada("Respuesta ok", "00", "Respuesta exitosa");
			
		} catch (Exception e) {
			response.setMetada("Respuesta nok", "-1", "Error al consultar libros");
			log.error("error al consultar libros: ", e.getMessage());
			e.getStackTrace();
			return new ResponseEntity<LibroResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<LibroResponseRest>(response,HttpStatus.OK);
	}

//buscar libro por id
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<LibroResponseRest> buscarPorId(Long id) {
		log.info("Inicio metodo buscarPorId");
		
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try {
			Optional<Libro> libro = libroDao.findById(id);
			
			if(libro.isPresent()) {
				list.add(libro.get());
				response.getLibroResponse().setLibro(list);
			}else {
				log.error("Error en consultar libro");
				response.setMetada("Respuesta nok", "-1", "Libro no encontrado");
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Error en consultar libro");
			response.setMetada("Respuesta nok", "-1", "Error al consultar el libro");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		
		response.setMetada("Respuesta ok", "00", "Respuesta exitosa");
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);//devuelve 200
	}
	
	
	

//guardar libro
	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> guardarLibro(Libro libro) {
		
		log.info("inicio metodo guardarLibro()");
		
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		
		try {
			
			Libro libroGuardado = libroDao.save(libro);
			
			if( libroGuardado != null ) {
				list.add(libroGuardado);
				response.getLibroResponse().setLibro(list);
			}else {
				
				log.error("Error en grabar libro");
				response.setMetada("Respuesta nok", "-1", "Libro no guardada");
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.BAD_REQUEST);
				
			}
			
		} catch (Exception e) {
			log.error("Error en buscar libro");
			response.setMetada("Respuesta nok", "-1", "Error al grabar libro");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.setMetada("Respuesta ok", "00", "Respuesta exitosa");
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);//devuelve 200
	}
	
	//actualizar
	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> actualizar(Libro libro, Long id) {
		
		log.info("Inicio metodo actualizar()");
		
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try {
			
			Optional<Libro> libroBuscado = libroDao.findById(id);
			
			if(libroBuscado.isPresent()) {
				libroBuscado.get().setNombre(libro.getNombre());
				libroBuscado.get().setDescripcion(libro.getDescripcion());
				libroBuscado.get().setCategoria(libro.getCategoria());
				
				Libro libroActualizar = libroDao.save(libroBuscado.get());//actualizando
				
				if( libroActualizar != null) {
					response.setMetada("Respuesta ok", "00", "Libro actualizado");
					list.add(libroActualizar);
					response.getLibroResponse().setLibro(list);
				}else {
					log.error("error en actualizar libro");
					response.setMetada("Respuesta nok", "-1", "Libro no actualizado");
					return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);//devuelve 200
				}
			}
			
		} catch (Exception e) {
			log.error("error en actualizar libro", e.getMessage());
			response.setMetada("Respuesta nok", "-1", "Libro no actualizado");
			return new ResponseEntity<LibroResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<LibroResponseRest>(response,HttpStatus.OK);
	}

//eliminar
	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> eliminar(Long id) {
		
		log.info("Inicio metodo eliminar libro");
		
		LibroResponseRest response = new LibroResponseRest();
		
		try {
			
			//eliminamos el registro
			libroDao.deleteById(id);
			response.setMetada("Respuesta ok", "00", "Libro eliminado");

			
		} catch (Exception e) {
			log.error("error en eliminar libro", e.getMessage());
			e.getStackTrace();
			response.setMetada("Respuesta nok", "-1", "Libro no eliminado");
			return new ResponseEntity<LibroResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<LibroResponseRest>(response,HttpStatus.OK);
	}


}
