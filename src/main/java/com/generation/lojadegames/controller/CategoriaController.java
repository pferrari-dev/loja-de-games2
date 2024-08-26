package com.generation.lojadegames.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.lojadegames.model.Categoria;
import com.generation.lojadegames.repository.CategoriaRepository;

import jakarta.validation.Valid;

@RestController // Indica que a classe é um controlador, o que significa que ela pode lidar com requisições HTTP
@RequestMapping("/categorias") //é uma ferramenta para criar rotas e manipular diferentes tipos de requisições
@CrossOrigin(origins = "*",allowedHeaders = "*")  // O CORS é um mecanismo de segurança do navegador que restringe as requisições HTTP
public class CategoriaController {

	@Autowired // é usada para realizar a injeção de dependências automaticamente.
	private CategoriaRepository categoriaRepository;
	
	@GetMapping // é utilizada quando você deseja criar um endpoint que retorna dados, sem modificar o estado do servidor.
	public ResponseEntity<List<Categoria>> getAll(){
		return ResponseEntity.ok(categoriaRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Categoria> getById(@PathVariable Long id){
		return categoriaRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/nome/{nome}")
    public ResponseEntity<List<Categoria>> getByNome(@PathVariable 
    String nome){
        return ResponseEntity.ok(categoriaRepository
            .findAllByNomeContainingIgnoreCase(nome));
    }
	
	@PostMapping // é uma anotação para definir endpoints que lidam com requisições HTTP POST
    public ResponseEntity<Categoria> post(@Valid @RequestBody Categoria categoria){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaRepository.save(categoria));
    }
	
	@PutMapping // é geralmente utilizada quando você deseja criar um endpoint que atualiza um recurso existente.
    public ResponseEntity<Categoria> put(@Valid @RequestBody Categoria categoria){
        return categoriaRepository.findById(categoria.getId())
            .map(resposta -> ResponseEntity.status(HttpStatus.CREATED)
            .body(categoriaRepository.save(categoria)))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)  //é usada para definir o código de status HTTP que deve ser retornado por um método de um controlador ou uma exceção. 
    @DeleteMapping("/{id}") //é uma forma de mapear requisições HTTP do tipo DELETE para um método específico de um controlador.
    public void delete(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        
        if(categoria.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        
        categoriaRepository.deleteById(id);              
    }
    
}
