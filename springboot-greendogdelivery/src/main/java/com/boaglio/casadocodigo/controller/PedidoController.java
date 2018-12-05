package com.boaglio.casadocodigo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.boaglio.casadocodigo.model.Item;
import com.boaglio.casadocodigo.model.Pedido;
import com.boaglio.casadocodigo.repositories.ClienteRepository;
import com.boaglio.casadocodigo.repositories.ItemRepository;
import com.boaglio.casadocodigo.repositories.PedidoRepository;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ModelAndView list() {
		List<Pedido> pedidos = pedidoRepository.findAll();
		return new ModelAndView("pedidos/list", "pedidos", pedidos);
	}
	
	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Pedido pedido)	{
		return new ModelAndView("pedidos/view","pedido",pedido);
	}
	
	@GetMapping("/novo")
	public ModelAndView createForm(@ModelAttribute Pedido pedido) {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("todosItens",itemRepository.findAll());
		model.put("todosClientes",clienteRepository.findAll());
		return new ModelAndView("pedidos/form", model);
	}
	
	@PostMapping(params = "form")
	public ModelAndView create(@Valid Pedido pedido,BindingResult result,RedirectAttributes redirect) {
		if (result.hasErrors()) { return new ModelAndView("pedidos/form","formErrors",result.getAllErrors()); }

		if (pedido.getId() != null) {
			Pedido pedidoParaAlterar = pedidoRepository.findById(pedido.getId()).get();
			Cliente c = clienteRepository.findById(pedidoParaAlterar.getCliente().getId()).get();
			pedidoParaAlterar.setItens(pedido.getItens());
			double valorTotal = 0;
			for (Item i : pedido.getItens()) {
				valorTotal +=i.getPreco();
			}
			pedidoParaAlterar.setData(pedido.getData());
			pedidoParaAlterar.setValorTotal(valorTotal);			
			c.getPedidos().remove(pedidoParaAlterar.getId());
			c.getPedidos().add(pedidoParaAlterar);
			this.clienteRepository.save(c);
		} else {
			Cliente c = clienteRepository.findById(pedido.getCliente().getId()).get();
			double valorTotal = 0;
			for (Item i : pedido.getItens()) {
				valorTotal +=i.getPreco();
			}
			pedido.setValorTotal(valorTotal);
			pedido = this.pedidoRepository.save(pedido);
			c.getPedidos().add(pedido);
			this.clienteRepository.save(c);
		}
		redirect.addFlashAttribute("globalMessage","Pedido gravado com sucesso");
		return new ModelAndView("redirect:/pedidos/{pedido.id}","pedido.id",pedido.getId());
	}

	@GetMapping(value = "remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id,RedirectAttributes redirect) {

		Pedido pedidoParaRemover = pedidoRepository.findById(id).get();

		Cliente c = clienteRepository.findById(pedidoParaRemover.getCliente().getId()).get();
		c.getPedidos().remove(pedidoParaRemover);

		this.clienteRepository.save(c);
		this.pedidoRepository.deleteById(id);

		Iterable<Pedido> pedidos = this.pedidoRepository.findAll();

		ModelAndView mv = new ModelAndView("pedidos/list","pedidos",pedidos);
		mv.addObject("globalMessage","Pedido removido com sucesso");

		return mv;
	}

	@GetMapping(value = "alterar/{id}")
	public ModelAndView alterarForm(@PathVariable("id") Pedido pedido) {

		Map<String,Object> model = new HashMap<String,Object>();
		model.put("todosItens",itemRepository.findAll());
		model.put("todosClientes",clienteRepository.findAll());
		model.put("pedido",pedido);

		return new ModelAndView("pedidos/form",model);
	}
}
