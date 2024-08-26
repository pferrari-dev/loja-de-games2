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

import com.generation.lojadegames.model.Produto;
import com.generation.lojadegames.repository.ProdutoRepository;
import com.generation.lojadegames.repository.CategoriaRepository;

import jakarta.validation.Valid;

@RestController //é usada para criar controladores RESTful em aplicações Spring
@RequestMapping("/produtos") //é uma ferramenta para criar rotas e manipular diferentes tipos de requisições
@CrossOrigin(origins = "*", allowedHeaders = "*") // O CORS é um mecanismo de segurança do navegador que restringe as requisições HTTP
public class ProdutoController {
	
	@Autowired // é usada para realizar a injeção de dependências automaticamente.
	private ProdutoRepository produtoRepository;
	
	@Autowired // é usada para realizar a injeção de dependências automaticamente.
	private CategoriaRepository categoriaRepository;
	
	@GetMapping // é utilizada quando você deseja criar um endpoint que retorna dados, sem modificar o estado do servidor.
	public ResponseEntity<List<Produto>> getAll(){
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id){
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produto>> getByTitulo(@PathVariable String nome){
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	}

	@PostMapping // é uma anotação para definir endpoints que lidam com requisições HTTP POST
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto){
		if (categoriaRepository.existsById(produto.getCategoria().getId()))
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(produtoRepository.save(produto));
		
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe!", null);
	}
	
	@PutMapping // é geralmente utilizada quando você deseja criar um endpoint que atualiza um recurso existente.
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto){
		if(produtoRepository.existsById(produto.getId())){
			
			if(categoriaRepository.existsById(produto.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.OK)
				.body(produtoRepository.save(produto));
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe!", null);
		}
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT) //é usada para definir o código de status HTTP que deve ser retornado por um método de um controlador ou uma exceção. 
	@DeleteMapping("/{id}") //é uma forma de mapear requisições HTTP do tipo DELETE para um método específico de um controlador.
	public void delete(@PathVariable Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		
		if(produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		produtoRepository.deleteById(id);
	}
}
