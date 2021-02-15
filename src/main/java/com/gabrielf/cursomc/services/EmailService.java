package com.gabrielf.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.gabrielf.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
