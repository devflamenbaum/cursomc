package com.gabrielf.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielf.cursomc.domain.ItemPedido;
import com.gabrielf.cursomc.domain.PagamentoComBoleto;
import com.gabrielf.cursomc.domain.Pedido;
import com.gabrielf.cursomc.domain.enums.EstadoPagamento;
import com.gabrielf.cursomc.repository.ItemPedidoRepository;
import com.gabrielf.cursomc.repository.PagamentoRepository;
import com.gabrielf.cursomc.repository.PedidoRepository;
import com.gabrielf.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstant(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstant());
		}
		
		repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for(ItemPedido i : obj.getItens()) {
			i.setDesconto(0.0);
			i.setPreco(produtoService.find(i.getProduto().getId()).getPreco());
			i.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		
		return obj;
		
	}
}
