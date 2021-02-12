package com.gabrielf.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.gabrielf.cursomc.domain.Cliente;
import com.gabrielf.cursomc.domain.enums.TipoCliente;
import com.gabrielf.cursomc.dto.ClienteNewDTO;
import com.gabrielf.cursomc.repository.ClienteRepository;
import com.gabrielf.cursomc.resources.exceptions.FieldMessage;
import com.gabrielf.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClientInsert, ClienteNewDTO> {
	
	
	@Autowired
	private ClienteRepository repo;
	
	
	@Override
	public void initialize(ClientInsert ann) {
		
	}
	
	
	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if(aux != null) {
			list.add(new FieldMessage("email", "email já existe"));
		}
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidSsn(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidTfn(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}
		
		for(FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		
		
		return list.isEmpty();
	}

	
	

}
