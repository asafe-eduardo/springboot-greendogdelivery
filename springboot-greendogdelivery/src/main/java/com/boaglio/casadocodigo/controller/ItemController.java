package com.boaglio.casadocodigo.controller;

import java.util.List;

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

import com.boaglio.casadocodigo.model.Item;
import com.boaglio.casadocodigo.repositories.ItemRepository;

@Controller
@RequestMapping("/itens")
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

	@GetMapping
	public ModelAndView list() {
		List<Item> itens = itemRepository.findAll();
		return new ModelAndView("itens/list", "itens", itens);
	}
	
	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Item item)	{
		return new ModelAndView("itens/view","item",item);
	}
	
	@GetMapping("/novo")
	public String createForm(@ModelAttribute Item item) {
		return "itens/form";
	}
	
	@PostMapping(params = "form")
	public ModelAndView create(@Valid Item item,BindingResult result,RedirectAttributes redirect) {
		if (result.hasErrors()) { return new ModelAndView("itens/form","formErrors",result.getAllErrors()); }
		item = this.itemRepository.save(item);
		redirect.addFlashAttribute("globalMessage","Item gravado com sucesso");
		return new ModelAndView("redirect:/itens/{item.id}","item.id",item.getId());
	}
	
	@GetMapping(value = "remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id,RedirectAttributes redirect) {
		this.itemRepository.deleteById(id);
		Iterable<Item> itens = this.itemRepository.findAll();
		
		ModelAndView mv = new ModelAndView("clientes/list","clientes",itens);
		mv.addObject("globalMessage","Item removido com sucesso");
	
		return mv;
	}

	@GetMapping(value = "alterar/{id}")
	public ModelAndView alterarForm(@PathVariable("id") Item item) {
		return new ModelAndView("itens/form","item",item);
	}
	
}
