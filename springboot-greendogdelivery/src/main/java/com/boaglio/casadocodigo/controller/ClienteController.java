package com.boaglio.casadocodigo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boaglio.casadocodigo.model.Cliente;
import com.boaglio.casadocodigo.repositories.ClienteRepository;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@GetMapping
	public ModelAndView list() {
		Iterable<Cliente> clientes = this.clienteRepository.findAll();
		return new ModelAndView("clientes/list", "clientes", clientes);
	}
	
	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Cliente cliente)	{
		return new ModelAndView("clientes/view","cliente",cliente);
	}
	
	@GetMapping("/novo")
	public String createForm(@ModelAttribute Cliente cliente) {
		return "clientes/form";
	}
	
	@PostMapping(params = "form")
	public ModelAndView create(@Valid Cliente cliente,BindingResult result,RedirectAttributes redirect) {
		if (result.hasErrors()) { return new ModelAndView("clientes/form","formErrors",result.getAllErrors()); }
		cliente = this.clienteRepository.save(cliente);
		redirect.addFlashAttribute("globalMessage","Cliente gravado com sucesso");
		return new ModelAndView("redirect:/clientes/{cliente.id}","cliente.id",cliente.getId());
	}
	
	@GetMapping(value = "remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id,RedirectAttributes redirect) {
		this.clienteRepository.deleteById(id);
		Iterable<Cliente> clientes = this.clienteRepository.findAll();
		
		ModelAndView mv = new ModelAndView("clientes/list","clientes",clientes);
		mv.addObject("globalMessage","Cliente removido com sucesso");
	
		return mv;
	}

	@GetMapping(value = "alterar/{id}")
	public ModelAndView alterarForm(@PathVariable("id") Cliente cliente) {
		return new ModelAndView("clientes/form","cliente",cliente);
	}
}
