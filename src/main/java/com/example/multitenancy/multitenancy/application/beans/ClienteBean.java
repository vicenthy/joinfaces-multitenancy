package com.example.multitenancy.multitenancy.application.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.multitenancy.multitenancy.domain.entity.Cliente;
import com.example.multitenancy.multitenancy.domain.repository.ClienteRepository;

@Named("clienteBean")
@ViewScoped
public class ClienteBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4066167029092073472L;
	private String nome;
	private Date dataNascimento;
	@Autowired
	private ClienteRepository clienteRepository;
	private List<Cliente> clientes;
	
	
	
	@PostConstruct
	public void init() {
			clientes = clienteRepository.findAll();
	}
	
	
	
	
	public void salvar() {
		Cliente c = new Cliente(null, nome, convertToLocalDateViaInstant(dataNascimento));
		clienteRepository.save(c);
		nome = "";
		dataNascimento = null;
		clientes = clienteRepository.findAll();
	}
	
	
	
	public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public List<Cliente> getClientes() {
		return clientes;
	}
	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
	
	
	
	
	

}
